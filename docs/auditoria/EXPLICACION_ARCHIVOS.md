# Explicación individual de archivos — Taller 13

Proyecto examinado: `/home/victor_cespedes0710/proyectos/saberpro-taller13`. Fecha: 16/11/2026 UTC. Las rutas que siguen son completas **dentro de esta raíz** y enlazan a sus archivos. Base Git: `e857f96`; rama actual `feature/taller13-fase2`. Se comparó el árbol de trabajo con esa base local del repositorio original, sin fetch, commit, push ni merge.

La auditoría comprende **145 archivos entregables preexistentes: 51 Java (40 de producción y 11 de pruebas), 11 PNG y 83 archivos de configuración, documentación, scripts u otras evidencias**. La exploración recursiva inicial incluye además metadatos Git, clases y salidas generadas; están enumerados en [inventario-recursivo.txt](evidencias/inventario-recursivo.txt). No se presentan objetos Git, dependencias descargadas o target como clases escritas por el equipo. Los reportes de target se respaldaron antes de clean.

No se modificaron las implementaciones ni los documentos anteriores para corregir hallazgos. HALL-01 identifica el defecto de normalización; HALL-02–HALL-06, pendientes académicos o documentales. Véase [errores y pendientes](ERRORES_Y_PENDIENTES.md). G = guía paso a paso (5 páginas); E = entrega final (3 páginas); U = solicitud del usuario. Los identificadores de hallazgo HALL-01–HALL-06 se distinguen de los ítems H01–H11 de pruebas. Los identificadores A01–I15 corresponden a [la matriz completa](MATRIZ_CUMPLIMIENTO_TALLER13.md).

## Clasificación frente a Taller 12

| Clasificación | Archivos |
|---|---:|
| Nuevo | 103 |
| Modificado | 5 |
| Reutilizado sin cambios | 13 |
| Trasladado/reutilizado (solo paquete/nombre) | 11 |
| Trasladado y modificado | 13 |

«Nuevo» describe una ruta nueva respecto de Git: los cuatro documentos en docs/taller12 son copias de antecedentes, no contenido histórico inventado. Los 24 archivos trasladados tienen correspondencia explícita más abajo. Las pruebas HTTP trasladadas cambiaron expectativas de 200 a 202 y ahora integran dependencias reales.

## Raíz del proyecto

### `.gitignore`

1. **Ruta:** [.gitignore](<../../.gitignore>).
2. **Nombre:** `.gitignore`.
3. **Estado:** Modificado. Origen: `.gitignore` en e857f96.
4. **Para qué sirve:** Excluir compilados, datos locales y configuración personal del versionado.
5. **Contenido y funcionamiento:** Patrones recursivos impiden añadir target, archivos H2, data, __pycache__, IDE y .env.
6. **Métodos, anotaciones o configuraciones importantes:** **/target/, **/data/, *.mv.db, *.trace.db y .env.
7. **Relaciones:** Maven, H2 y Git; no controla Docker
8. **Requisito:** U: preservar un repositorio reproducible
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

### `.mvn/wrapper/maven-wrapper.properties`

1. **Ruta:** [.mvn/wrapper/maven-wrapper.properties](<../../.mvn/wrapper/maven-wrapper.properties>).
2. **Nombre:** `maven-wrapper.properties`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Fijar la distribución Maven usada por el wrapper.
5. **Contenido y funcionamiento:** El wrapper only-script descarga Maven 3.9.9 de Maven Central si falta en caché.
6. **Métodos, anotaciones o configuraciones importantes:** wrapperVersion=3.3.4; distributionType=only-script; distributionUrl de apache-maven-3.9.9-bin.zip.
7. **Relaciones:** mvnw y mvnw.cmd; wrappers de cada módulo
8. **Requisito:** G p.3: ./mvnw package
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

### `ejemplo-pregunta.json`

1. **Ruta:** [ejemplo-pregunta.json](<../../ejemplo-pregunta.json>).
2. **Nombre:** `ejemplo-pregunta.json`.
3. **Estado:** Reutilizado sin cambios. Origen: `ejemplo-pregunta.json` en e857f96.
4. **Para qué sirve:** Dar un cuerpo válido reutilizable para crear preguntas.
5. **Contenido y funcionamiento:** Incluye enunciado, justificación y cuatro opciones con exactamente una correcta.
6. **Métodos, anotaciones o configuraciones importantes:** Textos simples en request; correcta es boolean, no cadena.
7. **Relaciones:** scripts/smoke_test.py, README y peticiones HTTP
8. **Requisito:** E02/E08; G p.5
9. **Evaluación:** Reutilizado sin cambios; produjo 201 y 202 en ambos adaptadores.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

### `mvnw`

1. **Ruta:** [mvnw](<../../mvnw>).
2. **Nombre:** `mvnw`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Ejecutar Maven de versión fijada en entornos Unix.
5. **Contenido y funcionamiento:** Script oficial del wrapper: localiza propiedades, resuelve caché/descarga y transfiere argumentos al ejecutable Maven.
6. **Métodos, anotaciones o configuraciones importantes:** Shell, JAVA_HOME opcional, resolución de directorio y mvn; no incorpora una JVM.
7. **Relaciones:** maven-wrapper.properties y POM seleccionado con -f
8. **Requisito:** G p.3: compilación con wrapper
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** La primera descarga requiere red. La auditoría ejecutó Maven instalado y Maven/Java 17 en Docker; no atribuye esas ejecuciones al wrapper.

### `mvnw.cmd`

1. **Ruta:** [mvnw.cmd](<../../mvnw.cmd>).
2. **Nombre:** `mvnw.cmd`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Ofrecer Maven Wrapper para Windows.
5. **Contenido y funcionamiento:** Script de arranque oficial que resuelve distribución y ejecuta Maven con los argumentos recibidos.
6. **Métodos, anotaciones o configuraciones importantes:** Lógica cmd/PowerShell del wrapper 3.3.4 y propiedades de distribución.
7. **Relaciones:** maven-wrapper.properties; POM raíz o -f
8. **Requisito:** Portabilidad de la compilación
9. **Evaluación:** Contenido inspeccionado; ejecución en Windows NO VERIFICADA en este entorno Linux.
10. **Errores, riesgos o mejora:** No se debe presentar una ejecución Linux como prueba del wrapper Windows.

### `pom.xml`

1. **Ruta:** [pom.xml](<../../pom.xml>).
2. **Nombre:** `pom.xml`.
3. **Estado:** Modificado. Origen: `pom.xml` en e857f96.
4. **Para qué sirve:** Agrupar los tres microservicios en un reactor Maven.
5. **Contenido y funcionamiento:** El packaging pom evita producir un cuarto servicio; modules ordena Validation, Question y Lifecycle.
6. **Métodos, anotaciones o configuraciones importantes:** Parent Spring Boot 3.4.5; java.version=17; versión 2.0.0. No es el parent local de los módulos: cada uno hereda Boot directamente.
7. **Relaciones:** Los tres pom.xml de servicio; mvnw; scripts/verify.sh
8. **Requisito:** G p.2; E p.1–2; C08/H09
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

## Validation Service

### `validation-service/src/main/java/co/edu/unicauca/saberpro/validation/ValidationServiceApplication.java`

1. **Ruta:** [validation-service/src/main/java/co/edu/unicauca/saberpro/validation/ValidationServiceApplication.java](<../../validation-service/src/main/java/co/edu/unicauca/saberpro/validation/ValidationServiceApplication.java>).
2. **Nombre:** `ValidationServiceApplication.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Arrancar el microservicio de validación.
5. **Contenido y funcionamiento:** Spring descubre los componentes bajo el paquete validation y levanta HTTP en el puerto configurado.
6. **Métodos, anotaciones o configuraciones importantes:** main(String[] args) recibe argumentos de JVM, llama SpringApplication.run y no devuelve valor; @SpringBootApplication habilita configuración y escaneo.
7. **Relaciones:** validation/api/*; validation-service/src/main/resources/application.properties
8. **Requisito:** G p.2; E p.1–2; C01–C02
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** Validation no tendría punto de entrada ejecutable; Question no podría completar la creación.

### `validation-service/src/main/java/co/edu/unicauca/saberpro/validation/api/ValidationExceptionHandler.java`

1. **Ruta:** [validation-service/src/main/java/co/edu/unicauca/saberpro/validation/api/ValidationExceptionHandler.java](<../../validation-service/src/main/java/co/edu/unicauca/saberpro/validation/api/ValidationExceptionHandler.java>).
2. **Nombre:** `ValidationExceptionHandler.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Entregar un resultado de validación legible cuando el JSON es inválido.
5. **Contenido y funcionamiento:** Intercepta fallos de Bean Validation o deserialización, recopila errores de campos y responde 400.
6. **Métodos, anotaciones o configuraciones importantes:** invalid(MethodArgumentNotValidException) devuelve ResponseEntity<Resultado> con errores ordenados; unreadable(HttpMessageNotReadableException) devuelve mensaje general; @RestControllerAdvice y @ExceptionHandler.
7. **Relaciones:** ValidationRestController.Resultado; RestValidationAdapter interpreta HTTP 400
8. **Requisito:** C02–C03/E08
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** Los errores perderían el contrato uniforme {valida:false,errores:[...]}.

### `validation-service/src/main/java/co/edu/unicauca/saberpro/validation/api/ValidationRestController.java`

1. **Ruta:** [validation-service/src/main/java/co/edu/unicauca/saberpro/validation/api/ValidationRestController.java](<../../validation-service/src/main/java/co/edu/unicauca/saberpro/validation/api/ValidationRestController.java>).
2. **Nombre:** `ValidationRestController.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Validar el contrato técnico del contenido recibido por REST.
5. **Contenido y funcionamiento:** Bean Validation se ejecuta antes de validar(); los records describen límites de las columnas JPA. No revisa cuatro opciones, una correcta o estados.
6. **Métodos, anotaciones o configuraciones importantes:** Solicitud(enunciado,justificacion,opciones), Opcion(texto,correcta), Resultado(valida,errores); @RestController, @RequestMapping, @PostMapping, @Valid, @NotNull, @Size. validar(Solicitud) devuelve Resultado(true, lista vacía) únicamente si las anotaciones admiten la entrada.
7. **Relaciones:** ValidationExceptionHandler.java; RestValidationAdapter.java de Question
8. **Requisito:** G p.2,5; C02–C03
9. **Evaluación:** Validación real comprobada por seis tests HTTP y rechazo remoto de longitudes excesivas.
10. **Errores, riesgos o mejora:** Acepta textos vacíos deliberadamente porque las invariantes son de Question; el fallo HALL-01 debe corregirse en dominio, no duplicando la regla aquí.

**Si no existiera:** Se perdería el contrato REST síncrono y la validación técnica.

## Question Service

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/QuestionServiceApplication.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/QuestionServiceApplication.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/QuestionServiceApplication.java>).
2. **Nombre:** `QuestionServiceApplication.java`.
3. **Estado:** Trasladado/reutilizado (solo paquete/nombre). Origen: `src/main/java/co/edu/unicauca/saberpro/SaberProApplication.java` en e857f96.
4. **Para qué sirve:** Arrancar Question Service y descubrir API, beans y JPA.
5. **Contenido y funcionamiento:** Es el punto de entrada del JAR y delimita el escaneo al paquete question.
6. **Métodos, anotaciones o configuraciones importantes:** main(String[] args): SpringApplication.run; @SpringBootApplication.
7. **Relaciones:** HexagonalConfiguration.java; QuestionRestController.java; application.properties
8. **Requisito:** G p.2; E p.1–2; C04–C05
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** No arrancaría el servicio principal ni se conectarían sus componentes.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/api/ApiExceptionHandler.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/api/ApiExceptionHandler.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/api/ApiExceptionHandler.java>).
2. **Nombre:** `ApiExceptionHandler.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/in/web/ApiExceptionHandler.java` en e857f96.
4. **Para qué sirve:** Traducir excepciones a estados HTTP.
5. **Contenido y funcionamiento:** Centraliza errores; no replica reglas del agregado. Registra las fallas externas antes de devolver 503.
6. **Métodos, anotaciones o configuraciones importantes:** negocio(ReglaNegocioException) →400 {error}; estructura(Exception) →400; noExiste(NoSuchElementException) →404; dependencia(DependenciaNoDisponibleException) →503. @RestControllerAdvice/@ExceptionHandler.
7. **Relaciones:** QuestionRestController; excepciones del dominio y aplicación; RabbitMQPublisher; RestValidationAdapter
8. **Requisito:** A07/E04–E05/D08
9. **Evaluación:** 400/404/503 comprobados. No se detectó una conversión falsa a 202.
10. **Errores, riesgos o mejora:** No puede detectar una violación que el objeto de valor no lance: HALL-01 ocurre antes de este traductor.

**Si no existiera:** Se perdería el mapeo controlado de fallas del dominio y dependencias.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/api/QuestionRestController.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/api/QuestionRestController.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/api/QuestionRestController.java>).
2. **Nombre:** `QuestionRestController.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/in/web/PreguntaController.java` en e857f96.
4. **Para qué sirve:** Adaptar peticiones HTTP a los puertos de entrada.
5. **Contenido y funcionamiento:** Valida estructura con records, transforma opciones a objetos de valor y delega. Serializa el agregado conservando el contrato del Taller 12.
6. **Métodos, anotaciones o configuraciones importantes:** crear(CrearRequest): recibe textos y opciones, devuelve 201 con Location y Pregunta. listar(): List<Pregunta>, HTTP 200. buscar(UUID): Pregunta o excepción 404. enviarRevision(UUID): 202 solo tras retorno del caso de uso. @Valid, @NotNull, @RestController, @GetMapping, @PostMapping.
7. **Relaciones:** CrearPreguntaUseCase; EvaluarPreguntaUseCase; OpcionRespuesta; ApiExceptionHandler
8. **Requisito:** G p.2,5; E p.2; E01–E08
9. **Evaluación:** Rutas normales, errores 400/404, Location y 202 comprobados con ambos repositorios.
10. **Errores, riesgos o mejora:** Hereda HALL-01 de los objetos de valor: el adaptador memory puede responder 201 con texto normalizado vacío. Devuelve dominio directamente para conservar JSON; futuros cambios en getters afectarían la API.

**Si no existiera:** Los casos de uso existirían, pero no serían accesibles por HTTP.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/application/QuestionApplicationService.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/application/QuestionApplicationService.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/application/QuestionApplicationService.java>).
2. **Nombre:** `QuestionApplicationService.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/main/java/co/edu/unicauca/saberpro/application/CrearPreguntaService.java` en e857f96.
4. **Para qué sirve:** Coordinar creación, consultas y revisión sin infraestructura concreta.
5. **Contenido y funcionamiento:** Recibe tres puertos por constructor. Construye el agregado, pide validación externa y persiste. La revisión entrega al repositorio dos acciones: cambiar el agregado y notificar tras guardar.
6. **Métodos, anotaciones o configuraciones importantes:** Constructor(repo,notificaciones,validacion) exige no null. crear(String,String,List<OpcionRespuesta>) →Pregunta BORRADOR. buscar(UUID) →Pregunta o NoSuchElementException. listar() →List. enviarRevision(UUID) →Pregunta usando actualizar(id,Pregunta::enviarRevision,notificaciones::notificarPreguntaPendiente).
7. **Relaciones:** Cinco puertos; Pregunta/Enunciado/Justificacion; HexagonalConfiguration
8. **Requisito:** G p.2–3; E p.2; B05/B10
9. **Evaluación:** Compila con Java puro; creación, consulta, rollback y concurrencia verificados.
10. **Errores, riesgos o mejora:** La transacción local se delega al contrato del repositorio. No existe atomicidad entre H2 y RabbitMQ (L01). HALL-01 pertenece a los VO construidos/reutilizados.

**Si no existiera:** El controlador tendría que coordinar persistencia, validación y mensajería, mezclando responsabilidades.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/application/exception/DependenciaNoDisponibleException.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/application/exception/DependenciaNoDisponibleException.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/application/exception/DependenciaNoDisponibleException.java>).
2. **Nombre:** `DependenciaNoDisponibleException.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Representar la imposibilidad de completar una interacción externa.
5. **Contenido y funcionamiento:** RuntimeException lleva mensaje público y causa técnica; provoca rollback local y se convierte en 503.
6. **Métodos, anotaciones o configuraciones importantes:** Constructor(String mensaje, Throwable causa) delega a RuntimeException. No tiene anotaciones ni reglas académicas.
7. **Relaciones:** RestValidationAdapter; RabbitMQPublisher; ApiExceptionHandler; tests de aplicación
8. **Requisito:** U: fallas sin falso 202; D08
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** Se perdería una señal específica para distinguir indisponibilidad de errores de negocio.

## Lifecycle Service

### `lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/LifecycleServiceApplication.java`

1. **Ruta:** [lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/LifecycleServiceApplication.java](<../../lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/LifecycleServiceApplication.java>).
2. **Nombre:** `LifecycleServiceApplication.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Arrancar el servicio consumidor y su API de comprobantes.
5. **Contenido y funcionamiento:** Spring carga listener, topología, servicio de registro, JPA y controlador bajo lifecycle.
6. **Métodos, anotaciones o configuraciones importantes:** main(String[] args) llama SpringApplication.run; @SpringBootApplication.
7. **Relaciones:** QuestionReviewListener; ReviewProcessingService; ReviewRestController; application.properties
8. **Requisito:** G p.2; E p.1–2; C06–C08
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** No habría proceso independiente que consumiera los eventos.

### `lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/api/ReviewRestController.java`

1. **Ruta:** [lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/api/ReviewRestController.java](<../../lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/api/ReviewRestController.java>).
2. **Nombre:** `ReviewRestController.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Exponer consultas de los eventos efectivamente procesados.
5. **Contenido y funcionamiento:** Lee recibos, sin crear revisiones ni modificar el agregado remoto. Permite observar que el consumidor ya actuó.
6. **Métodos, anotaciones o configuraciones importantes:** @RestController/@RequestMapping(/api/revisiones); listar() →List<ReviewReceipt>; buscar(UUID preguntaId) →ResponseEntity.of(Optional), 200 o 404.
7. **Relaciones:** ReviewReceiptRepository; pruebas de flujo de Question y script smoke
8. **Requisito:** D09; endpoint adicional documentado, no exigido literalmente en los PDF
9. **Evaluación:** Consulta 200 del ID enviado y 404 antes de existir recibo comprobadas.
10. **Errores, riesgos o mejora:** Depende directamente de persistencia: este endpoint auxiliar no se presenta como el núcleo hexagonal de Question. Una futura capa de consulta sería mejora de diseño, no requisito faltante.

**Si no existiera:** El consumo seguiría funcionando, pero habría que demostrarlo exclusivamente con logs/Management.

### `lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/application/ReviewProcessingService.java`

