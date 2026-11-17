# Guía de sustentación — Taller 13

Esta guía describe el sistema que existe y fue probado el 16/11/2026. Hay un defecto funcional pendiente (HALL-01) y pendientes de entrega. Deben explicarse con honestidad; no se debe presentar el proyecto como totalmente terminado hasta resolverlos.

## Presentación inicial sugerida

«Partimos del Taller 12, una aplicación Spring Boot en el puerto 8080 que creaba preguntas, permitía consultar por ID y cambiaba su estado a revisión. El dominio ya estaba separado de la tecnología y podía usar JPA o memoria, pero la notificación era un log.

En el Taller 13 separamos tres servicios. Question administra las preguntas en 8083. Validation recibe solicitudes de validación técnica por REST en 8082. Lifecycle recibe eventos mediante RabbitMQ y registra comprobantes en 8084. Cada servicio tiene su POM y Dockerfile; Docker Compose construye y ejecuta el conjunto en la red saberpro-net.

Verificamos la ejecución real y 55 pruebas Maven. La auditoría adicional encontró un caso límite en la normalización de texto que la suite no cubría y que hace responder distinto a JPA y memoria. Está documentado y requiere corrección antes de declarar cumplimiento completo. También faltan el Word institucional y las capturas finales.»

## Mapa del sistema para explicar sin memorizar paquetes

| Componente | Responsabilidad concreta | Entrada | Salida o almacenamiento |
|---|---|---|---|
| Validation, :8082 | Rechazar estructura/tipos/límites técnicos inválidos | POST /api/validaciones/preguntas | Resultado válida/errores; sin base propia |
| Question, :8083 | Crear, listar, consultar y solicitar revisión de preguntas | REST del cliente | JPA/H2 o HashMap; REST hacia Validation; evento a RabbitMQ |
| RabbitMQ, :5672 y :15672 | Recibir y entregar mensajes; mostrar administración | AMQP de Question | Cola pregunta.revisar.queue y entrega a Lifecycle |
| Lifecycle, :8084 | Validar el evento, registrar procesamiento y evitar repetirlo | @RabbitListener | H2 de comprobantes; GET /api/revisiones/{preguntaId} |

Hay **tres microservicios y cuatro contenedores principales**. Los dos H2 se ejecutan dentro de las JVM de Question y Lifecycle; sus archivos quedan en volúmenes separados. No hay un contenedor PostgreSQL ni un frontend. La guía permite probar con un cliente HTTP.

El mapa del README y el C4 nivel 2 muestran esta vista. Los demás C4 son acercamientos: contexto y actores en nivel 1; componentes de Question en nivel 3; clases y contratos en nivel 4. No confundir «contenedor C4» con «siempre un contenedor Docker».

## DDD explicado con el caso real

**Pregunta es la raíz del agregado.** Tiene un UUID, enunciado, justificación, opciones y estado. Controla cuándo puede cambiar a PENDIENTE_REVISION; los clientes no asignan ese estado mediante un setter público.

**Los objetos de valor son Enunciado, Justificacion y OpcionRespuesta.** Se comparan por su contenido, son records inmutables y encapsulan validación/normalización. OpcionRespuesta contiene texto y un booleano que señala si es la clave correcta. La lista de opciones se copia defensivamente con List.copyOf; devolver esa lista no permite añadir opciones desde fuera.

**Las reglas tienen dos momentos.** Al crear se revisan la existencia de los datos y las reglas de los VO; la justificación necesita al menos 15 caracteres después de normalizar. Se permite guardar un borrador con opciones incompletas. Al enviar a revisión se exige estado BORRADOR, exactamente cuatro opciones y exactamente una correcta. Solo después se cambia el estado. La reconstrucción de una pregunta ya pendiente vuelve a comprobar cuatro/una.

**ReglaNegocioException pertenece al dominio.** Dice que una operación viola una regla, sin conocer HTTP. La API la transforma en 400. No se introdujeron @Entity ni anotaciones Spring en estas clases. La prueba más directa de independencia es que dominio y aplicación compilan con javac sin classpath de frameworks.

**HALL-01 muestra por qué importa validar después de normalizar.** Enunciado y OpcionRespuesta comprueban isBlank antes de trim. Una cadena con NUL/tab/espacio puede pasar la condición y convertirse luego en texto vacío. Ser inmutable no garantiza ser válido. La corrección debe hacerse en el constructor del VO y probarse en ambos adaptadores; todavía no se aplicó.

