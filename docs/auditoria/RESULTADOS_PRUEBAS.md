# Resultados reales de la auditoría

Fecha: **16/11/2026 UTC**. Se ejecutaron los comandos solicitados, compilación Java 17, HTTP contra ambos repositorios y RabbitMQ real. **55 pruebas Maven aprobaron y los dos protocolos de 28 respuestas HTTP aprobaron. Una sonda adicional encontró HALL-01 y falló: no se oculta bajo el resultado verde de Maven.**

Las nuevas salidas están en docs/auditoria/evidencias. Los archivos de docs/evidencias se conservaron; los reportes existentes de target se copiaron a evidencias/reportes-previos antes de clean. No se alteraron fuentes o pruebas de los microservicios. Las comprobaciones introdujeron preguntas y recibos de prueba en el entorno local, como parte de las peticiones solicitadas.

## Entorno y alcance Git

| Elemento | Resultado observado | Evidencia |
|---|---|---|
| Proyecto | /home/victor_cespedes0710/proyectos/saberpro-taller13 | Inventario recursivo |
| Rama/base | feature/taller13-fase2, HEAD e857f96 | [Git inicial](evidencias/git-inicial.txt) |
| Original local | e857f96, referencias locales main/origin/main del repositorio britneybenitez-gif/saberpro-hexagonal-taller12 | [Clasificación](evidencias/clasificacion-git.json) |
| Java host | OpenJDK 21.0.12.1; POM configura release17 | [java-host.txt](evidencias/java-host.txt) |
| Maven host | 3.9.16 | [maven-host.txt](evidencias/maven-host.txt) |
| JVM del contenedor | Temurin17.0.21 | [java-contenedor.txt](evidencias/java-contenedor.txt) |
| Maven para integración | Imagen maven:3.9.9-eclipse-temurin-17 | [Log de integración](evidencias/maven-java17-integration.log) |
| Docker cliente/motor | 29.9.3, motor disponible | [docker-version.txt](evidencias/docker-version.txt) |
| Compose | v5.7.0 | [compose-version.txt](evidencias/compose-version.txt) |
| Red | saberpro-net, bridge, DNS entre servicios | [red](evidencias/red-saberpro-net.json), [HTTP interno](evidencias/conectividad-interna.txt) |

Se trabajó sobre cambios ya existentes del Taller13, comparándolos con la base local. No se hizo fetch, commit, push o merge y no se verificó entrega en plataforma. Se leyeron ambas guías completas: sus extracciones con marcadores de página están en esta carpeta de evidencias; los PDF originales no se modificaron.

## Comandos Maven solicitados: ejecución individual

| Comando exacto | Resultado | Duración | Log |
|---|---|---:|---|
| `mvn -f question-service/pom.xml clean test` | BUILD SUCCESS; 24 tests; exit 0 | 14.6 s | [question-service](evidencias/question-service-clean-test.log) |
| `mvn -f validation-service/pom.xml clean test` | BUILD SUCCESS; 6 tests; exit 0 | 14.85 s | [validation-service](evidencias/validation-service-clean-test.log) |
| `mvn -f lifecycle-service/pom.xml clean test` | BUILD SUCCESS; 2 tests; exit 0 | 15.15 s | [lifecycle-service](evidencias/lifecycle-service-clean-test.log) |

Las ejecuciones individuales comenzaron a las 20:48:10, 20:48:25 y 20:48:40 UTC respectivamente. **32 casos Surefire en total**, no 55: los POM excluyen `*IntegrationTest` de `test`. Estos 32 incluyen pruebas unitarias y también pruebas Spring/HTTP/H2 autocontenidas; no se llaman indiscriminadamente «32 unitarias».

## Integración completa ejecutada con Java 17

Se reutilizó la red de los servicios reales y el repositorio Maven en caché. Comando ejecutado desde la raíz:

```bash
docker run --rm --network saberpro-net \
  --user "$(id -u):$(id -g)" \
  -e MAVEN_CONFIG=/tmp/maven-cache \
  -v "$PWD:/workspace" \
  -v /home/victor_cespedes0710/.m2:/tmp/maven-cache \
  -w /workspace maven:3.9.9-eclipse-temurin-17 \
  mvn -B -ntp \
  -Dmaven.repo.local=/tmp/maven-cache/repository \
  -Duser.home=/tmp \
  -Dapp.validation.base-url=http://validation-service:8082 \
  -Dspring.rabbitmq.host=rabbitmq \
  -Dtest.lifecycle-url=http://lifecycle-service:8084 \
  -Pintegration verify
```

