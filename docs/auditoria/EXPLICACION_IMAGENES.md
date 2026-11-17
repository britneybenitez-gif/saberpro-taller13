# Explicación y verificación de imágenes y diagramas

Auditoría del 16 de noviembre de 2026 (UTC). Se abrieron visualmente los **11 PNG**: 3 diagramas y 8 capturas, de las cuales dos archivos contienen exactamente la misma captura. Todas las imágenes coinciden byte por byte con el commit `e857f96` del Taller 12. Las capturas muestran salidas históricas; esta auditoría no puede autenticar su ejecución original más allá de su contenido visible y procedencia Git. No se generaron pantallazos artificiales.

Los resultados actuales son logs y JSON en [evidencias](evidencias/); esos archivos no sustituyen las capturas solicitadas en E p.3. G y E se definen con páginas en [la matriz](MATRIZ_CUMPLIMIENTO_TALLER13.md).

## 1. Activación del adaptador en memoria.PNG

1. **Nombre y ruta:** [Activación del adaptador en memoria.PNG](<../../docs/figurasYEvidencias/Activación del adaptador en memoria.PNG>); 1362 × 565 px.
2. **Representa:** Terminal con arranque de la aplicación histórica.
3. **Elementos visibles:** SaberProApplication, Tomcat en 8080, mensajes de Hikari/H2 y arranque Spring.
4. **Parte del sistema:** Configuración histórica del Taller 12.
5. **Requisito y alcance:** No acredita por sí sola la selección HashMap ni los servicios Docker de Taller 13.
6. **Origen:** Taller 12; SHA-256 `b29ba59ea9e1bd424dd54fe318dd531c9d1745beb4f1757366947d959730ee5c`, idéntico al objeto de Git en `e857f96`.
7. **Actualización:** Histórica: puerto 8080 y clase anterior; no se ve el argumento app.repository=memory.
8. **Tipo de evidencia:** Captura de terminal con salida de ejecución histórica; no generada en esta auditoría.
9. **Validez para entrega:** Limitada: prueba visual de un arranque, no identifica inequívocamente el repositorio efectivo.
10. **Acción propuesta:** No usar como prueba aislada de memoria; una captura actual debe incluir configuración y peticiones. Es idéntica a Evidencia_05_Adaptador_Memoria.PNG.

## 2. Cambio de estado de una pregunta mediante el adaptador JPA.PNG

1. **Nombre y ruta:** [Cambio de estado de una pregunta mediante el adaptador JPA.PNG](<../../docs/figurasYEvidencias/Cambio de estado de una pregunta mediante el adaptador JPA.PNG>); 1364 × 160 px.
2. **Representa:** Respuesta histórica al envío a revisión.
3. **Elementos visibles:** UUID 952125f7-987d-4bb5-8866-8a462464f1fc, curl en 8080, HTTP 200, PENDIENTE_REVISION.
4. **Parte del sistema:** Agregado, API anterior y supuesto modo JPA.
5. **Requisito y alcance:** Apoya transición de estado; no acredita por sí sola JPA ni RabbitMQ.
6. **Origen:** Taller 12; SHA-256 `e3f1dab6dbabb0df34ec958aa22091aedff43138c4e5cb33d9a26166ea111870`, idéntico al objeto de Git en `e857f96`.
7. **Actualización:** Desactualizada: Taller 13 debe devolver 202 y usar 8083.
8. **Tipo de evidencia:** Captura de terminal de ejecución histórica.
9. **Validez para entrega:** Solo antecedente; no sustituye evidencia de persistencia/AMQP.
10. **Acción propuesta:** Crear captura actual con 202 y prueba del mismo ID persistido/consumido.

## 3. Creación de una pregunta con JPA y H2.PNG