1. **Ruta:** [lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/application/ReviewProcessingService.java](<../../lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/application/ReviewProcessingService.java>).
2. **Nombre:** `ReviewProcessingService.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Guardar una única constancia por evento procesado.
5. **Contenido y funcionamiento:** Valida el contrato, consulta si eventId existe y guarda un recibo con instante de procesamiento si es nuevo.
6. **Métodos, anotaciones o configuraciones importantes:** @Service/@Transactional; registrar(QuestionReviewMessage) →boolean: false si ya existe, true tras saveAndFlush.
7. **Relaciones:** ReviewReceiptRepository; ReviewReceipt; QuestionReviewListener
8. **Requisito:** D07/D09; U idempotencia/errores
9. **Evaluación:** H2 y reentrega normal verificados; el segundo mensaje mantiene el timestamp original.
10. **Errores, riesgos o mejora:** La comprobación y guardado asumen concurrencia 1 configurada; no se afirma garantía entre múltiples réplicas. La guía no exige escalado.

**Si no existiera:** El consumidor no tendría registro duradero ni base para deduplicar.

### `lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/persistence/ReviewReceipt.java`

1. **Ruta:** [lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/persistence/ReviewReceipt.java](<../../lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/persistence/ReviewReceipt.java>).
2. **Nombre:** `ReviewReceipt.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Representar el comprobante persistido por Lifecycle.
5. **Contenido y funcionamiento:** Entidad separada con identidad del evento, pregunta, estado y fecha; no es el agregado Pregunta.
6. **Métodos, anotaciones o configuraciones importantes:** @Entity/@Table(revisiones_recibidas); @Id eventId; preguntaId unique/non-null; estado/processedAt no null. Constructor protegido, constructor completo y getters.
7. **Relaciones:** ReviewReceiptRepository; ReviewProcessingService; ReviewRestController
8. **Requisito:** D07/D09; evidencia funcional complementaria
9. **Evaluación:** Comprobantes consultados por HTTP con IDs correctos.
10. **Errores, riesgos o mejora:** La API expone esta entidad simple directamente; cambios de campos pueden cambiar JSON. No se considera incumplimiento de la guía, que centra el dominio en Question.

**Si no existiera:** No se conservaría la prueba del consumo tras reiniciar.

### `lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/persistence/ReviewReceiptRepository.java`

1. **Ruta:** [lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/persistence/ReviewReceiptRepository.java](<../../lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/persistence/ReviewReceiptRepository.java>).
2. **Nombre:** `ReviewReceiptRepository.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Consultar y guardar recibos mediante Spring Data.
5. **Contenido y funcionamiento:** Hereda operaciones por eventId y añade búsqueda por preguntaId.
6. **Métodos, anotaciones o configuraciones importantes:** JpaRepository<ReviewReceipt,UUID>; findByPreguntaId(UUID) →Optional<ReviewReceipt>.
7. **Relaciones:** ReviewProcessingService; ReviewRestController; ReviewReceipt
8. **Requisito:** D07/D09; G04
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** No sería posible persistir/deduplicar ni consultar comprobantes.

## Dominio DDD

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/exception/ReglaNegocioException.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/exception/ReglaNegocioException.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/exception/ReglaNegocioException.java>).
2. **Nombre:** `ReglaNegocioException.java`.
3. **Estado:** Trasladado/reutilizado (solo paquete/nombre). Origen: `src/main/java/co/edu/unicauca/saberpro/domain/exception/ReglaNegocioException.java` en e857f96.
4. **Para qué sirve:** Señalar violaciones de invariantes sin depender de HTTP.
5. **Contenido y funcionamiento:** Excepción de dominio que viaja a la aplicación/API; su semántica es negocio, no transporte.
6. **Métodos, anotaciones o configuraciones importantes:** Constructor(String mensaje); extiende RuntimeException.
7. **Relaciones:** VO; Pregunta; RestValidationAdapter por contenido rechazado; ApiExceptionHandler
8. **Requisito:** G p.3; A07/E05
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** El núcleo perdería el tipo de error exigido por la guía.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Enunciado.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Enunciado.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Enunciado.java>).
2. **Nombre:** `Enunciado.java`.
3. **Estado:** Trasladado/reutilizado (solo paquete/nombre). Origen: `src/main/java/co/edu/unicauca/saberpro/domain/valueobject/Enunciado.java` en e857f96.
4. **Para qué sirve:** Representar un enunciado inmutable no vacío.
5. **Contenido y funcionamiento:** Record valida null/isBlank y después aplica trim. El orden permite que caracteres de control no considerados blancos se conviertan en cadena vacía.
6. **Métodos, anotaciones o configuraciones importantes:** Constructor compacto Enunciado(String texto); accesor texto(), equals/hashCode del record. Líneas 8–11: validación anterior a normalización.
7. **Relaciones:** Pregunta; QuestionApplicationService; QuestionDomainMapper; ReglaNegocioException
8. **Requisito:** G p.3 §3.1; E p.1; A04/A08
9. **Evaluación:** ❌ Invariante incompleta: new Enunciado("\u0000\t ").texto() tiene longitud 0.
10. **Errores, riesgos o mejora:** HALL-01 ALTO: normalizar primero y validar el resultado; agregar test unitario y HTTP en ambos adaptadores. Defecto ya presente en e857f96.

**Si no existiera:** El agregado recibiría cadenas sin un tipo que garantice la regla.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/EstadoPregunta.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/EstadoPregunta.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/EstadoPregunta.java>).
2. **Nombre:** `EstadoPregunta.java`.
3. **Estado:** Trasladado/reutilizado (solo paquete/nombre). Origen: `src/main/java/co/edu/unicauca/saberpro/domain/model/EstadoPregunta.java` en e857f96.
4. **Para qué sirve:** Limitar los estados del agregado a los dos especificados.
5. **Contenido y funcionamiento:** Enum representa BORRADOR y PENDIENTE_REVISION. Pregunta define la transición, no el enum.
6. **Métodos, anotaciones o configuraciones importantes:** Constantes BORRADOR/PENDIENTE_REVISION; name() para persistir y valueOf() en mapper.
7. **Relaciones:** Pregunta; QuestionDomainMapper; pruebas de transición
8. **Requisito:** A10/A14; G p.3
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Es un enum; no debe presentarse como implementación del patrón GoF State de cortes anteriores.

**Si no existiera:** Se usarían cadenas libres y sería más fácil introducir estados no previstos.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Justificacion.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Justificacion.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Justificacion.java>).
2. **Nombre:** `Justificacion.java`.
3. **Estado:** Trasladado/reutilizado (solo paquete/nombre). Origen: `src/main/java/co/edu/unicauca/saberpro/domain/valueobject/Justificacion.java` en e857f96.
4. **Para qué sirve:** Representar un sustento inmutable de longitud mínima.
5. **Contenido y funcionamiento:** Evalúa la longitud después de trim y conserva el texto normalizado, antes de permitir construir el record.
6. **Métodos, anotaciones o configuraciones importantes:** Constructor Justificacion(String): null o trim().length()<15 lanza ReglaNegocioException; texto() retorna cadena normalizada. Sin Spring/JPA.
7. **Relaciones:** Pregunta; QuestionApplicationService; QuestionDomainMapper; ReglaNegocioException
8. **Requisito:** G p.3; A05/A09
9. **Evaluación:** Casos null, 14 y 15 caracteres comprobados; sin el problema de orden observado en Enunciado.
10. **Errores, riesgos o mejora:** La longitud utiliza unidades UTF-16 de String; la guía no exige otro cómputo Unicode. No se consideró un incumplimiento.

**Si no existiera:** No habría garantía local de justificación mínima.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/OpcionRespuesta.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/OpcionRespuesta.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/OpcionRespuesta.java>).
2. **Nombre:** `OpcionRespuesta.java`.
3. **Estado:** Trasladado/reutilizado (solo paquete/nombre). Origen: `src/main/java/co/edu/unicauca/saberpro/domain/valueobject/OpcionRespuesta.java` en e857f96.
4. **Para qué sirve:** Representar una opción y si es la respuesta correcta.
5. **Contenido y funcionamiento:** Record inmutable; comprueba texto obligatorio y luego normaliza. La unicidad de correcta no es responsabilidad de una opción aislada.
6. **Métodos, anotaciones o configuraciones importantes:** OpcionRespuesta(String texto, boolean correcta); texto(), correcta(), igualdad por valor. Validación líneas 8–11 antes de trim.
7. **Relaciones:** Pregunta.validarInvariantes; QuestionRestController; QuestionDomainMapper; ReglaNegocioException
8. **Requisito:** G p.3; A06/A11–A12
9. **Evaluación:** Inmutabilidad correcta; ❌ acepta texto que queda vacío tras normalización (HALL-01).
10. **Errores, riesgos o mejora:** JPA termina rechazándolo al rehidratar; HashMap lo conserva y permite revisión. Normalizar primero; probar ambas APIs.

**Si no existiera:** Las opciones carecerían de representación explícita y validación local.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Pregunta.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Pregunta.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Pregunta.java>).
2. **Nombre:** `Pregunta.java`.
3. **Estado:** Trasladado/reutilizado (solo paquete/nombre). Origen: `src/main/java/co/edu/unicauca/saberpro/domain/model/Pregunta.java` en e857f96.
4. **Para qué sirve:** Ser la raíz del agregado y proteger su transición de estado.
5. **Contenido y funcionamiento:** Guarda UUID, VO, copia inmutable de opciones y estado. Valida también las reconstrucciones que llegan en PENDIENTE_REVISION.
6. **Métodos, anotaciones o configuraciones importantes:** Constructor(UUID,Enunciado,Justificacion,List,EstadoPregunta) rechaza referencias/elementos nulos y valida estado pendiente. crear(Enunciado,Justificacion,List) genera UUID/BORRADOR. enviarRevision() exige BORRADOR, invoca validarInvariantes() y cambia estado. validarInvariantes(): exactamente 4 opciones y count(correcta)==1. Getters exponen VO/lista inmutables.
7. **Relaciones:** Enunciado; Justificacion; OpcionRespuesta; EstadoPregunta; ReglaNegocioException; mappers/repositorios
8. **Requisito:** G p.3; E p.1; A01/A10–A14
9. **Evaluación:** Cardinalidad, clave única, reenvío e inmutabilidad pasan los 14 tests. Hay una brecha de texto heredada de sus VO (HALL-01).
10. **Errores, riesgos o mejora:** El agregado confía correctamente en VO válidos; HALL-01 permite construir uno vacío y no vuelve a validar ese texto. No corregirlo duplicando condiciones en el controlador.

**Si no existiera:** Las reglas y el cambio de estado quedarían dispersos entre capas.

## Puertos y adaptadores

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/inbound/CrearPreguntaUseCase.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/inbound/CrearPreguntaUseCase.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/inbound/CrearPreguntaUseCase.java>).
2. **Nombre:** `CrearPreguntaUseCase.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/main/java/co/edu/unicauca/saberpro/domain/ports/inbound/CrearPreguntaUseCase.java` en e857f96.
4. **Para qué sirve:** Definir operaciones de creación y consulta desde el núcleo.
5. **Contenido y funcionamiento:** Interfaz de entrada implementada por la aplicación e inyectada en la API; la revisión fue separada al segundo puerto.
6. **Métodos, anotaciones o configuraciones importantes:** crear(String enunciado,String justificacion,List<OpcionRespuesta>) →Pregunta; buscar(UUID) →Pregunta; listar() →List<Pregunta>.
7. **Relaciones:** QuestionApplicationService; QuestionRestController
8. **Requisito:** G p.2–3; E p.2; B01
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** El controlador dependería de una clase concreta de aplicación.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/inbound/EvaluarPreguntaUseCase.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/inbound/EvaluarPreguntaUseCase.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/inbound/EvaluarPreguntaUseCase.java>).
2. **Nombre:** `EvaluarPreguntaUseCase.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Definir la evaluación de preparación y solicitud de revisión.
5. **Contenido y funcionamiento:** La guía E enumera este nombre sin definir una calificación. La implementación conserva el alcance documentado: comprobar invariantes y solicitar revisión.
6. **Métodos, anotaciones o configuraciones importantes:** enviarRevision(UUID id) →Pregunta en PENDIENTE_REVISION o excepción. Interfaz Java pura.
7. **Relaciones:** QuestionApplicationService; QuestionRestController; Pregunta
8. **Requisito:** E p.2; B02
9. **Evaluación:** Implementación coherente con la operación de revisión definida en G p.5; comprobada por HTTP.
10. **Errores, riesgos o mejora:** Explicar esta interpretación en sustentación. No afirmar que califica exámenes ni aprueba preguntas.

**Si no existiera:** La API no tendría el contrato separado exigido por la arborescencia E.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/outbound/NotificacionRevisionPort.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/outbound/NotificacionRevisionPort.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/outbound/NotificacionRevisionPort.java>).
2. **Nombre:** `NotificacionRevisionPort.java`.
3. **Estado:** Trasladado/reutilizado (solo paquete/nombre). Origen: `src/main/java/co/edu/unicauca/saberpro/domain/ports/outbound/NotificacionRevisionPort.java` en e857f96.
4. **Para qué sirve:** Describir la notificación de una pregunta que pasó a revisión.
5. **Contenido y funcionamiento:** Se conserva el nombre del Taller 12 como equivalente a NotificacionPort de la guía. La interfaz no menciona RabbitMQ.
6. **Métodos, anotaciones o configuraciones importantes:** notificarPreguntaPendiente(Pregunta) →void; un fallo debe propagarse al coordinador.
7. **Relaciones:** QuestionApplicationService; RabbitMQPublisher; RegistroNotificaciones en test unitario
8. **Requisito:** E p.1–2; B04
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** La aplicación tendría que conocer RabbitTemplate o el canal concreto.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/outbound/PreguntaRepositoryPort.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/outbound/PreguntaRepositoryPort.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/outbound/PreguntaRepositoryPort.java>).
2. **Nombre:** `PreguntaRepositoryPort.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/main/java/co/edu/unicauca/saberpro/domain/ports/outbound/PreguntaRepositoryPort.java` en e857f96.
4. **Para qué sirve:** Abstraer almacenamiento y actualización local consistente.
5. **Contenido y funcionamiento:** Permite sustituir JPA por HashMap. Su método actualizar exige serialización y rollback si falla el efecto posterior al guardado.
6. **Métodos, anotaciones o configuraciones importantes:** save(Pregunta) →Pregunta; findById(UUID) →Optional; findAll() →List; actualizar(UUID,Consumer<Pregunta> cambio,Consumer<Pregunta> despuesDeGuardar) →Pregunta.
7. **Relaciones:** QuestionApplicationService; JpaQuestionAdapter; InMemoryQuestionAdapter
8. **Requisito:** G p.3; E p.2; B03/B09–B12/G07
9. **Evaluación:** Contrato abstracto y operación transaccional útiles; se cumple en flujos normales.
10. **Errores, riesgos o mejora:** HALL-01 revela una diferencia de admisión entre adaptadores. El efecto externo no se vuelve transaccional por estar en un callback (L01).

**Si no existiera:** La aplicación quedaría acoplada al motor de persistencia.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/outbound/ValidacionContenidoPort.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/outbound/ValidacionContenidoPort.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/outbound/ValidacionContenidoPort.java>).
2. **Nombre:** `ValidacionContenidoPort.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Definir validación externa sin acoplarse a REST.
5. **Contenido y funcionamiento:** La aplicación invoca validar antes de guardar; el adaptador decide transporte y traducción de errores.
6. **Métodos, anotaciones o configuraciones importantes:** validar(Pregunta) →void; éxito permite continuar, excepción impide guardar.
7. **Relaciones:** QuestionApplicationService; RestValidationAdapter; HexagonalConfiguration
8. **Requisito:** U: validación REST funcional; C03/B11
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** La aplicación tendría que usar RestClient directamente.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/validation/RestValidationAdapter.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/validation/RestValidationAdapter.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/validation/RestValidationAdapter.java>).
2. **Nombre:** `RestValidationAdapter.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Implementar la salida REST hacia Validation.
5. **Contenido y funcionamiento:** Envía cadenas normalizadas/opciones a /api/validaciones/preguntas; verifica respuesta y traduce errores.
6. **Métodos, anotaciones o configuraciones importantes:** validar(Pregunta) →void; Resultado(Boolean valida,List<String> errores). RestClient.post/body/retrieve. Null o respuesta incompleta →DependenciaNoDisponibleException; false o HTTP 400 →ReglaNegocioException; RestClientException →503 vía API.
7. **Relaciones:** ValidacionContenidoPort; ValidationRestController remoto; HexagonalConfiguration; excepciones
8. **Requisito:** C03/E08; G p.2,5
9. **Evaluación:** Validación remota, límite excesivo y dependencia caída comprobados.
10. **Errores, riesgos o mejora:** El 400 remoto se resume como límite técnico y no conserva todos sus detalles. No sustituye las invariantes DDD; HALL-01 debe corregirse en los VO.

**Si no existiera:** La creación dejaría de realizar la interacción REST requerida.

## Persistencia JPA y memoria

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/JpaQuestionAdapter.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/JpaQuestionAdapter.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/JpaQuestionAdapter.java>).
2. **Nombre:** `JpaQuestionAdapter.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/out/jpa/JpaQuestionAdapter.java` en e857f96.
4. **Para qué sirve:** Implementar persistencia y rollback local con JPA.
5. **Contenido y funcionamiento:** Usa mapper y repositorio Spring. En actualizar bloquea, cambia, flush, notifica y deja que Spring confirme o revierta.
6. **Métodos, anotaciones o configuraciones importantes:** @Transactional en save y actualizar; readOnly en findById/findAll. save(Pregunta) →dominio rehidratado; findById(UUID) →Optional; findAll() →lista ordenada por UUID; actualizar(UUID,cambio,efecto) →Pregunta o excepción.
7. **Relaciones:** PreguntaRepositoryPort; QuestionDomainMapper; SpringDataPreguntaRepository; callback de RabbitMQPublisher
8. **Requisito:** G p.3; E p.2; G01/G05/G07/D08
9. **Evaluación:** H2 real, rollback con mensaje sin ruta y concurrencia comprobados.
10. **Errores, riesgos o mejora:** HALL-01: admite menos entradas que memory por rehidratar tras guardar. L01: ACK antes del commit no elimina la ventana de fallo distribuida.

**Si no existiera:** No habría almacenamiento relacional intercambiable ni rollback JPA.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/OpcionEmbeddable.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/OpcionEmbeddable.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/OpcionEmbeddable.java>).
2. **Nombre:** `OpcionEmbeddable.java`.
3. **Estado:** Trasladado/reutilizado (solo paquete/nombre). Origen: `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/out/jpa/OpcionEmbeddable.java` en e857f96.
4. **Para qué sirve:** Mapear cada alternativa a la colección relacional.
5. **Contenido y funcionamiento:** Almacena texto y bandera; depende de la entidad propietaria y de la posición en la lista.
6. **Métodos, anotaciones o configuraciones importantes:** @Embeddable; @Column(nullable=false,length=2000) texto; boolean correcta. Constructor protegido y constructor(String,boolean); getTexto/isCorrecta.
7. **Relaciones:** QuestionRecord.opciones; QuestionDomainMapper
8. **Requisito:** G01–G03; E p.2
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** No contiene reglas de negocio por diseño; no debe confundirse con OpcionRespuesta.

