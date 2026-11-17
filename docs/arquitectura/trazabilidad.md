# Trazabilidad de requisitos y evaluación

Fuentes: **G** = `lisw2_guia_paso_a_paso_taller13_estudiantes.pdf` (5 páginas); **E** = `lisw2_taller13_entrega_final_DDDHexagonalDocker.pdf` (3 páginas). Ambos están en `docs/guias/`. **U** = solicitud de implementación adjunta por el usuario, que amplía detalles operativos no definidos en los PDF.

## Requisitos → implementación → prueba

Las rutas Java de Question se expresan desde `question-service/src/main/java/co/edu/unicauca/saberpro/question/`.

| Requisito y fuente | Archivo / componente | Verificación |
|---|---|---|
| Dominio Java puro, G p.3 / E p.1 | `domain/model/*`, `domain/exception/*` | `scripts/check-domain.sh` compila dominio y aplicación sin classpath externo |
| Enunciado obligatorio, G p.3 | `domain/model/Enunciado.java` | `PreguntaTest.enunciadoNuloOVacioSeRechaza` |
| Justificación ≥15, G p.3 | `domain/model/Justificacion.java` | Pruebas de 14, 15 caracteres y null; HTTP 400 |
| Opciones inmutables, G p.3 / U | `domain/model/OpcionRespuesta.java`, `Pregunta.java` | Copia defensiva, rechazo de null y texto vacío |
| 4 opciones y 1 correcta al revisar, G p.3 | `Pregunta.enviarRevision` | Tres/cinco opciones, cero/dos correctas; estado no cambia al fallar |
| BORRADOR y rechazo de reenvío, U/Taller 12 | `Pregunta`, `EstadoPregunta` | Dominio, HTTP 400 y concurrencia |
| CrearPreguntaUseCase, G p.2–3 / E p.2 | `domain/ports/inbound/CrearPreguntaUseCase.java` | Creación, búsqueda y listado por API |
| EvaluarPreguntaUseCase, E p.2 | `domain/ports/inbound/EvaluarPreguntaUseCase.java` | Evaluación de preparación y envío, sin calificaciones inventadas |
| Puerto de repositorio, G p.3 | `domain/ports/outbound/PreguntaRepositoryPort.java` | Contrato ejecutado con dos adaptadores |
| Puerto notificaciones, E p.1–2 | `domain/ports/outbound/NotificacionRevisionPort.java` | RabbitMQPublisher real; equivalente conservado de Taller 12 |
| Aplicación coordinadora, G p.2 / E p.2 | `application/QuestionApplicationService.java` | Compilación Java pura y pruebas de puertos |
| JPA y mapper, E p.2 | `infrastructure/jpa/QuestionRecord.java`, `QuestionDomainMapper.java`, `JpaQuestionAdapter.java` | `ApiJpaIntegrationTest`, almacenamiento y rollback reales |
| HashMap intercambiable, G p.3 | `infrastructure/memory/InMemoryQuestionAdapter.java`, `HexagonalConfiguration.java` | `IntercambiabilidadTest`, `ApiMemoryIntegrationTest`, smoke-memory |
| Estructura de tres servicios, G p.2 / E p.2 | Carpetas `validation-service`, `question-service`, `lifecycle-service` | Reactor Maven y arranque real de contextos |
| REST Validation, G p.2 / U | `validation-service/.../api/ValidationRestController.java`, `RestValidationAdapter.java` | `ValidationApiTest`, integración y caída real del servicio |
| GET colección 200, G p.5 | `api/QuestionRestController.java` | `ApiEmptyIntegrationTest` y pruebas de listado |
| POST creación 201, G p.5 | `api/QuestionRestController.java` | JPA/Memory y scripts HTTP; Location y BORRADOR |
| GET individual 200/404, U | Controller + `buscar` | Pruebas JUnit y smoke |
| Envío 202, G p.5 | Controller + `RabbitMQPublisher.java` | Confirmación AMQP, consumo y comprobante Lifecycle |
| Fallo de publicación no devuelve 202, U | Confirm/return y `PreguntaRepositoryPort.actualizar` | NACK/timeout/return unitarios; no-route real y broker detenido →503/BORRADOR |
| Cola exacta, G p.5 | `RabbitTopology.java` de ambos servicios | Management, RabbitListener y logs con IDs reales |
| Lifecycle consumidor, G p.2 / E p.2 | `lifecycle-service/.../messaging/QuestionReviewListener.java` | `LifecycleIntegrationTest`, deduplicación y DLQ con broker real |
| Tres Dockerfile, G p.3 / E p.1 | `*/Dockerfile` | Compilan y ejecutan Java 17; alternativa de imagen documentada |
| Compose y red bridge, G p.3–4 / E p.1 | `docker-compose.yml` | `docker compose config`, red inspeccionada y contenedores saludables |
| Puertos 8082/8083/8084/5672/15672, G p.4 / E p.1 | `ports` y `server.port` | `docker compose ps`, HTTP y Management |
| PostgreSQL/H2, E p.1 | H2 separado por servicio, volúmenes Compose | Persistencia, consulta y comprobantes |
| Thunder Client, G p.5 | `docs/pruebas/SaberPRO_Taller13.postman_collection.json` | Colección preparada; solicitudes equivalentes ejecutadas por script |
| C4 niveles 1–4, E p.3 | `docs/arquitectura/diagramas.md` | Contexto, contenedores, componentes y clases reales |
| Trazabilidad semestral, E p.1,3 | Tabla siguiente | Distingue hitos institucionales de código observado |
| Informe técnico en plantilla, E p.3 | `docs/INFORME_TECNICO.md` | Contenido disponible; traslado a plantilla oficial pendiente por ausencia del archivo |
| Capturas Docker/RabbitMQ, E p.3 | `docs/evidencias/README.md` | Logs/JSON reales disponibles; capturas de interfaz pendientes |
| Repositorio GitHub, E p.3 / U | Rama local `feature/taller13-fase2` | Sin push/merge; publicación pendiente de revisión del propietario |

