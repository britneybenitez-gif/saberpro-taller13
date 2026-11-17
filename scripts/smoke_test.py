#!/usr/bin/env python3
"""Pruebas HTTP/AMQP reales. Usa solo la biblioteca estándar de Python 3."""
import argparse
import base64
import copy
import json
import os
from pathlib import Path
import subprocess
import time
from datetime import datetime, timezone
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

ROOT = Path(__file__).resolve().parents[1]
parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('--output', type=Path, help='Guardar resultados reales en JSON')
parser.add_argument('--label', default='despliegue', help='Etiqueta de la ejecución (jpa o memory)')
parser.add_argument('--faults', action='store_true', help='Detener y recuperar RabbitMQ/Validation del Compose de este proyecto')
args = parser.parse_args()
QUESTION = os.getenv('QUESTION_URL', 'http://localhost:8083')
VALIDATION = os.getenv('VALIDATION_URL', 'http://localhost:8082')
LIFECYCLE = os.getenv('LIFECYCLE_URL', 'http://localhost:8084')
MANAGEMENT = os.getenv('RABBITMQ_MANAGEMENT_URL', 'http://localhost:15672')
report = {'startedAt': datetime.now(timezone.utc).isoformat(), 'label': args.label, 'checks': []}
stopped = set()


def request(base, method, path, body=None, auth=False):
    headers = {'Content-Type': 'application/json'}
    if auth:
        credentials = os.getenv('RABBITMQ_USER', 'guest') + ':' + os.getenv('RABBITMQ_PASSWORD', 'guest')
        headers['Authorization'] = 'Basic ' + base64.b64encode(credentials.encode()).decode()
    data = None if body is None else json.dumps(body, ensure_ascii=False).encode()
    req = Request(base + path, data=data, headers=headers, method=method)
    try:
        response = urlopen(req, timeout=15)
    except HTTPError as error:
        response = error
    with response:
        raw = response.read().decode()
        return response.status, json.loads(raw) if raw else None


def check(name, expected, base, method, path, body=None, auth=False):
    status, result = request(base, method, path, body, auth)
    report['checks'].append({'name': name, 'method': method, 'url': base + path,
                             'expected': expected, 'status': status, 'body': result})
    print(f'{name}: HTTP {status}', flush=True)
    assert status == expected, f'{name}: esperado {expected}, recibido {status}: {result}'
    return result


def wait_for(base, path, auth=False, timeout=90):
    deadline = time.monotonic() + timeout
    while time.monotonic() < deadline:
        try:
            status, body = request(base, 'GET', path, auth=auth)
            if status == 200:
                return body
        except (URLError, TimeoutError, ConnectionError):
            pass
        time.sleep(0.5)
    raise AssertionError(f'No estuvo disponible: {base}{path}')


def compose(action, service):
    subprocess.run(['docker', 'compose', action, service], cwd=ROOT, check=True)


def pause(service):
    stopped.add(service)
    compose('stop', service)


def resume(service):
    compose('start', service)
    stopped.discard(service)


def receive(question_id):
    path = '/api/revisiones/' + question_id
    wait_for(LIFECYCLE, path, timeout=30)
    receipt = check('Consumo real Lifecycle', 200, LIFECYCLE, 'GET', path)
    assert receipt['preguntaId'] == question_id
    assert receipt['estado'] == 'PENDIENTE_REVISION'
    assert receipt['eventId'] and receipt['processedAt']


