# Informe técnico final — Taller 13

**Universidad del Cauca · Laboratorio de Ingeniería de Software II · 2026.2**  
**Docente:** Paola Andrea Bedoya Toro.  
**Proyecto:** Banco de Preguntas Saber PRO.  
**Fecha de verificación:** 16 de noviembre de 2026, America/Bogota.  
**Base examinada:** commit `e857f96` del repositorio `britneybenitez-gif/saberpro-hexagonal-taller12`.  
**Rama de trabajo:** `feature/taller13-fase2`.

Este documento contiene el material técnico para trasladarlo a la plantilla institucional `Formato-Documento.docx`. La plantilla no fue suministrada ni encontrada en el entorno. Los integrantes y demás datos de portada deben completarse con información real del equipo; no se inventaron nombres.

## 1. Problema y objetivo

El Taller 12 implementaba un módulo Spring Boot para crear borradores, consultar por ID y enviar preguntas a revisión. Encapsulaba las invariantes en un dominio Java puro y permitía seleccionar JPA/H2 o HashMap. La notificación era únicamente un log, la aplicación escuchaba en el puerto 8080 y no había listado de preguntas, contenedores ni otros microservicios.

El objetivo del Taller 13 fue convertir esa base en un ecosistema verificable de tres servicios, con REST síncrono y AMQP asíncrono, conservando el aislamiento del dominio y las reglas existentes. Los documentos institucionales se copiaron sin cambios a `docs/guias/`; sus hashes están en las evidencias.

## 2. Auditoría de la base y preservación

Se revisaron recursivamente el código, POM, README, pruebas, colección HTTP, documentación y configuración. Git estaba limpio en una rama de funcionalidad, fuera de main. Antes de migrar se ejecutaron las 23 pruebas originales: todas pasaron.

Se preservaron `Pregunta`, `EstadoPregunta`, `ReglaNegocioException` y los tres objetos de valor, cambiando sus paquetes a la estructura oficial. Los 14 tests del dominio conservaron sus comprobaciones. Se migraron los tres tests de intercambiabilidad y los seis tests HTTP, actualizando estos últimos para el nuevo contrato 202 y dependencias reales. La documentación y las capturas históricas permanecen identificadas como Taller 12.

El servicio de aplicación pasó de `CrearPreguntaService` a `QuestionApplicationService`. El controlador pasó de `PreguntaController` a `QuestionRestController`. La persistencia se separó en entidad `QuestionRecord`, mapper y adaptador, y el adaptador de log fue sustituido por un publicador real.

## 3. Diseño de dominio y arquitectura

La raíz `Pregunta` conserva la identidad UUID, el estado y una colección inmutable de opciones. Enunciado exige texto no vacío; Justificacion exige al menos 15 caracteres tras normalización. La creación produce BORRADOR; la revisión exige cuatro opciones, una correcta y estado previo BORRADOR. La rehidratación también protege las invariantes del estado pendiente.

Los puertos de entrada son `CrearPreguntaUseCase` y `EvaluarPreguntaUseCase`. Los de salida son `PreguntaRepositoryPort`, `NotificacionRevisionPort` y `ValidacionContenidoPort`. El servicio de aplicación coordina exclusivamente contratos; se configura mediante beans fuera del núcleo. `scripts/check-domain.sh` compila dominio y aplicación sin frameworks.

La implementación completa y las responsabilidades están en [arquitectura](arquitectura/arquitectura.md). Los [C4 de niveles 1, 2, 3 y 4](arquitectura/diagramas.md) muestran contexto, contenedores, componentes y código real. Ese archivo también contiene las secuencias de creación y revisión.

## 4. Flujo síncrono y asíncrono

Question recibe JSON y valida su estructura, construye el agregado, llama por REST a Validation y persiste. Validation comprueba límites técnicos acordes con las columnas originales; no sustituye las invariantes DDD. La creación responde 201 y devuelve el agregado con UUID. La consulta de colección responde 200, incluida la lista vacía.

El envío a revisión cambia el estado, persiste provisionalmente y publica JSON persistente a `pregunta.revisar.queue`. El publicador espera confirmación positiva del broker y comprueba que no hubo devolución por falta de ruta. Solo entonces termina la transacción local y se devuelve 202. Ante fallo se revierte a BORRADOR y se responde 503.