Resultado: **BUILD SUCCESS**, reactor de los tres módulos y agregador; finalización 20:51:45 UTC, duración total aproximada 1 min 40 s. Surefire 32 + Failsafe 23 = **55 tests, 0 fallos, 0 errores y 0 omitidos**. La ejecución de Java 17 vuelve a incluir los 32 del paso anterior: no se suman como 87 casos distintos.

| Módulo / clase | Runner | Casos | Fallos | Errores | Omitidos |
|---|---|---:|---:|---:|---:|
| question-service / IntercambiabilidadTest | surefire | 3 | 0 | 0 | 0 |
| question-service / QuestionApplicationServiceTest | surefire | 3 | 0 | 0 | 0 |
| question-service / PreguntaTest | surefire | 14 | 0 | 0 | 0 |
| question-service / RabbitMQPublisherTest | surefire | 4 | 0 | 0 | 0 |
| question-service / ApiEmptyIntegrationTest | failsafe | 1 | 0 | 0 | 0 |
| question-service / ApiJpaIntegrationTest | failsafe | 10 | 0 | 0 | 0 |
| question-service / ApiMemoryIntegrationTest | failsafe | 10 | 0 | 0 | 0 |
| validation-service / ValidationApiTest | surefire | 6 | 0 | 0 | 0 |
| lifecycle-service / ReviewProcessingTest | surefire | 2 | 0 | 0 | 0 |
| lifecycle-service / LifecycleIntegrationTest | failsafe | 2 | 0 | 0 | 0 |

Los diez casos de ApiJpaIntegrationTest y de ApiMemoryIntegrationTest incluyen siete heredados de AdditionalApiContract más tres propios. AdditionalApiContract no produce una suite adicional independiente. Los reportes nuevos se preservan en [junit-actual](evidencias/junit-actual/) y el conteo sale de los XML Surefire/Failsafe, [resumen-pruebas.json](evidencias/resumen-pruebas.json).

**Qué es real y qué es un doble:** RabbitMQPublisherTest usa Mockito para ACK/NACK/return/timeout; los tests de aplicación aíslan puertos. Los tests `*IntegrationTest` usan Validation y RabbitMQ reales y comprueban Lifecycle; no se sustituyen por mocks. LifecycleIntegrationTest usa una cola propia terminada en `.integration-lifecycle`, para no competir con el consumidor desplegado. Verifica evento duplicado y mensaje incompatible enviado a su DLQ.

## Dominio sin dependencias externas

Comando ejecutado: `bash scripts/check-domain.sh`. Resultado exit 0: dominio y aplicación compilaron con `javac --release 17`, sin classpath de Spring/JPA. [Salida](evidencias/dominio-puro.txt). Esto acredita pureza de dependencias, no ausencia de errores de negocio; la sonda HALL-01 ilustra esa diferencia.

## Docker: configuración, construcción, ejecución y logs

| Comando ejecutado | Resultado real | Evidencia |
|---|---|---|
| `docker compose config` | exit 0; YAML resuelto válido | [config](evidencias/compose-config.yml) |
| `docker compose up -d --build` | exit 0; imágenes construidas/reutilizadas por caché, servicios activos | [build/up](evidencias/compose-up-build.log) |
| `docker compose ps` | Cuatro servicios running/healthy | [cierre](evidencias/compose-ps-cierre.txt) |
| `docker ps` | Contenedores y puertos visibles | [listado](evidencias/docker-ps-final.txt) |
| `docker network inspect saberpro-net` | driver bridge, contenedores conectados | [red](evidencias/red-saberpro-net.json) |
| `docker compose exec -T question-service java -version` | Java 17 real | [JVM](evidencias/java-contenedor.txt) |
| `docker compose exec -T question-service curl -fsS http://validation-service:8082/actuator/health` | HTTP interno correcto, UP | [DNS/HTTP](evidencias/conectividad-interna.txt) |
| `docker compose logs --no-color question-service` | Arranque8083 y errores controlados al detener dependencias | [log Question](evidencias/logs-question-service.txt) |
| `docker compose logs --no-color validation-service` | Arranque8082 y recuperación después de la parada de prueba | [log Validation](evidencias/logs-validation-service.txt) |
| `docker compose logs --no-color lifecycle-service` | Recepción real con IDs, reconexión tras caída | [log Lifecycle](evidencias/logs-lifecycle-service.txt) |
| `docker compose logs --no-color rabbitmq` | Arranque y conexiones reales; recuperación de broker | [log RabbitMQ](evidencias/logs-rabbitmq.txt) |