**Si no existiera:** No se persistirían las opciones completas del agregado.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/QuestionDomainMapper.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/QuestionDomainMapper.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/QuestionDomainMapper.java>).
2. **Nombre:** `QuestionDomainMapper.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Traducir entre entidad JPA y dominio puro.
5. **Contenido y funcionamiento:** Genera objetos distintos en cada sentido y reconstruye VO al leer, lo que vuelve a aplicar sus invariantes.
6. **Métodos, anotaciones o configuraciones importantes:** toRecord(Pregunta) →QuestionRecord, copia UUID/textos/estado/opciones. toDomain(QuestionRecord) →Pregunta usando Enunciado, Justificacion, OpcionRespuesta y EstadoPregunta.valueOf. Métodos static, constructor privado.
7. **Relaciones:** QuestionRecord; OpcionEmbeddable; VO; JpaQuestionAdapter
8. **Requisito:** E p.2; G02–G03
9. **Evaluación:** Conversión correcta en los casos normales; segunda construcción de VO explica el rechazo JPA del caso HALL-01.
10. **Errores, riesgos o mejora:** El mapper detecta incidentalmente el texto ya vacío; eso no arregla la invariante del VO ni protege el adaptador memory.

**Si no existiera:** La persistencia y el dominio compartirían representación o duplicarían conversión dispersa.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/QuestionRecord.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/QuestionRecord.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/QuestionRecord.java>).
2. **Nombre:** `QuestionRecord.java`.
3. **Estado:** Trasladado/reutilizado (solo paquete/nombre). Origen: `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/out/jpa/PreguntaJpaEntity.java` en e857f96.
4. **Para qué sirve:** Representar la entidad relacional de una pregunta.
5. **Contenido y funcionamiento:** Almacena UUID, textos, estado y opciones en tabla secundaria ordenada. No se expone como dominio.
6. **Métodos, anotaciones o configuraciones importantes:** @Entity/@Table(preguntas), @Id, @Column(4000), @ElementCollection(EAGER), @CollectionTable(opciones_pregunta), @OrderColumn(posicion). Constructor protegido para Hibernate; setters/getters; setOpciones(List) copia a ArrayList mutable.
7. **Relaciones:** OpcionEmbeddable; QuestionDomainMapper; SpringDataPreguntaRepository
8. **Requisito:** E p.2; G01–G03
9. **Evaluación:** Persistencia, orden y estado verificados mediante API JPA.
10. **Errores, riesgos o mejora:** EAGER/listado completo cargan todas las opciones; coste aceptable para el taller, no prueba de escalabilidad.

**Si no existiera:** JPA no tendría una entidad separada y se tentaría anotar el agregado.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/SpringDataPreguntaRepository.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/SpringDataPreguntaRepository.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/SpringDataPreguntaRepository.java>).
2. **Nombre:** `SpringDataPreguntaRepository.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/out/jpa/SpringDataPreguntaRepository.java` en e857f96.
4. **Para qué sirve:** Proveer consultas JPA y bloqueo de una pregunta al revisar.
5. **Contenido y funcionamiento:** Spring genera las operaciones de JpaRepository. La consulta con bloqueo evita dos transiciones concurrentes.
6. **Métodos, anotaciones o configuraciones importantes:** JpaRepository<QuestionRecord,UUID>; findForUpdate(@Param("id") UUID) →Optional<QuestionRecord>; @Lock(PESSIMISTIC_WRITE), @Query JPQL.
7. **Relaciones:** JpaQuestionAdapter; QuestionRecord; gestor de transacciones/H2
8. **Requisito:** G01/G05; D08
9. **Evaluación:** Dos envíos simultáneos producen un 202 y un 400.
10. **Errores, riesgos o mejora:** Necesita transacción activa, proporcionada por JpaQuestionAdapter.actualizar.

**Si no existiera:** Faltaría acceso JPA y la serialización de revisiones.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/memory/InMemoryQuestionAdapter.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/memory/InMemoryQuestionAdapter.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/memory/InMemoryQuestionAdapter.java>).
2. **Nombre:** `InMemoryQuestionAdapter.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/out/memory/InMemoryQuestionAdapter.java` en e857f96.
4. **Para qué sirve:** Implementar el mismo puerto con HashMap y copias.
5. **Contenido y funcionamiento:** Todos los métodos públicos sincronizan sobre el adaptador; la revisión conserva una copia previa para restaurarla si falla notificar.
6. **Métodos, anotaciones o configuraciones importantes:** save(Pregunta) →copia; findById(UUID) →Optional copia; findAll() →lista inmutable ordenada; actualizar(UUID,cambio,efecto) →copia, restaura ante RuntimeException/Error; copiar(Pregunta) conserva VO y copia el agregado.
7. **Relaciones:** PreguntaRepositoryPort; Pregunta; servicio de aplicación
8. **Requisito:** G p.3; B07/B09/B12; G06–G07
9. **Evaluación:** Flujos normales y rollback funcionan; ⚠️ HALL-01 demuestra inequivalencia con JPA.
10. **Errores, riesgos o mejora:** Reutiliza VO inmutables en vez de reconstruirlos; un VO inválido por HALL-01 permanece inválido. Es temporal y el monitor serializa todas las revisiones; no sirve para compartir estado entre réplicas.

**Si no existiera:** No se podría demostrar cambio de mecanismo de almacenamiento sin alterar el núcleo.

## RabbitMQ y comunicación asíncrona

### `lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/messaging/QuestionReviewListener.java`

1. **Ruta:** [lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/messaging/QuestionReviewListener.java](<../../lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/messaging/QuestionReviewListener.java>).
2. **Nombre:** `QuestionReviewListener.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Consumir eventos reales con @RabbitListener.
5. **Contenido y funcionamiento:** Deserializa el cuerpo sin depender de headers de clase Java, valida, registra transaccionalmente y escribe IDs. Retorna solo tras completar procesamiento para permitir ACK.
6. **Métodos, anotaciones o configuraciones importantes:** recibir(Message) →void; @RabbitListener(queues="${app.messaging.review-queue}"); mapper.readValue; validate; processing.registrar. Contrato inválido →AmqpRejectAndDontRequeueException; logs procesada/duplicada ignorada.
7. **Relaciones:** QuestionReviewMessage; ReviewProcessingService; RabbitTopology; configuración de retry
8. **Requisito:** G p.2,5; E p.2; C07/D04/D07/D09
9. **Evaluación:** Consumo real, ID coincidente, deduplicación y DLQ verificados.
10. **Errores, riesgos o mejora:** Los reintentos son finitos y el ACK ocurre después del commit local. Puede recibir antes del commit de Question: L01.

**Si no existiera:** Habría eventos acumulados pero no un consumidor independiente, incumpliendo la integración.

### `lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/messaging/QuestionReviewMessage.java`

1. **Ruta:** [lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/messaging/QuestionReviewMessage.java](<../../lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/messaging/QuestionReviewMessage.java>).
2. **Nombre:** `QuestionReviewMessage.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Representar y validar el contrato JSON recibido.
5. **Contenido y funcionamiento:** Record propio del consumidor; no importa clases de Question. Rechaza campos fundamentales ausentes, versión/tipo/estado desconocidos.
6. **Métodos, anotaciones o configuraciones importantes:** validate() →void o IllegalArgumentException; exige eventId/preguntaId/occurredAt no null, schemaVersion==1, type PreguntaEnviadaARevision y estado PENDIENTE_REVISION.
7. **Relaciones:** ObjectMapper de QuestionReviewListener; ReviewProcessingService
8. **Requisito:** D04–D05; U errores de consumo
9. **Evaluación:** Evento v1 aceptado y versión desconocida enviada a DLQ.
10. **Errores, riesgos o mejora:** Valida el sobre, no vuelve a validar el contenido de la pregunta, que pertenece a Question. No constituye una aprobación académica.

**Si no existiera:** El consumidor aceptaría mensajes sin garantías mínimas de contrato.

### `lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/messaging/RabbitTopology.java`

1. **Ruta:** [lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/messaging/RabbitTopology.java](<../../lifecycle-service/src/main/java/co/edu/unicauca/saberpro/lifecycle/messaging/RabbitTopology.java>).
2. **Nombre:** `RabbitTopology.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Declarar la cola de revisión y su ruta de errores desde lifecycle.
5. **Contenido y funcionamiento:** Construye los mismos objetos durables en ambos servicios; RabbitAdmin los declara al conectarse.
6. **Métodos, anotaciones o configuraciones importantes:** reviewTopology(@Value String queue) →Declarables; QueueBuilder.durable con deadLetterExchange/routingKey; DirectExchange(queue+".dlx"); Queue(queue+".dlq"); BindingBuilder enlaza DLQ. @Configuration/@Bean.
7. **Relaciones:** QuestionReviewListener
8. **Requisito:** G p.5; D01/D03
9. **Evaluación:** Cola requerida, consumidor y DLQ comprobados con broker real.
10. **Errores, riesgos o mejora:** Las declaraciones duplicadas deben mantener idénticos argumentos. Es independencia de despliegue, no duplicación del dominio.

**Si no existiera:** Se perdería la declaración automática; una cola inexistente haría fallar la publicación/escucha.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/messaging/QuestionReviewEvent.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/messaging/QuestionReviewEvent.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/messaging/QuestionReviewEvent.java>).
2. **Nombre:** `QuestionReviewEvent.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Definir el JSON v1 producido al enviar a revisión.
5. **Contenido y funcionamiento:** Genera eventId determinista por pregunta y un timestamp de intento, con tipo/estado explícitos.
6. **Métodos, anotaciones o configuraciones importantes:** Record(UUID eventId,int schemaVersion,String type,UUID preguntaId,String estado,Instant occurredAt); from(Pregunta) →evento con UUID.nameUUIDFromBytes("revision:"+id), versión 1 y estado del agregado.
7. **Relaciones:** RabbitMQPublisher; contrato independiente QuestionReviewMessage en Lifecycle
8. **Requisito:** D02/D05; G p.5
9. **Evaluación:** Se comprobó preguntaId, versión, estado e identidad estable en tests y consumo real.
10. **Errores, riesgos o mejora:** La identidad fija es adecuada para una única transición; nuevos ciclos futuros exigirían revisar el contrato. No se inventan esos ciclos ahora.

**Si no existiera:** Productor y consumidor no tendrían un formato estable documentado.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/messaging/RabbitMQPublisher.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/messaging/RabbitMQPublisher.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/messaging/RabbitMQPublisher.java>).
2. **Nombre:** `RabbitMQPublisher.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Enviar un evento real y comprobar aceptación por RabbitMQ.
5. **Contenido y funcionamiento:** Serializa JSON, marca mensaje persistente y mandatory en configuración; espera future de confirmación. Un ACK con return se rechaza también.
6. **Métodos, anotaciones o configuraciones importantes:** Constructor(RabbitTemplate,ObjectMapper,String queue,Duration timeout). notificarPreguntaPendiente(Pregunta) →void; MessageProperties JSON/UTF-8/PERSISTENT/messageId; CorrelationData único por intento; send("",queue,message,correlation); get(timeout); isAck/getReturned. InterruptedException restaura flag; demás fallas →DependenciaNoDisponibleException.
7. **Relaciones:** NotificacionRevisionPort; QuestionReviewEvent; RabbitTopology; HexagonalConfiguration; broker
8. **Requisito:** G p.2,5; D01–D02/D08; E06
9. **Evaluación:** ACK/NACK/timeout/return en unitarias y publicación/no-route/consumo real en integración.
10. **Errores, riesgos o mejora:** L01: publisher confirm acredita aceptación, no consumo ni commit H2. La espera del broker puede ocupar la transacción hasta el timeout.

