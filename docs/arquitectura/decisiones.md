# Decisiones, diferencias de guías y límites

| Tema | Decisión y razón |
|---|---|
| Nombres Pregunta/Question | Se conserva `Pregunta` y los objetos de valor españoles exigidos en el texto; se usan `QuestionRecord`, `QuestionDomainMapper` y nombres de servicios de la arborescencia institucional |
| Value Objects | Se trasladan de `domain.valueobject` a `domain.model` para cumplir ambos PDF |
| NotificacionPort | Se conserva `NotificacionRevisionPort`, equivalente ya existente y explícitamente permitido en la solicitud |
| EvaluarPreguntaUseCase | Evalúa preparación mediante `enviarRevision`, sin inventar calificación ni aprobaciones no definidas |
| Borradores | Se conserva la admisión de opciones incompletas; las guías exigen 4/1 al cambiar de estado y las pruebas originales verifican esa distinción |
| Base de datos | H2, aceptada por la entrega institucional; archivos con volúmenes en Compose y memoria para pruebas |
| Validation | Límites técnicos de las columnas existentes y estructura JSON; no copia las invariantes del agregado. Endpoint definido como decisión al no existir contrato específico en los PDF |
| Lifecycle | Registra recepción y expone comprobantes. No cambia por su cuenta el agregado propiedad de Question |
| openjdk:17-jdk-slim | El 16/11/2026 el registro devolvió `no such manifest`; se sustituyó por `eclipse-temurin:17-jdk-jammy` compatible con Java 17 |
| Compilación Docker | Multi-stage con Maven 3.9.9. Permite `docker compose up -d --build` desde fuentes; la guía paso a paso presupone compilar el JAR antes |
| Compose version | Se omite la clave obsoleta `version: '3.8'`; se usa la especificación Compose vigente, conservando servicios, puertos y red |
| Pruebas externas | Perfil Failsafe `integration`, sin mocks. Requiere servicios reales; no simula ni omite pruebas al faltar broker |
| Capturas/plantilla | Se preservan las históricas; los resultados nuevos son logs/JSON. La plantilla Word y las capturas manuales de la entrega se identifican como pendientes |

## Consistencia sin Outbox

Se eligió una transacción local con confirmación del broker y deduplicación del consumidor para mantener una solución pequeña y verificable.

1. JPA toma bloqueo de escritura y lee el agregado; HashMap toma su monitor y copia el agregado.
2. Se verifica el cambio de BORRADOR a PENDIENTE_REVISION en el dominio.
3. Se guarda/flush el nuevo estado.
4. Se publica y se espera ACK y ausencia de devolución.
5. El adaptador confirma localmente y la API devuelve 202.

Si fallan las reglas, no se publica. Si falla guardar, tampoco se publica. Si falla o se agota la espera de publicación, JPA revierte la transacción y HashMap restaura el borrador, devolviendo 503. Dos revisiones simultáneas no publican dos eventos por la misma transición dentro del despliegue probado.

**Ventanas inevitables:** un timeout puede ocurrir después de que el broker aceptó el mensaje; y un fallo de proceso/commit después del ACK puede dejar un comprobante en Lifecycle mientras Question permanece en BORRADOR. La persistencia y RabbitMQ no son un recurso transaccional único. El evento tiene identidad estable y Lifecycle conserva el ID para que el reintento no repita el procesamiento, pero eso no repara automáticamente el estado de Question. Tras un resultado incierto se deben consultar ambos servicios antes de reintentar. En una evolución productiva, Outbox transaccional y un publicador con reintentos resolverían esa ventana con consistencia eventual; aquí no se afirma entrega exactamente una vez.

El uso de H2 en archivo y un HashMap local supone una instancia por servicio en esta entrega. No se afirma que los volúmenes H2 puedan compartirse entre réplicas. El recibo de Lifecycle solo acredita procesamiento del evento, no evaluación humana de la pregunta.

## Fuentes técnicas consultadas

- [Spring AMQP: AmqpTemplate y confirms/returns](https://docs.spring.io/spring-amqp/reference/amqp/template.html): fundamenta la espera del future de confirmación y la comprobación de mensajes devueltos.
- [CorrelationData](https://docs.spring.io/spring-amqp/api/org/springframework/amqp/rabbit/connection/CorrelationData.html): orden entre devolución y confirmación.
- [Docker Hub: Eclipse Temurin 17](https://hub.docker.com/_/eclipse-temurin/tags?name=17-jdk-jammy): alternativa Java 17 verificada además con pull y ejecución.
- [Thunder Client: importar/exportar](https://docs.thunderclient.com/features/import): colección Postman v2.1 importable para las pruebas manuales.

Las decisiones anteriores complementan los dos PDF incluidos en `docs/guias/`; no les atribuyen requisitos que no contienen.