El build de auditoría usó caché porque las fuentes no cambiaron. La construcción anterior desde fuentes quedó preservada en docs/evidencias/docker-compose-build.log. Las imágenes multietapa generan su JAR; `-DskipTests` allí no se presenta como prueba aprobada. Los cuatro contenedores tienen los puertos correctos: Validation8082, Question8083, Lifecycle8084 y RabbitMQ5672/15672.

Los logs conservan historia anterior además de la auditoría. Se correlacionaron los avisos de conexión/health con paradas deliberadas a las 20:53 y20:58–20:59 UTC; después se observó reconexión, recibos y salud. Los ERROR de redeclaración del consumidor mientras el broker está detenido no se escondieron; corresponden a la prueba de indisponibilidad. El rechazo del contrato inválido en la suite de Lifecycle corresponde a la comprobación de DLQ. No se afirma que los logs carezcan de WARN/ERROR.

## Protocolo HTTP real con JPA y memoria

Ejecución JPA (20:53:05–20:53:32 UTC):

```bash
python3 scripts/smoke_test.py --label auditoria-jpa --faults \
  --output docs/auditoria/evidencias/smoke-jpa.json
```

Para memoria se creó un contenedor temporal de la misma imagen; se mantuvo el servicio principal en JPA y se evitó modificar su volumen/configuración:

```bash
docker run -d --rm --name saberpro-auditoria-memory \
  --network saberpro-net -p 127.0.0.1:18083:8083 \
  -e APP_REPOSITORY=memory \
  -e VALIDATION_SERVICE_URL=http://validation-service:8082 \
  -e SPRING_RABBITMQ_HOST=rabbitmq \
  saberpro-taller13-question-service

QUESTION_URL=http://localhost:18083 python3 scripts/smoke_test.py \
  --label auditoria-memory --faults \
  --output docs/auditoria/evidencias/smoke-memory.json
```

Ejecución memoria:20:58:53–20:59:21 UTC. Resultado **PASS en ambas**,28 respuestas registradas por modo; además se verifican cuerpos/estados/IDs, cola durable con consumidor y que no aumente el listado cuando falla Validation. `--label` es solo una etiqueta; la selección efectiva de memoria se hizo con APP_REPOSITORY.

| Comprobación | Esperado | JPA real | Memoria real |
|---|---:|---:|---:|
| Salud del servicio :8082 | 200 | 200 | 200 |
| Salud del servicio :8083 | 200 | 200 | 200 |
| Salud del servicio :8084 | 200 | 200 | 200 |
| Listado inicial | 200 | 200 | 200 |
| Validación REST directa | 200 | 200 | 200 |
| Creación | 201 | 201 | 201 |
| Consulta individual | 200 | 200 | 200 |
| Envío a revisión | 202 | 202 | 202 |
| Consumo real Lifecycle | 200 | 200 | 200 |
| Envío duplicado | 400 | 400 | 400 |
| ID inexistente | 404 | 404 | 404 |
| UUID mal formado | 400 | 400 | 400 |
| Dato inválido: justificacion | 400 | 400 | 400 |
| Dato inválido: enunciado | 400 | 400 | 400 |
| Dato inválido: opciones | 400 | 400 | 400 |
| Borrador: Tres opciones | 201 | 201 | 201 |
| Rechazo: Tres opciones | 400 | 400 | 400 |
| Borrador: Dos claves correctas | 201 | 201 | 201 |
| Rechazo: Dos claves correctas | 400 | 400 | 400 |
| Límite técnico Validation | 400 | 400 | 400 |
| Integración síncrona del límite | 400 | 400 | 400 |
| Borrador para fallo de broker | 201 | 201 | 201 |
| Broker caído | 503 | 503 | 503 |
| Rollback tras fallo | 200 | 200 | 200 |
| Reintento tras recuperación | 202 | 202 | 202 |
| Consumo real Lifecycle | 200 | 200 | 200 |
| Validation caído | 503 | 503 | 503 |
| Validación recuperada | 201 | 201 | 201 |