**Si no existiera:** La revisión volvería a ser solo cambio de estado/log, sin integración asíncrona.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/messaging/RabbitTopology.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/messaging/RabbitTopology.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/messaging/RabbitTopology.java>).
2. **Nombre:** `RabbitTopology.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Declarar la cola de revisión y su ruta de errores desde question.
5. **Contenido y funcionamiento:** Construye los mismos objetos durables en ambos servicios; RabbitAdmin los declara al conectarse.
6. **Métodos, anotaciones o configuraciones importantes:** reviewTopology(@Value String queue) →Declarables; QueueBuilder.durable con deadLetterExchange/routingKey; DirectExchange(queue+".dlx"); Queue(queue+".dlq"); BindingBuilder enlaza DLQ. @Configuration/@Bean.
7. **Relaciones:** RabbitMQPublisher
8. **Requisito:** G p.5; D01/D03
9. **Evaluación:** Cola requerida, consumidor y DLQ comprobados con broker real.
10. **Errores, riesgos o mejora:** Las declaraciones duplicadas deben mantener idénticos argumentos. Es independencia de despliegue, no duplicación del dominio.

**Si no existiera:** Se perdería la declaración automática; una cola inexistente haría fallar la publicación/escucha.

## Docker y Docker Compose

### `.dockerignore`

1. **Ruta:** [.dockerignore](<../../.dockerignore>).
2. **Nombre:** `.dockerignore`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Limitar un eventual contexto Docker situado en la raíz.
5. **Contenido y funcionamiento:** Excluye .git, target recursivo, docs, logs e IDE.
6. **Métodos, anotaciones o configuraciones importantes:** Patrones de exclusión; no contiene código Java.
7. **Relaciones:** docker build con contexto raíz; los tres contextos Compose usan sus propios .dockerignore
8. **Requisito:** F13: apoyo a contenerización
9. **Evaluación:** Sintaxis razonable; no interviene en los tres builds del Compose actual.
10. **Errores, riesgos o mejora:** Potencialmente innecesario en el flujo actual: build usa ./servicio, no la raíz. No es un fallo de construcción.

### `docker-compose.yml`

1. **Ruta:** [docker-compose.yml](<../../docker-compose.yml>).
2. **Nombre:** `docker-compose.yml`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Construir y ejecutar el ecosistema completo.
5. **Contenido y funcionamiento:** Orquesta RabbitMQ y tres imágenes propias, red bridge, puertos, volúmenes y esperas por salud.
6. **Métodos, anotaciones o configuraciones importantes:** name saberpro-taller13; APP_REPOSITORY por defecto jpa; VALIDATION_SERVICE_URL; SPRING_RABBITMQ_HOST; DB_URL; service_healthy; healthchecks; tres volúmenes.
7. **Relaciones:** Los tres Dockerfile, properties, RabbitTopology y scripts de verificación
8. **Requisito:** G p.3–5; E p.1; F07–F14/D01
9. **Evaluación:** config, build, up, healthchecks, conectividad y puertos comprobados en Docker real.
10. **Errores, riesgos o mejora:** guest/guest es la configuración didáctica de la guía; el entorno probado es una instancia por servicio. H2 en archivo no está diseñado aquí para compartir el volumen entre réplicas.

### `lifecycle-service/.dockerignore`

1. **Ruta:** [lifecycle-service/.dockerignore](<../../lifecycle-service/.dockerignore>).
2. **Nombre:** `.dockerignore`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Excluir salidas locales del contexto de lifecycle-service.
5. **Contenido y funcionamiento:** Evita copiar target, .git y logs del módulo; Docker compila desde pom.xml y src.
6. **Métodos, anotaciones o configuraciones importantes:** target/, .git/, *.log.
7. **Relaciones:** Dockerfile del mismo servicio y build: ./lifecycle-service
8. **Requisito:** F13: construcción reproducible
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

### `lifecycle-service/Dockerfile`

1. **Ruta:** [lifecycle-service/Dockerfile](<../../lifecycle-service/Dockerfile>).
2. **Nombre:** `Dockerfile`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Construir y ejecutar lifecycle-service con Java 17.
5. **Contenido y funcionamiento:** Etapa Maven compila el JAR; la etapa Temurin copia el ejecutable y lo inicia como usuario 10001.
6. **Métodos, anotaciones o configuraciones importantes:** FROM maven:3.9.9-eclipse-temurin-17; RUN clean package -DskipTests; FROM eclipse-temurin:17-jdk-jammy; WORKDIR /app; COPY --from=build; EXPOSE 8084; ENTRYPOINT java -jar app.jar.
7. **Relaciones:** pom.xml y src del módulo; docker-compose.yml
8. **Requisito:** G p.3; E p.1; F01–F06
9. **Evaluación:** Build y arranque reales correctos; JVM 17 verificada dentro del contenedor.
10. **Errores, riesgos o mejora:** -DskipTests no certifica pruebas: estas se ejecutaron aparte. La imagen Java difiere justificadamente de la receta G; véase decisiones.md y evidencia del manifiesto.

### `question-service/.dockerignore`

1. **Ruta:** [question-service/.dockerignore](<../../question-service/.dockerignore>).
2. **Nombre:** `.dockerignore`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Excluir salidas locales del contexto de question-service.
5. **Contenido y funcionamiento:** Evita copiar target, .git y logs del módulo; Docker compila desde pom.xml y src.
6. **Métodos, anotaciones o configuraciones importantes:** target/, .git/, *.log.
7. **Relaciones:** Dockerfile del mismo servicio y build: ./question-service
8. **Requisito:** F13: construcción reproducible
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

### `question-service/Dockerfile`

1. **Ruta:** [question-service/Dockerfile](<../../question-service/Dockerfile>).
2. **Nombre:** `Dockerfile`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Construir y ejecutar question-service con Java 17.
5. **Contenido y funcionamiento:** Etapa Maven compila el JAR; la etapa Temurin copia el ejecutable y lo inicia como usuario 10001.
6. **Métodos, anotaciones o configuraciones importantes:** FROM maven:3.9.9-eclipse-temurin-17; RUN clean package -DskipTests; FROM eclipse-temurin:17-jdk-jammy; WORKDIR /app; COPY --from=build; EXPOSE 8083; ENTRYPOINT java -jar app.jar.
7. **Relaciones:** pom.xml y src del módulo; docker-compose.yml
8. **Requisito:** G p.3; E p.1; F01–F06
9. **Evaluación:** Build y arranque reales correctos; JVM 17 verificada dentro del contenedor.
10. **Errores, riesgos o mejora:** -DskipTests no certifica pruebas: estas se ejecutaron aparte. La imagen Java difiere justificadamente de la receta G; véase decisiones.md y evidencia del manifiesto.

### `validation-service/.dockerignore`

1. **Ruta:** [validation-service/.dockerignore](<../../validation-service/.dockerignore>).
2. **Nombre:** `.dockerignore`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Excluir salidas locales del contexto de validation-service.
5. **Contenido y funcionamiento:** Evita copiar target, .git y logs del módulo; Docker compila desde pom.xml y src.
6. **Métodos, anotaciones o configuraciones importantes:** target/, .git/, *.log.
7. **Relaciones:** Dockerfile del mismo servicio y build: ./validation-service
8. **Requisito:** F13: construcción reproducible
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

### `validation-service/Dockerfile`

1. **Ruta:** [validation-service/Dockerfile](<../../validation-service/Dockerfile>).
2. **Nombre:** `Dockerfile`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Construir y ejecutar validation-service con Java 17.
5. **Contenido y funcionamiento:** Etapa Maven compila el JAR; la etapa Temurin copia el ejecutable y lo inicia como usuario 10001.
6. **Métodos, anotaciones o configuraciones importantes:** FROM maven:3.9.9-eclipse-temurin-17; RUN clean package -DskipTests; FROM eclipse-temurin:17-jdk-jammy; WORKDIR /app; COPY --from=build; EXPOSE 8082; ENTRYPOINT java -jar app.jar.
7. **Relaciones:** pom.xml y src del módulo; docker-compose.yml
8. **Requisito:** G p.3; E p.1; F01–F06
9. **Evaluación:** Build y arranque reales correctos; JVM 17 verificada dentro del contenedor.
10. **Errores, riesgos o mejora:** -DskipTests no certifica pruebas: estas se ejecutaron aparte. La imagen Java difiere justificadamente de la receta G; véase decisiones.md y evidencia del manifiesto.

## Configuraciones

### `lifecycle-service/mvnw`

1. **Ruta:** [lifecycle-service/mvnw](<../../lifecycle-service/mvnw>).
2. **Nombre:** `mvnw`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Permitir el comando ./mvnw desde lifecycle-service.
5. **Contenido y funcionamiento:** Shell calcula la ruta del módulo y delega al wrapper raíz con -f al POM propio, conservando los argumentos.
6. **Métodos, anotaciones o configuraciones importantes:** exec, dirname y -f; no contiene otra distribución Maven.
7. **Relaciones:** ../mvnw, ../.mvn/wrapper/maven-wrapper.properties, pom.xml del módulo
8. **Requisito:** G p.3: mvnw desde cada servicio
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Requiere conservar el módulo dentro del repositorio; el POM y Dockerfile sí son autónomos, pero este wrapper delegador depende de la raíz.

### `lifecycle-service/pom.xml`

1. **Ruta:** [lifecycle-service/pom.xml](<../../lifecycle-service/pom.xml>).
2. **Nombre:** `pom.xml`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Definir compilación y dependencias autónomas de lifecycle-service.
5. **Contenido y funcionamiento:** Hereda Spring Boot 3.4.5 directamente y empaqueta un ejecutable Java 17, sin dependencia Maven de los otros servicios.
6. **Métodos, anotaciones o configuraciones importantes:** Dependencias: Web, Bean Validation, Actuator, JPA, H2, AMQP, spring-retry y starter-test. spring-boot-maven-plugin; Surefire excluye *IntegrationTest; perfil integration activa Failsafe integration-test y verify.
7. **Relaciones:** Fuentes/test del módulo, Dockerfile, POM agregador
8. **Requisito:** G p.2–3; E p.1–2; H09–H10
9. **Evaluación:** Compiló individualmente y en el reactor Java 17; tests de su módulo aprobados.
10. **Errores, riesgos o mejora:** mvn test por sí solo no ejecuta los tests de integración. En Validation el perfil existe por uniformidad aunque no hay clases *IntegrationTest.

### `lifecycle-service/src/main/resources/application.properties`

1. **Ruta:** [lifecycle-service/src/main/resources/application.properties](<../../lifecycle-service/src/main/resources/application.properties>).
2. **Nombre:** `application.properties`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Configurar el consumidor y la persistencia de comprobantes.
5. **Contenido y funcionamiento:** Escucha en 8084, usa H2 y RabbitMQ; procesa con un consumidor y reintentos limitados.
6. **Métodos, anotaciones o configuraciones importantes:** concurrency/max-concurrency=1; prefetch=10; default-requeue-rejected=false; retry.max-attempts=3; 500ms y multiplier=2; DB_URL; cola; health/info.
7. **Relaciones:** QuestionReviewListener, ReviewProcessingService, RabbitTopology, Compose
8. **Requisito:** C06–C07/D04/D07/F10
9. **Evaluación:** Recepción, deduplicación y DLQ reales verificadas.
10. **Errores, riesgos o mejora:** La garantía observada de serialización corresponde a una instancia con concurrencia 1; no se probó escalado horizontal.

### `question-service/mvnw`

1. **Ruta:** [question-service/mvnw](<../../question-service/mvnw>).
2. **Nombre:** `mvnw`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Permitir el comando ./mvnw desde question-service.
5. **Contenido y funcionamiento:** Shell calcula la ruta del módulo y delega al wrapper raíz con -f al POM propio, conservando los argumentos.
6. **Métodos, anotaciones o configuraciones importantes:** exec, dirname y -f; no contiene otra distribución Maven.
7. **Relaciones:** ../mvnw, ../.mvn/wrapper/maven-wrapper.properties, pom.xml del módulo
8. **Requisito:** G p.3: mvnw desde cada servicio
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Requiere conservar el módulo dentro del repositorio; el POM y Dockerfile sí son autónomos, pero este wrapper delegador depende de la raíz.

### `question-service/pom.xml`

1. **Ruta:** [question-service/pom.xml](<../../question-service/pom.xml>).
2. **Nombre:** `pom.xml`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Definir compilación y dependencias autónomas de question-service.
5. **Contenido y funcionamiento:** Hereda Spring Boot 3.4.5 directamente y empaqueta un ejecutable Java 17, sin dependencia Maven de los otros servicios.
6. **Métodos, anotaciones o configuraciones importantes:** Dependencias: Web, Bean Validation, Actuator, JPA, H2, AMQP y starter-test. spring-boot-maven-plugin; Surefire excluye *IntegrationTest; perfil integration activa Failsafe integration-test y verify.
7. **Relaciones:** Fuentes/test del módulo, Dockerfile, POM agregador
8. **Requisito:** G p.2–3; E p.1–2; H09–H10
9. **Evaluación:** Compiló individualmente y en el reactor Java 17; tests de su módulo aprobados.
10. **Errores, riesgos o mejora:** mvn test por sí solo no ejecuta los tests de integración. En Validation el perfil existe por uniformidad aunque no hay clases *IntegrationTest.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/config/HexagonalConfiguration.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/config/HexagonalConfiguration.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/config/HexagonalConfiguration.java>).
2. **Nombre:** `HexagonalConfiguration.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/main/java/co/edu/unicauca/saberpro/infrastructure/config/HexagonalConfiguration.java` en e857f96.
4. **Para qué sirve:** Conectar los puertos con implementaciones mediante inyección de dependencias.
5. **Contenido y funcionamiento:** Selecciona JPA/memory por propiedad y construye el cliente HTTP, publicador y servicio de aplicación.
6. **Métodos, anotaciones o configuraciones importantes:** @Configuration/@Bean/@ConditionalOnProperty; jpaQuestionPort(SpringDataPreguntaRepository) →JpaQuestionAdapter; memoryQuestionPort() →InMemoryQuestionAdapter; validacionContenidoPort(builder,baseUrl,Duration) →RestValidationAdapter con timeouts; notificacionRevisionPort(template,mapper,queue,timeout) →RabbitMQPublisher mandatory; questionApplicationService(repo,notificaciones,validacion) →servicio puro.
7. **Relaciones:** Todos los puertos/adaptadores; application.properties; Spring
8. **Requisito:** G p.3 §3.2; B08–B09/B12
9. **Evaluación:** Ambos contextos y la instancia memory aislada seleccionaron sus adaptadores por configuración.
10. **Errores, riesgos o mejora:** Un valor distinto de jpa/memory impide resolver el puerto; fail-fast razonable. Sincronización/confirmaciones se comprueban fuera del dominio.

**Si no existiera:** Spring no sabría construir el núcleo ni elegir su almacenamiento.

### `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/config/JsonConfiguration.java`

1. **Ruta:** [question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/config/JsonConfiguration.java](<../../question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/config/JsonConfiguration.java>).
2. **Nombre:** `JsonConfiguration.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Rechazar coerciones de entrada en la API de preguntas.
5. **Contenido y funcionamiento:** Configura los tipos textuales del ObjectMapper de Spring sin añadir Jackson al dominio.
6. **Métodos, anotaciones o configuraciones importantes:** strictTextTypes() →Jackson2ObjectMapperBuilderCustomizer; Integer/Float/Boolean →Fail; @Configuration/@Bean.
7. **Relaciones:** QuestionRestController; ApiExceptionHandler; application.properties
8. **Requisito:** E08; U contratos JSON
9. **Evaluación:** Un enunciado numérico devuelve 400 en integración.
10. **Errores, riesgos o mejora:** Valida tipos JSON, no contenido textual: no resuelve HALL-01.

**Si no existiera:** La API podría aceptar tipos numéricos transformados silenciosamente a cadena.

### `question-service/src/main/resources/application.properties`

1. **Ruta:** [question-service/src/main/resources/application.properties](<../../question-service/src/main/resources/application.properties>).
2. **Nombre:** `application.properties`.
3. **Estado:** Trasladado y modificado. Origen: `src/main/resources/application.properties` en e857f96.
4. **Para qué sirve:** Configurar persistencia, validación REST y publicación de Question.
5. **Contenido y funcionamiento:** Por defecto JPA con H2 en memoria local; Compose cambia DB_URL a archivo. Las variables sustituyen repositorio, URL y broker.
6. **Métodos, anotaciones o configuraciones importantes:** 8083; APP_REPOSITORY; timeout REST 3s; cola pregunta.revisar.queue; confirm-timeout 5s; confirms correlated; returns y mandatory; H2; open-in-view=false; ddl-auto=update.
7. **Relaciones:** HexagonalConfiguration, RestValidationAdapter, RabbitMQPublisher, Compose
8. **Requisito:** B08–B09/C04/D01/F10/G04
9. **Evaluación:** Selección efectiva y conexiones comprobadas en ambos modos.
10. **Errores, riesgos o mejora:** El modo memory conserva dependencias y autoconfiguración JPA disponibles; que Hikari aparezca en logs no prueba que el puerto de repositorio use JPA. Fuera de Compose, H2 predeterminada es efímera.

### `validation-service/mvnw`

1. **Ruta:** [validation-service/mvnw](<../../validation-service/mvnw>).
2. **Nombre:** `mvnw`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Permitir el comando ./mvnw desde validation-service.
5. **Contenido y funcionamiento:** Shell calcula la ruta del módulo y delega al wrapper raíz con -f al POM propio, conservando los argumentos.
6. **Métodos, anotaciones o configuraciones importantes:** exec, dirname y -f; no contiene otra distribución Maven.
7. **Relaciones:** ../mvnw, ../.mvn/wrapper/maven-wrapper.properties, pom.xml del módulo
8. **Requisito:** G p.3: mvnw desde cada servicio
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Requiere conservar el módulo dentro del repositorio; el POM y Dockerfile sí son autónomos, pero este wrapper delegador depende de la raíz.

### `validation-service/pom.xml`

1. **Ruta:** [validation-service/pom.xml](<../../validation-service/pom.xml>).
2. **Nombre:** `pom.xml`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Definir compilación y dependencias autónomas de validation-service.
5. **Contenido y funcionamiento:** Hereda Spring Boot 3.4.5 directamente y empaqueta un ejecutable Java 17, sin dependencia Maven de los otros servicios.
6. **Métodos, anotaciones o configuraciones importantes:** Dependencias: Web, Bean Validation, Actuator y starter-test. spring-boot-maven-plugin; Surefire excluye *IntegrationTest; perfil integration activa Failsafe integration-test y verify.
7. **Relaciones:** Fuentes/test del módulo, Dockerfile, POM agregador
8. **Requisito:** G p.2–3; E p.1–2; H09–H10
9. **Evaluación:** Compiló individualmente y en el reactor Java 17; tests de su módulo aprobados.
10. **Errores, riesgos o mejora:** mvn test por sí solo no ejecuta los tests de integración. En Validation el perfil existe por uniformidad aunque no hay clases *IntegrationTest.

### `validation-service/src/main/java/co/edu/unicauca/saberpro/validation/api/JsonConfiguration.java`

1. **Ruta:** [validation-service/src/main/java/co/edu/unicauca/saberpro/validation/api/JsonConfiguration.java](<../../validation-service/src/main/java/co/edu/unicauca/saberpro/validation/api/JsonConfiguration.java>).
2. **Nombre:** `JsonConfiguration.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Evitar que números o booleanos se acepten como textos.
5. **Contenido y funcionamiento:** Personaliza el ObjectMapper de Spring para que las coerciones Integer/Float/Boolean → Textual fallen.
6. **Métodos, anotaciones o configuraciones importantes:** @Configuration, @Bean; strictTextTypes() devuelve Jackson2ObjectMapperBuilderCustomizer; postConfigurer y CoercionAction.Fail.
7. **Relaciones:** ValidationRestController.java; ValidationExceptionHandler.java; application.properties
8. **Requisito:** U: contratos JSON correctos; C02/E08
9. **Evaluación:** La prueba con enunciado numérico devuelve 400.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** Jackson podría convertir 23 en "23" y aceptar una estructura no prevista.

### `validation-service/src/main/resources/application.properties`

1. **Ruta:** [validation-service/src/main/resources/application.properties](<../../validation-service/src/main/resources/application.properties>).
2. **Nombre:** `application.properties`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Configurar la API de validación.
5. **Contenido y funcionamiento:** Arranca en 8082, expone health/info y deshabilita coerción escalar permisiva.
6. **Métodos, anotaciones o configuraciones importantes:** spring.application.name; server.port=8082; allow-coercion-of-scalars=false; management.endpoints.
7. **Relaciones:** ValidationServiceApplication, JsonConfiguration y healthcheck Compose
8. **Requisito:** C01/E08/F09
9. **Evaluación:** Correcto en los escenarios inspeccionados y ejecutados.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

## Pruebas automatizadas

### `lifecycle-service/src/test/java/co/edu/unicauca/saberpro/lifecycle/LifecycleIntegrationTest.java`

1. **Ruta:** [lifecycle-service/src/test/java/co/edu/unicauca/saberpro/lifecycle/LifecycleIntegrationTest.java](<../../lifecycle-service/src/test/java/co/edu/unicauca/saberpro/lifecycle/LifecycleIntegrationTest.java>).
2. **Nombre:** `LifecycleIntegrationTest.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Probar recepción real, comprobante, deduplicación y DLQ.
5. **Contenido y funcionamiento:** Arranca Lifecycle de prueba con cola aislada y H2 propio, pero usa RabbitMQ real. Publica JSON mediante RabbitTemplate.
6. **Métodos, anotaciones o configuraciones importantes:** consumeJsonRealYExponeComprobanteUnaSolaVez(): publica, espera registro, reentrega y usa evento barrera para verificar timestamp estable; consulta HTTP/health. contratoInvalidoTerminaEnDlqSinBucle(): publica versión999, recibe en DLQ y verifica x-death. @SpringBootTest/Awaitility.
7. **Relaciones:** QuestionReviewListener; ReviewProcessingService; RabbitTopology; RabbitTemplate; ReviewRestController
8. **Requisito:** D03–D09/H07–H08
9. **Evaluación:** Dos tests pasan; logs de rechazo forman parte del escenario esperado.
10. **Errores, riesgos o mejora:** La cola fija de pruebas permanece declarada tras ejecutar la suite. No es una cola de producción ni prueba de rendimiento.

**Si no existiera:** No habría una prueba automática que distinguiera un consumidor real de un simple log del productor.

### `lifecycle-service/src/test/java/co/edu/unicauca/saberpro/lifecycle/ReviewProcessingTest.java`

1. **Ruta:** [lifecycle-service/src/test/java/co/edu/unicauca/saberpro/lifecycle/ReviewProcessingTest.java](<../../lifecycle-service/src/test/java/co/edu/unicauca/saberpro/lifecycle/ReviewProcessingTest.java>).
2. **Nombre:** `ReviewProcessingTest.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Verificar persistencia y deduplicación con H2 real.
5. **Contenido y funcionamiento:** DataJpaTest configura una base de prueba e importa el servicio. Inserta un evento dos veces y verifica una fila; rechaza versión 2 sin guardar.
6. **Métodos, anotaciones o configuraciones importantes:** @DataJpaTest/@Import(ReviewProcessingService.class); registraYDeduplicaEnH2Real(); rechazaContratoInvalidoSinPersistir().
7. **Relaciones:** ReviewProcessingService; ReviewReceiptRepository; QuestionReviewMessage
8. **Requisito:** H07/H08; persistencia del consumidor
9. **Evaluación:** Dos tests pasan. Es una prueba de componente JPA, no un mock de repositorio.
10. **Errores, riesgos o mejora:** No verifica el ACK AMQP ni persistencia en disco tras reiniciar; son otras evidencias.

**Si no existiera:** Podría duplicarse el registro sin que las pruebas unitarias lo advirtieran.

### `question-service/src/test/java/co/edu/unicauca/saberpro/question/application/IntercambiabilidadTest.java`

1. **Ruta:** [question-service/src/test/java/co/edu/unicauca/saberpro/question/application/IntercambiabilidadTest.java](<../../question-service/src/test/java/co/edu/unicauca/saberpro/question/application/IntercambiabilidadTest.java>).
2. **Nombre:** `IntercambiabilidadTest.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/test/java/co/edu/unicauca/saberpro/application/IntercambiabilidadTest.java` en e857f96.
4. **Para qué sirve:** Verificar el caso de uso a través de puertos y el repositorio memory.
5. **Contenido y funcionamiento:** Usa HashMap real, un contador de notificaciones y una validación unitaria sin efecto; no requiere contexto Spring.
6. **Métodos, anotaciones o configuraciones importantes:** Tres @Test de guardar/buscar/revisar, no notificar si faltan opciones y copias defensivas. RegistroNotificaciones implementa el puerto e incrementa cantidad.
7. **Relaciones:** QuestionApplicationService; InMemoryQuestionAdapter; NotificacionRevisionPort
8. **Requisito:** G p.3; HALL-02/HALL-04
9. **Evaluación:** Tres tests pasan; se conservaron los del Taller 12 con el nuevo constructor.
10. **Errores, riesgos o mejora:** Por sí solo no prueba JPA ni RabbitMQ real. La intercambiabilidad completa se demuestra en las dos clases API de integración.

**Si no existiera:** Se perdería una comprobación directa de desacoplamiento sin framework.

### `question-service/src/test/java/co/edu/unicauca/saberpro/question/application/QuestionApplicationServiceTest.java`

1. **Ruta:** [question-service/src/test/java/co/edu/unicauca/saberpro/question/application/QuestionApplicationServiceTest.java](<../../question-service/src/test/java/co/edu/unicauca/saberpro/question/application/QuestionApplicationServiceTest.java>).
2. **Nombre:** `QuestionApplicationServiceTest.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Comprobar fallos de coordinación y protección del almacenamiento en memoria.
5. **Contenido y funcionamiento:** Inyecta puertos unitarios que lanzan excepciones y un HashMap real; consulta el estado después del fallo.
6. **Métodos, anotaciones o configuraciones importantes:** falloDePublicacionRevierteHashMap(); falloDeValidacionNoPersiste(); listadoNoExponeReferenciasMutables(); options() →4 VO válidos.
7. **Relaciones:** QuestionApplicationService; InMemoryQuestionAdapter; DependenciaNoDisponibleException
8. **Requisito:** HALL-02/HALL-04/D08
9. **Evaluación:** Tres tests pasan.
10. **Errores, riesgos o mejora:** No acredita broker ni HTTP real; esa cobertura está en Failsafe/smoke.

