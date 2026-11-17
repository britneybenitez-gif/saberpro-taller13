# Matriz de cumplimiento del Taller 13

Fecha: 16/11/2026 UTC. **El taller no está completo para entrega**: funciona el ecosistema y pasan las suites existentes, pero HALL-01 vulnera la invariante de texto no vacío y la equivalencia entre repositorios. También faltan el informe en la plantilla Word, capturas actuales y completar la entrega/publicación. No se hicieron correcciones de código.

## Fuentes y criterio de valoración

- **G:** [guía paso a paso, 5 páginas](../guias/lisw2_guia_paso_a_paso_taller13_estudiantes.pdf), leída completa. SHA-256 `dfe9340b942e177947dd65b3818cef8af32de62877dc643989b44100fa66a907`.
- **E:** [entrega final, 3 páginas](../guias/lisw2_taller13_entrega_final_DDDHexagonalDocker.pdf), leída completa. SHA-256 `bbd2e7f7c7dacc3d46dcc3771804f7efe90ed34be6dcc8660ab72637be771631`.
- **U:** peticiones del usuario de implementación y auditoría. Las guías son especificaciones académicas; no autorizan publicación ni cambios de código en esta etapa.
- **✅ CUMPLE:** contenido y comportamiento comprobados en el alcance indicado. **⚠️ PARCIAL:** deficiencia concreta o alcance incompleto. **❌ NO CUMPLE:** evidencia demuestra ausencia o comportamiento incorrecto. **❓ NO VERIFICADO:** falta prueba suficiente.

Las rutas abreviadas de Question parten de `question-service/src/main/java/co/edu/unicauca/saberpro/question/`; Validation y Lifecycle usan sus paquetes propios. Los 99 enunciados siguientes conservan literalmente el checklist solicitado. La valoración de los entregables oficiales se presenta además en una tabla separada: «pendiente identificado» no significa «entregable terminado».

## Índice de evidencia

| Clave | Evidencia real |
|---|---|
| TD / TA / TV / TL / IT | [Reportes Maven por clase](evidencias/resumen-pruebas.json), [log Java 17](evidencias/maven-java17-integration.log), [fichas de pruebas](EXPLICACION_ARCHIVOS.md). TD: dominio; TA: aplicación; TV: Validation; TL: persistencia Lifecycle; IT: integración completa. |
| PURE | [Compilación Java pura](evidencias/dominio-puro.txt). |
| SJ / SM | [28 HTTP JPA](evidencias/smoke-jpa.json) / [28 HTTP memoria](evidencias/smoke-memory.json), más aserciones y Management. |
| HALL-01 | [Sonda Java fallida](evidencias/dominio-normalizacion.log) y [cuatro escenarios HTTP](evidencias/http-normalizacion.json). |
| DO | [Build/arranque](evidencias/compose-up-build.log), [configuración](evidencias/compose-config.yml), [estado al cierre](evidencias/compose-ps-cierre.txt), [red](evidencias/red-saberpro-net.json), [JVM](evidencias/java-contenedor.txt). |
| MQ | [HTTP de Management, recibos y logs correlacionados](evidencias/estado-runtime.json); [logs Lifecycle](evidencias/logs-lifecycle-service.txt). |
| INV / GR | [Inventario clasificado](evidencias/clasificacion-git.json), [explicación individual](EXPLICACION_ARCHIVOS.md), [Git inicial](evidencias/git-inicial.txt). |
| DM / IM / LC | [Parse Mermaid](evidencias/mermaid-parse.json), [revisión visual](EXPLICACION_IMAGENES.md), [enlaces locales](evidencias/enlaces-locales.json). |

## Resumen del checklist solicitado

**99 ítems: 95 ✅, 3 ⚠️, 1 ❌ y 0 ❓.** Esta cuenta no es una nota: contiene ítems de existencia y otros que permiten declarar un pendiente, y no sustituye la rúbrica ponderada.

| Grupo | ✅ | ⚠️ | ❌ | ❓ |
|---|---:|---:|---:|---:|
| A. DDD Y DOMINIO | 13 | 0 | 1 | 0 |
| B. ARQUITECTURA HEXAGONAL | 12 | 0 | 0 | 0 |
| C. MICROSERVICIOS | 9 | 0 | 0 | 0 |
| D. RABBITMQ Y COMUNICACIÓN ASÍNCRONA | 9 | 0 | 0 | 0 |
| E. API REST | 7 | 1 | 0 | 0 |
| F. DOCKER | 14 | 0 | 0 | 0 |
| G. PERSISTENCIA | 6 | 1 | 0 | 0 |
| H. PRUEBAS | 11 | 0 | 0 | 0 |
| I. DOCUMENTACIÓN Y ENTREGA | 14 | 1 | 0 | 0 |

## A. DDD Y DOMINIO

