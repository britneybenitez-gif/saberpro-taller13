# Auditoría contra Taller 12 (fase I)

| Exigencia de la guía | Ubicación / verificación prevista |
|---|---|
| Agregado Pregunta, estado BORRADOR y transición | `domain/model/Pregunta`, `EstadoPregunta` |
| Enunciado inmutable no vacío | `domain/valueobject/Enunciado` |
| Justificacion inmutable de 15 o más caracteres | `domain/valueobject/Justificacion` |
| OpcionRespuesta inmutable (texto, correcta) | `domain/valueobject/OpcionRespuesta` |
| Exactamente 4 opciones y 1 correcta al enviar | `Pregunta.validarInvariantes()` |
| Excepción del dominio | `domain/exception/ReglaNegocioException` |
| Puerto de entrada (nombre / ubicación requeridos) | `domain/ports/inbound/CrearPreguntaUseCase` |
| Puerto de persistencia `save`,`findById` | `domain/ports/outbound/PreguntaRepositoryPort` |
| Puerto de notificaciones citado en objetivos | `domain/ports/outbound/NotificacionRevisionPort` |
| JPA + H2 | `infrastructure/adapters/out/jpa/*` |
| Adaptador HashMap | `infrastructure/adapters/out/memory/InMemoryQuestionAdapter` |
| REST | `infrastructure/adapters/in/web/PreguntaController` |
| Desacoplamiento configurable | `infrastructure/config/HexagonalConfiguration`, `app.repository` |
| Pruebas unitarias JUnit 5 | `domain/PreguntaTest`, `application/IntercambiabilidadTest` |
| Pruebas de integración HTTP ambos adaptadores | `infrastructure/ApiJpaIntegrationTest`, `ApiMemoryIntegrationTest` |
| Separación explícita de capas | carpetas `domain`, `application`, `infrastructure` |
| Diagramas | `docs/diagramas.md` y borrador Word |
| Código en GitHub | **PENDIENTE**: publicación por los integrantes |
| Informe en Formato-Documento.docx | **PENDIENTE**: la plantilla oficial no fue adjuntada |
| Evidencias de intercambiabilidad con capturas reales | **PENDIENTE**: ejecutar Maven/Postman en equipo o CI |

## Verificación ambiental

El contenedor de revisión dispone de Java, pero no de Maven ni de acceso a Maven Central. Se compila y prueba el subconjunto Java puro con `javac` / programa ejecutable de comprobación; **eso NO equivale a un `mvn clean test` completo** con Spring Boot/H2.

Nota: en el diseño de aula el adaptador de notificación escribe al log. La guía cita notificaciones en objetivos, pero no define un canal de entrega específico.