## Arquitectura hexagonal: quién depende de quién

La API es un adaptador de entrada. Convierte JSON en datos de la operación y llama a interfaces: CrearPreguntaUseCase o EvaluarPreguntaUseCase. QuestionApplicationService implementa esas interfaces y coordina reglas, validación remota, persistencia y notificación. El núcleo declara los puertos que necesita; infraestructura aporta sus implementaciones.

| Puerto del núcleo | Operaciones | Implementación conectada |
|---|---|---|
| CrearPreguntaUseCase | crear, buscar, listar | QuestionApplicationService |
| EvaluarPreguntaUseCase | enviarRevision | QuestionApplicationService |
| PreguntaRepositoryPort | save, findById, findAll, actualizar | JpaQuestionAdapter o InMemoryQuestionAdapter |
| ValidacionContenidoPort | validar(Pregunta) | RestValidationAdapter |
| NotificacionRevisionPort | notificarPreguntaPendiente(Pregunta) | RabbitMQPublisher |

HexagonalConfiguration crea los beans fuera del núcleo. APP_REPOSITORY selecciona jpa o memory. Cambiarlo no modifica Pregunta ni el caso de uso. Las pruebas reales demostraron la sustitución y el funcionamiento ordinario; HALL-01 demuestra que todavía no se puede afirmar equivalencia para todas las entradas.

Los nombres Pregunta/OpcionRespuesta se conservaron porque el texto de las guías los pide y ya existían. Los árboles también muestran Question/AnswerOption como nombres de ejemplo. NotificacionRevisionPort es el equivalente anterior permitido por la solicitud. EvaluarPreguntaUseCase comprueba la preparación para revisión; no calcula una nota ni aprueba una pregunta, operaciones que las guías no definen.

El diseño hexagonal detallado corresponde al núcleo de Question. Validation y Lifecycle son servicios auxiliares pequeños con API/mensajería y sus propias responsabilidades; no hay que inventar un agregado Pregunta en cada uno para afirmar separación de servicios.

## Métodos que conviene poder explicar

| Método | Recibe | Devuelve o efecto | Capa y motivo |
|---|---|---|---|
| Pregunta.crear | Enunciado, Justificacion y lista de opciones | Nuevo UUID y agregado BORRADOR | Dominio: define creación válida del negocio |
| Pregunta.enviarRevision | Sin parámetros; usa su propio estado/datos | void; valida y cambia estado, o lanza regla | Dominio: protege la transición |
| Pregunta.validarInvariantes | Sin parámetros; usa las opciones | void; exige cuatro y una correcta | Método privado del agregado, también usado al rehidratar pendiente |
| QuestionApplicationService.crear | Strings y lista de OpcionRespuesta | Pregunta persistida | Aplicación: crea agregado, valida por puerto y guarda |
| QuestionApplicationService.buscar | UUID | Pregunta o NoSuchElementException | Aplicación: consulta sin conocer HTTP/JPA |
| QuestionApplicationService.listar | Nada | List<Pregunta> | Aplicación: delega almacenamiento mediante puerto |
| QuestionApplicationService.enviarRevision | UUID | Pregunta actualizada | Aplicación: llama actualizar con cambio de dominio y callback de notificación |
| PreguntaRepositoryPort.actualizar | UUID, Consumer de cambio y Consumer posterior al guardado | Pregunta o excepción | Contrato que permite proteger actualización y revertir fallo de notificación |
| JpaQuestionAdapter.actualizar | Los tres argumentos del puerto | Agregado; commit o rollback | Infraestructura: bloqueo y transacción JPA |
| InMemoryQuestionAdapter.actualizar | Los mismos argumentos | Copia del agregado; restaura copia anterior si falla notificación | Infraestructura: HashMap y monitor sincronizado |
| QuestionDomainMapper.toRecord/toDomain | Agregado o entidad | La representación opuesta | Infraestructura: evita anotar el agregado con JPA |
| RestValidationAdapter.validar | Pregunta | void si válida; excepción si rechazo o indisponibilidad | Adaptador REST de salida |
| RabbitMQPublisher.notificarPreguntaPendiente | Pregunta revisada | void tras confirmación aceptada, o excepción | Adaptador AMQP; serializa el evento y espera confirms/returns |
| QuestionReviewListener.recibir | Message AMQP | void después de procesar; rechazo si contrato inválido | Entrada de Lifecycle mediante RabbitMQ |
| ReviewProcessingService.registrar | QuestionReviewMessage | true si creó recibo, false si evento ya estaba procesado | Servicio de Lifecycle, transacción local de idempotencia |