try:
    valid = json.loads((ROOT / 'ejemplo-pregunta.json').read_text())
    for base in [VALIDATION, QUESTION, LIFECYCLE]:
        wait_for(base, '/actuator/health')
        check('Salud del servicio', 200, base, 'GET', '/actuator/health')
    initial = check('Listado inicial', 200, QUESTION, 'GET', '/api/preguntas')
    assert isinstance(initial, list)
    check('Validación REST directa', 200, VALIDATION, 'POST', '/api/validaciones/preguntas', valid)
    created = check('Creación', 201, QUESTION, 'POST', '/api/preguntas', valid)
    question_id = created['id']
    report['preguntaId'] = question_id
    assert created['estado'] == 'BORRADOR'
    assert created['enunciado']['texto'] == valid['enunciado']
    check('Consulta individual', 200, QUESTION, 'GET', '/api/preguntas/' + question_id)
    revision = check('Envío a revisión', 202, QUESTION, 'POST', '/api/preguntas/' + question_id + '/enviar-revision')
    assert revision['estado'] == 'PENDIENTE_REVISION'
    receive(question_id)
    check('Envío duplicado', 400, QUESTION, 'POST', '/api/preguntas/' + question_id + '/enviar-revision')
    check('ID inexistente', 404, QUESTION, 'GET', '/api/preguntas/00000000-0000-0000-0000-000000000000')
    check('UUID mal formado', 400, QUESTION, 'GET', '/api/preguntas/incorrecto')
    for field, value in [('justificacion', 'corta'), ('enunciado', '   '), ('opciones', None)]:
        invalid = copy.deepcopy(valid)
        invalid[field] = value
        check('Dato inválido: ' + field, 400, QUESTION, 'POST', '/api/preguntas', invalid)
    for name, options in [('Tres opciones', valid['opciones'][:3]),
                          ('Dos claves correctas', [{'texto': str(i), 'correcta': i < 2} for i in range(4)])]:
        incomplete = copy.deepcopy(valid)
        incomplete['opciones'] = options
        draft = check('Borrador: ' + name, 201, QUESTION, 'POST', '/api/preguntas', incomplete)
        check('Rechazo: ' + name, 400, QUESTION, 'POST', '/api/preguntas/' + draft['id'] + '/enviar-revision')
    large = copy.deepcopy(valid)
    large['enunciado'] = 'x' * 4001
    check('Límite técnico Validation', 400, VALIDATION, 'POST', '/api/validaciones/preguntas', large)
    check('Integración síncrona del límite', 400, QUESTION, 'POST', '/api/preguntas', large)
    queue = wait_for(MANAGEMENT, '/api/queues/%2F/pregunta.revisar.queue', auth=True)
    assert queue['durable'] and queue['consumers'] >= 1
    report['rabbitmq'] = {key: queue.get(key) for key in ['name', 'durable', 'consumers', 'messages', 'message_stats']}
    print('RabbitMQ Management: cola durable y consumidor real disponibles', flush=True)

    if args.faults:
        draft = check('Borrador para fallo de broker', 201, QUESTION, 'POST', '/api/preguntas', valid)
        pause('rabbitmq')
        try:
            check('Broker caído', 503, QUESTION, 'POST', '/api/preguntas/' + draft['id'] + '/enviar-revision')
            state = check('Rollback tras fallo', 200, QUESTION, 'GET', '/api/preguntas/' + draft['id'])
            assert state['estado'] == 'BORRADOR'
        finally:
            resume('rabbitmq')
        wait_for(MANAGEMENT, '/api/overview', auth=True)
        wait_for(QUESTION, '/actuator/health')
        check('Reintento tras recuperación', 202, QUESTION, 'POST', '/api/preguntas/' + draft['id'] + '/enviar-revision')
        receive(draft['id'])
        before = request(QUESTION, 'GET', '/api/preguntas')[1]
        pause('validation-service')
        try:
            check('Validation caído', 503, QUESTION, 'POST', '/api/preguntas', valid)
            after = request(QUESTION, 'GET', '/api/preguntas')[1]
            assert len(before) == len(after), 'Se persistió sin validación externa'
        finally:
            resume('validation-service')
        wait_for(VALIDATION, '/actuator/health')
        check('Validación recuperada', 201, QUESTION, 'POST', '/api/preguntas', valid)
    report['success'] = True
except Exception as error:
    report['success'] = False
    report['error'] = str(error)
    raise
finally:
    for service in sorted(stopped):
        compose('start', service)
    report['finishedAt'] = datetime.now(timezone.utc).isoformat()
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n')
    print('RESULTADO: ' + ('PASS' if report.get('success') else 'FAIL'), flush=True)