Los JSON conservan cuerpos completos y URLs: [JPA](evidencias/smoke-jpa.json), [memoria](evidencias/smoke-memory.json); salida de consola: [JPA](evidencias/smoke-jpa.log), [memoria](evidencias/smoke-memory.log). Las dos respuestas de «Consumo real Lifecycle» son el flujo normal y el reintento después de recuperar el broker. Las peticiones se ejecutaron por Python/HTTP y JUnit: no se abrió la GUI de Thunder Client.

**Fallos controlados:** se detuvo y reinició únicamente RabbitMQ/Validation de este Compose. Broker caído devolvió 503 y conservó BORRADOR; recuperación devolvió 202 y generó recibo. Validation caído devolvió 503 sin insertar; recuperación permitió 201. No se borraron contenedores ajenos ni volúmenes.

## Correlación completa de publicación y consumo

| Modo | preguntaId devuelto al crear | eventId recibido por Lifecycle | processedAt UTC |
|---|---|---|---|
| smoke-jpa.json | `5bc8bafc-b9cf-4ded-a 4d3-45496f27360e` | `231ceb28-8ffa-3be3-bfbd-bc83cc449ad5` | 2026-11-16T20:53:05.887453Z |
| smoke-memory.json | `8e7a22f4-f7f9-4a4b-af29-6408af8ddaa6` | `dfc48c67-2aaa-38c9-ae74-6f3d6a713f60` | 2026-11-16T20:58:53.498553Z |

Para cada fila se comprobaron POST 201, revisión 202, GET de recibo 200 y log de QuestionReviewListener con los mismos preguntaId/eventId. [estado-runtime.json](evidencias/estado-runtime.json) conserva recibos y líneas exactas; los logs completos están enlazados arriba. La API autenticada de Management devolvió la cola `pregunta.revisar.queue`, durable y con consumidor; los smoke guardaron contadores publish/deliver/ack. Un contador es agregado, no un identificador de evento: la correlación individual se acredita mediante recibo y log.

`GET http://localhost:15672` devolvió200 y HTML de RabbitMQ; `/api/queues/%2F/pregunta.revisar.queue` respondió correctamente con guest/guest. La disponibilidad del Dashboard está comprobada por HTTP; no se capturó ni inspeccionó visualmente su interfaz en navegador. Cero mensajes en cola tras consumir no significa que no se publicó. Las estadísticas pueden reiniciarse al reiniciar el broker.

## Contraejemplo adicional HALL-01: resultado desfavorable conservado

Se compiló una sonda separada contra el dominio y se ejecutó con Java del host. La compilación usa release17; el comportamiento además se reprodujo por HTTP en la imagen Java 17. Comandos equivalentes para reproducir la sonda ejecutada, con fuentes/temporal propios de la auditoría:

```bash
javac --release 17 -d /tmp/t13-audit-domain/classes \
  $(find question-service/src/main/java/co/edu/unicauca/saberpro/question/domain -name "*.java") \
  /tmp/t13-audit-domain/DomainBoundaryProbe.java
java -cp /tmp/t13-audit-domain/classes DomainBoundaryProbe
```

La primera operación compila; la segunda termina con **exit 1 / AssertionError**. Salida real:

```text
Enunciado normalizado: longitud=0
Opcion normalizada: longitud=0
Estado=PENDIENTE_REVISION
java.lang.AssertionError: Se aceptó texto vacío tras normalización
```

El reproductor HTTP adicional guardado en [http_probe.py](evidencias/http_probe.py) aplicó el mismo cuerpo de ejemplo con `"\u0000\t "` a enunciado o a una opción. Se ejecutaron cuatro combinaciones de campo/repositorio. JPA respondió 400 en ambas; memoria respondió 201 en ambas, permitió 202 y Lifecycle devolvió200. Son **dos contraejemplos de equivalencia**, fuera de la suite Maven. [Respuesta completa](evidencias/http-normalizacion.json), [resumen de consola](evidencias/http-normalizacion.log), [fuente Java](evidencias/DomainBoundaryProbe.java).