La explicación individual de los **51 archivos Java**, incluidos DTOs, configuraciones y tests, está en [EXPLICACION_ARCHIVOS.md](EXPLICACION_ARCHIVOS.md). Allí también se explica qué responsabilidad se perdería si faltara cada clase.

## Recorrido completo de creación

1. El cliente envía JSON a Question: enunciado y justificación son strings; opciones es una lista de objetos con texto y correcta booleano.
2. QuestionRestController valida la estructura, construye OpcionRespuesta y llama al puerto CrearPreguntaUseCase. La lógica de cuatro/una no se copia al controller.
3. QuestionApplicationService construye Enunciado, Justificacion y Pregunta en BORRADOR.
4. Mediante ValidacionContenidoPort, RestValidationAdapter llama a Validation por HTTP. Validation revisa campos/tipos y límites técnicos de almacenamiento: 4000/4000/2000 caracteres. Las reglas semánticas siguen en el dominio.
5. Si la validación externa acepta, el servicio guarda por PreguntaRepositoryPort. JPA convierte a QuestionRecord; memoria guarda una copia.
6. La API devuelve 201 con Location e ID. La respuesta mantiene los VO anidados del Taller12, por ejemplo `enunciado: {texto: ...}`. La forma de entrada y la de salida no tienen por qué ser idénticas.

Si Validation está detenido, se responde 503 y no se inserta. Si una regla de negocio se detecta, se responde 400. HALL-01 es precisamente una regla que en un caso de texto no se detecta correctamente en memoria.

## Recorrido completo de revisión

1. El cliente envía POST /api/preguntas/{id}/enviar-revision.
2. El controlador llama a EvaluarPreguntaUseCase; el servicio usa repositorio.actualizar.
3. JPA obtiene la fila con PESSIMISTIC_WRITE dentro de una transacción; memoria sincroniza el acceso y conserva la copia anterior.
4. Pregunta.enviarRevision comprueba BORRADOR, cuatro opciones y una correcta; después cambia el estado.
5. Se guarda/flush el nuevo estado y se llama al puerto de notificación.
6. RabbitMQPublisher crea un JSON con eventId, schemaVersion 1, type, preguntaId, estado y occurredAt. El mensaje es persistente, usa la cola requerida y espera confirmación del broker, comprobando también que no haya devolución por falta de ruta.
7. Si falla la publicación comprobable, la operación devuelve 503 y se revierte el estado local. Si todo termina correctamente, la API devuelve **202 Accepted**.
8. De manera asíncrona, Lifecycle recibe el mensaje, valida el contrato, guarda un recibo y registra preguntaId/eventId. El ACK del consumidor se produce después del retorno correcto del listener.

**202 significa aceptación de la solicitud y publicación confirmada**, no que un profesor haya evaluado la pregunta ni que Lifecycle haya terminado antes de la respuesta. El consumidor puede terminar antes o después del retorno HTTP. Por eso una consulta inmediata de recibo puede devolver 404 y luego 200.

**Publisher confirm y ACK de consumo son diferentes.** El primero responde al productor sobre aceptación por el broker; el segundo confirma que el consumidor terminó. Una cola vacía después de enviar puede indicar que el mensaje ya fue consumido. Para demostrarlo se correlaciona el mismo UUID en respuesta, log y recibo.

**Idempotencia:** eventId se deriva de la transición de revisión de esa pregunta y se conserva ante reintentos; Lifecycle guarda ese ID como clave y no crea un segundo recibo al recibirlo de nuevo. Esto no equivale a garantizar entrega exactamente una vez.

**Límite de atomicidad:** no hay transacción única entre H2 y RabbitMQ. Puede aceptarse el mensaje y fallar después el commit de Question. Los límites están documentados y la solicitud permitió una solución sin Outbox. No se debe prometer una garantía que no existe.

## Persistencia y cambio de adaptador

JPA usa una entidad QuestionRecord y un OpcionEmbeddable separados del dominio. El mapper convierte los datos completos, incluido el orden de las opciones. H2 en archivo dentro de Compose conserva los datos en question-data. Lifecycle tiene otro H2 y otro volumen para sus recibos.