**Si no existiera:** Una regresión podría guardar estados tras fallos o exponer referencias mutables.

### `question-service/src/test/java/co/edu/unicauca/saberpro/question/domain/PreguntaTest.java`

1. **Ruta:** [question-service/src/test/java/co/edu/unicauca/saberpro/question/domain/PreguntaTest.java](<../../question-service/src/test/java/co/edu/unicauca/saberpro/question/domain/PreguntaTest.java>).
2. **Nombre:** `PreguntaTest.java`.
3. **Estado:** Trasladado/reutilizado (solo paquete/nombre). Origen: `src/test/java/co/edu/unicauca/saberpro/domain/PreguntaTest.java` en e857f96.
4. **Para qué sirve:** Comprobar las invariantes del agregado sin Spring.
5. **Contenido y funcionamiento:** Construye VO/agregados directamente y verifica estados, excepciones y copias defensivas.
6. **Métodos, anotaciones o configuraciones importantes:** 14 @Test: BORRADOR, transición, null/blancos, justificación 14/15, opción vacía, 3/5 opciones, 0/2 correctas, reenvío, lista inmutable, opción null y rehidratación inválida. Helpers opcionesValidas() →List y crear(List) →Pregunta.
7. **Relaciones:** Pregunta y todos los VO; ReglaNegocioException
8. **Requisito:** A01–A14/HALL-01/HALL-06
9. **Evaluación:** 14 tests pasan tanto en host como en Java 17.
10. **Errores, riesgos o mejora:** HALL-01 no está cubierto: falta control que no sea isBlank antes de trim. Pasar estos tests no demuestra la invariante para toda cadena.

**Si no existiera:** Se perdería la protección de las reglas más importantes al refactorizar.

### `question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/AdditionalApiContract.java`

1. **Ruta:** [question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/AdditionalApiContract.java](<../../question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/AdditionalApiContract.java>).
2. **Nombre:** `AdditionalApiContract.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Reutilizar las mismas siete comprobaciones HTTP/AMQP en JPA y memory.
5. **Contenido y funcionamiento:** Clase abstracta con dependencias reales autoinyectadas. Sus @Test se heredan por cada clase concreta; no se ejecuta como una suite independiente.
6. **Métodos, anotaciones o configuraciones importantes:** body() →Map válido; create() →String id tras 201/Location. Tests: listado/contrato, estructuras/UUID, REST excesivo, múltiples claves, consumo Lifecycle, rollback con ruta inexistente y concurrencia 202/400. Awaitility espera comprobante; ExecutorService sincroniza dos peticiones.
7. **Relaciones:** ApiJpaIntegrationTest; ApiMemoryIntegrationTest; TestRestTemplate; RabbitTemplate real; repositorio/mapper
8. **Requisito:** HALL-03–H08; D08–D09/E01–E08/G07
9. **Evaluación:** Siete pruebas ejecutadas dos veces, sin mocks de servicios externos.
10. **Errores, riesgos o mejora:** HALL-01 demuestra que compartir casos no cubre toda entrada posible. La ruta inexistente se prueba en el repositorio; el HTTP 503 se cubre con smoke --faults.

**Si no existiera:** Se duplicaría el contrato de integración o quedaría desigual cobertura por adaptador.

### `question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/ApiEmptyIntegrationTest.java`

1. **Ruta:** [question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/ApiEmptyIntegrationTest.java](<../../question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/ApiEmptyIntegrationTest.java>).
2. **Nombre:** `ApiEmptyIntegrationTest.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Acreditar GET 200 con colección vacía.
5. **Contenido y funcionamiento:** Arranca un contexto memory distinto y no inserta preguntas antes de consultar.
6. **Métodos, anotaciones o configuraciones importantes:** @SpringBootTest(RANDOM_PORT), app.repository=memory, H2 empty-it; devuelve 200YListaVacia() compara estado y List.of().
7. **Relaciones:** QuestionRestController.listar; InMemoryQuestionAdapter
8. **Requisito:** G p.5 b; E01/HALL-05
9. **Evaluación:** Una prueba pasa; valida explícitamente [] en vez de inferirlo por la existencia de un GET.
10. **Errores, riesgos o mejora:** Sin defecto específico demostrado en este archivo.

**Si no existiera:** El caso sin registros no tendría una aserción HTTP dedicada.

### `question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/ApiJpaIntegrationTest.java`

1. **Ruta:** [question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/ApiJpaIntegrationTest.java](<../../question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/ApiJpaIntegrationTest.java>).
2. **Nombre:** `ApiJpaIntegrationTest.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/test/java/co/edu/unicauca/saberpro/infrastructure/ApiJpaIntegrationTest.java` en e857f96.
4. **Para qué sirve:** Probar API y repositorio Jpa con dependencias externas reales.
5. **Contenido y funcionamiento:** SpringBootTest inicia HTTP aleatorio, selecciona app.repository y una base H2 de prueba propia. Mantiene los tres escenarios originales y hereda siete adicionales.
6. **Métodos, anotaciones o configuraciones importantes:** @SpringBootTest(RANDOM_PORT); crear(String JSON) →ResponseEntity<Map>. Tres @Test propios: crear/consultar/revisar y reenvío; tres opciones conservan BORRADOR; justificación corta y 404. assertInstanceOf(JpaQuestionAdapter); revisión espera 202.
7. **Relaciones:** AdditionalApiContract; QuestionRestController; JpaQuestionAdapter; Validation/RabbitMQ/Lifecycle reales
8. **Requisito:** HALL-03/HALL-04/HALL-05/H08; G p.3,5
9. **Evaluación:** 10 tests pasan en esta clase bajo Failsafe/Java 17.
10. **Errores, riesgos o mejora:** Se ejecuta con -Pintegration verify; mvn test no la incluye. HALL-01 exige añadir el mismo caso límite en ambas variantes.

**Si no existiera:** No habría comprobación completa del contrato REST con Jpa.

### `question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/ApiMemoryIntegrationTest.java`

1. **Ruta:** [question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/ApiMemoryIntegrationTest.java](<../../question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/ApiMemoryIntegrationTest.java>).
2. **Nombre:** `ApiMemoryIntegrationTest.java`.
3. **Estado:** Trasladado y modificado. Origen: `src/test/java/co/edu/unicauca/saberpro/infrastructure/ApiMemoryIntegrationTest.java` en e857f96.
4. **Para qué sirve:** Probar API y repositorio Memory con dependencias externas reales.
5. **Contenido y funcionamiento:** SpringBootTest inicia HTTP aleatorio, selecciona app.repository y una base H2 de prueba propia. Mantiene los tres escenarios originales y hereda siete adicionales.
6. **Métodos, anotaciones o configuraciones importantes:** @SpringBootTest(RANDOM_PORT); crear(String JSON) →ResponseEntity<Map>. Tres @Test propios: crear/consultar/revisar y reenvío; tres opciones conservan BORRADOR; justificación corta y 404. assertInstanceOf(InMemoryQuestionAdapter); revisión espera 202.
7. **Relaciones:** AdditionalApiContract; QuestionRestController; InMemoryQuestionAdapter; Validation/RabbitMQ/Lifecycle reales
8. **Requisito:** HALL-03/HALL-04/HALL-05/H08; G p.3,5
9. **Evaluación:** 10 tests pasan en esta clase bajo Failsafe/Java 17.
10. **Errores, riesgos o mejora:** Se ejecuta con -Pintegration verify; mvn test no la incluye. HALL-01 exige añadir el mismo caso límite en ambas variantes.

**Si no existiera:** No habría comprobación completa del contrato REST con Memory.

### `question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/RabbitMQPublisherTest.java`

1. **Ruta:** [question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/RabbitMQPublisherTest.java](<../../question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/RabbitMQPublisherTest.java>).
2. **Nombre:** `RabbitMQPublisherTest.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Verificar decisiones del publicador ante confirmaciones y errores.
5. **Contenido y funcionamiento:** Mockito simula únicamente RabbitTemplate; se inspecciona el mensaje real construido por la clase y se controla el future.
6. **Métodos, anotaciones o configuraciones importantes:** Cuatro @Test: JSON persistente/eventId estable; NACK; ACK con return; timeout. reply(Consumer<CorrelationData>) configura el doble; question() construye agregado pendiente. ArgumentCaptor inspecciona Message.
7. **Relaciones:** RabbitMQPublisher; QuestionReviewEvent; ObjectMapper; Mockito
8. **Requisito:** H07/D02/D08
9. **Evaluación:** Cuatro tests pasan; se reconoce explícitamente su carácter unitario con mock.
10. **Errores, riesgos o mejora:** No basta como prueba de publicación real; se complementa con integración y logs del consumidor.

**Si no existiera:** No habría detección rápida de falsos éxitos al recibir NACK/return/timeout.

### `scripts/check-domain.sh`

1. **Ruta:** [scripts/check-domain.sh](<../../scripts/check-domain.sh>).
2. **Nombre:** `check-domain.sh`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Comprobar que dominio y aplicación no requieren frameworks.
5. **Contenido y funcionamiento:** Crea una carpeta temporal, lista fuentes de ambas capas, compila con javac --release 17 sin classpath externo y elimina solo su temporal mediante trap.
6. **Métodos, anotaciones o configuraciones importantes:** set -eu; mktemp; find; javac; trap.
7. **Relaciones:** domain/**, application/**; docs de pruebas
8. **Requisito:** A02–A03/B11; G p.3
9. **Evaluación:** Ejecutado correctamente durante esta auditoría.
10. **Errores, riesgos o mejora:** Demuestra independencia de compilación, no corrección de todas las reglas: HALL-01 sigue existiendo.

### `scripts/smoke_test.py`

1. **Ruta:** [scripts/smoke_test.py](<../../scripts/smoke_test.py>).
2. **Nombre:** `smoke_test.py`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Verificar por HTTP y AMQP real el despliegue.
5. **Contenido y funcionamiento:** Lee ejemplo-pregunta, prueba salud, CRUD permitido, revisión, errores, Management y recibos. --faults detiene y recupera RabbitMQ/Validation con finally.
6. **Métodos, anotaciones o configuraciones importantes:** request(base,method,path,body,auth) → status/body; check valida status y guarda evidencia; wait_for espera 200; receive valida IDs; --label/--output/--faults; variables *_URL.
7. **Relaciones:** Los tres servicios, RabbitMQ Management, docker compose, ejemplo-pregunta.json
8. **Requisito:** C03/D02–D09/E01–E08/H11; G p.5
9. **Evaluación:** 28 comprobaciones HTTP y aserciones adicionales aprobadas por modo en esta auditoría.
10. **Errores, riesgos o mejora:** --label solo etiqueta; no selecciona adaptador. --output sobrescribe el destino indicado: usar ruta nueva. No cubría HALL-01; su PASS no equivale a ausencia de defectos.

### `scripts/verify.sh`

1. **Ruta:** [scripts/verify.sh](<../../scripts/verify.sh>).
2. **Nombre:** `verify.sh`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Encadenar la verificación habitual desde la raíz.
5. **Contenido y funcionamiento:** Valida Compose, construye/espera contenedores, ejecuta el perfil integration y lanza smoke con argumentos recibidos.
6. **Métodos, anotaciones o configuraciones importantes:** set -eu; docker compose config --quiet; up --build --wait; ./mvnw -Pintegration verify; Python.
7. **Relaciones:** Compose, Maven Wrapper, POMs y smoke_test.py
8. **Requisito:** F12–F14/H09–H11
9. **Evaluación:** Contenido coherente; los pasos equivalentes se ejecutaron separadamente para conservar sus salidas.
10. **Errores, riesgos o mejora:** No se atribuye al script completo una ejecución que se realizó por comandos individuales. --faults afecta dependencias de este proyecto durante la prueba.

### `validation-service/src/test/java/co/edu/unicauca/saberpro/validation/ValidationApiTest.java`

1. **Ruta:** [validation-service/src/test/java/co/edu/unicauca/saberpro/validation/ValidationApiTest.java](<../../validation-service/src/test/java/co/edu/unicauca/saberpro/validation/ValidationApiTest.java>).
2. **Nombre:** `ValidationApiTest.java`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Probar el controlador Validation mediante HTTP real sin dependencias externas.
5. **Contenido y funcionamiento:** Levanta contexto y puerto aleatorio; envía mapas y JSON malformado a la API.
6. **Métodos, anotaciones o configuraciones importantes:** Seis @Test: health; contenido válido; borrador incompleto sin duplicar dominio; límites 4001/2001; campos ausentes; JSON y tipo incorrecto. post(Object) →ResponseEntity<Map>.
7. **Relaciones:** ValidationRestController; ValidationExceptionHandler; JsonConfiguration
8. **Requisito:** C01–C03/HALL-05/HALL-06/H09
9. **Evaluación:** Seis tests pasan. Aunque su nombre termina Test, es integración HTTP local de componente.
10. **Errores, riesgos o mejora:** No prueba la llamada desde Question; la suite API de Question cubre esa interacción.

**Si no existiera:** No se comprobaría que las anotaciones y coerciones producen respuestas reales.

## Documentación

### `INFORME_AUDITORIA.md`

1. **Ruta:** [INFORME_AUDITORIA.md](<../../INFORME_AUDITORIA.md>).
2. **Nombre:** `INFORME_AUDITORIA.md`.
3. **Estado:** Modificado. Origen: `INFORME_AUDITORIA.md` en e857f96.
4. **Para qué sirve:** Orientar hacia los informes producidos durante la implementación.
5. **Contenido y funcionamiento:** Resume el despliegue, las pruebas previas y pendientes administrativos; enlaza el histórico.
6. **Métodos, anotaciones o configuraciones importantes:** Enlaces Markdown y resumen de 55 pruebas.
7. **Relaciones:** docs/INFORME_IMPLEMENTACION.md, arquitectura/trazabilidad.md y docs/taller12.
8. **Requisito:** I11/E p.3
9. **Evaluación:** El resumen de pruebas fue corroborado.
10. **Errores, riesgos o mejora:** Su estado previo no recoge el defecto funcional HALL-01; la auditoría final vigente está en docs/auditoria/.

### `README.md`

1. **Ruta:** [README.md](<../../README.md>).
2. **Nombre:** `README.md`.
3. **Estado:** Modificado. Origen: `README.md` en e857f96.
4. **Para qué sirve:** Punto de entrada del Taller 13.
5. **Contenido y funcionamiento:** Explica requisitos, arquitectura, Docker, ejecución local, API, repositorios, pruebas, fallos y pendientes de entrega.
6. **Métodos, anotaciones o configuraciones importantes:** Diagrama Mermaid; comandos con 8082/8083/8084; 201/202; variables; enlaces a arquitectura, pruebas y evidencia.
7. **Relaciones:** Todos los servicios, scripts y docs.
8. **Requisito:** I01; G p.2–5/E p.3
9. **Evaluación:** Actualizado y reproducible para los flujos normales.
10. **Errores, riesgos o mejora:** Debe incorporar HALL-01 en una revisión posterior; no tomar el éxito de 55 pruebas como garantía completa.

### `docs/INFORME_IMPLEMENTACION.md`

1. **Ruta:** [docs/INFORME_IMPLEMENTACION.md](<../../docs/INFORME_IMPLEMENTACION.md>).
2. **Nombre:** `INFORME_IMPLEMENTACION.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Registrar la implementación anterior y su verificación.
5. **Contenido y funcionamiento:** Describe migración, estructura, endpoints, pruebas, fallos, resultados y límites.
6. **Métodos, anotaciones o configuraciones importantes:** Tablas y comandos; comparación de pruebas antiguas y actuales.
7. **Relaciones:** INVENTARIO_CAMBIOS, evidencias y arquitectura.
8. **Requisito:** I01/I11; E p.3
9. **Evaluación:** Resultados ordinarios corroborados nuevamente.
10. **Errores, riesgos o mejora:** La afirmación amplia de intercambiabilidad debe matizarse por HALL-01; se preservó como documento previo.

### `docs/INFORME_TECNICO.md`

1. **Ruta:** [docs/INFORME_TECNICO.md](<../../docs/INFORME_TECNICO.md>).
2. **Nombre:** `INFORME_TECNICO.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Preparar contenido para el informe académico institucional.
5. **Contenido y funcionamiento:** Incluye problema, evolución, dominio, REST/AMQP, Docker, pruebas, trazabilidad y entrega.
6. **Métodos, anotaciones o configuraciones importantes:** Portada sin nombres inventados; ocho secciones; tablas y referencias.
7. **Relaciones:** Guías oficiales, C4, evidencias y README.
8. **Requisito:** I11–I12; E p.3
9. **Evaluación:** Contenido técnico sustancial disponible.
10. **Errores, riesgos o mejora:** No es el documento Word en Formato-Documento.docx; falta portada real, capturas y actualizar invariantes con HALL-01 (HALL-02).

### `docs/INVENTARIO_CAMBIOS.md`

1. **Ruta:** [docs/INVENTARIO_CAMBIOS.md](<../../docs/INVENTARIO_CAMBIOS.md>).
2. **Nombre:** `INVENTARIO_CAMBIOS.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Enumerar archivos de la migración.
5. **Contenido y funcionamiento:** Lista rutas añadidas y transformaciones desde el monolito, con histórico preservado.
6. **Métodos, anotaciones o configuraciones importantes:** Listado Markdown de archivos.
7. **Relaciones:** Todos los módulos y docs/taller12.
8. **Requisito:** Trazabilidad del cambio T12→T13
9. **Evaluación:** Útil como índice; contrastado con Git.
10. **Errores, riesgos o mejora:** No sustituye la explicación individual ni los estados precisos del inventario de esta auditoría.

### `docs/arquitectura/arquitectura.md`