1. **Nombre y ruta:** [Creación de una pregunta con JPA y H2.PNG](<../../docs/figurasYEvidencias/Creación de una pregunta con JPA y H2.PNG>); 1366 × 526 px.
2. **Representa:** Petición histórica de creación y su respuesta.
3. **Elementos visibles:** curl a 8080, HTTP 201, UUID 952125f7-987d-4bb5-8866-8a462464f1fc y BORRADOR.
4. **Parte del sistema:** Creación de Pregunta en la API del Taller 12.
5. **Requisito y alcance:** Acredita visualmente el contrato antiguo de creación; la captura no prueba por sí sola qué adaptador guardó.
6. **Origen:** Taller 12; SHA-256 `dd90f59d2aa436f4efc0e97fb4dc8b6018975bf0de164445e49471a691711294`, idéntico al objeto de Git en `e857f96`.
7. **Actualización:** Histórica; 8080 y entorno anterior.
8. **Tipo de evidencia:** Captura de terminal de ejecución histórica.
9. **Validez para entrega:** Limitada: el título dice JPA/H2, pero no hay evidencia SQL o configuración suficiente en ese cuadro.
10. **Acción propuesta:** En nueva captura incluir selección JPA y consulta/reinicio o evidencia de persistencia junto a la respuesta 201.

## 4. Crear una pregunta usando el adaptador en memoria.PNG

1. **Nombre y ruta:** [Crear una pregunta usando el adaptador en memoria.PNG](<../../docs/figurasYEvidencias/Crear una pregunta usando el adaptador en memoria.PNG>); 1366 × 340 px.
2. **Representa:** Creación histórica de una pregunta con modo memoria visible.
3. **Elementos visibles:** Comando --app.repository=memory, curl al puerto 8080, HTTP 201, UUID eb0ddf6b-6d2c-49d2-b1ed-9b97a41e4d23, BORRADOR y cuatro opciones.
4. **Parte del sistema:** InMemoryQuestionAdapter y creación por API en Taller 12.
5. **Requisito y alcance:** Sustenta el antecedente de creación e intercambio de adaptador, no la integración REST/AMQP actual.
6. **Origen:** Taller 12; SHA-256 `12a7c45e7050a7de1ec1473d8ae91882e1458cf82bc84ba995431ac500a09e2e`, idéntico al objeto de Git en `e857f96`.
7. **Actualización:** Histórica; el código final atiende en 8083 y valida por REST.
8. **Tipo de evidencia:** Captura de terminal de ejecución histórica.
9. **Validez para entrega:** Sí como evidencia del Taller 12 con alcance limitado; no acredita el sistema final.
10. **Acción propuesta:** Obtener captura actual de configuración memory, POST 201, revisión 202 y recibo Lifecycle; no editar la antigua para cambiar puertos.

## 5. Envío de una pregunta a revisión utilizando el adaptador en memoria..PNG

1. **Nombre y ruta:** [Envío de una pregunta a revisión utilizando el adaptador en memoria..PNG](<../../docs/figurasYEvidencias/Envío de una pregunta a revisión utilizando el adaptador en memoria..PNG>); 1363 × 180 px.
2. **Representa:** Revisión histórica de la pregunta creada en memoria.
3. **Elementos visibles:** POST en 8080, UUID eb0ddf6b-6d2c-49d2-b1ed-9b97a41e4d23, HTTP 200 y estado PENDIENTE_REVISION.
4. **Parte del sistema:** Transición del agregado y API anterior.
5. **Requisito y alcance:** Demuestra visualmente la transición antigua, no el 202 requerido por G p.5.
6. **Origen:** Taller 12; SHA-256 `e56f90c0d7242452e6b159f77687ec5184ba7bb9568e109068780e6087a912eb`, idéntico al objeto de Git en `e857f96`.
7. **Actualización:** Desactualizada respecto a código HTTP, puerto y mensajería.
8. **Tipo de evidencia:** Captura de terminal de ejecución histórica.
9. **Validez para entrega:** Válida únicamente como antecedente; no muestra RabbitMQ ni consumidor.
10. **Acción propuesta:** Tomar una captura nueva de 202 en 8083 y enlazar el mismo ID con Management/logs/recibo.