Lifecycle consume con `@RabbitListener`, valida el contrato, guarda un comprobante y registra el ID de la pregunta. El identificador estable del evento y la persistencia del comprobante permiten deduplicar redeliveries. Los errores definitivos se envían a una DLQ. `/api/revisiones/{preguntaId}` permite comprobar el procesamiento real.

Se eligió una solución sin Outbox para esta entrega: persistencia y broker no comparten una transacción. Se documentan las ventanas de timeout o caída entre ACK y commit en [decisiones](arquitectura/decisiones.md), sin atribuir atomicidad distribuida ni entrega exactamente una vez.

## 5. Contenerización y persistencia

Cada microservicio dispone de POM autónomo y Dockerfile. La construcción Maven con Java 17 ocurre dentro de la imagen; el contenedor final ejecuta únicamente el JAR con usuario 10001. Se sustituyó la imagen `openjdk:17-jdk-slim` porque el registro respondió `no such manifest`; Temurin 17 fue descargada y ejecutada correctamente.

Compose levanta Validation :8082, Question :8083, Lifecycle :8084 y RabbitMQ :5672/:15672. La red bridge se llama `saberpro-net`. Las aplicaciones se comunican por nombres de servicio Docker. Healthchecks y dependencias de disponibilidad controlan el arranque.

H2 en archivo usa volúmenes separados para preguntas y comprobantes; RabbitMQ también tiene volumen. La alternativa `APP_REPOSITORY=memory` cambia el adaptador sin modificar el dominio. La recreación en JPA conservó preguntas, y el reinicio de Lifecycle conservó comprobantes y deduplicación.

## 6. Verificación realizada

| Ejecución | Resultado real |
|---|---|
| Base Taller 12 | 23 pruebas, 0 fallos/errores/omitidas |
| Suite habitual final | 32 pruebas, 0 fallos/errores/omitidas |
| Perfil de integración | 23 pruebas adicionales, 0 fallos/errores/omitidas |
| Total con Java 17 / Maven 3.9.9 | **55 pruebas aprobadas**, BUILD SUCCESS |
| Dominio y aplicación sin classpath externo | Compilación `javac --release 17` correcta |
| Compose config, build y arranque | Cuatro contenedores saludables |
| HTTP + AMQP en JPA | 28 comprobaciones HTTP aprobadas, Management y consumidor real |
| HTTP + AMQP en HashMap | Las mismas 28 comprobaciones aprobadas |
| Fallo de broker en ambos modos | 503, BORRADOR conservado, recuperación y consumo posterior |
| Fallo de Validation en ambos modos | 503 sin insertar, recuperación posterior 201 |
| Mensaje inválido y duplicado | DLQ real y deduplicación verificadas por integración |
| Recreación/reinicio | Persistencia JPA y deduplicación de Lifecycle conservadas |

Los logs, resúmenes JUnit, respuestas JSON, estado Docker y prueba de conectividad están en [evidencias](evidencias/README.md). Las peticiones equivalentes para Thunder Client están preparadas; no se afirma haber ejecutado su interfaz gráfica.

## 7. Trazabilidad y evaluación

La [matriz](arquitectura/trazabilidad.md) vincula cada requisito con páginas de las guías, archivos y comprobaciones. Incluye los cortes I (25%), II (35%) y III (40%), diferenciando el recorrido institucional del código histórico realmente disponible. No se atribuyen implementaciones de Microkernel, Builder o State a una base donde no se encontraron.

Los componentes de arquitectura (35%), Docker/Compose (30%) y flujo integrador (20%) cuentan con implementación y pruebas ejecutadas. Para informe y C4 (15%) existen el contenido técnico, diagramas editables y evidencias textuales; resta acomodar el documento a la plantilla oficial y añadir capturas manuales. La valoración académica final corresponde a la docente.

## 8. Entrega y reproducción

```bash
docker compose up -d --build --wait
./mvnw -Pintegration verify
python3 scripts/smoke_test.py --label jpa
```

El [README principal](../README.md) incluye endpoints, JSON, cambio de repositorios, ejecución local, compilación y solución de errores. El [inventario](INVENTARIO_CAMBIOS.md) detalla los archivos modificados y creados. La rama queda disponible localmente para revisión; no se hizo commit, push ni merge.

Para completar el envío institucional: completar portada real, trasladar este contenido a `Formato-Documento.docx`, exportar los Mermaid si se requieren imágenes, capturar los pantallazos indicados y publicar/entregar después de la revisión del propietario.