1. **Ruta:** [docs/arquitectura/arquitectura.md](<../../docs/arquitectura/arquitectura.md>).
2. **Nombre:** `arquitectura.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Describir responsabilidades y contratos implementados.
5. **Contenido y funcionamiento:** Detalla capas, repositorios, validación externa, JSON de evento, errores y despliegue.
6. **Métodos, anotaciones o configuraciones importantes:** Contrato v1, confirms, deduplicación, reintentos, DLX/DLQ, límites de consistencia.
7. **Relaciones:** Clases de dominio/aplicación/infraestructura de los tres módulos.
8. **Requisito:** I07–I09; B/C/D
9. **Evaluación:** Coincide con los flujos observados.
10. **Errores, riesgos o mejora:** La descripción de textos normalizados debe advertir HALL-01 hasta corregirlo; no garantiza consistencia distribuida.

### `docs/arquitectura/decisiones.md`

1. **Ruta:** [docs/arquitectura/decisiones.md](<../../docs/arquitectura/decisiones.md>).
2. **Nombre:** `decisiones.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Explicar alternativas y diferencias respecto de las recetas.
5. **Contenido y funcionamiento:** Justifica nombres, H2, imagen Java, multi-stage, puertos, tests externos y atomicidad limitada.
6. **Métodos, anotaciones o configuraciones importantes:** Tabla de decisiones y secuencia guardar/publicar/confirmar; enlaces oficiales técnicos.
7. **Relaciones:** Dockerfile, puertos, publicador y consumidor.
8. **Requisito:** Comparación G/E; I09
9. **Evaluación:** Decisiones razonables y límites expuestos con honestidad.
10. **Errores, riesgos o mejora:** La ventana ACK/commit (L01) sigue siendo una limitación aceptada; no confundir deduplicación con exactamente una vez.

### `docs/arquitectura/diagramas.md`

1. **Ruta:** [docs/arquitectura/diagramas.md](<../../docs/arquitectura/diagramas.md>).
2. **Nombre:** `diagramas.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Mantener diagramas actuales editables.
5. **Contenido y funcionamiento:** Cuatro C4 y dos secuencias Mermaid reflejan estructura y flujos reales.
6. **Métodos, anotaciones o configuraciones importantes:** flowchart, classDiagram, sequenceDiagram.
7. **Relaciones:** Compose, Question y servicios externos.
8. **Requisito:** I03–I08; E p.3
9. **Evaluación:** Seis bloques parseados correctamente; contenido contrastado con código.
10. **Errores, riesgos o mejora:** Falta exportar/insertar en Word y revisar legibilidad final; son diagramas, no capturas.

### `docs/arquitectura/trazabilidad.md`

1. **Ruta:** [docs/arquitectura/trazabilidad.md](<../../docs/arquitectura/trazabilidad.md>).
2. **Nombre:** `trazabilidad.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Relacionar requisitos, implementación, prueba y cortes del semestre.
5. **Contenido y funcionamiento:** Tablas con G/E/U y páginas; distingue código disponible de antecedentes no aportados.
6. **Métodos, anotaciones o configuraciones importantes:** Cortes I 25%, II 35%, III 40%; rúbrica final 35/30/20/15.
7. **Relaciones:** PDF, clases y pruebas, informe técnico.
8. **Requisito:** I10; E p.1,3
9. **Evaluación:** Honesta al reconocer límites históricos.
10. **Errores, riesgos o mejora:** La trazabilidad I/II es conceptual sin repositorios históricos; su antigua valoración de invariantes debe actualizarse por HALL-01 (HALL-03).

### `docs/diagramas.md`

1. **Ruta:** [docs/diagramas.md](<../../docs/diagramas.md>).
2. **Nombre:** `diagramas.md`.
3. **Estado:** Modificado. Origen: `docs/diagramas.md` en e857f96.
4. **Para qué sirve:** Redirigir al lector hacia diagramas actuales y anteriores.
5. **Contenido y funcionamiento:** Índice corto con enlaces separados.
6. **Métodos, anotaciones o configuraciones importantes:** Markdown, enlaces relativos.
7. **Relaciones:** arquitectura/diagramas.md y taller12/diagramas.md.
8. **Requisito:** I03–I06
9. **Evaluación:** Destinos existentes y separación temporal clara.
10. **Errores, riesgos o mejora:** Sin defecto específico observado.

### `docs/evidencias/README.md`

1. **Ruta:** [docs/evidencias/README.md](<../../docs/evidencias/README.md>).
2. **Nombre:** `README.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Catalogar evidencia de la ejecución anterior.
5. **Contenido y funcionamiento:** Relaciona archivo, alcance, IDs concretos y capturas todavía pendientes.
6. **Métodos, anotaciones o configuraciones importantes:** Tabla de resultados, fecha y advertencias sobre fallos deliberados.
7. **Relaciones:** Logs/JSON de docs/evidencias y capturas históricas.
8. **Requisito:** I13–I14/H11
9. **Evaluación:** Distingue correctamente evidencia textual de capturas gráficas.
10. **Errores, riesgos o mejora:** No debe reemplazarse con resultados nuevos: esta auditoría conserva salidas separadas.

### `docs/guias/lisw2_guia_paso_a_paso_taller13_estudiantes.pdf`

1. **Ruta:** [docs/guias/lisw2_guia_paso_a_paso_taller13_estudiantes.pdf](<../../docs/guias/lisw2_guia_paso_a_paso_taller13_estudiantes.pdf>).
2. **Nombre:** `lisw2_guia_paso_a_paso_taller13_estudiantes.pdf`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar la fuente oficial de requisitos.
5. **Contenido y funcionamiento:** Copia íntegra del PDF aportado por el usuario; Guía operativa con estructura, dominio, Docker, Compose y pruebas HTTP/AMQP.
6. **Métodos, anotaciones o configuraciones importantes:** 5 páginas; contenido leído y extraído a texto en evidencias de esta auditoría.
7. **Relaciones:** MATRIZ_CUMPLIMIENTO_TALLER13 y trazabilidad previa
8. **Requisito:** Fuente G
9. **Evaluación:** Accesible, leído y comparado por página; SHA-256 conservado.
10. **Errores, riesgos o mejora:** El PDF define requisitos académicos; sus ejemplos no autorizan por sí mismos cambios o publicación durante esta auditoría.

### `docs/guias/lisw2_taller13_entrega_final_DDDHexagonalDocker.pdf`

1. **Ruta:** [docs/guias/lisw2_taller13_entrega_final_DDDHexagonalDocker.pdf](<../../docs/guias/lisw2_taller13_entrega_final_DDDHexagonalDocker.pdf>).
2. **Nombre:** `lisw2_taller13_entrega_final_DDDHexagonalDocker.pdf`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar la fuente oficial de requisitos.
5. **Contenido y funcionamiento:** Copia íntegra del PDF aportado por el usuario; Guía de entrega con objetivos, paquetes, evolución por cortes, entregables y rúbrica.
6. **Métodos, anotaciones o configuraciones importantes:** 3 páginas; contenido leído y extraído a texto en evidencias de esta auditoría.
7. **Relaciones:** MATRIZ_CUMPLIMIENTO_TALLER13 y trazabilidad previa
8. **Requisito:** Fuente E
9. **Evaluación:** Accesible, leído y comparado por página; SHA-256 conservado.
10. **Errores, riesgos o mejora:** El PDF define requisitos académicos; sus ejemplos no autorizan por sí mismos cambios o publicación durante esta auditoría.

### `docs/pruebas/README.md`

1. **Ruta:** [docs/pruebas/README.md](<../../docs/pruebas/README.md>).
2. **Nombre:** `README.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Permitir reproducir suites y peticiones manuales.
5. **Contenido y funcionamiento:** Explica Surefire/Failsafe, fallos controlados, cambio de repositorio, importación de colección y resultados esperados.
6. **Métodos, anotaciones o configuraciones importantes:** Comandos, variables *_URL y secuencia manual de copiar preguntaId.
7. **Relaciones:** scripts, colección Postman v2.1, peticiones.http, evidencias.
8. **Requisito:** I15/E07/H
9. **Evaluación:** Peticiones equivalentes verificadas por scripts y JUnit.
10. **Errores, riesgos o mejora:** No se abrió Thunder Client; importación GUI no verificada. Añadir caso HALL-01 en la corrección futura.

### `docs/pruebas/SaberPRO_Taller13.postman_collection.json`

1. **Ruta:** [docs/pruebas/SaberPRO_Taller13.postman_collection.json](<../../docs/pruebas/SaberPRO_Taller13.postman_collection.json>).
2. **Nombre:** `SaberPRO_Taller13.postman_collection.json`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Preparar solicitudes actuales para el cliente HTTP.
5. **Contenido y funcionamiento:** Colección Postman v2.1 con 11 peticiones y variables de URLs e ID; el usuario copia el ID creado.
6. **Métodos, anotaciones o configuraciones importantes:** item/request; cuerpos raw JSON; baseUrl 8083, validationUrl 8082 y lifecycleUrl 8084; no scripts de tests.
7. **Relaciones:** docs/pruebas/README.md y los tres controladores.
8. **Requisito:** G p.5; I15/E07
9. **Evaluación:** JSON legible y solicitudes compatibles con el contrato comprobado.
10. **Errores, riesgos o mejora:** La importación en la interfaz de Thunder Client no se ejecutó en la auditoría; usar el protocolo documentado.

### `docs/pruebas/peticiones.http`

1. **Ruta:** [docs/pruebas/peticiones.http](<../../docs/pruebas/peticiones.http>).
2. **Nombre:** `peticiones.http`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Ofrecer peticiones copiables y separadas por escenario.
5. **Contenido y funcionamiento:** Define URLs, cuerpos y operaciones para creación, consulta, revisión y errores.
6. **Métodos, anotaciones o configuraciones importantes:** Variables baseUrl/preguntaId, separadores ### y JSON.
7. **Relaciones:** Colección JSON, controllers y README de pruebas.
8. **Requisito:** G p.5; E07/I15
9. **Evaluación:** Peticiones equivalentes cubiertas por pruebas reales.
10. **Errores, riesgos o mejora:** El archivo es ayuda textual; no constituye por sí mismo resultados ejecutados.

### `docs/taller12/INFORME_AUDITORIA.md`

1. **Ruta:** [docs/taller12/INFORME_AUDITORIA.md](<../../docs/taller12/INFORME_AUDITORIA.md>).
2. **Nombre:** `INFORME_AUDITORIA.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar la revisión anterior de la base.
5. **Contenido y funcionamiento:** Documenta el sistema monolítico, sus pruebas y observaciones previas.
6. **Métodos, anotaciones o configuraciones importantes:** Texto Markdown histórico; no ejecuta comprobaciones.
7. **Relaciones:** Código de e857f96 y documentación T12.
8. **Requisito:** Trazabilidad T12→T13
9. **Evaluación:** Preservado como antecedente.
10. **Errores, riesgos o mejora:** No usar sus conclusiones como revisión del sistema distribuido actual.

### `docs/taller12/README.md`

1. **Ruta:** [docs/taller12/README.md](<../../docs/taller12/README.md>).
2. **Nombre:** `README.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Preservar la documentación original del monolito.
5. **Contenido y funcionamiento:** Explica puertos 8080, clases y pruebas antiguas.
6. **Métodos, anotaciones o configuraciones importantes:** Markdown, comandos históricos y enlaces copiados con sus rutas originales.
7. **Relaciones:** Imágenes antiguas, colección T12 y diagramas archivados.
8. **Requisito:** Comparación de origen; I10
9. **Evaluación:** Contenido histórico útil, no guía operativa actual.
10. **Errores, riesgos o mejora:** 15 referencias locales rotas por el traslado, incluidas tres imágenes (HALL-05).

### `docs/taller12/diagramas.md`

1. **Ruta:** [docs/taller12/diagramas.md](<../../docs/taller12/diagramas.md>).
2. **Nombre:** `diagramas.md`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Preservar los tres diagramas fuente antiguos.
5. **Contenido y funcionamiento:** Muestra hexágono monolítico, secuencia de revisión y agregado.
6. **Métodos, anotaciones o configuraciones importantes:** Tres bloques Mermaid válidos; nombres y HTTP 200 antiguos.
7. **Relaciones:** PNG históricos y README de T12.
8. **Requisito:** Comparación de evolución
9. **Evaluación:** Sintaxis válida; contenido correcto como histórico.
10. **Errores, riesgos o mejora:** No sustituye los cuatro niveles C4 actuales.

### `docs/taller12/pom-original.xml`

1. **Ruta:** [docs/taller12/pom-original.xml](<../../docs/taller12/pom-original.xml>).
2. **Nombre:** `pom-original.xml`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar la construcción original de Taller 12.
5. **Contenido y funcionamiento:** Define la aplicación Spring única previa y sus dependencias.
6. **Métodos, anotaciones o configuraciones importantes:** XML Maven original; artefacto monolítico.
7. **Relaciones:** Commit base y antiguos tests.
8. **Requisito:** Trazabilidad de estructura
9. **Evaluación:** Documento de referencia, no POM activo del reactor.
10. **Errores, riesgos o mejora:** Potencialmente confuso si se compila como proyecto vigente; está separado deliberadamente.

### `postman/SaberPRO_Taller12.postman_collection.json`

1. **Ruta:** [postman/SaberPRO_Taller12.postman_collection.json](<../../postman/SaberPRO_Taller12.postman_collection.json>).
2. **Nombre:** `SaberPRO_Taller12.postman_collection.json`.
3. **Estado:** Reutilizado sin cambios. Origen: `postman/SaberPRO_Taller12.postman_collection.json` en e857f96.
4. **Para qué sirve:** Conservar la colección original de pruebas.
5. **Contenido y funcionamiento:** Contiene las solicitudes del monolito en 8080 y el contrato anterior.
6. **Métodos, anotaciones o configuraciones importantes:** Colección JSON histórica.
7. **Relaciones:** README T12 y ejemplo-pregunta.json.
8. **Requisito:** Antecedente de API
9. **Evaluación:** Reutilizada sin cambios y conservada como histórico.
10. **Errores, riesgos o mejora:** No usar para certificar Taller 13; su puerto y expectativa de revisión son antiguos.

## Imágenes, diagramas y evidencias

### `docs/evidencias/conectividad-interna.txt`

1. **Ruta:** [docs/evidencias/conectividad-interna.txt](<../../docs/evidencias/conectividad-interna.txt>).
2. **Nombre:** `conectividad-interna.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar llamada http interna entre contenedores.
5. **Contenido y funcionamiento:** Respuesta health desde Question hacia validation-service:8082; comprueba DNS y red.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/docker-compose-build.log`

1. **Ruta:** [docs/evidencias/docker-compose-build.log](<../../docs/evidencias/docker-compose-build.log>).
2. **Nombre:** `docker-compose-build.log`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar construcción previa de las tres imágenes.
5. **Contenido y funcionamiento:** Salida del build multietapa y copia de JAR; no acredita ejecución de tests omitidos por -DskipTests.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/docker-compose-config.yml`

1. **Ruta:** [docs/evidencias/docker-compose-config.yml](<../../docs/evidencias/docker-compose-config.yml>).
2. **Nombre:** `docker-compose-config.yml`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar configuración resuelta anterior.
5. **Contenido y funcionamiento:** Valores finales de servicios, puertos, red, volúmenes y variables de Compose.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/docker-compose-ps.txt`

1. **Ruta:** [docs/evidencias/docker-compose-ps.txt](<../../docs/evidencias/docker-compose-ps.txt>).
2. **Nombre:** `docker-compose-ps.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar estado anterior de los cuatro servicios.
5. **Contenido y funcionamiento:** Tabla de contenedores saludables y puertos publicados.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/docker-compose-up.log`

1. **Ruta:** [docs/evidencias/docker-compose-up.log](<../../docs/evidencias/docker-compose-up.log>).
2. **Nombre:** `docker-compose-up.log`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar arranque previo de compose.
5. **Contenido y funcionamiento:** Salida real de construcción/creación/inicio de los servicios.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/docker-ps.txt`

1. **Ruta:** [docs/evidencias/docker-ps.txt](<../../docs/evidencias/docker-ps.txt>).
2. **Nombre:** `docker-ps.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar listado anterior de contenedores activos.
5. **Contenido y funcionamiento:** Incluye nombres e imágenes; instantánea de su momento.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/dominio-puro.txt`

1. **Ruta:** [docs/evidencias/dominio-puro.txt](<../../docs/evidencias/dominio-puro.txt>).
2. **Nombre:** `dominio-puro.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar compilación previa del núcleo sin frameworks.
5. **Contenido y funcionamiento:** Salida satisfactoria de scripts/check-domain.sh; no prueba todas las invariantes.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/guias-sha256.json`

1. **Ruta:** [docs/evidencias/guias-sha256.json](<../../docs/evidencias/guias-sha256.json>).
2. **Nombre:** `guias-sha256.json`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar identidad de las dos guías.
5. **Contenido y funcionamiento:** SHA-256 de los PDF originales y copias; permite detectar alteraciones.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/java-contenedor.txt`

1. **Ruta:** [docs/evidencias/java-contenedor.txt](<../../docs/evidencias/java-contenedor.txt>).
2. **Nombre:** `java-contenedor.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar jvm efectiva del contenedor anterior.
5. **Contenido y funcionamiento:** Salida java -version que acredita Java 17, no solamente la etiqueta de imagen.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/junit/lifecycle-service/co.edu.unicauca.saberpro.lifecycle.LifecycleIntegrationTest.txt`

1. **Ruta:** [docs/evidencias/junit/lifecycle-service/co.edu.unicauca.saberpro.lifecycle.LifecycleIntegrationTest.txt](<../../docs/evidencias/junit/lifecycle-service/co.edu.unicauca.saberpro.lifecycle.LifecycleIntegrationTest.txt>).
2. **Nombre:** `co.edu.unicauca.saberpro.lifecycle.LifecycleIntegrationTest.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar el reporte previo de LifecycleIntegrationTest.
5. **Contenido y funcionamiento:** Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 14.31 s -- in co.edu.unicauca.saberpro.lifecycle.LifecycleIntegrationTest
6. **Métodos, anotaciones o configuraciones importantes:** Reporte textual Surefire/Failsafe de LifecycleIntegrationTest, no código fuente.
7. **Relaciones:** LifecycleIntegrationTest.java y resumen-pruebas.json
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/junit/lifecycle-service/co.edu.unicauca.saberpro.lifecycle.ReviewProcessingTest.txt`

1. **Ruta:** [docs/evidencias/junit/lifecycle-service/co.edu.unicauca.saberpro.lifecycle.ReviewProcessingTest.txt](<../../docs/evidencias/junit/lifecycle-service/co.edu.unicauca.saberpro.lifecycle.ReviewProcessingTest.txt>).
2. **Nombre:** `co.edu.unicauca.saberpro.lifecycle.ReviewProcessingTest.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar el reporte previo de ReviewProcessingTest.
5. **Contenido y funcionamiento:** Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.801 s -- in co.edu.unicauca.saberpro.lifecycle.ReviewProcessingTest
6. **Métodos, anotaciones o configuraciones importantes:** Reporte textual Surefire/Failsafe de ReviewProcessingTest, no código fuente.
7. **Relaciones:** ReviewProcessingTest.java y resumen-pruebas.json
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.application.IntercambiabilidadTest.txt`