## 6. Evidencia_05_Adaptador_Memoria.PNG

1. **Nombre y ruta:** [Evidencia_05_Adaptador_Memoria.PNG](<../../docs/figurasYEvidencias/Evidencia_05_Adaptador_Memoria.PNG>); 1362 × 565 px.
2. **Representa:** La misma terminal de arranque que la imagen Activación del adaptador en memoria.PNG.
3. **Elementos visibles:** Mismos píxeles, tamaño y SHA-256; Spring, Hikari y puerto 8080.
4. **Parte del sistema:** Evidencia histórica repetida.
5. **Requisito y alcance:** No aporta una comprobación adicional de intercambiabilidad.
6. **Origen:** Taller 12; SHA-256 `b29ba59ea9e1bd424dd54fe318dd531c9d1745beb4f1757366947d959730ee5c`, idéntico al objeto de Git en `e857f96`.
7. **Actualización:** Desactualizada para Taller 13 y duplicada exactamente.
8. **Tipo de evidencia:** Captura histórica duplicada, no una ejecución independiente.
9. **Validez para entrega:** No contar como segunda evidencia; el nombre no demuestra una configuración ausente de la imagen.
10. **Acción propuesta:** Referenciar una sola copia o explicar el duplicado en el archivo histórico; cualquier limpieza futura debe preservar la historia.

## 7. Pruebas JUnit 5.PNG

1. **Nombre y ruta:** [Pruebas JUnit 5.PNG](<../../docs/figurasYEvidencias/Pruebas JUnit 5.PNG>); 1323 × 699 px.
2. **Representa:** Resultados Maven del Taller 12.
3. **Elementos visibles:** 23 pruebas, cero fallos/errores y BUILD SUCCESS; clases del diseño anterior.
4. **Parte del sistema:** Pruebas originales preservadas y adaptadas.
5. **Requisito y alcance:** Prueba visual de la base; no demuestra los 55 casos actuales.
6. **Origen:** Taller 12; SHA-256 `8b6e34768333e3be41f8ee3ed31224a85254b87511b524437a3ce513107adc78`, idéntico al objeto de Git en `e857f96`.
7. **Actualización:** Histórica: número y clases no corresponden a la suite final.
8. **Tipo de evidencia:** Captura de terminal de ejecución histórica.
9. **Validez para entrega:** Válida para comparar la base, no para certificar la auditoría actual.
10. **Acción propuesta:** Agregar captura nueva del verify -Pintegration y resumen de 55; no cambiar el número sobre esta imagen.

## 8. Validación de invariantes del dominio mediante DDD..PNG

1. **Nombre y ruta:** [Validación de invariantes del dominio mediante DDD..PNG](<../../docs/figurasYEvidencias/Validación de invariantes del dominio mediante DDD..PNG>); 1270 × 307 px.
2. **Representa:** Rechazo histórico de una justificación corta.
3. **Elementos visibles:** curl en 8080 con justificación corta, HTTP 400 y mensaje del mínimo de 15 caracteres.
4. **Parte del sistema:** Justificacion y tratamiento de ReglaNegocioException.
5. **Requisito y alcance:** Demuestra visualmente una regla DDD de G p.3.
6. **Origen:** Taller 12; SHA-256 `64dd890cfec74be7aad05428f6cf3fb0f56852e3eff9ea47aca5f80c145f4632`, idéntico al objeto de Git en `e857f96`.
7. **Actualización:** La regla sigue vigente, pero la ejecución es del sistema anterior.
8. **Tipo de evidencia:** Captura de terminal de ejecución histórica.
9. **Validez para entrega:** Útil como antecedente de una regla; no demuestra todas las invariantes ni descarta HALL-01.
10. **Acción propuesta:** Añadir evidencias actuales de 400 y explicar que esta captura cubre solo la justificación.

## 9. arquitectura.png

