# Errores y pendientes priorizados

Auditoría del 16/11/2026 UTC. **No se corrigió código ni se añadieron funcionalidades.** Se preservaron los documentos y las evidencias anteriores. Hay **un hallazgo funcional ALTO**, pendientes documentales/administrativos MEDIOS y dos observaciones BAJAS. No se encontró un bloqueo CRÍTICO de compilación o arranque: los tres servicios y RabbitMQ funcionan realmente.

Los resultados de pruebas aprobadas y los contraejemplos se presentan conjuntamente en [RESULTADOS_PRUEBAS.md](RESULTADOS_PRUEBAS.md). G y E corresponden a las dos guías oficiales, con referencias completas en [la matriz](MATRIZ_CUMPLIMIENTO_TALLER13.md).

## HALL-01 — ALTO — Se puede crear un objeto de valor cuyo texto final está vacío; JPA y memoria responden distinto

**Archivos y ubicaciones:**

- [Enunciado.java](../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Enunciado.java), constructor compacto, líneas 8–11.
- [OpcionRespuesta.java](../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/OpcionRespuesta.java), constructor compacto, líneas 8–11.
- Efecto observable en [InMemoryQuestionAdapter.java](../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/memory/InMemoryQuestionAdapter.java), `save` líneas 18–20 y `copiar` líneas 51–52; y [JpaQuestionAdapter.java](../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/JpaQuestionAdapter.java), `save` líneas 21–23.
- Cobertura pendiente en [PreguntaTest.java](../../question-service/src/test/java/co/edu/unicauca/saberpro/question/domain/PreguntaTest.java) y [AdditionalApiContract.java](../../question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/AdditionalApiContract.java).

**Qué está mal:** se comprueba `texto.isBlank()` antes de hacer `texto.trim()`. Para una cadena formada por NUL, tabulador y espacio (`"\u0000\t "` en JSON), `isBlank()` es falso porque NUL no se considera whitespace en esa operación. Luego `trim()` elimina los caracteres y deja `""`. El constructor acepta un objeto que contradice su propia invariante. Los records son inmutables, pero pueden quedar inmutablemente inválidos.

**Reproducción real:** la sonda Java construyó Enunciado y OpcionRespuesta de longitud cero y el agregado llegó a PENDIENTE_REVISION. La aserción de auditoría falló con `AssertionError`. Los dos casos se reprodujeron sobre HTTP real:

| Campo alterado | POST con JPA, :8083 | POST con memoria, :18083 temporal | Revisión memoria | Comprobante Lifecycle |
|---|---:|---:|---:|---:|
| enunciado | 400 | **201, texto vacío** | **202** | **200, mismo preguntaId** |
| opciones[0].texto | 400 | **201, texto vacío** | **202** | **200, mismo preguntaId** |

**Por qué los adaptadores difieren:** JPA guarda y vuelve a construir los VO mediante el mapper; el segundo constructor recibe ya `""`, rechaza y provoca rollback. Memoria copia el agregado conservando los VO inmutables existentes; no repite su constructor y permite el dato inválido. La raíz está en los VO, no en que el HashMap reutilice objetos inmutables. Añadir una validación aislada al adaptador dejaría el dominio incorrecto.

**Requisito afectado:** G p.3 exige enunciado no nulo/vacío y un dominio que encapsule reglas; G p.3 y E p.1,3 exigen intercambiabilidad. Checklist A08 ❌; E05 y G07 ⚠️. La validación no vacía del texto de opciones es además un contrato conservado del Taller 12. Git muestra que el orden defectuoso ya estaba en la base: se heredó y no fue detectado en las 55 pruebas existentes.

**Corrección propuesta, no aplicada:** comprobar null, normalizar y después validar el valor normalizado que se va a guardar; aplicar una política coherente a ambos VO. Si se decide rechazar caracteres de control explícitamente, documentar esa política de entrada. Mantener las reglas dentro del dominio y evitar validarlas solo en JPA, REST o Validation.

**Prueba de aceptación de la corrección:**

1. Tests unitarios de ambos constructores con null, vacío, espacios y NUL/tab/espacio; todos los valores vacíos tras la normalización deben lanzar ReglaNegocioException.
2. Mantener casos válidos con espacios alrededor del texto para verificar la normalización prevista y no romper contenido permitido.
3. Añadir los dos cuerpos al contrato HTTP compartido: ambos modos deben devolver **400** y no incrementar el listado de preguntas.
4. Confirmar que no se publica ni se genera un recibo para una creación rechazada; repetir después el flujo válido 201→202→recibo 200 en JPA y memoria.
5. Volver a ejecutar el perfil de integración tras la corrección; los 55 casos actuales y las nuevas regresiones deben pasar.

**Evidencia:** [sonda fuente](evidencias/DomainBoundaryProbe.java), [salida fallida](evidencias/dominio-normalizacion.log), [peticiones y respuestas completas](evidencias/http-normalizacion.json), [reproductor HTTP](evidencias/http_probe.py). El contenedor temporal fue retirado después de conservar sus logs; no se borraron los volúmenes principales.

