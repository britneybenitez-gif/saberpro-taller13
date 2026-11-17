# Protocolo de pruebas

## Automatizadas sin servicios externos

```bash
./mvnw clean verify
./scripts/check-domain.sh
```

Surefire ejecuta los tests de dominio, casos de uso, productor con dobles unitarios, Validation por HTTP real y persistencia H2 de Lifecycle. El segundo comando comprueba que dominio y aplicación compilan exclusivamente con Java.

## Integración con servicios reales

```bash
docker compose up -d --build --wait
./mvnw -Pintegration verify
python3 scripts/smoke_test.py --label jpa --output docs/evidencias/smoke-local.json
```

Failsafe ejecuta `ApiJpaIntegrationTest`, `ApiMemoryIntegrationTest`, `ApiEmptyIntegrationTest` y `LifecycleIntegrationTest`. No se reemplaza RabbitMQ ni Validation por mocks. Las APIs de preguntas se inician en puertos aleatorios con bases H2 de prueba separadas; utilizan Validation :8082, RabbitMQ :5672 y comprueban el consumo en Lifecycle :8084. LifecycleIntegrationTest usa una cola aislada `pregunta.revisar.queue.integration-lifecycle` con su DLX/DLQ, sin competir con el consumidor del despliegue.

Para apuntar a otra red: propiedades Maven `-Dapp.validation.base-url=...`, `-Dspring.rabbitmq.host=...`, `-Dspring.rabbitmq.port=...` y `-Dtest.lifecycle-url=...`. La cola de producción siempre es `pregunta.revisar.queue`.

### Pruebas de fallos controlados

```bash
python3 scripts/smoke_test.py --label jpa --faults --output docs/evidencias/smoke-fallos-local.json
```

El argumento `--faults` detiene y reinicia brevemente RabbitMQ y Validation de este Compose. Comprueba 503, rollback a BORRADOR, ausencia de inserción cuando falla Validation y recuperación posterior. Usarlo en el entorno de desarrollo del taller, sin otra suite concurrente sobre esas dependencias. El script restaura los servicios en bloques `finally`.

### Intercambiabilidad en contenedores

```bash
APP_REPOSITORY=memory docker compose up -d --no-deps --force-recreate --wait question-service
python3 scripts/smoke_test.py --label memory --faults --output docs/evidencias/smoke-memory-local.json
APP_REPOSITORY=jpa docker compose up -d --no-deps --force-recreate --wait question-service
```

El mismo script realiza las mismas operaciones en ambos modos. No modifica el dominio ni borra volúmenes. Cada ejecución crea preguntas de prueba nuevas y deja sus comprobantes como evidencia.

## Thunder Client en VS Code

Importar `SaberPRO_Taller13.postman_collection.json` desde **Collections → Import**. Se entrega formato Postman v2.1 porque [Thunder Client admite su importación](https://docs.thunderclient.com/features/import). La colección no necesita scripts. Si el importador no aplica variables de colección, crear un entorno con `baseUrl=http://localhost:8083`, `validationUrl=http://localhost:8082`, `lifecycleUrl=http://localhost:8084` y `preguntaId`.

1. Ejecutar **01 - Listar**: esperar 200 y una lista (vacía en el primer arranque sin datos).
2. Ejecutar **02 - Crear**: esperar 201 y BORRADOR; copiar el campo `id` a `preguntaId` en el entorno.
3. Ejecutar **03 - Consultar**: esperar 200 y el mismo ID.
4. Ejecutar **04 - Enviar a revisión**: esperar 202 y PENDIENTE_REVISION.
5. Esperar el procesamiento y ejecutar **05 - Comprobante Lifecycle**: 200 con el mismo ID y `eventId`.
6. Ejecutar reenvío y justificación corta: ambos 400; ID inexistente: 404.
7. Ejecutar las tres peticiones Validation: válida 200, estructura incompleta 400 y longitud excesiva 400.

También se incluye `peticiones.http` con los cuerpos completos para copiar. El archivo `.http` no requiere que Thunder Client lo importe: la colección JSON es el artefacto de importación.

En http://localhost:15672 acceder con `guest / guest`, abrir Queues and Streams y buscar `pregunta.revisar.queue`. Debe ser durable y tener un consumidor. Es normal observar cero mensajes si ya se consumieron. Comparar el ID con:

```bash
docker logs -f saberpro-lifecycle-service
curl http://localhost:8084/api/revisiones/UUID_GENERADO
```

No se afirma haber usado la interfaz gráfica de Thunder Client. Las respuestas guardadas fueron obtenidas por los scripts HTTP y JUnit contra los servicios reales.

## Resultados esperados y evidencias

| Caso | Resultado |
|---|---|
| Crear contenido válido | 201, UUID, BORRADOR y Location |
| Listar/consultar | 200; listado sin registros es `[]` |
| Enunciado vacío / justificación corta | 400, sin guardar |
| Borrador con 3 opciones o 2 correctas | Creación 201; intento de revisión 400 |
| UUID válido inexistente | 404 |
| UUID mal formado / JSON inválido | 400 |
| Envío correcto | 202 y evento real consumido |
| Segundo envío | 400, sin un nuevo procesamiento |
| Dos envíos concurrentes | Un 202 y un 400 |
| Publicación a cola inexistente | Excepción de dependencia y rollback, incluso con ACK + return |
| Broker detenido | 503 y BORRADOR conservado |
| Validation detenido | 503 y no se inserta la pregunta |
| Mensaje mal formado o versión desconocida | Rechazo definitivo a DLQ, sin bucle |
| Evento repetido | Un comprobante; timestamp de procesamiento original conservado |

Los resultados de la ejecución realizada están en `docs/evidencias/`; no confundir resultados esperados con peticiones ya ejecutadas. Los logs de la prueba de DLQ y de las caídas contienen advertencias esperadas.