1. **Nombre y ruta:** [arquitectura.png](<../../docs/figurasYEvidencias/arquitectura.png>); 1429 × 989 px.
2. **Representa:** Arquitectura hexagonal de la aplicación del Taller 12.
3. **Elementos visibles:** PreguntaController, CrearPreguntaUseCase, CrearPreguntaService, dominio, repositorio, JPA, HashMap y LoggingNotificacionAdapter.
4. **Parte del sistema:** Antecedente de Question Service y de la inversión de dependencias.
5. **Requisito y alcance:** Ilustra separación de capas; no acredita los tres servicios de G p.2/E p.1–2.
6. **Origen:** Taller 12; SHA-256 `c61873b380827b2dfc52b656d672278acf94d619bad09d84f317ee585da003d7`, idéntico al objeto de Git en `e857f96`.
7. **Actualización:** Desactualizada para Taller 13: nombres antiguos, adaptador de log y ausencia de Validation, Lifecycle y RabbitMQ.
8. **Tipo de evidencia:** Diagrama ilustrativo; no es una captura de ejecución.
9. **Validez para entrega:** Válida como antecedente identificado; no como arquitectura final.
10. **Acción propuesta:** Conservar en histórico y usar los C4 actuales en el informe final; no presentar el adaptador de log como AMQP.

## 10. flujo_revision.png

1. **Nombre y ruta:** [flujo_revision.png](<../../docs/figurasYEvidencias/flujo_revision.png>); 2331 × 556 px.
2. **Representa:** Secuencia de envío a revisión del Taller 12.
3. **Elementos visibles:** Cliente Postman, controlador, caso de uso, agregado con regla 4/1, repositorio, notificación y respuesta HTTP 200.
4. **Parte del sistema:** Antecedente de EvaluarPreguntaUseCase.enviarRevision.
5. **Requisito y alcance:** Explica la regla de revisión, pero no demuestra HTTP 202 ni consumo AMQP de G p.5.
6. **Origen:** Taller 12; SHA-256 `f93e6f4b4eb928f7f04df9a64e394080960cdbf5e07a9452bb056e25a0324b44`, idéntico al objeto de Git en `e857f96`.
7. **Actualización:** Desactualizada: representa 200, nombres anteriores y notificación local.
8. **Tipo de evidencia:** Diagrama de secuencia ilustrativo.
9. **Validez para entrega:** Solo histórica; no acredita el flujo distribuido final.
10. **Acción propuesta:** Usar la secuencia actual con confirms, 202, cola y consumidor; conservar esta imagen como comparación.

## 11. modelo_ddd.png

1. **Nombre y ruta:** [modelo_ddd.png](<../../docs/figurasYEvidencias/modelo_ddd.png>); 2128 × 833 px.
2. **Representa:** Estructura del agregado Pregunta.
3. **Elementos visibles:** UUID, Enunciado, Justificacion, opciones, EstadoPregunta, BORRADOR/PENDIENTE_REVISION, creación y revisión.
4. **Parte del sistema:** domain/model/Pregunta.java y sus objetos de valor.
5. **Requisito y alcance:** Ilustra DDD y separación de objetos de valor, G p.3/E p.1.
6. **Origen:** Taller 12; SHA-256 `657eca3cb7583d735d1406c9863bdfea3dccd5d4f3925a9020d02b140a793aab`, idéntico al objeto de Git en `e857f96`.
7. **Actualización:** La estructura esencial sigue vigente. La multiplicidad 0..* describe borradores; la revisión exige 4/1. No muestra el defecto HALL-01.
8. **Tipo de evidencia:** Diagrama de clases ilustrativo.
9. **Validez para entrega:** Sí como apoyo conceptual etiquetado como reutilizado; no demuestra que las invariantes se ejecuten correctamente.
10. **Acción propuesta:** Añadir nota de la condición 4/1 al revisar y usar el C4 nivel 4 actual para mostrar puertos y adaptadores.

## Diagramas C4 actuales: explicación individual