## HALL-02 — MEDIO — El informe institucional y las capturas finales aún no están entregados

**Archivos:** [docs/INFORME_TECNICO.md](../INFORME_TECNICO.md), introducción y sección 8; [docs/evidencias/README.md](../evidencias/README.md), sección «Capturas que debe añadir el equipo»; [docs/arquitectura/diagramas.md](../arquitectura/diagramas.md).

**Qué falta:** hay contenido Markdown y C4 editables, pero no un informe final en `Formato-Documento.docx`. No hay archivos DOCX en el proyecto. Las 11 imágenes existentes provienen del Taller 12; ninguna demuestra la consola Docker o Management del Taller 13. Los logs y JSON actuales son evidencia técnica válida, pero no son pantallazos.

**Requisito:** E p.3 exige el documento en la plantilla, C4 niveles 1–4 y capturas de consola Docker y RabbitMQ. Los ítems I11–I14 de la solicitud permiten identificar pendientes; cumplir esa redacción no satisface todavía el entregable oficial.

**Corrección propuesta:** incorporar la plantilla auténtica, completar integrantes/portada reales, trasladar el contenido corregido con HALL-01, exportar o insertar los C4 y tomar capturas de la ejecución auténtica. Usar un mismo UUID en POST 201, revisión 202, consola del consumidor y comprobante. No retocar capturas antiguas para simular puertos o resultados nuevos.

**Comprobación:** abrir el DOCX final y revisar secciones, tablas, cuatro niveles C4 legibles y capturas actuales con nombres/puertos/UUID coherentes. Reproducir la demostración y comparar las capturas con los logs/JSON. La evaluación visual del Word sigue pendiente; no basta con crear un archivo de ese nombre.

**Observación:** no se inventó ni se reconstruyó una plantilla institucional ausente. La auditoría deja el contenido disponible y detalla el pendiente.

## HALL-03 — MEDIO — La trazabilidad de los Cortes I y II está descrita, pero no demostrada por completo

**Archivo:** [docs/arquitectura/trazabilidad.md](../arquitectura/trazabilidad.md), sección «Corte I → Corte III»; [docs/INFORME_TECNICO.md](../INFORME_TECNICO.md), sección 7.

**Qué falta:** el repositorio permite demostrar la migración Taller 12→Taller 13. No aporta código histórico verificable de HU03/Pipes & Filters/Microkernel del Corte I ni evidencia completa de Builder/State y demás hitos del Corte II. La tabla reconoce correctamente esa limitación, pero una explicación conceptual no prueba toda la evolución semestral.

**Requisito:** objetivo de trazabilidad de E p.1 y matriz de E p.3. Checklist I10 ⚠️.

**Corrección propuesta:** incorporar referencias a entregas, commits, diagramas o repositorios realmente realizados en esos cortes, y mapear cada hito a evidencia. Si no existen o no están disponibles, mantenerlo explícito y no atribuir a este proyecto patrones que no implementa. No corresponde añadir ahora Microkernel, Builder o State solo para aparentar una historia inexistente.

**Comprobación:** otra persona debe poder abrir cada referencia, localizar la implementación histórica y explicar qué cambió hasta la versión contenerizada. Separar hechos comprobados de antecedentes institucionales.

## HALL-04 — MEDIO — Pendiente administrativo de versionado/publicación y entrega final

**Ubicación:** estado Git de la rama `feature/taller13-fase2`, [git-inicial.txt](evidencias/git-inicial.txt) y sección de entrega de [INFORME_TECNICO.md](../INFORME_TECNICO.md).

**Estado observado:** HEAD local continúa en `e857f96`, igual que las referencias locales main/origin/main inspeccionadas. El Taller13 se encuentra en cambios del árbol de trabajo, sin un commit final creado en esta sesión. No se hizo fetch ni se consultó el estado remoto actualizado; por ello no se afirma que nadie más haya publicado una entrega externa. La entrega GitHub/plataforma queda **NO VERIFICADA**, y el trabajo local requiere revisión y versionado.

**Requisito:** E p.3 solicita repositorio final en GitHub y artefactos en la plataforma. La solicitud previa del usuario prohíbe push/merge automático y reserva la revisión antes de publicar. Este pendiente no es un incumplimiento de autorización por parte del agente.

**Acción propuesta:** revisar los resultados, decidir y ejecutar la corrección de HALL-01, completar informe/evidencias, revisar el diff y luego versionar/publicar mediante la decisión del propietario. No se pide permiso para una publicación imprecisa ni se publica durante la auditoría.

**Comprobación:** un clon del commit final debe construir y ejecutar los tres servicios; el repositorio remoto debe contener los artefactos correctos, y la entrega en plataforma debe tener su confirmación real. Comprobar desde el enlace final, no solo desde archivos locales.

## HALL-05 — BAJO — Quince enlaces relativos rotos en el README histórico