## Corte I → Corte III

| Corte y ponderación institucional | Hitos descritos en la guía | Evidencia disponible en este repositorio |
|---|---|---|
| I · 25% | Monolito limpio, SOLID, MVC, HU03 Pipes & Filters, Microkernel | La guía define esos antecedentes. No se aportó el código histórico de Corte I: no se afirma haber verificado filtros ni plugins |
| II · 35% | Bounded contexts, REST/AMQP, Builder/State/Adapter, JPA | La base Taller 12 observada era una aplicación Spring con DDD, puertos, JPA y HashMap; su notificación era un log. No había microservicios, AMQP ni implementaciones GoF Builder/State verificables |
| III · 40% | DDD puro, hexagonal, intercambio JPA/Memory, Dockerfile y Compose | Dominio migrado y pruebas originales preservadas; servicios REST/AMQP funcionales, imágenes Java 17, pruebas y trazabilidad de esta entrega |

La tabla explica evolución conceptual y evidencia concreta; no presenta código inexistente de cortes previos como entregado.

## Rúbrica final y alcance de cumplimiento

| Criterio | Peso institucional | Estado sustentado |
|---|---:|---|
| Arquitectura DDD y Hexagonal | 35% | Implementada: núcleo puro, invariantes, puertos y dos adaptadores; verificaciones unitarias e integración |
| Contenerización y Docker Compose | 30% | Implementada y ejecutada: tres imágenes, JAR, cuatro contenedores y red; cambio justificado de imagen base |
| Flujo integrador de servicios | 20% | Verificado con REST y AMQP reales, 201/400/202 y escenarios de fallo; pruebas JPA/Memory |
| Informe técnico y C4 | 15% | Contenido, C4 1–4 y evidencias textuales preparados; falta la plantilla Word oficial y capturas manuales |

Esto es una revisión de cumplimiento técnico, no una nota asignada por la docente. La entrega administrativa en GitHub/plataforma no se realizó porque el usuario reservó la publicación para su revisión.
