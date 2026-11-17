# Arquitectura implementada

## Contextos y responsabilidades

Question es dueño del agregado Pregunta y de su almacenamiento. Validation ofrece una política técnica de admisión de contenido por REST. Lifecycle registra solicitudes de revisión recibidas por AMQP. No existe acceso cruzado a las bases de datos ni dependencia Maven entre los microservicios. Cada POM hereda Spring Boot directamente; el POM raíz solo los agrupa.

El flujo de creación atraviesa `QuestionRestController → CrearPreguntaUseCase → QuestionApplicationService`. El servicio construye objetos de valor y el agregado, solicita `ValidacionContenidoPort.validar`, y persiste mediante `PreguntaRepositoryPort.save`. La API devuelve 201 con Location después de guardar.

El flujo de revisión usa `EvaluarPreguntaUseCase.enviarRevision`. `PreguntaRepositoryPort.actualizar` define el límite de actualización: obtiene y bloquea la pregunta, aplica `Pregunta.enviarRevision`, guarda y ejecuta `NotificacionRevisionPort`. El adaptador confirma la operación local solo cuando la notificación termina correctamente.

## Dominio

`Pregunta`, `EstadoPregunta`, `Enunciado`, `Justificacion`, `OpcionRespuesta`, `ReglaNegocioException` y todos los puertos están bajo `question/domain`. Los records normalizan los textos y se comparan por valor. El agregado copia la lista de opciones y valida su reconstrucción si el estado es PENDIENTE_REVISION. No permite mutaciones de opciones a través de los getters.

Se conservó el diseño del Taller 12: no se exige cuatro opciones al guardar un borrador, sino al enviar a revisión. No se incorporaron aprobaciones, calificaciones o estados no especificados. El puerto `EvaluarPreguntaUseCase` representa la evaluación de preparación para revisión, dado que la guía enumera su nombre sin definir una operación de calificación.

## Persistencia

`QuestionRecord` y `OpcionEmbeddable` representan tablas propias de infraestructura. `QuestionDomainMapper` convierte las entidades en agregados completos y viceversa, incluyendo orden de opciones y estado. JPA usa `saveAndFlush` para detectar restricciones antes de publicar. La revisión toma un bloqueo `PESSIMISTIC_WRITE` dentro de una transacción.

El HashMap guarda copias defensivas y sincroniza lecturas y escrituras. Durante la revisión, conserva la copia anterior, guarda la nueva y restaura la anterior si la notificación lanza una excepción. Ningún lector observa ese estado intermedio porque usa el mismo monitor. Esta solución serializa todas las revisiones en memoria: apropiada para la escala didáctica, sin prometer concurrencia distribuida.

En Compose cada servicio con datos tiene su propio H2 en archivo y volumen. Un contenedor Question usa JPA o HashMap, nunca ambos para una operación. El modo memoria no conserva preguntas entre reinicios. Los registros de consumo de Lifecycle son independientes y siempre se guardan en su H2.

## Validación REST

`POST /api/validaciones/preguntas` recibe el mismo JSON de creación. Bean Validation comprueba campos y elementos no nulos, booleans presentes y longitudes máximas de almacenamiento (4000/4000/2000). Jackson rechaza tipos incompatibles y JSON mal formado. Devuelve `{"valida":true,"errores":[]}` o HTTP 400 con errores por campo.

Question invoca este endpoint con `RestClient`; tanto conexión como lectura tienen timeout de 3 s. HTTP 400 remoto se traduce a una violación de contenido; fallo de conexión, timeout, respuesta ilegible o error del servidor se traduce a 503 sin guardar. Las invariantes semánticas se verifican antes en el dominio. No hay alternativa silenciosa que omita Validation.

## Contrato de revisión v1

```json
{
  "eventId": "UUID determinista de revision:preguntaId",
  "schemaVersion": 1,
  "type": "PreguntaEnviadaARevision",
  "preguntaId": "UUID de la pregunta",
  "estado": "PENDIENTE_REVISION",
  "occurredAt": "2026-11-16T20:00:00Z"
}
```

El ejemplo define el formato; no constituye evidencia de una ejecución. Los IDs reales están en `docs/evidencias/`.

- `eventId`: UUID derivado de `revision:` + ID de pregunta. Estable ante reintentos del único cambio admitido.
- `schemaVersion`: 1. Las versiones desconocidas se rechazan.
- `type`, `estado`: valores exactos anteriores.
- `occurredAt`: instante UTC de cada intento de publicación.
- Mensaje `application/json`, UTF-8, delivery mode PERSISTENT. No incluye referencias a clases Java ni necesita compartir paquetes entre productor y consumidor.

Productor: exchange por defecto `""`, routing key `pregunta.revisar.queue`, mandatory y confirms correlacionados. Espera hasta 5 s por ACK, además del tiempo de conexión. Un ACK con `basic.return` tampoco se considera entrega satisfactoria. La respuesta 202 acredita aceptación por el broker y confirmación de la persistencia local, no procesamiento final ni calificación académica.

Consumidor: `@RabbitListener`, concurrencia 1, prefetch 10, ACK automático al finalizar correctamente. Primero valida el contrato; luego `ReviewProcessingService` guarda el comprobante transaccional. El ID del evento es clave primaria y el ID de pregunta es único. Una redelivery del mismo evento devuelve el comprobante existente, sin duplicar procesamiento.

Los fallos se reintentan hasta tres intentos con intervalo inicial de 500 ms y multiplicador 2. El rechazo definitivo usa la DLX durable `pregunta.revisar.queue.dlx`, con ruta a la DLQ durable `pregunta.revisar.queue.dlq`. Los logs del consumidor incluyen el ID real procesado. La consulta REST de comprobantes permite comprobar el resultado aunque la cola ya se haya vaciado.

## Errores HTTP

La API de preguntas distingue reglas y estructura inválidas (400), ausencia (404) y dependencia externa no disponible (503). No intercepta los errores para devolver éxitos aparentes. La consulta de revisión de Lifecycle devuelve 404 mientras no exista comprobante; debe tenerse en cuenta la asincronía.

## Despliegue

Los Dockerfile compilan con Maven y JDK 17 y ejecutan el JAR como UID 10001. Cada imagen expone su puerto específico. Las comprobaciones HTTP usan `/actuator/health`; RabbitMQ usa `rabbitmq-diagnostics ping`. `depends_on: condition: service_healthy` hace que Question espere a Validation y RabbitMQ, y Lifecycle a RabbitMQ.

La red bridge `saberpro-net` ofrece resolución DNS por nombre de servicio. Los puertos públicos habilitan Thunder Client y Management. No se introdujo PostgreSQL porque la guía acepta PostgreSQL/H2 y H2 ya era la base del Taller 12.