El primer intento del helper HTTP necesitó manejar correctamente el cuerpo vacío de un 404 temporal de Lifecycle; se ajustó únicamente el helper de auditoría y se repitió. No fue un error de la aplicación: antes del consumo asíncrono ese404 es esperable. El helper conservado usa rutas locales y escribe en su carpeta fija; para otra ejecución debe copiarse/adaptarse a una salida nueva y recrear el contenedor temporal, sin sobrescribir la evidencia registrada aquí.

La suite Maven no cubría la interacción entre isBlank y trim para caracteres de control. Por ello sus 55 aprobaciones no contradicen el defecto. La explicación y corrección propuesta están en [HALL-01](ERRORES_Y_PENDIENTES.md).

## Diagramas, imágenes y enlaces

- Se inspeccionaron visualmente los 11 PNG; todos coinciden con Git T12; dos archivos son el mismo pantallazo. [Metadatos/hashes](evidencias/imagenes.json) y [evaluación individual](EXPLICACION_IMAGENES.md).
- Se extrajeron10 bloques Mermaid y se validaron con Mermaid 11.17.2: todos parsearon. Se usó un contenedor Node 22 con mermaid y jsdom instalados solo en un temporal, sin instalación global. [Parser ejecutado](evidencias/mermaid-check.mjs), [fuentes analizadas](evidencias/mermaid-fuentes.json), [resultado](evidencias/mermaid-parse.json) y [log](evidencias/mermaid-parse.log). Para reproducir el parser, copiar mermaid-fuentes.json como diagrams.json en un directorio nuevo y ejecutar allí mermaid-check.mjs con esas dependencias; no reutilizar la carpeta de evidencia como salida. No se renderizaron nuevos PNG/SVG ni se verificó su composición en Word.
- Se comprobaron destinos de enlaces locales Markdown.15 fallan en el README histórico; las referencias actuales inspeccionadas existen. [Resultados](evidencias/enlaces-locales.json). No se certifica disponibilidad de todos los enlaces externos.

## Evidencias anteriores consultadas, sin presentarlas como reejecuciones

[persistencia-reinicio.json](../evidencias/persistencia-reinicio.json) registra una comprobación previa de conservación de preguntas al restaurar JPA, recibos tras reiniciar Lifecycle y deduplicación tras el reinicio. Fue inspeccionada y preservada; en esta auditoría se volvieron a comprobar persistencia por API, transacciones, deduplicación de integración y recuperación de broker, pero no se repitió aquella secuencia exacta de recreación de Question/reinicio de Lifecycle.

[openjdk-manifest.txt](../evidencias/openjdk-manifest.txt) conserva la respuesta previa no such manifest que justificó la imagen alternativa autorizada por U. La imagen Temurin17 sí se comprobó ejecutándose en esta auditoría. [taller12-baseline-tests.log](../evidencias/taller12-baseline-tests.log) contiene23 pruebas anteriores a la migración, no una ejecución nueva de la base en esta fase.

## Cierre y límites de lo verificado

Se guardaron logs del temporal memory y se ejecutó `docker stop saberpro-auditoria-memory`; al haberse creado con --rm, solo ese contenedor propio se retiró. [Constancia](evidencias/limpieza-contenedor-auditoria.txt). Los cuatro servicios principales permanecieron healthy y Question siguió en JPA. Los recibos generados por pruebas permanecen en Lifecycle; las preguntas del HashMap temporal se pierden al retirarlo, por diseño.

No se verificaron la importación GUI en Thunder Client, el aspecto final de Mermaid exportado, el documento institucional Word, capturas actuales, publicación/entrega remota, Windows ni ejecución en todas las máquinas de un clon externo. Tampoco se inyectó un crash en la ventana exacta ACK/commit. Cada límite se distingue de las funcionalidades que sí pasaron con evidencia real.

La comparación SHA-256 de cierre confirmó que los **145 archivos entregables preexistentes permanecen idénticos** al inicio de la auditoría, incluidas las fuentes, pruebas y evidencias anteriores. [integridad-final.json](evidencias/integridad-final.json). Los cambios nuevos de documentación y sondas están bajo docs/auditoria; target se regeneró por los comandos Maven solicitados.