**Archivo:** [docs/taller12/README.md](../taller12/README.md), líneas 53, 70, 183 (dos referencias), 222, 224, 228–235 y 237.

**Qué está mal:** al copiar el README desde la raíz se conservaron destinos como `docs/figurasYEvidencias/...` y `ejemplo-pregunta.json`; desde docs/taller12 apuntan a lugares inexistentes. Tres referencias rotas son imágenes insertadas. El README actual y las referencias locales activas inspeccionadas sí resuelven.

**Relación con la guía:** dificulta consultar la evidencia histórica y sustentar trazabilidad, E p.1,3; no bloquea compilación ni endpoints.

**Corrección propuesta:** en una revisión autorizada de documentación, usar desde el archivo histórico `../figurasYEvidencias/...`, `../../ejemplo-pregunta.json`, `../../postman/...` y `diagramas.md`. Otra opción es preservar el original íntegro y añadir un índice funcional con una nota clara de archivo histórico. No se aplicó ninguna de esas opciones durante esta auditoría.

**Comprobación:** verificar cada enlace y abrir las tres imágenes en el renderizador Markdown. Evidencia y destinos exactos: [enlaces-locales.json](evidencias/enlaces-locales.json) y [EXPLICACION_IMAGENES.md](EXPLICACION_IMAGENES.md).

## HALL-06 — BAJO — Una captura duplicada y un título que no acredita la selección del adaptador

**Archivos:** [Activación del adaptador en memoria.PNG](<../figurasYEvidencias/Activación del adaptador en memoria.PNG>) y [Evidencia_05_Adaptador_Memoria.PNG](../figurasYEvidencias/Evidencia_05_Adaptador_Memoria.PNG); referencia descriptiva en el README histórico.

**Qué está mal:** son idénticas byte por byte, SHA-256 `b29ba59ea9e1bd424dd54fe318dd531c9d1745beb4f1757366947d959730ee5c`. Muestran arranque y puerto 8080, pero no el argumento `--app.repository=memory`. La presencia de Hikari no determina qué implementación del puerto está inyectada. No son dos ejecuciones independientes.

**Relación con la guía:** la intercambiabilidad se debe demostrar, G p.3, no deducir del nombre de un PNG. La captura «Crear una pregunta usando el adaptador en memoria» sí muestra ese argumento, aunque también pertenece al Taller12.

**Corrección propuesta:** identificar el duplicado, referenciar una sola copia o explicar por qué se conserva; describir solo lo visible. Para la entrega actual tomar evidencia de configuración efectiva y de comportamiento con ambos repositorios.

**Comprobación:** comparación de hashes, inspección visual y repetición actual del protocolo. No eliminar material histórico durante esta auditoría.

## L01 — Limitación técnica conocida, no un requisito nuevo pendiente de implementación

**Archivos:** `JpaQuestionAdapter.actualizar` líneas41–47, `RabbitMQPublisher.notificarPreguntaPendiente`, `ReviewProcessingService.registrar` y [decisiones.md, «Consistencia sin Outbox»](../arquitectura/decisiones.md).

El broker puede aceptar el evento y Lifecycle procesarlo antes de que Question confirme su transacción local. Un fallo de proceso/commit después del ACK puede dejar un recibo con Question aún en BORRADOR. Un timeout también puede ocurrir después de la aceptación del broker. El ID estable y la deduplicación reducen efectos de reentrega, pero no reparan por sí solos ese estado distribuido.

La solicitud original permitió una solución simple con límites de atomicidad documentados si no se usaba Outbox. Los PDF no exigen Outbox, replicación, transacciones distribuidas ni «exactamente una vez». Por eso L01 se registra como límite explicado, no como motivo para introducir funcionalidad nueva o declarar falsamente un fallo ya observado. Las caídas ordinarias del broker, no-route y rollback sí se probaron con éxito; no se inyectó un crash exactamente entre ACK y commit.

Para una evolución posterior, un Outbox transaccional y reintentos de publicación permitirían una estrategia de consistencia eventual más robusta. Su validación requeriría fallos controlados en cada frontera, reinicio y reconciliación. No se implementó en esta auditoría.

## Orden recomendado antes de entregar

1. Corregir HALL-01 y agregar las regresiones de dominio/API compartida, después de revisar esta auditoría.
2. Actualizar las afirmaciones sobre invariantes e intercambiabilidad en README e informes previos para reflejar el resultado corregido. No reescribir los logs antiguos.
3. Completar HALL-02 y aportar la evidencia histórica disponible para HALL-03.
4. Resolver HALL-05/HALL-06 si se decide mejorar la navegación del histórico, conservando su procedencia.
5. Revisar y realizar el cierre HALL-04 por decisión del propietario.

No es correcto declarar «100% completo» con el estado actual, aunque las suites anteriores estén verdes. Tampoco es correcto decir que el sistema no funciona: la ejecución de los tres servicios, Docker y RabbitMQ está demostrada.