Memoria usa un HashMap por proceso y copias defensivas para que modificar un agregado devuelto no cambie silenciosamente lo guardado. Sincroniza operaciones y restaura el borrador si falla notificar. Sus datos desaparecen al reiniciar el proceso: es una característica esperada, no un fallo del taller. El monitor bloquea operaciones durante la revisión; no se presenta como una solución para muchas réplicas.

Para una demostración posterior, desde la raíz:

```bash
APP_REPOSITORY=memory docker compose up -d --no-deps --force-recreate --wait question-service
```

Repetir creación, consulta, revisión y recibo con un UUID nuevo. Después restaurar:

```bash
APP_REPOSITORY=jpa docker compose up -d --no-deps --force-recreate --wait question-service
```

No usar `docker compose down -v` para esa prueba: eliminaría los volúmenes y no es necesario. En la auditoría se usó un contenedor temporal en 18083 para demostrar memory sin recrear el principal; ese temporal ya fue retirado y sus respuestas quedaron guardadas.

## Docker para explicar a la profesora

**Dockerfile:** es la receta de una imagen. La primera etapa usa Maven y Java 17 para producir el JAR; la segunda usa Temurin17, establece WORKDIR /app, copia el JAR, declara EXPOSE y ejecuta ENTRYPOINT java -jar app.jar con UID 10001. No depende de que el anfitrión tenga target previamente construido.

**Imagen:** contiene el sistema de archivos y el ejecutable; no es un proceso en ejecución. **Contenedor:** es la instancia en ejecución de esa imagen. **Compose:** define cómo construir y conectar las cuatro instancias, variables, puertos, volúmenes y salud.

**Red y puertos:** saberpro-net es bridge. Entre contenedores se usa `validation-service:8082` y `rabbitmq:5672`, porque localhost dentro de Question sería Question mismo. Desde el anfitrión se usa localhost y los puertos publicados. EXPOSE documenta el puerto de la imagen; `ports` de Compose lo publica hacia el anfitrión.

**Dependencias de arranque:** Question espera a Validation y RabbitMQ saludables; Lifecycle espera a RabbitMQ. Healthchecks comprueban HTTP o el broker. Las dependencias iniciales no eliminan las caídas posteriores; por eso hay timeouts, errores503 y pruebas de recuperación.

La guía usa openjdk:17-jdk-slim. La comprobación conservada del registro devolvió ausencia de ese manifiesto, y la solicitud permitió una alternativa compatible: Temurin17 fue ejecutado y verificado. Debe explicarse esta diferencia literal, sin afirmar que el Dockerfile es idéntico al ejemplo institucional.

## Demostración práctica sugerida, de unos diez minutos

Antes de presentar, resolver HALL-01 o informar claramente que sigue abierto, y preparar el Word/capturas pendientes. Los comandos siguientes son instrucciones para una nueva demostración; sus resultados futuros no se cuentan como ya ejecutados.

1. Mostrar la estructura de tres módulos, el archivo Compose y Pregunta sin anotaciones de frameworks.
2. Ejecutar `docker compose up -d --build --wait` y `docker compose ps`. Explicar los cuatro servicios y puertos.
3. Abrir el C4 nivel2 y señalar REST, AMQP y los almacenes independientes.
4. Hacer GET http://localhost:8083/api/preguntas y mostrar 200.
5. Crear con el cuerpo de ejemplo y copiar el UUID real:

```bash
curl -i -H 'Content-Type: application/json' \
  --data-binary @ejemplo-pregunta.json \
  http://localhost:8083/api/preguntas
```

6. En Thunder Client usar ese UUID en `preguntaId`, enviar revisión y mostrar 202. Repetir la misma petición y mostrar 400. Probar también una justificación corta y un UUID válido inexistente.
7. Abrir http://localhost:15672 con guest/guest, mostrar pregunta.revisar.queue y su consumidor. Consultar /api/revisiones/{preguntaId} en 8084 y buscar el mismo UUID en `docker compose logs --no-color lifecycle-service`.
8. Explicar el selector de repositorio y, si el tiempo permite, repetir el flujo con memory y volver a JPA.
9. Mostrar el reporte de 55 pruebas y explicar cuáles usan mocks y cuáles usan RabbitMQ real. La suite completa se reproduce con `./mvnw -Pintegration verify` después de levantar las dependencias; `mvn test` solo ejecuta32.
10. Cerrar con la comparación Taller 12→Taller 13 y las limitaciones reales: HALL-01 si sigue abierto, ACK/commit, informe/capturas y trazabilidad histórica disponible.