1. **Ruta:** [docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.application.IntercambiabilidadTest.txt](<../../docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.application.IntercambiabilidadTest.txt>).
2. **Nombre:** `co.edu.unicauca.saberpro.question.application.IntercambiabilidadTest.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar el reporte previo de IntercambiabilidadTest.
5. **Contenido y funcionamiento:** Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.019 s -- in co.edu.unicauca.saberpro.question.application.IntercambiabilidadTest
6. **Métodos, anotaciones o configuraciones importantes:** Reporte textual Surefire/Failsafe de IntercambiabilidadTest, no código fuente.
7. **Relaciones:** IntercambiabilidadTest.java y resumen-pruebas.json
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.application.QuestionApplicationServiceTest.txt`

1. **Ruta:** [docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.application.QuestionApplicationServiceTest.txt](<../../docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.application.QuestionApplicationServiceTest.txt>).
2. **Nombre:** `co.edu.unicauca.saberpro.question.application.QuestionApplicationServiceTest.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar el reporte previo de QuestionApplicationServiceTest.
5. **Contenido y funcionamiento:** Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.032 s -- in co.edu.unicauca.saberpro.question.application.QuestionApplicationServiceTest
6. **Métodos, anotaciones o configuraciones importantes:** Reporte textual Surefire/Failsafe de QuestionApplicationServiceTest, no código fuente.
7. **Relaciones:** QuestionApplicationServiceTest.java y resumen-pruebas.json
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.domain.PreguntaTest.txt`

1. **Ruta:** [docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.domain.PreguntaTest.txt](<../../docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.domain.PreguntaTest.txt>).
2. **Nombre:** `co.edu.unicauca.saberpro.question.domain.PreguntaTest.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar el reporte previo de PreguntaTest.
5. **Contenido y funcionamiento:** Tests run: 14, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.102 s -- in co.edu.unicauca.saberpro.question.domain.PreguntaTest
6. **Métodos, anotaciones o configuraciones importantes:** Reporte textual Surefire/Failsafe de PreguntaTest, no código fuente.
7. **Relaciones:** PreguntaTest.java y resumen-pruebas.json
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.ApiEmptyIntegrationTest.txt`

1. **Ruta:** [docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.ApiEmptyIntegrationTest.txt](<../../docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.ApiEmptyIntegrationTest.txt>).
2. **Nombre:** `co.edu.unicauca.saberpro.question.infrastructure.ApiEmptyIntegrationTest.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar el reporte previo de ApiEmptyIntegrationTest.
5. **Contenido y funcionamiento:** Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 12.23 s -- in co.edu.unicauca.saberpro.question.infrastructure.ApiEmptyIntegrationTest
6. **Métodos, anotaciones o configuraciones importantes:** Reporte textual Surefire/Failsafe de ApiEmptyIntegrationTest, no código fuente.
7. **Relaciones:** ApiEmptyIntegrationTest.java y resumen-pruebas.json
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.ApiJpaIntegrationTest.txt`

1. **Ruta:** [docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.ApiJpaIntegrationTest.txt](<../../docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.ApiJpaIntegrationTest.txt>).
2. **Nombre:** `co.edu.unicauca.saberpro.question.infrastructure.ApiJpaIntegrationTest.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar el reporte previo de ApiJpaIntegrationTest.
5. **Contenido y funcionamiento:** Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.594 s -- in co.edu.unicauca.saberpro.question.infrastructure.ApiJpaIntegrationTest
6. **Métodos, anotaciones o configuraciones importantes:** Reporte textual Surefire/Failsafe de ApiJpaIntegrationTest, no código fuente.
7. **Relaciones:** ApiJpaIntegrationTest.java y resumen-pruebas.json
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.ApiMemoryIntegrationTest.txt`

1. **Ruta:** [docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.ApiMemoryIntegrationTest.txt](<../../docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.ApiMemoryIntegrationTest.txt>).
2. **Nombre:** `co.edu.unicauca.saberpro.question.infrastructure.ApiMemoryIntegrationTest.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar el reporte previo de ApiMemoryIntegrationTest.
5. **Contenido y funcionamiento:** Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.743 s -- in co.edu.unicauca.saberpro.question.infrastructure.ApiMemoryIntegrationTest
6. **Métodos, anotaciones o configuraciones importantes:** Reporte textual Surefire/Failsafe de ApiMemoryIntegrationTest, no código fuente.
7. **Relaciones:** ApiMemoryIntegrationTest.java y resumen-pruebas.json
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.RabbitMQPublisherTest.txt`

1. **Ruta:** [docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.RabbitMQPublisherTest.txt](<../../docs/evidencias/junit/question-service/co.edu.unicauca.saberpro.question.infrastructure.RabbitMQPublisherTest.txt>).
2. **Nombre:** `co.edu.unicauca.saberpro.question.infrastructure.RabbitMQPublisherTest.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar el reporte previo de RabbitMQPublisherTest.
5. **Contenido y funcionamiento:** Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.436 s -- in co.edu.unicauca.saberpro.question.infrastructure.RabbitMQPublisherTest
6. **Métodos, anotaciones o configuraciones importantes:** Reporte textual Surefire/Failsafe de RabbitMQPublisherTest, no código fuente.
7. **Relaciones:** RabbitMQPublisherTest.java y resumen-pruebas.json
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/junit/validation-service/co.edu.unicauca.saberpro.validation.ValidationApiTest.txt`

1. **Ruta:** [docs/evidencias/junit/validation-service/co.edu.unicauca.saberpro.validation.ValidationApiTest.txt](<../../docs/evidencias/junit/validation-service/co.edu.unicauca.saberpro.validation.ValidationApiTest.txt>).
2. **Nombre:** `co.edu.unicauca.saberpro.validation.ValidationApiTest.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar el reporte previo de ValidationApiTest.
5. **Contenido y funcionamiento:** Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.833 s -- in co.edu.unicauca.saberpro.validation.ValidationApiTest
6. **Métodos, anotaciones o configuraciones importantes:** Reporte textual Surefire/Failsafe de ValidationApiTest, no código fuente.
7. **Relaciones:** ValidationApiTest.java y resumen-pruebas.json
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/logs-lifecycle-service.txt`

1. **Ruta:** [docs/evidencias/logs-lifecycle-service.txt](<../../docs/evidencias/logs-lifecycle-service.txt>).
2. **Nombre:** `logs-lifecycle-service.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar logs previos de lifecycle-service.
5. **Contenido y funcionamiento:** Incluye IDs de preguntas/eventos procesados y duplicados; permite relacionar consumo con respuestas HTTP.
6. **Métodos, anotaciones o configuraciones importantes:** Marcas de tiempo, niveles y mensajes de ejecución.
7. **Relaciones:** lifecycle-service, Compose y smoke JSON
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/logs-question-service.txt`

1. **Ruta:** [docs/evidencias/logs-question-service.txt](<../../docs/evidencias/logs-question-service.txt>).
2. **Nombre:** `logs-question-service.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar logs previos de question-service.
5. **Contenido y funcionamiento:** Arranque en 8083 y mensajes sobre publicación/fallos durante las pruebas.
6. **Métodos, anotaciones o configuraciones importantes:** Marcas de tiempo, niveles y mensajes de ejecución.
7. **Relaciones:** question-service, Compose y smoke JSON
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/logs-rabbitmq.txt`

1. **Ruta:** [docs/evidencias/logs-rabbitmq.txt](<../../docs/evidencias/logs-rabbitmq.txt>).
2. **Nombre:** `logs-rabbitmq.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar logs previos de rabbitmq.
5. **Contenido y funcionamiento:** Arranque del broker, conexiones, colas y reconexión tras las pruebas de fallo.
6. **Métodos, anotaciones o configuraciones importantes:** Marcas de tiempo, niveles y mensajes de ejecución.
7. **Relaciones:** rabbitmq, Compose y smoke JSON
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/logs-validation-service.txt`

1. **Ruta:** [docs/evidencias/logs-validation-service.txt](<../../docs/evidencias/logs-validation-service.txt>).
2. **Nombre:** `logs-validation-service.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar logs previos de validation-service.
5. **Contenido y funcionamiento:** Arranque en 8082 y operación del endpoint técnico.
6. **Métodos, anotaciones o configuraciones importantes:** Marcas de tiempo, niveles y mensajes de ejecución.
7. **Relaciones:** validation-service, Compose y smoke JSON
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/maven-host-verify.log`

1. **Ruta:** [docs/evidencias/maven-host-verify.log](<../../docs/evidencias/maven-host-verify.log>).
2. **Nombre:** `maven-host-verify.log`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar suite previa en la jvm del host.
5. **Contenido y funcionamiento:** BUILD SUCCESS, 32 pruebas Surefire con Java 21 y release 17; no es suite AMQP completa.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/maven-java17-integration.log`

1. **Ruta:** [docs/evidencias/maven-java17-integration.log](<../../docs/evidencias/maven-java17-integration.log>).
2. **Nombre:** `maven-java17-integration.log`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar suite completa previa con java 17.
5. **Contenido y funcionamiento:** Reactor Maven verify -Pintegration; 55 pruebas aprobadas, incluidos servicios reales.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/openjdk-manifest.txt`

1. **Ruta:** [docs/evidencias/openjdk-manifest.txt](<../../docs/evidencias/openjdk-manifest.txt>).
2. **Nombre:** `openjdk-manifest.txt`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar justificación de la imagen alternativa.
5. **Contenido y funcionamiento:** El registro respondió no such manifest para openjdk:17-jdk-slim en la comprobación previa; respalda el uso de Temurin 17.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/persistencia-reinicio.json`

1. **Ruta:** [docs/evidencias/persistencia-reinicio.json](<../../docs/evidencias/persistencia-reinicio.json>).
2. **Nombre:** `persistencia-reinicio.json`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar persistencia y deduplicación de la ejecución anterior.
5. **Contenido y funcionamiento:** Conserva preguntas/recibos tras recreación/reinicio y resultado de reentrega del mismo evento; se cita como evidencia anterior, no como nueva ejecución.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/rabbitmq-queue.json`

1. **Ruta:** [docs/evidencias/rabbitmq-queue.json](<../../docs/evidencias/rabbitmq-queue.json>).
2. **Nombre:** `rabbitmq-queue.json`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar inspección anterior de la cola real.
5. **Contenido y funcionamiento:** Management devuelve nombre, durable, consumidor y contadores de actividad.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/red-saberpro-net.json`

1. **Ruta:** [docs/evidencias/red-saberpro-net.json](<../../docs/evidencias/red-saberpro-net.json>).
2. **Nombre:** `red-saberpro-net.json`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar red bridge inspeccionada previamente.
5. **Contenido y funcionamiento:** docker network inspect muestra nombre, driver y contenedores conectados.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/resumen-pruebas.json`

1. **Ruta:** [docs/evidencias/resumen-pruebas.json](<../../docs/evidencias/resumen-pruebas.json>).
2. **Nombre:** `resumen-pruebas.json`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar conteo anterior consolidado de maven.
5. **Contenido y funcionamiento:** Desglose de 55 casos con cero fallos, errores y omitidas.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/revision-final.json`

1. **Ruta:** [docs/evidencias/revision-final.json](<../../docs/evidencias/revision-final.json>).
2. **Nombre:** `revision-final.json`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar comprobación final de la etapa de implementación.
5. **Contenido y funcionamiento:** Registro de comprobaciones de API, despliegue y/o artefactos al cierre previo; no incorpora el nuevo caso límite HALL-01.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/smoke-jpa.json`

1. **Ruta:** [docs/evidencias/smoke-jpa.json](<../../docs/evidencias/smoke-jpa.json>).
2. **Nombre:** `smoke-jpa.json`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar respuestas anteriores del flujo jpa.
5. **Contenido y funcionamiento:** 28 comprobaciones HTTP más Management y recibos, con IDs y tiempos reales; escenarios normales y fallos controlados.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/smoke-jpa.log`

1. **Ruta:** [docs/evidencias/smoke-jpa.log](<../../docs/evidencias/smoke-jpa.log>).
2. **Nombre:** `smoke-jpa.log`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar salida de consola anterior del protocolo jpa.
5. **Contenido y funcionamiento:** Mensajes por comprobación y resultado PASS; complementa los cuerpos del JSON homónimo.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/smoke-memory.json`

1. **Ruta:** [docs/evidencias/smoke-memory.json](<../../docs/evidencias/smoke-memory.json>).
2. **Nombre:** `smoke-memory.json`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar respuestas anteriores del flujo hashmap.
5. **Contenido y funcionamiento:** Mismo protocolo de 28 comprobaciones con configuración memory; no cubre equivalencia para toda entrada posible.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/smoke-memory.log`

1. **Ruta:** [docs/evidencias/smoke-memory.log](<../../docs/evidencias/smoke-memory.log>).
2. **Nombre:** `smoke-memory.log`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar salida de consola anterior del protocolo hashmap.
5. **Contenido y funcionamiento:** Mensajes de HTTP, detenciones/recuperación y PASS; complementa el JSON homónimo.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/evidencias/taller12-baseline-tests.log`

1. **Ruta:** [docs/evidencias/taller12-baseline-tests.log](<../../docs/evidencias/taller12-baseline-tests.log>).
2. **Nombre:** `taller12-baseline-tests.log`.
3. **Estado:** Nuevo.
4. **Para qué sirve:** Conservar pruebas de la base antes de migrar.
5. **Contenido y funcionamiento:** 23 casos originales aprobados; útil para comparar con la suite actual de 55.
6. **Métodos, anotaciones o configuraciones importantes:** Artefacto textual de ejecución (.log/.txt) o datos estructurados (.json/.yml), según su extensión.
7. **Relaciones:** Comando/suite referido en el archivo y docs/evidencias/README.md
8. **Requisito:** E p.3: evidencia; D/F/H según la operación
9. **Evaluación:** Contenido inspeccionado y preservado; las comprobaciones nuevas se guardan en docs/auditoria/evidencias.
10. **Errores, riesgos o mejora:** Es una instantánea previa, no una captura gráfica ni prueba universal. El nuevo defecto HALL-01 no aparece en la suite antigua.

### `docs/figurasYEvidencias/Activación del adaptador en memoria.PNG`

1. **Ruta:** [docs/figurasYEvidencias/Activación del adaptador en memoria.PNG](<../../docs/figurasYEvidencias/Activación del adaptador en memoria.PNG>).
2. **Nombre:** `Activación del adaptador en memoria.PNG`.
3. **Estado:** Reutilizado sin cambios. Origen: `docs/figurasYEvidencias/Activación del adaptador en memoria.PNG` en e857f96.
4. **Para qué sirve:** Terminal con arranque de la aplicación histórica.
5. **Contenido y funcionamiento:** SaberProApplication, Tomcat en 8080, mensajes de Hikari/H2 y arranque Spring.
6. **Métodos, anotaciones o configuraciones importantes:** Captura de terminal con salida de ejecución histórica; no generada en esta auditoría.
7. **Relaciones:** Configuración histórica del Taller 12.
8. **Requisito:** No acredita por sí sola la selección HashMap ni los servicios Docker de Taller 13.
9. **Evaluación:** Limitada: prueba visual de un arranque, no identifica inequívocamente el repositorio efectivo.
10. **Errores, riesgos o mejora:** Histórica: puerto 8080 y clase anterior; no se ve el argumento app.repository=memory. No usar como prueba aislada de memoria; una captura actual debe incluir configuración y peticiones. Es idéntica a Evidencia_05_Adaptador_Memoria.PNG.

### `docs/figurasYEvidencias/Cambio de estado de una pregunta mediante el adaptador JPA.PNG`

1. **Ruta:** [docs/figurasYEvidencias/Cambio de estado de una pregunta mediante el adaptador JPA.PNG](<../../docs/figurasYEvidencias/Cambio de estado de una pregunta mediante el adaptador JPA.PNG>).
2. **Nombre:** `Cambio de estado de una pregunta mediante el adaptador JPA.PNG`.
3. **Estado:** Reutilizado sin cambios. Origen: `docs/figurasYEvidencias/Cambio de estado de una pregunta mediante el adaptador JPA.PNG` en e857f96.
4. **Para qué sirve:** Respuesta histórica al envío a revisión.
5. **Contenido y funcionamiento:** UUID 952125f7-987d-4bb5-8866-8a462464f1fc, curl en 8080, HTTP 200, PENDIENTE_REVISION.
6. **Métodos, anotaciones o configuraciones importantes:** Captura de terminal de ejecución histórica.
7. **Relaciones:** Agregado, API anterior y supuesto modo JPA.
8. **Requisito:** Apoya transición de estado; no acredita por sí sola JPA ni RabbitMQ.
9. **Evaluación:** Solo antecedente; no sustituye evidencia de persistencia/AMQP.
10. **Errores, riesgos o mejora:** Desactualizada: Taller 13 debe devolver 202 y usar 8083. Crear captura actual con 202 y prueba del mismo ID persistido/consumido.

### `docs/figurasYEvidencias/Creación de una pregunta con JPA y H2.PNG`

1. **Ruta:** [docs/figurasYEvidencias/Creación de una pregunta con JPA y H2.PNG](<../../docs/figurasYEvidencias/Creación de una pregunta con JPA y H2.PNG>).
2. **Nombre:** `Creación de una pregunta con JPA y H2.PNG`.
3. **Estado:** Reutilizado sin cambios. Origen: `docs/figurasYEvidencias/Creación de una pregunta con JPA y H2.PNG` en e857f96.
4. **Para qué sirve:** Petición histórica de creación y su respuesta.
5. **Contenido y funcionamiento:** curl a 8080, HTTP 201, UUID 952125f7-987d-4bb5-8866-8a462464f1fc y BORRADOR.
6. **Métodos, anotaciones o configuraciones importantes:** Captura de terminal de ejecución histórica.
7. **Relaciones:** Creación de Pregunta en la API del Taller 12.
8. **Requisito:** Acredita visualmente el contrato antiguo de creación; la captura no prueba por sí sola qué adaptador guardó.
9. **Evaluación:** Limitada: el título dice JPA/H2, pero no hay evidencia SQL o configuración suficiente en ese cuadro.
10. **Errores, riesgos o mejora:** Histórica; 8080 y entorno anterior. En nueva captura incluir selección JPA y consulta/reinicio o evidencia de persistencia junto a la respuesta 201.

### `docs/figurasYEvidencias/Crear una pregunta usando el adaptador en memoria.PNG`

