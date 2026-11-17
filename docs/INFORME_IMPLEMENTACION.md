# Informe de implementación y revisión final

## Resultado

Se implementó el Taller 13 sobre el código original en `feature/taller13-fase2`. Tres servicios compilados y ejecutándose en Java 17, RabbitMQ real, JPA/H2 y HashMap intercambiables, contratos HTTP, manejo de fallos, Docker y documentación. El despliegue final quedó en **JPA** con cuatro contenedores saludables.

## Estructura y archivos

La raíz contiene `docker-compose.yml`, POM agregador, Maven Wrapper, README, archivos ignore y scripts. Los módulos son `validation-service`, `question-service` y `lifecycle-service`, cada uno con POM, Dockerfile, fuentes, configuración y pruebas. `docs/` contiene guías, arquitectura, pruebas, evidencias e históricos.

El `src/` original se trasladó a `question-service/src/` con los paquetes requeridos. Se actualizaron controladores, servicio, configuración y repositorios. El adaptador que solo escribía logs fue reemplazado por RabbitMQPublisher. Los originales de README, auditoría, POM y diagramas están preservados en `docs/taller12/`. Ver [inventario completo](INVENTARIO_CAMBIOS.md).

## Correspondencia con las guías

La [matriz de trazabilidad](arquitectura/trazabilidad.md) vincula requisitos de los dos PDF con archivos y pruebas. Se preservan sus nombres de servicios, puertos, paquetes, cola y red. Las diferencias justificadas son la imagen Java 17 alternativa, el Dockerfile multi-stage y la conservación de los nombres españoles del dominio/puerto de notificación del Taller 12.

## Pruebas ejecutadas

- Base antes de modificar: `mvn -B test`, 23 pruebas aprobadas.
- Migración del dominio: 14 pruebas aprobadas.
- Compilación completa local: `mvn -B -ntp clean verify`, 32 pruebas aprobadas, host Java 21 con `release 17`.
- Suite completa en contenedor Maven/Java 17: `verify -Pintegration`, **55 pruebas**, 0 fallos, 0 errores, 0 omitidas.
- `scripts/check-domain.sh`: dominio y aplicación compilados como Java puro.
- Compose: configuración, imágenes, arranque, `ps`, logs y red verificados.
- `smoke_test.py --faults` con JPA y con memory: **28 respuestas HTTP verificadas por modo**, más Management y comprobantes del consumidor.
- Persistencia tras recrear Question, reiniciar Lifecycle y reentregar un evento: verificada.

Las 23 pruebas originales se mantuvieron: los tests HTTP adoptaron 202 según el nuevo requisito y ahora usan servicios externos reales. El resto amplía cobertura de listado, fallos, contratos, concurrencia y AMQP. No se usaron mocks en las pruebas integradas.

## Docker, RabbitMQ y endpoints

| Componente | Puerto | Endpoint principal |
|---|---:|---|
| validation-service | 8082 | `POST /api/validaciones/preguntas` |
| question-service | 8083 | `GET/POST /api/preguntas`, `GET /api/preguntas/{id}`, `POST /api/preguntas/{id}/enviar-revision` |
| lifecycle-service | 8084 | `GET /api/revisiones`, `GET /api/revisiones/{preguntaId}` |
| rabbitmq | 5672 / 15672 | AMQP / Management (`guest / guest`, desarrollo) |

Cola real: `pregunta.revisar.queue`. El consumidor registra IDs y persiste comprobantes. La entrega válida produce 202; el broker detenido produce 503 y rollback. Existen DLX/DLQ para errores y deduplicación persistente. H2 queda en volúmenes independientes.

## Comandos exactos

Desde la raíz:

```bash
./mvnw clean verify
docker compose config --quiet
docker compose up -d --build --wait
./mvnw -Pintegration verify
python3 scripts/smoke_test.py --label jpa
```

La ejecución completa efectuada con Java 17 usó este comando reproducible en la red de Compose (Linux):

```bash
docker run --rm --network saberpro-net --user "$(id -u):$(id -g)" \
  -e MAVEN_CONFIG=/tmp/maven-cache \
  -v "$PWD:/workspace" -v "$HOME/.m2:/tmp/maven-cache" -w /workspace \
  maven:3.9.9-eclipse-temurin-17 mvn -B -ntp \
  -Dmaven.repo.local=/tmp/maven-cache/repository -Duser.home=/tmp \
  -Dapp.validation.base-url=http://validation-service:8082 \
  -Dspring.rabbitmq.host=rabbitmq \
  -Dtest.lifecycle-url=http://lifecycle-service:8084 -Pintegration verify
```

## Problemas encontrados y soluciones

| Hallazgo | Solución aplicada |
|---|---|
| Notificación original solo registraba logs | Publicación persistente real, confirms, returns y listener independiente |
| Faltaban listado GET y respuesta 202 | Nuevo puerto/listado y contrato de revisión actualizado |
| Posible estado pendiente sin publicación | Actualización transaccional o con restauración, confirmación y 503 ante fallo |
| Envíos simultáneos | Bloqueo pesimista JPA / monitor HashMap; prueba un 202 y un 400 |
| Jackson convertía números a cadenas | Configuración de coerción estricta y prueba corregida sin debilitar la aserción |
| `openjdk:17-jdk-slim` no tiene manifiesto | Temurin 17 verificada, diferencia documentada |
| No había Maven Wrapper | Wrapper oficial 3.9.9 y lanzadores por módulo |
| Host tiene Java 21 | Compilación release 17 y suite completa/runtimes probados en Java 17 dentro de Docker |
| Poco espacio en `/home` | Verificación de consumo; los builds Docker usan su almacenamiento. No se borraron archivos ajenos ni datos de otros proyectos |
| Plantilla Word no disponible | Informe técnico completo en Markdown, con traslado a formato institucional identificado como pendiente |

## Límites y pendientes

No quedan endpoints o integraciones de la solicitud sin implementar. La falta de Outbox implica una ventana de inconsistencia entre ACK y commit que se explica en [decisiones](arquitectura/decisiones.md); no se presenta como atomicidad distribuida.

Quedan por completar los datos de portada reales, traslado a `Formato-Documento.docx`, capturas manuales de Thunder Client/Docker/Management y publicación en GitHub/plataforma. No se fabricaron capturas ni historial de cortes anteriores. No se realizó commit, push, merge ni eliminación de ramas.

La rúbrica técnica está cubierta en arquitectura, contenerización y flujo integrador. La parte de informe/diagramas está preparada en fuentes editables y evidencias textuales, pero la presentación final en plantilla y capturas permanece pendiente. Ver [rúbrica](arquitectura/trazabilidad.md).