Las solicitudes preparadas están en [colección T13](../pruebas/SaberPRO_Taller13.postman_collection.json), [peticiones.http](../pruebas/peticiones.http) y [protocolo](../pruebas/README.md). La auditoría verificó HTTP equivalente; no afirma haber importado o usado la interfaz Thunder Client.

## Evidencia que conviene tener a mano

| Afirmación que se quiere demostrar | Archivo o comprobación |
|---|---|
| El núcleo no depende de Spring/JPA | scripts/check-domain.sh y dominio-puro.txt |
| Los tres servicios compilan y las suites pasan | maven-java17-integration.log y resumen-pruebas.json |
| No es una simulación de RabbitMQ | smoke JSON, estado-runtime.json y log de Lifecycle con UUID correlacionado |
| Hay recuperación ante fallos | Respuestas503, BORRADOR, recuperación202 y recibo en smoke de ambos modos |
| La integración cubre duplicados/mensajes inválidos | LifecycleIntegrationTest y su reporte Failsafe |
| JPA/memory aún difieren en un caso | http-normalizacion.json y sonda de HALL-01 |
| La arquitectura actual tiene cuatro niveles C4 | docs/arquitectura/diagramas.md |
| Las imágenes viejas no son del despliegue final | EXPLICACION_IMAGENES.md, hashes y puerto 8080 visible |

Las once imágenes antiguas pueden servir para explicar el antecedente. No usarlas para afirmar «estos son los contenedores actuales» ni «esta captura demuestra55 pruebas»: la captura de JUnit muestra23 y las revisiones antiguas muestran200.

## Ideas que deben quedar claras durante preguntas de la profesora

- DDD protege significado y reglas; hexagonal separa contratos de tecnologías; microservicios separan procesos/responsabilidades; Docker empaqueta y ejecuta. Son decisiones relacionadas, pero distintas.
- La entidad JPA no es la raíz del agregado: se usa un mapper para mantener independencia.
- Un HTTP 400 de regla detectada es correcto; HALL-01 existe porque en memoria esa regla no se detecta, no porque el handler traduzca mal la excepción.
- Un 202 y una cola vacía no prueban por sí solos consumo; el recibo y el log del mismo ID sí lo sustentan.
- El listener registra una solicitud de revisión, no una evaluación humana terminada.
- Las55 pruebas aprobadas cubren escenarios concretos. El contraejemplo adicional exige ampliar cobertura; no invalida las pruebas aprobadas ni elimina el defecto.
- La trazabilidad de Taller 12→Taller 13 es comprobable por Git; la de todo el semestre requiere las fuentes históricas faltantes. No afirmar Builder, State o Microkernel donde no se observan.
- La nota estimada 89,5/100 es orientativa de auditoría, no una calificación ni una autorización de entrega.

## Respuestas explícitas sobre el estado final

| Cuestión | Respuesta sustentada |
|---|---|
| ¿Está completo? | No. HALL-01 y entregables formales pendientes impiden declararlo completo. |
| ¿Cumple ambas guías? | Cumple gran parte del objetivo técnico; no todo. Falla la invariante en un caso y faltan Word/capturas/trazabilidad completa. Hay diferencias justificadas de imagen y nombres. |
| ¿La estructura es correcta? | Sí en servicios, paquetes, capas y ubicación de componentes, con los equivalentes de nombres documentados. |
| ¿Los tres microservicios funcionan? | Sí; compilación, HTTP, integración y salud se verificaron realmente. |
| ¿La arquitectura hexagonal está bien implementada? | Sí en separación y dependencia de puertos; la equivalencia total de repositorios está afectada por HALL-01. |
| ¿Docker y RabbitMQ funcionan de verdad? | Sí; despliegue, red, publicación, consumo y recuperación fueron probados. |
| ¿Qué falta para entregar? | Corregir/probar HALL-01, actualizar conclusiones, finalizar Word/portada/C4/capturas, aportar historia disponible y realizar publicación/entrega por decisión del propietario. |
| ¿Qué falta para sustentar? | Preparar evidencia actual coherente, practicar el flujo por UUID y explicar reglas, capas, confirmaciones, diferencias de guías y límites sin atribuir garantías inexistentes. |