1. **Ruta:** [docs/figurasYEvidencias/Crear una pregunta usando el adaptador en memoria.PNG](<../../docs/figurasYEvidencias/Crear una pregunta usando el adaptador en memoria.PNG>).
2. **Nombre:** `Crear una pregunta usando el adaptador en memoria.PNG`.
3. **Estado:** Reutilizado sin cambios. Origen: `docs/figurasYEvidencias/Crear una pregunta usando el adaptador en memoria.PNG` en e857f96.
4. **Para qué sirve:** Creación histórica de una pregunta con modo memoria visible.
5. **Contenido y funcionamiento:** Comando --app.repository=memory, curl al puerto 8080, HTTP 201, UUID eb0ddf6b-6d2c-49d2-b1ed-9b97a41e4d23, BORRADOR y cuatro opciones.
6. **Métodos, anotaciones o configuraciones importantes:** Captura de terminal de ejecución histórica.
7. **Relaciones:** InMemoryQuestionAdapter y creación por API en Taller 12.
8. **Requisito:** Sustenta el antecedente de creación e intercambio de adaptador, no la integración REST/AMQP actual.
9. **Evaluación:** Sí como evidencia del Taller 12 con alcance limitado; no acredita el sistema final.
10. **Errores, riesgos o mejora:** Histórica; el código final atiende en 8083 y valida por REST. Obtener captura actual de configuración memory, POST 201, revisión 202 y recibo Lifecycle; no editar la antigua para cambiar puertos.

### `docs/figurasYEvidencias/Envío de una pregunta a revisión utilizando el adaptador en memoria..PNG`

1. **Ruta:** [docs/figurasYEvidencias/Envío de una pregunta a revisión utilizando el adaptador en memoria..PNG](<../../docs/figurasYEvidencias/Envío de una pregunta a revisión utilizando el adaptador en memoria..PNG>).
2. **Nombre:** `Envío de una pregunta a revisión utilizando el adaptador en memoria..PNG`.
3. **Estado:** Reutilizado sin cambios. Origen: `docs/figurasYEvidencias/Envío de una pregunta a revisión utilizando el adaptador en memoria..PNG` en e857f96.
4. **Para qué sirve:** Revisión histórica de la pregunta creada en memoria.
5. **Contenido y funcionamiento:** POST en 8080, UUID eb0ddf6b-6d2c-49d2-b1ed-9b97a41e4d23, HTTP 200 y estado PENDIENTE_REVISION.
6. **Métodos, anotaciones o configuraciones importantes:** Captura de terminal de ejecución histórica.
7. **Relaciones:** Transición del agregado y API anterior.
8. **Requisito:** Demuestra visualmente la transición antigua, no el 202 requerido por G p.5.
9. **Evaluación:** Válida únicamente como antecedente; no muestra RabbitMQ ni consumidor.
10. **Errores, riesgos o mejora:** Desactualizada respecto a código HTTP, puerto y mensajería. Tomar una captura nueva de 202 en 8083 y enlazar el mismo ID con Management/logs/recibo.

### `docs/figurasYEvidencias/Evidencia_05_Adaptador_Memoria.PNG`

1. **Ruta:** [docs/figurasYEvidencias/Evidencia_05_Adaptador_Memoria.PNG](<../../docs/figurasYEvidencias/Evidencia_05_Adaptador_Memoria.PNG>).
2. **Nombre:** `Evidencia_05_Adaptador_Memoria.PNG`.
3. **Estado:** Reutilizado sin cambios. Origen: `docs/figurasYEvidencias/Evidencia_05_Adaptador_Memoria.PNG` en e857f96.
4. **Para qué sirve:** La misma terminal de arranque que la imagen Activación del adaptador en memoria.PNG.
5. **Contenido y funcionamiento:** Mismos píxeles, tamaño y SHA-256; Spring, Hikari y puerto 8080.
6. **Métodos, anotaciones o configuraciones importantes:** Captura histórica duplicada, no una ejecución independiente.
7. **Relaciones:** Evidencia histórica repetida.
8. **Requisito:** No aporta una comprobación adicional de intercambiabilidad.
9. **Evaluación:** No contar como segunda evidencia; el nombre no demuestra una configuración ausente de la imagen.
10. **Errores, riesgos o mejora:** Desactualizada para Taller 13 y duplicada exactamente. Referenciar una sola copia o explicar el duplicado en el archivo histórico; cualquier limpieza futura debe preservar la historia.

### `docs/figurasYEvidencias/Pruebas JUnit 5.PNG`

1. **Ruta:** [docs/figurasYEvidencias/Pruebas JUnit 5.PNG](<../../docs/figurasYEvidencias/Pruebas JUnit 5.PNG>).
2. **Nombre:** `Pruebas JUnit 5.PNG`.
3. **Estado:** Reutilizado sin cambios. Origen: `docs/figurasYEvidencias/Pruebas JUnit 5.PNG` en e857f96.
4. **Para qué sirve:** Resultados Maven del Taller 12.
5. **Contenido y funcionamiento:** 23 pruebas, cero fallos/errores y BUILD SUCCESS; clases del diseño anterior.
6. **Métodos, anotaciones o configuraciones importantes:** Captura de terminal de ejecución histórica.
7. **Relaciones:** Pruebas originales preservadas y adaptadas.
8. **Requisito:** Prueba visual de la base; no demuestra los 55 casos actuales.
9. **Evaluación:** Válida para comparar la base, no para certificar la auditoría actual.
10. **Errores, riesgos o mejora:** Histórica: número y clases no corresponden a la suite final. Agregar captura nueva del verify -Pintegration y resumen de 55; no cambiar el número sobre esta imagen.

### `docs/figurasYEvidencias/Validación de invariantes del dominio mediante DDD..PNG`

1. **Ruta:** [docs/figurasYEvidencias/Validación de invariantes del dominio mediante DDD..PNG](<../../docs/figurasYEvidencias/Validación de invariantes del dominio mediante DDD..PNG>).
2. **Nombre:** `Validación de invariantes del dominio mediante DDD..PNG`.
3. **Estado:** Reutilizado sin cambios. Origen: `docs/figurasYEvidencias/Validación de invariantes del dominio mediante DDD..PNG` en e857f96.
4. **Para qué sirve:** Rechazo histórico de una justificación corta.
5. **Contenido y funcionamiento:** curl en 8080 con justificación corta, HTTP 400 y mensaje del mínimo de 15 caracteres.
6. **Métodos, anotaciones o configuraciones importantes:** Captura de terminal de ejecución histórica.
7. **Relaciones:** Justificacion y tratamiento de ReglaNegocioException.
8. **Requisito:** Demuestra visualmente una regla DDD de G p.3.
9. **Evaluación:** Útil como antecedente de una regla; no demuestra todas las invariantes ni descarta HALL-01.
10. **Errores, riesgos o mejora:** La regla sigue vigente, pero la ejecución es del sistema anterior. Añadir evidencias actuales de 400 y explicar que esta captura cubre solo la justificación.

### `docs/figurasYEvidencias/arquitectura.png`

1. **Ruta:** [docs/figurasYEvidencias/arquitectura.png](<../../docs/figurasYEvidencias/arquitectura.png>).
2. **Nombre:** `arquitectura.png`.
3. **Estado:** Reutilizado sin cambios. Origen: `docs/figurasYEvidencias/arquitectura.png` en e857f96.
4. **Para qué sirve:** Arquitectura hexagonal de la aplicación del Taller 12.
5. **Contenido y funcionamiento:** PreguntaController, CrearPreguntaUseCase, CrearPreguntaService, dominio, repositorio, JPA, HashMap y LoggingNotificacionAdapter.
6. **Métodos, anotaciones o configuraciones importantes:** Diagrama ilustrativo; no es una captura de ejecución.
7. **Relaciones:** Antecedente de Question Service y de la inversión de dependencias.
8. **Requisito:** Ilustra separación de capas; no acredita los tres servicios de G p.2/E p.1–2.
9. **Evaluación:** Válida como antecedente identificado; no como arquitectura final.
10. **Errores, riesgos o mejora:** Desactualizada para Taller 13: nombres antiguos, adaptador de log y ausencia de Validation, Lifecycle y RabbitMQ. Conservar en histórico y usar los C4 actuales en el informe final; no presentar el adaptador de log como AMQP.

### `docs/figurasYEvidencias/flujo_revision.png`

1. **Ruta:** [docs/figurasYEvidencias/flujo_revision.png](<../../docs/figurasYEvidencias/flujo_revision.png>).
2. **Nombre:** `flujo_revision.png`.
3. **Estado:** Reutilizado sin cambios. Origen: `docs/figurasYEvidencias/flujo_revision.png` en e857f96.
4. **Para qué sirve:** Secuencia de envío a revisión del Taller 12.
5. **Contenido y funcionamiento:** Cliente Postman, controlador, caso de uso, agregado con regla 4/1, repositorio, notificación y respuesta HTTP 200.
6. **Métodos, anotaciones o configuraciones importantes:** Diagrama de secuencia ilustrativo.
7. **Relaciones:** Antecedente de EvaluarPreguntaUseCase.enviarRevision.
8. **Requisito:** Explica la regla de revisión, pero no demuestra HTTP 202 ni consumo AMQP de G p.5.
9. **Evaluación:** Solo histórica; no acredita el flujo distribuido final.
10. **Errores, riesgos o mejora:** Desactualizada: representa 200, nombres anteriores y notificación local. Usar la secuencia actual con confirms, 202, cola y consumidor; conservar esta imagen como comparación.

### `docs/figurasYEvidencias/modelo_ddd.png`

1. **Ruta:** [docs/figurasYEvidencias/modelo_ddd.png](<../../docs/figurasYEvidencias/modelo_ddd.png>).
2. **Nombre:** `modelo_ddd.png`.
3. **Estado:** Reutilizado sin cambios. Origen: `docs/figurasYEvidencias/modelo_ddd.png` en e857f96.
4. **Para qué sirve:** Estructura del agregado Pregunta.
5. **Contenido y funcionamiento:** UUID, Enunciado, Justificacion, opciones, EstadoPregunta, BORRADOR/PENDIENTE_REVISION, creación y revisión.
6. **Métodos, anotaciones o configuraciones importantes:** Diagrama de clases ilustrativo.
7. **Relaciones:** domain/model/Pregunta.java y sus objetos de valor.
8. **Requisito:** Ilustra DDD y separación de objetos de valor, G p.3/E p.1.
9. **Evaluación:** Sí como apoyo conceptual etiquetado como reutilizado; no demuestra que las invariantes se ejecuten correctamente.
10. **Errores, riesgos o mejora:** La estructura esencial sigue vigente. La multiplicidad 0..* describe borradores; la revisión exige 4/1. No muestra el defecto HALL-01. Añadir nota de la condición 4/1 al revisar y usar el C4 nivel 4 actual para mostrar puertos y adaptadores.

## Archivo eliminado y sustitución

### `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/out/notification/LoggingNotificacionAdapter.java`

1. **Ruta original:** la indicada arriba, accesible en Git e857f96, ausente del árbol actual.
2. **Nombre:** LoggingNotificacionAdapter.java.
3. **Estado:** eliminado sin traslado directo; sustituido por RabbitMQPublisher.
4. **Finalidad original:** notificar una revisión escribiendo un log local.
5. **Funcionamiento anterior:** recibía la pregunta revisada y registraba su identificador sin broker ni consumidor.
6. **Elemento principal:** implementación del puerto NotificacionRevisionPort y método de notificación.
7. **Relaciones:** antiguo servicio de aplicación y configuración de beans; hoy los reemplazan QuestionApplicationService, HexagonalConfiguration y RabbitMQPublisher.
8. **Requisito del cambio:** G p.5/E p.1–2 requieren AMQP real y QuestionReviewListener.
9. **Evaluación:** eliminación justificada; la publicación real y el consumo quedaron comprobados.
10. **Riesgo:** no reutilizar imágenes antiguas de este adaptador como evidencia de RabbitMQ. No queda como bean activo ni fallback silencioso.

## Correspondencia de traslados

| Ruta original en e857f96 | Ruta final | Tipo |
|---|---|---|
| `src/main/java/co/edu/unicauca/saberpro/SaberProApplication.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/QuestionServiceApplication.java` | Trasladado/reutilizado (solo paquete/nombre) |
| `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/in/web/ApiExceptionHandler.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/api/ApiExceptionHandler.java` | Trasladado y modificado |
| `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/in/web/PreguntaController.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/api/QuestionRestController.java` | Trasladado y modificado |
| `src/main/java/co/edu/unicauca/saberpro/application/CrearPreguntaService.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/application/QuestionApplicationService.java` | Trasladado y modificado |
| `src/main/java/co/edu/unicauca/saberpro/domain/exception/ReglaNegocioException.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/exception/ReglaNegocioException.java` | Trasladado/reutilizado (solo paquete/nombre) |
| `src/main/java/co/edu/unicauca/saberpro/domain/valueobject/Enunciado.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Enunciado.java` | Trasladado/reutilizado (solo paquete/nombre) |
| `src/main/java/co/edu/unicauca/saberpro/domain/model/EstadoPregunta.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/EstadoPregunta.java` | Trasladado/reutilizado (solo paquete/nombre) |
| `src/main/java/co/edu/unicauca/saberpro/domain/valueobject/Justificacion.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Justificacion.java` | Trasladado/reutilizado (solo paquete/nombre) |
| `src/main/java/co/edu/unicauca/saberpro/domain/valueobject/OpcionRespuesta.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/OpcionRespuesta.java` | Trasladado/reutilizado (solo paquete/nombre) |
| `src/main/java/co/edu/unicauca/saberpro/domain/model/Pregunta.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/model/Pregunta.java` | Trasladado/reutilizado (solo paquete/nombre) |
| `src/main/java/co/edu/unicauca/saberpro/domain/ports/inbound/CrearPreguntaUseCase.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/inbound/CrearPreguntaUseCase.java` | Trasladado y modificado |
| `src/main/java/co/edu/unicauca/saberpro/domain/ports/outbound/NotificacionRevisionPort.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/outbound/NotificacionRevisionPort.java` | Trasladado/reutilizado (solo paquete/nombre) |
| `src/main/java/co/edu/unicauca/saberpro/domain/ports/outbound/PreguntaRepositoryPort.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/domain/ports/outbound/PreguntaRepositoryPort.java` | Trasladado y modificado |
| `src/main/java/co/edu/unicauca/saberpro/infrastructure/config/HexagonalConfiguration.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/config/HexagonalConfiguration.java` | Trasladado y modificado |
| `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/out/jpa/JpaQuestionAdapter.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/JpaQuestionAdapter.java` | Trasladado y modificado |
| `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/out/jpa/OpcionEmbeddable.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/OpcionEmbeddable.java` | Trasladado/reutilizado (solo paquete/nombre) |
| `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/out/jpa/PreguntaJpaEntity.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/QuestionRecord.java` | Trasladado/reutilizado (solo paquete/nombre) |
| `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/out/jpa/SpringDataPreguntaRepository.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/jpa/SpringDataPreguntaRepository.java` | Trasladado y modificado |
| `src/main/java/co/edu/unicauca/saberpro/infrastructure/adapters/out/memory/InMemoryQuestionAdapter.java` | `question-service/src/main/java/co/edu/unicauca/saberpro/question/infrastructure/memory/InMemoryQuestionAdapter.java` | Trasladado y modificado |
| `src/main/resources/application.properties` | `question-service/src/main/resources/application.properties` | Trasladado y modificado |
| `src/test/java/co/edu/unicauca/saberpro/application/IntercambiabilidadTest.java` | `question-service/src/test/java/co/edu/unicauca/saberpro/question/application/IntercambiabilidadTest.java` | Trasladado y modificado |
| `src/test/java/co/edu/unicauca/saberpro/domain/PreguntaTest.java` | `question-service/src/test/java/co/edu/unicauca/saberpro/question/domain/PreguntaTest.java` | Trasladado/reutilizado (solo paquete/nombre) |
| `src/test/java/co/edu/unicauca/saberpro/infrastructure/ApiJpaIntegrationTest.java` | `question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/ApiJpaIntegrationTest.java` | Trasladado y modificado |
| `src/test/java/co/edu/unicauca/saberpro/infrastructure/ApiMemoryIntegrationTest.java` | `question-service/src/test/java/co/edu/unicauca/saberpro/question/infrastructure/ApiMemoryIntegrationTest.java` | Trasladado y modificado |

## Archivos potencialmente innecesarios y alcance del inventario

- `.dockerignore` raíz no participa en los builds con contextos de módulo; puede conservarse por un eventual build desde raíz.
- `Evidencia_05_Adaptador_Memoria.PNG` duplica exactamente otra captura; no aporta una segunda ejecución.
- `postman/SaberPRO_Taller12.postman_collection.json` y docs/taller12 son material histórico útil, no recursos activos del Taller 13. No eliminarlos sin decidir cómo conservar la historia.
- `target/` contiene clases, JAR, fuentes auxiliares de Maven y reportes regenerables; `.git/` conserva objetos, referencias e índice de Git; `__pycache__/` contiene bytecode Python regenerable. No se versionan como implementación.
- Los logs/JSON de docs/evidencias sí son entregables de evidencia; cada uno tiene ficha arriba. Los logs de arranque o pruebas no se confunden con código Java ni pantallazos.

## Material añadido por esta auditoría

Los seis informes solicitados y las nuevas evidencias quedan exclusivamente bajo docs/auditoria. EXPLICACION_ARCHIVOS documenta cada archivo; EXPLICACION_IMAGENES examina imágenes y C4; MATRIZ_CUMPLIMIENTO_TALLER13 relaciona todos los ítems y páginas; RESULTADOS_PRUEBAS conserva comandos/resultados; ERRORES_Y_PENDIENTES prioriza acciones sin aplicarlas; GUIA_SUSTENTACION prepara la defensa.

`evidencias/DomainBoundaryProbe.java` es una sonda aislada de auditoría: su main crea Enunciado y OpcionRespuesta con NUL/tab/espacio, muestra longitud normalizada cero, envía a revisión y falla con AssertionError. No se añadió a Maven ni a producción. `evidencias/http_probe.py` reproduce el mismo defecto por HTTP con JPA y memoria, espera el recibo de Lifecycle y conserva los cuerpos. Sus parámetros de entrada son las URLs/escenarios codificados para la prueba local; su salida es http-normalizacion.json. Ambos prueban HALL-01 y no lo corrigen.

Los JSON de clasificación, hashes, imágenes, enlaces, resúmenes y comandos describen observaciones verificables. reportes-previos conserva las salidas previas de target, y junit-actual conserva los reportes recién producidos. Los textos de las dos guías facilitan buscar requisitos sin alterar los PDF. Los logs y respuestas nuevas se identifican en RESULTADOS_PRUEBAS; su existencia no convierte un caso no ejecutado en aprobado.