| ID | Requisito literal | Estado | Archivo/componente | Evidencia y alcance | Fuente | Observaciones |
|---|---|---|---|---|---|---|
| A01 | Existe el agregado Pregunta. | ✅ CUMPLE | Pregunta.java | 14 casos de PreguntaTest; agregado y transición inspeccionados. | G p.3; E p.1 | UUID, estado, VO y lista encapsulados. |
| A02 | El dominio está escrito en Java puro. | ✅ CUMPLE | domain/** | scripts/check-domain.sh ejecutado: javac --release 17 sin classpath externo [PURE]. | G p.3; E p.1 | Solo tipos Java y clases del núcleo. |
| A03 | El dominio no depende de Spring ni de JPA. | ✅ CUMPLE | domain/** | Revisión de imports/anotaciones y compilación aislada [PURE]. | G p.2–3; E p.1 | No hay Spring/JPA dentro del dominio. |
| A04 | Existe Enunciado como objeto de valor inmutable. | ✅ CUMPLE | Enunciado.java | record inmutable y campo String inspeccionados; tests ordinarios aprobados [TD]. | G p.3 | Inmutabilidad cumplida; validación incorrecta en HALL-01 se evalúa en A08. |
| A05 | Existe Justificacion como objeto de valor inmutable. | ✅ CUMPLE | Justificacion.java | record; prueba de 14/15 caracteres, null y normalización [TD]. | G p.3 | Normaliza antes de contar. |
| A06 | Existe OpcionRespuesta como objeto de valor inmutable. | ✅ CUMPLE | OpcionRespuesta.java | record String/boolean sin setters; copia de lista del agregado [TD]. | G p.3 | Inmutable; HALL-01 afecta aceptación de su texto, no su mutabilidad. |
| A07 | Existe ReglaNegocioException. | ✅ CUMPLE | ReglaNegocioException.java | RuntimeException lanzada por VO/agregado; HTTP 400 en casos ordinarios [TD/SJ/SM]. | G p.3 | Separa regla de negocio de errores técnicos. |
| A08 | El enunciado no admite valores nulos o vacíos. | ❌ NO CUMPLE | Enunciado.java:8–11 | Sonda Java y HTTP: NUL+tab+espacio queda vacío; memory devuelve 201 y luego 202 [HALL-01]. | G p.3 | ❌ La condición se valida antes de trim; el valor final puede ser vacío. |
| A09 | La justificación exige mínimo 15 caracteres. | ✅ CUMPLE | Justificacion.java | PreguntaTest y HTTP con justificación corta →400 en ambos modos [TD/SJ/SM]. | G p.3 | Mínimo 15 tras trim, no la longitud previa. |
| A10 | Las preguntas nuevas empiezan en BORRADOR. | ✅ CUMPLE | Pregunta.crear | POST válido en ambos modos devuelve estado BORRADOR [SJ/SM]. | G p.3; U/T12 | El estado inicial no depende del repositorio. |
| A11 | Para enviar a revisión se exigen exactamente cuatro opciones. | ✅ CUMPLE | Pregunta.enviarRevision | Casos tres/cinco opciones rechazados; HTTP 400 con tres [TD/SJ/SM]. | G p.3 | Cuatro se exige al revisar; se admiten borradores incompletos. |
| A12 | Existe exactamente una respuesta correcta. | ✅ CUMPLE | Pregunta.validarInvariantes | Cero/dos correctas rechazadas en dominio; dos por HTTP; una permite revisión [TD/SJ/SM]. | G p.3 | La regla se aplica al cambio de estado, no a todos los borradores. |
| A13 | Se impide enviar dos veces una pregunta a revisión. | ✅ CUMPLE | Pregunta.enviarRevision | Reenvío HTTP 400 en ambos modos; concurrencia un 202 y un 400 [IT/SJ/SM]. | U/T12; coherente con G p.3 | No se repite la transición BORRADOR→PENDIENTE. |
| A14 | El estado cambia correctamente a PENDIENTE_REVISION. | ✅ CUMPLE | Pregunta.enviarRevision | Respuesta 202, consulta y recibo con PENDIENTE_REVISION [SJ/SM/MQ]. | G p.3,5 | Comprobado con ID correlacionado. |

## B. ARQUITECTURA HEXAGONAL

| ID | Requisito literal | Estado | Archivo/componente | Evidencia y alcance | Fuente | Observaciones |
|---|---|---|---|---|---|---|
| B01 | Existe CrearPreguntaUseCase. | ✅ CUMPLE | domain/ports/inbound/CrearPreguntaUseCase.java | Controller inyecta interfaz; creación, consulta/listado ejecutados [IT/SJ/SM]. | G p.2–3; E p.2 | crear, buscar y listar. |
| B02 | Existe EvaluarPreguntaUseCase conforme al alcance de la guía. | ✅ CUMPLE | domain/ports/inbound/EvaluarPreguntaUseCase.java | QuestionApplicationService lo implementa; revisión real [IT/SJ/SM]. | E p.2 | Evalúa preparación para revisión; el PDF no define calificar/aprobar. |
| B03 | Existe PreguntaRepositoryPort. | ✅ CUMPLE | domain/ports/outbound/PreguntaRepositoryPort.java | Dos implementaciones usadas por el mismo servicio y mismo protocolo [IT/SJ/SM]. | G p.3; E p.2 | save/findById/findAll/actualizar. |
| B04 | Existe el puerto de notificaciones correspondiente. | ✅ CUMPLE | domain/ports/outbound/NotificacionRevisionPort.java | RabbitMQPublisher implementa y aplicación invoca el puerto [MQ/IT]. | E p.1–2; U | Nombre equivalente conservado con autorización del usuario; árbol E lo llama NotificacionPort. |
| B05 | Existe QuestionApplicationService. | ✅ CUMPLE | application/QuestionApplicationService.java | Tests de aplicación, API y compilación Java pura [TA/IT/PURE]. | G p.2; E p.2 | Orquesta mediante tres puertos; no importa Spring. |
| B06 | Existe JpaQuestionAdapter. | ✅ CUMPLE | infrastructure/jpa/JpaQuestionAdapter.java | 10 casos ApiJpaIntegrationTest y 28 respuestas smoke JPA [IT/SJ]. | G p.3; E p.2 | JPA/H2 reales, bloqueo y rollback. |
| B07 | Existe InMemoryQuestionAdapter. | ✅ CUMPLE | infrastructure/memory/InMemoryQuestionAdapter.java | Tests unitarios, 10 integrados y 28 respuestas smoke memory [TA/IT/SM]. | G p.3; E p.2 | HashMap real; defecto de equivalencia se informa en G07. |
| B08 | Existe una configuración correcta de inyección de dependencias. | ✅ CUMPLE | HexagonalConfiguration.java | @Bean y @ConditionalOnProperty; ambas aplicaciones arrancaron y ejecutaron flujos [IT/SJ/SM]. | G p.3 | Dependencias enlazadas fuera del dominio. |
| B09 | Se puede cambiar entre JPA y memoria mediante configuración. | ✅ CUMPLE | app.repository/APP_REPOSITORY | Despliegue principal jpa y contenedor temporal memory con la misma imagen [SJ/SM]. | G p.3 | Cambio efectivo por variable; no se alteró ninguna clase. |
| B10 | Los controladores delegan correctamente a los casos de uso. | ✅ CUMPLE | QuestionRestController.java | Constructor recibe puertos; endpoints probados y rutas inspeccionadas [IT/SJ/SM]. | G p.2; E p.2 | Adapta HTTP y DTO; no usa SpringData directamente. |
| B11 | Las implementaciones tecnológicas no están dentro del dominio. | ✅ CUMPLE | domain y infrastructure | Imports/ubicaciones revisados y núcleo compilado aisladamente [PURE]. | G p.2–3; E p.1–2 | Entidades, HTTP y RabbitMQ están fuera del dominio. |
| B12 | El cambio de adaptador no requiere modificar el código del dominio. | ✅ CUMPLE | HexagonalConfiguration + dos adaptadores | Mismos archivos de dominio y mismo servicio en JPA/Memory; hashes preservados [IT/SJ/SM]. | G p.3 | Arquitectura intercambiable; HALL-01 impide afirmar equivalencia total de comportamiento. |

## C. MICROSERVICIOS

| ID | Requisito literal | Estado | Archivo/componente | Evidencia y alcance | Fuente | Observaciones |
|---|---|---|---|---|---|---|
| C01 | Existe validation-service y usa el puerto 8082. | ✅ CUMPLE | validation-service/application.properties y Compose | HTTP health 200 en 8082 y contenedor saludable [DO/SJ/SM]. | G p.2,4; E p.1–2 | Puerto real y mapeado. |
| C02 | Existe ValidationRestController funcional. | ✅ CUMPLE | ValidationRestController.java | 6 casos ValidationApiTest más endpoint real válido 200/límite 400 [TV/SJ/SM]. | G p.2; E p.2 | Valida estructura y límites técnicos. |
| C03 | Existe comunicación síncrona REST para validación. | ✅ CUMPLE | RestValidationAdapter.java | Límite remoto 400; parada Validation produce 503 sin guardar, recuperación 201 [SJ/SM]. | E p.3; U | No se simula Validation en la integración. |
| C04 | Existe question-service y usa el puerto 8083. | ✅ CUMPLE | question-service/application.properties y Compose | HTTP y health en 8083, contenedor saludable [DO/SJ]. | G p.2,4–5; E p.1–2 | El puerto antiguo 8080 queda en documentos históricos. |
| C05 | Existe QuestionRestController funcional. | ✅ CUMPLE | QuestionRestController.java | GET/POST/detail/revisión, errores y Location verificados [IT/SJ/SM]. | G p.2,5; E p.2 | El defecto semántico HALL-01 se registra en A08/E05/G07. |
| C06 | Existe lifecycle-service y usa el puerto 8084. | ✅ CUMPLE | lifecycle-service/application.properties y Compose | Health 200 y recibos por 8084 [DO/MQ]. | G p.2,4; E p.1–2 | Servicio y almacenamiento independientes. |
| C07 | Existe QuestionReviewListener. | ✅ CUMPLE | QuestionReviewListener.java | @RabbitListener real; logs con preguntaId/eventId y recibos [MQ]. | G p.2,5; E p.2 | Consume JSON sin compartir clases del productor. |
| C08 | Los microservicios pueden ejecutarse conjuntamente. | ✅ CUMPLE | docker-compose.yml y tres módulos | Cuatro contenedores saludables simultáneamente; flujo completo [DO/MQ]. | G p.4; E p.1 | RabbitMQ es infraestructura adicional a los tres microservicios. |
| C09 | La organización de paquetes corresponde a la guía institucional. | ✅ CUMPLE | Paquetes co.edu.unicauca.saberpro.validation/question/lifecycle | Inventario y revisión individual de clases; compilación de los tres [INV/IT]. | G p.2; E p.2 | Capas y nombres principales coinciden; equivalentes Pregunta/OpcionRespuesta/NotificacionRevisionPort explicados abajo. |

## D. RABBITMQ Y COMUNICACIÓN ASÍNCRONA

| ID | Requisito literal | Estado | Archivo/componente | Evidencia y alcance | Fuente | Observaciones |
|---|---|---|---|---|---|---|
| D01 | RabbitMQ está configurado correctamente. | ✅ CUMPLE | Compose, RabbitTopology y properties | Broker real, health, Management y cola durable [DO/MQ]. | G p.4–5 | guest/guest según guía local. |
| D02 | Question Service publica mensajes reales. | ✅ CUMPLE | RabbitMQPublisher.java | POST 202 seguido de recibo y log Lifecycle; contadores publish/deliver/ack [MQ/SJ/SM]. | G p.5; E p.3 | Mensajes JSON persistentes con confirms. |
| D03 | Existe la cola `pregunta.revisar.queue`. | ✅ CUMPLE | RabbitTopology.java en ambos servicios | API Management devuelve nombre exacto y durable=true [MQ]. | G p.5 | Cola principal distinta de la cola aislada de tests Lifecycle. |
| D04 | Lifecycle Service consume los mensajes mediante @RabbitListener. | ✅ CUMPLE | QuestionReviewListener.java | Anotación y ejecución contrastadas con logs y recibos del mismo ID [MQ]. | G p.2,5; E p.2 | ACK después de procesamiento correcto. |
| D05 | El evento contiene un identificador correcto de la pregunta. | ✅ CUMPLE | QuestionReviewEvent y QuestionReviewMessage | ID creado=ID en respuesta de revisión=ID en log/recibo [MQ/SJ/SM]. | G p.5; E p.3 | eventId es otro UUID determinista, no se confunde con preguntaId. |
| D06 | RabbitMQ puede visualizarse en localhost:15672. | ✅ CUMPLE | RabbitMQ Management :15672 | GET / devuelve HTML RabbitMQ 200; API autenticada de cola funciona [MQ]. | G p.4–5 | Disponibilidad verificada; no se tomó captura de la interfaz en navegador. |
| D07 | El consumidor registra el procesamiento del mensaje. | ✅ CUMPLE | QuestionReviewListener + ReviewProcessingService | Log «Revisión procesada» y comprobante persistido con timestamp [MQ]. | G p.5 | No es solo un mensaje de log simulado por Question. |
| D08 | Se maneja adecuadamente el fallo de publicación. | ✅ CUMPLE | RabbitMQPublisher + adapters.actualizar | Broker parado →503/BORRADOR; recuperación →202. NACK/timeout/return unitarios y no-route real [IT/SJ/SM]. | U; E p.3 | Ventana distribuida ACK/commit documentada L01; no se exige Outbox en los PDF. |
| D09 | Se verifica el flujo completo desde el POST hasta el consumidor. | ✅ CUMPLE | POST revisión→RabbitMQ→Lifecycle | Dos IDs reales, respuestas 201/202, recibos 200 y logs correlacionados [MQ]. | G p.5; E p.3 | No se deduce consumo solo de que la cola quede vacía. |

## E. API REST

| ID | Requisito literal | Estado | Archivo/componente | Evidencia y alcance | Fuente | Observaciones |
|---|---|---|---|---|---|---|
| E01 | GET /api/preguntas devuelve HTTP 200. | ✅ CUMPLE | QuestionRestController.listar | GET colección 200; caso vacío [] en ApiEmptyIntegrationTest [IT/SJ/SM]. | G p.5 | Lista JSON; orden estable por UUID. |
| E02 | POST /api/preguntas crea preguntas y devuelve HTTP 201. | ✅ CUMPLE | QuestionRestController.crear | POST válido →201, Location, UUID y BORRADOR [IT/SJ/SM]. | G p.5 | Se guarda después de validación remota satisfactoria. |
| E03 | GET /api/preguntas/{id} permite consultar por ID. | ✅ CUMPLE | QuestionRestController.buscar | GET del UUID recién creado →200 y mismo contenido [SJ/SM]. | U/T12 | No inventa otro identificador. |
| E04 | Una pregunta inexistente devuelve HTTP 404. | ✅ CUMPLE | buscar + ApiExceptionHandler | UUID inexistente →404 en ambos modos [SJ/SM]. | U/T12 | UUID mal formado →400, no 404. |
| E05 | Los errores de reglas de negocio producen HTTP 400. | ⚠️ PARCIAL | VO, Pregunta y ApiExceptionHandler | Reglas ordinarias →400; HALL-01 no lanza excepción en memoria y responde 201/202 [HALL-01/SJ/SM]. | G p.3; E p.3 | ⚠️ La traducción de excepción funciona, pero no todos los datos semánticamente inválidos la generan. |
| E06 | POST /api/preguntas/{id}/enviar-revision devuelve HTTP 202 cuando publica correctamente. | ✅ CUMPLE | QuestionRestController.enviarRevision | 202 cuando confirma el broker y termina la actualización; fallo →503 [SJ/SM/IT]. | G p.5 | No espera finalización del consumidor; sí confirma publicación. |
| E07 | Los endpoints funcionan mediante Thunder Client o peticiones HTTP equivalentes. | ✅ CUMPLE | Colección/.http + scripts y TestRestTemplate | Peticiones HTTP reales equivalentes ejecutadas [IT/SJ/SM]. | G p.5; U | No se afirma uso de la interfaz Thunder Client. |
| E08 | Los contratos JSON son correctos. | ✅ CUMPLE | DTOs, JsonConfiguration, evento y response | Tipos erróneos y estructura inválida rechazados; cuerpos reales conservados [TV/IT/SJ/SM]. | U: compatibilidad T12 y contratos | Request texto simple; response VO anidado. HALL-01 es validación de valor, no sintaxis JSON. |

## F. DOCKER

| ID | Requisito literal | Estado | Archivo/componente | Evidencia y alcance | Fuente | Observaciones |
|---|---|---|---|---|---|---|
| F01 | Existe Dockerfile en validation-service. | ✅ CUMPLE | validation-service/Dockerfile | Imagen construida y servicio iniciado en 8082 [DO]. | G p.3; E p.1 | Compila dentro de la etapa Maven. |
| F02 | Existe Dockerfile en question-service. | ✅ CUMPLE | question-service/Dockerfile | Imagen construida y servicio iniciado en 8083 [DO]. | G p.3; E p.1 | Misma imagen usada en pruebas JPA/memory. |
| F03 | Existe Dockerfile en lifecycle-service. | ✅ CUMPLE | lifecycle-service/Dockerfile | Imagen construida y consumidor iniciado en 8084 [DO/MQ]. | G p.3; E p.1 | JAR separado del productor. |
| F04 | Los tres utilizan una imagen compatible con Java 17. | ✅ CUMPLE | Tres Dockerfile | Etapas Temurin 17; java -version real en contenedor y verify Java 17 [DO/IT]. | G p.3; E p.1; U | Alternativa a openjdk:17-jdk-slim expresamente autorizada si no disponible; diferencia documentada. |
| F05 | Los Dockerfile configuran WORKDIR, COPY, EXPOSE y ENTRYPOINT. | ✅ CUMPLE | Tres Dockerfile | Directivas inspeccionadas y ENTRYPOINT ejecutado [DO]. | G p.1,3 | EXPOSE individual 8082/8083/8084; COPY desde etapa build. |
| F06 | Los JAR se generan correctamente. | ✅ CUMPLE | POMs y target/*.jar | verify -Pintegration genera tres JAR y Docker los ejecuta [IT/DO]. | G p.3; E p.3 | Build con skipTests por sí solo no cuenta como pruebas. |
| F07 | Existe docker-compose.yml en la raíz. | ✅ CUMPLE | docker-compose.yml raíz | config y up desde raíz efectivos [DO]. | G p.2–4; E p.1–2 | POM raíz solo agrega; no añade servicio. |
| F08 | Existe la red bridge saberpro-net. | ✅ CUMPLE | networks.saberpro-net | docker network inspect: name saberpro-net, driver bridge; DNS interno probado [DO]. | G p.1,3–4; E p.1 | No se usa localhost entre servicios. |
| F09 | Se exponen correctamente los puertos 8082, 8083, 8084, 5672 y 15672. | ✅ CUMPLE | Compose ports y properties | ps y peticiones reales a 8082/8083/8084/15672; AMQP real por 5672 [DO/MQ/IT]. | G p.4; E p.1 | EXPOSE y mapeo ports cumplen funciones diferentes. |
| F10 | Las variables de entorno están configuradas. | ✅ CUMPLE | Compose environment y properties | APP_REPOSITORY, URLs, broker y DB_URL efectivos en ejecución [DO/SJ/SM]. | G p.4 | Defaults locales y valores de contenedor diferenciados. |
| F11 | Las dependencias de arranque están configuradas. | ✅ CUMPLE | Compose depends_on/healthcheck | Question espera RabbitMQ y Validation; Lifecycle espera RabbitMQ; todos healthy [DO]. | G p.1,4 | Se espera salud, no solo proceso iniciado. |
| F12 | `docker compose config` es válido. | ✅ CUMPLE | docker-compose.yml | docker compose config finalizó con código 0 [DO]. | G p.3–4; U | Configuración resuelta guardada. |
| F13 | `docker compose up -d --build` funciona. | ✅ CUMPLE | Compose y Dockerfile | docker compose up -d --build finalizó 0 [DO]. | G p.4; E p.1 | Se usó caché de capas; construcción previa desde fuentes preservada. |
| F14 | Los contenedores se mantienen ejecutándose correctamente. | ✅ CUMPLE | Cuatro contenedores principales | Estado saludable antes/después de pruebas y al cierre [DO]. | G p.4–5 | Las paradas deliberadas se recuperaron; solo se retiró el temporal de auditoría. |

## G. PERSISTENCIA

| ID | Requisito literal | Estado | Archivo/componente | Evidencia y alcance | Fuente | Observaciones |
|---|---|---|---|---|---|---|
| G01 | JPA está implementado correctamente. | ✅ CUMPLE | JpaQuestionAdapter/SpringDataPreguntaRepository | API JPA real, lecturas, guardado, bloqueo, rollback y tests [IT/SJ]. | G p.3; E p.2 | Aislamiento transaccional local observado; no transacción distribuida. |
| G02 | Las entidades JPA están separadas del dominio. | ✅ CUMPLE | QuestionRecord/OpcionEmbeddable | @Entity/@Embeddable solo en infraestructura; domain compila sin JPA [PURE/INV]. | G p.3; E p.1 | El agregado no es una entidad Hibernate. |
| G03 | Existen los mapeadores necesarios. | ✅ CUMPLE | QuestionDomainMapper | Conversiones completas de ID/textos/opciones/estado; API persiste y recupera [IT/SJ]. | E p.2 | Rehidratar VO revela HALL-01 en JPA y explica su diferencia con memory. |
| G04 | H2 o PostgreSQL está configurado conforme a la guía. | ✅ CUMPLE | H2 en POM/properties/Compose | Base real en pruebas; DB_URL archivo y volúmenes en Compose [IT/DO]. | E p.1 | E permite PostgreSQL/H2; G introduce PostgreSQL pero su YAML no declara ese servicio. |
| G05 | Los repositorios permiten guardar y consultar preguntas. | ✅ CUMPLE | PreguntaRepositoryPort y adaptadores | save/findById/findAll ejecutados en ambas implementaciones [IT/SJ/SM]. | G p.3 | Revisión usa actualizar para aplicar cambio y notificar dentro del límite local. |
| G06 | El adaptador HashMap funciona. | ✅ CUMPLE | InMemoryQuestionAdapter | HashMap real, copias defensivas, rollback y protocolo ordinario aprobado [TA/IT/SM]. | G p.3 | Almacenamiento efímero esperado; defecto de contrato señalado a continuación. |
| G07 | Ambos adaptadores respetan los mismos contratos. | ⚠️ PARCIAL | Enunciado/OpcionRespuesta + ambos adaptadores | Misma entrada de control: JPA400 vs memory201→202→Lifecycle200 [HALL-01]. | G p.3; E p.1,3 | ⚠️ Intercambio configurable, pero no equivalencia total de admisión de valores. |

## H. PRUEBAS

| ID | Requisito literal | Estado | Archivo/componente | Evidencia y alcance | Fuente | Observaciones |
|---|---|---|---|---|---|---|
| H01 | Existen pruebas unitarias del dominio. | ✅ CUMPLE | PreguntaTest.java | 14 tests de dominio aprobados [TD]. | G p.3; U | Falta el caso HALL-01; existencia/paso no implica cobertura exhaustiva. |
| H02 | Existen pruebas de los casos de uso. | ✅ CUMPLE | QuestionApplicationServiceTest e IntercambiabilidadTest | 6 casos aprobados con dobles de puertos donde corresponde [TA]. | U; G p.3 | Aislan orquestación y rollback de memoria. |
| H03 | Existen pruebas de JPA. | ✅ CUMPLE | ApiJpaIntegrationTest y ReviewProcessingTest | 10 casos API JPA y 2 de recibos H2 aprobados [IT/TL]. | G p.3; U | JPA real en contextos de test separados. |
| H04 | Existen pruebas del adaptador en memoria. | ✅ CUMPLE | ApiMemoryIntegrationTest y tests de aplicación | 10 casos integrados memory más tests unitarios aprobados [IT/TA]. | G p.3; U | Contrato HALL-01 aún no cubierto por la suite Maven. |
| H05 | Existen pruebas de los controladores REST. | ✅ CUMPLE | ValidationApiTest y Api*IntegrationTest | HTTP real con contextos Spring, puertos de prueba y despliegue [TV/IT]. | G p.5; U | No son únicamente llamadas directas a métodos de controller. |
| H06 | Existen pruebas de reglas de negocio inválidas. | ✅ CUMPLE | PreguntaTest, Api* y ValidationApiTest | Justificación corta, opciones inválidas, reenvío, UUID y JSON inválidos [TD/IT/TV]. | G p.3,5; U | La cobertura de casos inválidos existe pero no es completa: HALL-01. |
| H07 | Existen pruebas del flujo de RabbitMQ. | ✅ CUMPLE | RabbitMQPublisherTest y LifecycleIntegrationTest | 4 casos unitarios producer; broker real, no-route, duplicado y DLQ en integración [IT]. | G p.5; U | Se distingue Mockito unitario de AMQP real. |
| H08 | Existen pruebas de integración entre servicios. | ✅ CUMPLE | AdditionalApiContract/LifecycleIntegrationTest/smoke | Question→Validation y Question→Rabbit→Lifecycle reales [IT/SJ/SM]. | E p.3; U | Sin mocks en los escenarios integrados. |
| H09 | Los tres microservicios compilan. | ✅ CUMPLE | POMs de tres servicios | clean test individual y verify Java 17 completados [IT]. | G p.3; U | 24 Question, 6 Validation, 2 Lifecycle en ejecución individual. |
| H10 | Las pruebas Maven terminan satisfactoriamente. | ✅ CUMPLE | Surefire/Failsafe | 55 tests, 0 fallos, 0 errores, 0 omitidas [IT]. | U | La sonda adicional de auditoría sí falla por HALL-01; no se oculta ni se cuenta dentro de los 55. |
| H11 | El despliegue Docker fue verificado realmente. | ✅ CUMPLE | Compose + HTTP + AMQP | Arranque real, salud, red, logs, recibos y fallos controlados [DO/MQ/SJ/SM]. | G p.4–5; E p.3 | No se limitó a inspección de YAML. |

## I. DOCUMENTACIÓN Y ENTREGA

| ID | Requisito literal | Estado | Archivo/componente | Evidencia y alcance | Fuente | Observaciones |
|---|---|---|---|---|---|---|
| I01 | README.md está actualizado al Taller 13. | ✅ CUMPLE | README.md | Comandos, puertos y flujo actual contrastados; enlaces actuales resuelven [LC/DO]. | G p.2; E p.2 | Actualizado a T13; debe incorporar el nuevo hallazgo HALL-01 cuando se apruebe su corrección. |
| I02 | La estructura de carpetas coincide con las guías. | ✅ CUMPLE | Raíz y paquetes por servicio | 145 archivos clasificados y clases inspeccionadas individualmente [INV]. | G p.2; E p.2 | Se conservan nombres españoles exigidos en el texto; diferencias explícitas más abajo. |
| I03 | Existen diagramas C4 nivel 1. | ✅ CUMPLE | docs/arquitectura/diagramas.md, bloque 1 | Contexto revisado y sintaxis parseada [DM]. | E p.3 | Actores y sistema; no requiere detallar procesos aquí. |
| I04 | Existen diagramas C4 nivel 2. | ✅ CUMPLE | Mismo documento, bloque 2 | Tres servicios, RabbitMQ, H2 y red contrastados con Compose [DM/DO]. | E p.3 | H2 embebido no es un quinto contenedor. |
| I05 | Existen diagramas C4 nivel 3. | ✅ CUMPLE | Mismo documento, bloque 3 | Zoom de Question y sus puertos/adaptadores contrastado con clases [DM/INV]. | E p.3 | Ámbito explícito: Question Service. |
| I06 | Existen diagramas C4 nivel 4. | ✅ CUMPLE | Mismo documento, bloque 4 | Clases de dominio, puertos y relaciones verificadas; parse válido [DM/INV]. | E p.3 | El dibujo no prueba reglas de ejecución ni corrige HALL-01. |
| I07 | Existe documentación del flujo REST. | ✅ CUMPLE | arquitectura.md y secuencia creación | REST síncrono documentado y probado por límite/caída real [SJ/SM]. | E p.3 | Validation técnica y negocio en dominio diferenciados. |
| I08 | Existe documentación del flujo AMQP. | ✅ CUMPLE | arquitectura.md y secuencia revisión | Evento v1, cola, confirms, ACK, recibo y límite de atomicidad comprobados [MQ/IT]. | G p.5; E p.3 | HTTP 202 no significa revisión académica terminada. |
| I09 | Existe documentación de los puertos y adaptadores. | ✅ CUMPLE | arquitectura.md, decisiones.md y fichas de clases | Puertos, implementaciones y selección por beans contrastados [INV/PURE]. | G p.3; E p.1 | Contratos documentados; limitación HALL-01 añadida en esta auditoría. |
| I10 | Existe trazabilidad entre los Cortes I, II y III. | ⚠️ PARCIAL | docs/arquitectura/trazabilidad.md | Hay tabla I/II/III, pero no código/commits de Corte I ni evidencia histórica completa de II [GR]. | E p.1,3 | ⚠️ Explica conceptos; no demuestra íntegramente HU03/Microkernel/Builder/State en su evolución histórica. |
| I11 | Existe un informe técnico final o están identificados sus pendientes. | ✅ CUMPLE | docs/INFORME_TECNICO.md y pendientes | Contenido leído; identifica plantilla, portada y capturas pendientes [INV]. | E p.3; redacción U | Cumple este ítem «informe o pendientes»; no equivale a tener el entregable Word terminado. |
| I12 | Se contempla la plantilla institucional Formato-Documento.docx. | ✅ CUMPLE | INFORME_TECNICO.md | Menciona Formato-Documento.docx y ausencia; no hay docx en el proyecto [INV]. | E p.3; redacción U | Cumple «se contempla»; requisito oficial de entrega en plantilla sigue NO CUMPLE. |
| I13 | Existen capturas reales de Docker, o están identificadas como pendientes. | ✅ CUMPLE | docs/evidencias/README.md | Enumera captura auténtica pendiente de compose ps [IM]. | E p.3; redacción U | Cumple «o identificada pendiente»; aún no existe captura final Docker. |
| I14 | Existen capturas reales de RabbitMQ, o están identificadas como pendientes. | ✅ CUMPLE | docs/evidencias/README.md | Enumera captura auténtica pendiente de Management [IM]. | E p.3; redacción U | Cumple «o identificada pendiente»; logs/API actuales no son pantallazos. |
| I15 | Existe documentación para reproducir las pruebas en Thunder Client. | ✅ CUMPLE | docs/pruebas/README.md/.json/.http | Colección y protocolo inspeccionados; HTTP equivalente ejecutado [IT/SJ/SM]. | G p.5 | Uso real de GUI e importación no verificados, instrucciones disponibles. |

## Comparación punto por punto con los PDF

| Requisito de la guía y página | Archivo que lo implementa | Evidencia o prueba | Estado | Observaciones |
|---|---|---|---|---|
| G p.2 / E p.1–2: raíz, tres servicios, paquetes y clases principales | Tres carpetas, sus POM/Dockerfile y paquetes | C01–C09/I02; inventario y ejecución | ✅ CUMPLE | La carpeta de trabajo se llama saberpro-taller13; nombres equivalentes del dominio explicados abajo. |
| G p.2–3 / E p.1: domain.model puro, VO y excepción | Pregunta, Enunciado, Justificacion, OpcionRespuesta, ReglaNegocioException | A01–A07/PURE | ✅ CUMPLE | Pureza e inmutabilidad verificadas; validez de valores se valora separadamente. |
| G p.3: enunciado no nulo/vacío | Enunciado.java:8–11 | A08/HALL-01, longitud final cero aceptada | ❌ NO CUMPLE | La protección previa a trim deja un caso inválido. |
| G p.3: justificación de al menos 15 caracteres | Justificacion.java | A09, pruebas límites y HTTP 400 | ✅ CUMPLE | Se cuenta después de trim. |
| G p.3: cuatro opciones y una correcta antes de PENDIENTE_REVISION | Pregunta.java | A11–A14, pruebas inválidas/reenvío/concurrencia | ✅ CUMPLE | Los borradores incompletos son admisibles. |
| G p.3 / E p.1–2: puertos inbound y outbound | CrearPreguntaUseCase, EvaluarPreguntaUseCase, PreguntaRepositoryPort, NotificacionRevisionPort | B01–B05 | ✅ CUMPLE | Notificación conserva nombre equivalente autorizado; evaluar significa preparar revisión. |
| G p.2–3 / E p.2: aplicación, JPA, mapper, memory, API, publisher/listener | Clases homónimas detalladas en inventario | B/C/D/G e integración | ✅ CUMPLE | Responsabilidades y dependencias coinciden. |
| G p.3 / E p.1,3: intercambiabilidad sin cambiar dominio | HexagonalConfiguration + ambos adaptadores | B08–B12/G07; 400 vs201/202 para la misma entrada | ⚠️ PARCIAL | Configurable y flujo ordinario equivalente; HALL-01 rompe el contrato en un caso límite. |
| G p.3: generar JAR ejecutable por microservicio | Tres pom.xml/Dockerfile y wrappers | F06/H09/H10; verify real | ✅ CUMPLE | Maven compila en etapa Docker; wrappers disponibles. Windows no ejecutado. |
| G p.1,3 / E p.1: Dockerfile por servicio basado en openjdk:17-jdk-slim | Tres Dockerfile con Temurin17 | F01–F05; java -version y manifiesto previo | ⚠️ PARCIAL | Cumple Java 17/funcionalidad, difiere literalmente de la imagen exigida. U autorizó alternativa cuando el manifiesto no estuviera disponible; decisión justificada, aceptación académica corresponde a docente. |
| G p.1,3: WORKDIR/COPY/EXPOSE/ENTRYPOINT | Tres Dockerfile | F05 y contenedores ejecutando JAR | ✅ CUMPLE | EXPOSE se adapta al puerto propio; COPY usa etapa build. |
| G p.3–4 / E p.1–2: Compose raíz y build de ecosistema | docker-compose.yml | F07/F12/F13; config/up reales | ✅ CUMPLE | Multi-stage permite construir desde fuentes; no se requiere JAR precompilado local. |
| G p.1,3–4 / E p.1: red bridge saberpro-net y DNS | Compose networks | F08; inspección y HTTP interno | ✅ CUMPLE | Nombre explícito y driver bridge. |
| G p.4 / E p.1: mapeos 8082/8083/8084/5672/15672 | Compose ports | F09/DO/MQ | ✅ CUMPLE | Puertos efectivamente accesibles. |
| G p.4: RabbitMQ3-management, host y dependencias | Compose/properties | D01/F10/F11; arranque y reconexión | ✅ CUMPLE | Mantiene nombres de contenedores de la receta y añade salud. |
| E p.1: PostgreSQL/H2 | H2 de Question y Lifecycle, volúmenes | G04/IT/DO; evidencia previa de reinicio | ✅ CUMPLE | H2 aceptado expresamente por E; no hay contenedor PostgreSQL. |
| G p.4–5: up, docker ps, logs y disponibilidad | Compose y aplicaciones | F13/F14; salidas reales y logs | ✅ CUMPLE | Se usaron logs finitos para conservar evidencia; -f es modo de seguimiento. |
| G p.5: GET listado200, POST pregunta201, UUID y cuerpo válido | QuestionRestController | E01–E04; SJ/SM/IT | ✅ CUMPLE | Entradas válidas con 4/1; ID consultable. |
| G p.5 / E p.3: envío asíncrono202 y publicación real | EvaluarPreguntaUseCase/RabbitMQPublisher | D02/D08/D09/E06 | ✅ CUMPLE | Confirma broker; no espera al consumidor. No se exige una latencia numérica en PDF. |
| G p.5: cola pregunta.revisar.queue y recepción Lifecycle | Topología y listener | D03–D07; Management, recibo y log | ✅ CUMPLE | Procesamiento vinculado al UUID creado. |
| G p.5: protocolo con Thunder Client y Dashboard en navegador | Colección y docs/pruebas; Management HTML/API | HTTP equivalente aprobado; GUI no abierta | ❓ NO VERIFICADO | Los endpoints sí funcionan. No se atribuye una instalación/uso/captura GUI no realizados. |
| E p.1,3: demostrar evolución Corte I→Corte II→Corte III | trazabilidad.md y Git base | I10 y comparación T12→T13 | ⚠️ PARCIAL | Hay tabla honesta; faltan fuentes históricas de HU03/Microkernel/Builder/State para demostrar todos los hitos. |
| E p.3: repositorio final entregado en GitHub/plataforma | Rama local feature/taller13-fase2 | Git HEAD sigue e857f96; cambios sin commit/publicación en esta sesión | ❓ NO VERIFICADO | Preparado localmente; usuario reservó publicación. No se consultó estado remoto actualizado ni entrega en plataforma. |
| E p.3: informe consolidado utilizando Formato-Documento.docx | docs/INFORME_TECNICO.md | Contenido existente; no hay DOCX final ni plantilla en proyecto | ❌ NO CUMPLE | Falta incorporar plantilla real, portada y evidencias; reconocer el pendiente no entrega el documento. |
| E p.3: C4 niveles1,2,3,4 | docs/arquitectura/diagramas.md | I03–I06; revisión de contenido y sintaxis | ✅ CUMPLE | Fuentes editables correctas; falta integrarlas legiblemente en el documento Word. |
| E p.3: capturas de consola Docker y RabbitMQ | docs/evidencias/README.md y logs | 11 imágenes históricas; ninguna captura final T13 de esas interfaces | ❌ NO CUMPLE | Evidencia textual real disponible; pantallazos auténticos pendientes. |
| E p.3: flujo201/400/202 sobre contenedores | API y mensajería | SJ/SM/MQ; casos ordinarios y fallos aprobados | ⚠️ PARCIAL | Flujo real correcto; HALL-01 admite un valor inválido con memory, por lo que no cubre todo el contrato de negocio. |

## Diferencias entre las guías y decisiones adoptadas

1. **Nombres del dominio:** E p.1 exige Pregunta/OpcionRespuesta y G p.3 define los VO en español; los árboles de G p.2/E p.2 ilustran Question/AnswerOption. Se conservaron los nombres españoles y se movieron a domain.model. Las capas, paquetes y clases de servicios/infraestructura siguen los árboles. No se duplicaron agregados solo para satisfacer ambos idiomas.
2. **Puertos adicionales:** E p.2 agrega EvaluarPreguntaUseCase y NotificacionPort que G no enumera. Se incorporó EvaluarPreguntaUseCase para comprobar y ejecutar el envío; se conservó NotificacionRevisionPort equivalente, permitido por U. No hay requisito de asignar notas ni aprobar preguntas.
3. **Base de datos:** G p.1 menciona PostgreSQL como infraestructura, pero su YAML p.4 no contiene ese servicio. E p.1 permite PostgreSQL/H2. Se usa H2 con archivos/volúmenes separados y sin acceso cruzado a bases.
4. **Imagen de Java:** ambos PDF citan openjdk:17-jdk-slim. Su manifiesto no estaba disponible en la comprobación de implementación conservada; U autorizó usar alternativa compatible y documentarla. Temurin17 funcionó realmente. Esto satisface el objetivo técnico, pero es una desviación literal que debe explicarse a la profesora.
5. **Compilación del JAR:** G p.3 propone compilar antes y copiar target; E p.1 exige up --build sobre el ecosistema. El multi-stage compila por módulo dentro de Docker, de modo que no depende de un target preexistente en la máquina del estudiante.
6. **Compose:** la receta G tiene version:3.8 y depends_on simple. El archivo actual omite version y usa salud para esperar dependencias. El validador Compose instalado aceptó el archivo y el despliegue funcionó; nombres, red y puertos se conservan.
7. **202 «inmediato»:** se interpreta como aceptación sin esperar el trabajo de Lifecycle. Esperar confirmación del broker hasta el timeout configurado permite cumplir la petición explícita U de no devolver 202 si la publicación falla. No se afirma latencia cero ni confirmación del trabajo académico.
8. **Entregables:** E agrega Word institucional, C4 completos, capturas y trazabilidad semestral. Tener Markdown y logs ayuda a elaborarlos, pero no reemplaza esos artefactos. Los ítems I11–I14 de U permiten identificar pendientes; la tabla oficial los señala sin suavizar la ausencia.

## Diferencias comprobadas frente al Taller 12

| Aspecto | Taller 12, e857f96 | Taller 13 actual | Evidencia |
|---|---|---|---|
| Estructura | Un POM/servicio/src | Tres módulos independientes y POM agregador | Inventario y Maven |
| HTTP de Question | 8080 | 8083 | Compose y HTTP real |
| Listado | No existía GET colección | GET /api/preguntas 200, incluido [] | ApiEmptyIntegrationTest |
| Notificación | LoggingNotificacionAdapter | RabbitMQPublisher + consumidor real | Recibos/logs/Management |
| Revisión | HTTP 200 | HTTP 202 tras confirmación | SJ/SM |
| Despliegue | Sin Dockerfiles/Compose | Tres imágenes + RabbitMQ/red/volúmenes | Config/build/ps |
| C4 | Diagramas del monolito | Niveles1–4 y secuencias actuales | Revisión de fuentes y parser |
| Reglas | Mismo orden isBlank/trim en VO | Defecto heredado HALL-01 aún presente | Git y sonda fallida |

## Evaluación razonada de la rúbrica

| Criterio | Peso | Implementado y verificado | Falta / errores | Cumplimiento estimado del criterio | Aporte |
|---|---:|---|---|---:|---:|
| DDD y hexagonal | 35% | Núcleo puro; agregado/VO; puertos; dos repositorios; compila sin frameworks; casos ordinarios y concurrencia pasan | HALL-01 vulnera valor no vacío y equivalencia; cobertura de esa entrada falta | 90% | 31,5/35 |
| Docker y Compose | 30% | Tres builds/JAR Java 17; cuatro contenedores; puertos, red, volúmenes, salud y recuperaciones reales | Desviación literal de imagen justificada por indisponibilidad y autorizada por U; no se probó cada plataforma externa | 100% del objetivo funcional probado | 30/30 |
| Integración REST y RabbitMQ | 20% | Flujo completo201→202→recibo; errores400/404/503; confirms, rollback, DLQ y duplicados probados | HALL-01 deja pasar entrada inválida en modo memoria; límite ACK/commit conocido y permitido, no oculto | 95% | 19/20 |
| Informe técnico y C4 | 15% | Markdown, cuatro niveles correctos, trazabilidad parcial, protocolos, logs y JSON reales | Word/portada/capturas faltan; historia I/II incompleta; enlaces históricos rotos | 60% | 9/15 |

**Estimación ponderada: 89,5/100 (aproximadamente 90%)**, orientativa, no es la calificación de la docente. Los porcentajes se basan en evidencia y juicio de auditoría, no en contar archivos o checks verdes. Una exigencia literal distinta sobre imagen, nombres o formato puede cambiar esa valoración. La publicación/entrega final sigue pendiente fuera de esta estimación de trabajo local.