Fuente: [docs/arquitectura/diagramas.md](../arquitectura/diagramas.md). Son fuentes Mermaid ilustrativas, no capturas. Se compararon con las clases y con los servicios desplegados. Los cuatro niveles son acercamientos sucesivos: no corresponde dibujar todos los microservicios dentro del diagrama de clases del agregado.

### Nivel 1 — Contexto

Sitúa el Banco de Preguntas Saber PRO y sus actores externos (docente/autor y estudiante/operador). Explica quién crea preguntas y quién despliega/verifica el procesamiento. Su límite es el sistema completo; ocultar aquí los detalles REST, bases y tres procesos es coherente con un nivel de contexto. No prueba ejecución ni evaluación humana automatizada. El diagrama declara expresamente que no representa autenticación, correos ni evaluación humana automatizada; esos componentes no están implementados.

### Nivel 2 — Contenedores

Expone Validation :8082, Question :8083 y Lifecycle :8084, RabbitMQ :5672/:15672, el cliente y los H2 separados. Coincide con Compose, la red bridge saberpro-net y los volúmenes. Question llama a Validation por HTTP; publica en RabbitMQ; Lifecycle consume. Los H2 están embebidos en las JVM, no son contenedores Docker adicionales: el despliegue tiene cuatro contenedores. La etiqueta «Entrega/ACK» resume un intercambio bidireccional; el ACK realmente vuelve desde el consumidor al broker. Esto es una simplificación gráfica, no una implementación diferente.

### Nivel 3 — Componentes

Acerca Question Service: QuestionRestController, puertos de entrada, QuestionApplicationService, agregado/VO, puertos de salida, adaptadores JPA/memoria, RestValidationAdapter y RabbitMQPublisher. Las interfaces pertenecen al núcleo y las implementaciones a infraestructura. Validation y RabbitMQ aparecen como colaboraciones externas. El alcance está explicitado como Question; no se exige repetir los tres servicios completos en este acercamiento. Las clases y relaciones indicadas existen y se usaron realmente.

### Nivel 4 — Código

Detalla Pregunta, EstadoPregunta, los tres VO, interfaces de casos de uso, servicio y puertos de salida. Los adaptadores concretos se detallan en el nivel 3. La multiplicidad de opciones describe la estructura de un borrador; la regla de cuatro y una correcta se comprueba al revisar. El dominio no tiene anotaciones JPA. Las asociaciones corresponden al código; el dibujo no certifica que todos los valores aceptados sean correctos: HALL-01 queda documentado por separado.

### Secuencia actual de creación

Representa HTTP → controlador → caso de uso → dominio → puerto de validación → Validation por REST → repositorio → respuesta 201. El DTO HTTP y el JSON saliente de validación contienen textos simples; la respuesta conserva VO anidados. Si Validation falla, no se guarda. Corresponde al código y a las pruebas con caída real.

### Secuencia actual de revisión

Representa lectura bloqueada, validación de 4/1 y estado, guardado/flush, publicación con confirmación, commit local y respuesta 202; en paralelo Lifecycle consume y guarda un comprobante. El consumidor puede procesar antes del commit de Question. La nota de consistencia describe esa ventana: no hay transacción distribuida ni promesa de exactamente una vez.

### Diagrama del README actual

Un flowchart resume tres servicios, REST y AMQP. Es un mapa de navegación coherente con el despliegue, no un quinto nivel C4 ni evidencia de contenedores activos.

### Tres Mermaid históricos en docs/taller12/diagramas.md

El primero representa el hexágono original con el adaptador de notificación por log. El segundo representa la revisión antigua y HTTP 200. El tercero muestra el agregado y VO originales. Son fuentes válidas para los tres PNG ilustrativos históricos, no documentación final de Taller 13. Se preservaron para comparación.

## Verificación de sintaxis y referencias

Los **10 bloques Mermaid** (1 README, 6 arquitectura actual y 3 históricos) pasaron `mermaid.parse` con Mermaid 11.17.2. Evidencia: [mermaid-parse.json](evidencias/mermaid-parse.json) y [log](evidencias/mermaid-parse.log). Se comprobó sintaxis y coherencia semántica; no se exportaron ni inspeccionaron nuevos PNG/SVG de esos bloques, por lo que su composición visual final en Word queda por revisar. La API utilizada está descrita en la [documentación oficial de Mermaid](https://mermaid.js.org/config/usage.html).

El chequeo de destinos locales encontró **15 referencias rotas, todas en docs/taller12/README.md**, incluidas tres imágenes insertadas. La copia histórica conserva rutas relativas de cuando estaba en la raíz. El README actual y la documentación activa tienen sus destinos locales accesibles en el inventario analizado. Este chequeo verifica archivos locales, no disponibilidad de enlaces web ni renderizado de cada visor.

| Archivo y línea | Destino roto | Tipo |
|---|---|---|
| `docs/taller12/README.md:53` | `docs/figurasYEvidencias/arquitectura.png` | Imagen insertada |
| `docs/taller12/README.md:70` | `docs/figurasYEvidencias/modelo_ddd.png` | Imagen insertada |
| `docs/taller12/README.md:183` | `ejemplo-pregunta.json` | Referencia local |
| `docs/taller12/README.md:183` | `postman/SaberPRO_Taller12.postman_collection.json` | Referencia local |
| `docs/taller12/README.md:222` | `docs/diagramas.md` | Referencia local |
| `docs/taller12/README.md:224` | `docs/figurasYEvidencias/flujo_revision.png` | Imagen insertada |
| `docs/taller12/README.md:228` | `docs/figurasYEvidencias/Pruebas%20JUnit%205.PNG` | Referencia local |
| `docs/taller12/README.md:229` | `docs/figurasYEvidencias/Creaci%C3%B3n%20de%20una%20pregunta%20con%20JPA%20y%20H2.PNG` | Referencia local |
| `docs/taller12/README.md:230` | `docs/figurasYEvidencias/Cambio%20de%20estado%20de%20una%20pregunta%20mediante%20el%20adaptador%20JPA.PNG` | Referencia local |
| `docs/taller12/README.md:231` | `docs/figurasYEvidencias/Validaci%C3%B3n%20de%20invariantes%20del%20dominio%20mediante%20DDD..PNG` | Referencia local |
| `docs/taller12/README.md:232` | `docs/figurasYEvidencias/Activaci%C3%B3n%20del%20adaptador%20en%20memoria.PNG` | Referencia local |
| `docs/taller12/README.md:233` | `docs/figurasYEvidencias/Evidencia_05_Adaptador_Memoria.PNG` | Referencia local |
| `docs/taller12/README.md:234` | `docs/figurasYEvidencias/Crear%20una%20pregunta%20usando%20el%20adaptador%20en%20memoria.PNG` | Referencia local |
| `docs/taller12/README.md:235` | `docs/figurasYEvidencias/Env%C3%ADo%20de%20una%20pregunta%20a%20revisi%C3%B3n%20utilizando%20el%20adaptador%20en%20memoria..PNG` | Referencia local |
| `docs/taller12/README.md:237` | `docs/figurasYEvidencias/` | Referencia local |

Propuesta para una corrección posterior: desde el README archivado usar `../figurasYEvidencias/...`, `../../ejemplo-pregunta.json`, `../../postman/...` y `diagramas.md` según corresponda. No se modificaron el histórico ni sus imágenes durante esta auditoría.

## Evidencia gráfica pendiente para E p.3

Tomar capturas auténticas de `docker compose ps` con los cuatro contenedores saludables y sus puertos; de RabbitMQ Management con la cola exacta, consumidor y actividad; y del flujo HTTP 201 → 202 y log/recibo del mismo UUID. Importar/exportar los C4 actuales en la plantilla y comprobar legibilidad. Las pruebas de esta auditoría ya producen IDs y logs verificables para orientar esa demostración, pero no se afirma que se haya usado la interfaz gráfica de Thunder Client ni de Management.
