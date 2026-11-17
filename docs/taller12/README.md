
# Banco de Preguntas Saber PRO

**Taller 12 — Domain-Driven Design (DDD) y Arquitectura Hexagonal · Fase I**

**Universidad del Cauca** · Laboratorio de Ingeniería de Software II · Periodo 2026.2  
**Docente:** Paola Andrea Bedoya Toro

> Implementación de un módulo para crear, consultar y enviar a revisión preguntas Saber PRO, con reglas de negocio independientes de los frameworks y de los mecanismos de persistencia.

**Validación realizada en Google Cloud Shell:** **23 pruebas JUnit 5 aprobadas**, 0 fallos, 0 errores y 0 omitidas (`BUILD SUCCESS`). También se verificaron manualmente las operaciones REST con JPA/H2 y con el repositorio en memoria (HashMap).

## Contenido

- [Propósito y alcance](#propósito-y-alcance)
- [Tecnologías](#tecnologías)
- [Arquitectura hexagonal](#arquitectura-hexagonal)
- [Modelo DDD y reglas de negocio](#modelo-ddd-y-reglas-de-negocio)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Instalación y ejecución](#instalación-y-ejecución)
- [API REST](#api-rest)
- [Intercambiabilidad JPA / memoria](#intercambiabilidad-jpa--memoria)
- [Pruebas automatizadas](#pruebas-automatizadas)
- [Diagramas y evidencias](#diagramas-y-evidencias)

## Propósito y alcance

Este proyecto desarrolla el avance obligatorio del **Banco de Preguntas Saber PRO**, aplicando patrones tácticos DDD y arquitectura de puertos y adaptadores. **GymClass es únicamente el proyecto de referencia arquitectónica**; no es la aplicación entregada.

El módulo permite crear borradores, consultar preguntas y enviarlas a revisión. No representa todavía una plataforma completa de simulacros o calificación de exámenes.

## Tecnologías

| Tecnología | Función |
|---|---|
| Java 17 | Modelo del dominio y lógica de negocio |
| Spring Boot 3.4.5 | API REST y configuración de componentes |
| Maven | Compilación y ejecución de pruebas |
| Spring Data JPA | Persistencia relacional |
| H2 Database | Base de datos en memoria para JPA |
| HashMap | Repositorio alternativo en memoria |
| JUnit 5 / Spring Boot Test | Pruebas unitarias e integración |
| Postman / cURL | Pruebas HTTP |

## Arquitectura hexagonal

La implementación separa tres responsabilidades:

- **Dominio:** entidades, objetos de valor, invariantes y puertos, sin importaciones de Spring/JPA.
- **Aplicación:** casos de uso que coordinan dominio, persistencia y notificación.
- **Infraestructura:** controladores REST, adaptadores de repositorio, notificaciones y configuración de Spring.

![Arquitectura hexagonal](docs/figurasYEvidencias/arquitectura.png)

| Componente | Responsabilidad |
|---|---|
| `CrearPreguntaUseCase` | Puerto de entrada: `crear`, `buscar`, `enviarRevision` |
| `CrearPreguntaService` | Implementación y orquestación de casos de uso |
| `Pregunta` | Raíz del agregado; controla el estado y las invariantes |
| `PreguntaRepositoryPort` | Puerto de salida: `save`, `findById` |
| `NotificacionRevisionPort` | Puerto de salida para avisos al enviar a revisión |
| `PreguntaController` | Adaptador HTTP de entrada |
| `JpaQuestionAdapter` | Persistencia con Spring Data JPA y H2 |
| `InMemoryQuestionAdapter` | Persistencia alternativa mediante HashMap |
| `LoggingNotificacionAdapter` | Registra una notificación en el log (no envía correos) |
| `HexagonalConfiguration` | Selecciona e inyecta las implementaciones de los puertos |

## Modelo DDD y reglas de negocio

![Modelo del agregado Pregunta](docs/figurasYEvidencias/modelo_ddd.png)

La clase **`Pregunta`** es la raíz del agregado. Contiene un identificador UUID, `Enunciado`, `Justificacion`, una lista de `OpcionRespuesta` y el estado `EstadoPregunta`. Los objetos de valor se implementan como `record` inmutables de Java.

Reglas aplicadas:

1. El enunciado no puede ser nulo ni vacío.
2. La justificación requiere **al menos 15 caracteres**.
3. Toda pregunta inicia en estado **`BORRADOR`**.
4. Un borrador puede tener opciones incompletas: **antes de pasar a `PENDIENTE_REVISION` deben existir exactamente cuatro opciones y solo una correcta**.
5. Una pregunta ya enviada no puede enviarse nuevamente a revisión.
6. Las violaciones de reglas de negocio producen `ReglaNegocioException`, transformada por la API en **HTTP 400**.

## Estructura del proyecto

```text
saberpro-hexagonal/
├── pom.xml
├── README.md
├── ejemplo-pregunta.json
├── postman/
│   └── SaberPRO_Taller12.postman_collection.json
├── docs/
│   ├── diagramas.md
│   └── figurasYEvidencias/
│       ├── arquitectura.png
│       ├── flujo_revision.png
│       ├── modelo_ddd.png
│       └── ... capturas de pruebas reales
└── src/
    ├── main/
    │   ├── java/co/edu/unicauca/saberpro/
    │   │   ├── domain/
    │   │   │   ├── model/
    │   │   │   ├── valueobject/
    │   │   │   ├── exception/
    │   │   │   └── ports/
    │   │   ├── application/
    │   │   └── infrastructure/
    │   │       ├── adapters/in/web/
    │   │       ├── adapters/out/jpa/
    │   │       ├── adapters/out/memory/
    │   │       ├── adapters/out/notification/
    │   │       └── config/
    │   └── resources/application.properties
    └── test/java/co/edu/unicauca/saberpro/
        ├── domain/PreguntaTest.java
        ├── application/IntercambiabilidadTest.java
        └── infrastructure/
            ├── ApiJpaIntegrationTest.java
            └── ApiMemoryIntegrationTest.java
```

## Instalación y ejecución

**Requisitos:** JDK 17 o superior, Maven 3.9+ y acceso a Maven Central para descargar dependencias.

```bash
git clone https://github.com/britneybenitez-gif/saberpro-hexagonal-taller12.git
cd saberpro-hexagonal-taller12
mvn clean test
mvn spring-boot:run
```

Por defecto, `src/main/resources/application.properties` usa `app.repository=jpa`.

- **API:** `http://localhost:8080/api/preguntas`
- **H2 Console:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:saberprodb`
- **Usuario H2:** `sa` — contraseña vacía.

> Este proyecto es una API REST, **no una interfaz web**. Abrir `/` puede devolver 404 porque no existe un controlador para esa ruta. `GET /api/preguntas` devuelve 405; la creación utiliza POST.

En **Google Cloud Shell**, se ejecutan los mismos comandos desde la carpeta que contiene `pom.xml`. En otra pestaña de terminal se pueden enviar peticiones con `curl` a `localhost:8080`.

## API REST

| Método | Endpoint | Respuesta esperada |
|---|---|---|
| `POST` | `/api/preguntas` | `201 Created`, estado `BORRADOR` |
| `GET` | `/api/preguntas/{id}` | `200 OK` o `404 Not Found` |
| `POST` | `/api/preguntas/{id}/enviar-revision` | `200 OK`, estado `PENDIENTE_REVISION` |
| `POST` repetido | `/api/preguntas/{id}/enviar-revision` | `400 Bad Request` si ya está en revisión |

### Crear una pregunta

```bash
curl -i -X POST http://localhost:8080/api/preguntas \
  -H "Content-Type: application/json" \
  -d '{
    "enunciado": "¿Qué permite la arquitectura hexagonal?",
    "justificacion": "Permite cambiar adaptadores sin modificar el dominio.",
    "opciones": [
      {"texto": "Desacoplar el dominio de la infraestructura", "correcta": true},
      {"texto": "Eliminar las interfaces", "correcta": false},
      {"texto": "Depender directamente de JPA", "correcta": false},
      {"texto": "Eliminar las pruebas unitarias", "correcta": false}
    ]
  }'
```

La respuesta devuelve HTTP 201, un UUID y `"estado":"BORRADOR"`.

### Enviar a revisión

Sustituye `UUID_GENERADO` por el `id` devuelto al crear la pregunta:

```bash
curl -i -X POST http://localhost:8080/api/preguntas/UUID_GENERADO/enviar-revision
```

La respuesta debe devolver HTTP 200 y `"estado":"PENDIENTE_REVISION"`.

También puedes utilizar [el JSON de ejemplo](ejemplo-pregunta.json) o importar la [colección Postman](postman/SaberPRO_Taller12.postman_collection.json), con cinco peticiones ordenadas. La primera guarda el `id` en la variable de colección `preguntaId`.

## Intercambiabilidad JPA / memoria

La prueba principal del taller consiste en **cambiar el adaptador de salida sin modificar una sola línea de `domain/`**.

**1. Ejecutar con JPA/H2:**

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--app.repository=jpa
```

**2. Detener con `Ctrl+C` y arrancar con HashMap:**

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--app.repository=memory
```

Repite la creación y el envío a revisión: en los dos modos se esperan **201 / `BORRADOR`** y **200 / `PENDIENTE_REVISION`**.

La selección se realiza con `@ConditionalOnProperty` en `HexagonalConfiguration`. En modo `memory`, las operaciones de preguntas utilizan `InMemoryQuestionAdapter`, aunque Spring puede seguir inicializando H2/JPA porque esas dependencias están incluidas en la aplicación. **Ambos almacenamientos son temporales**, no garantizan conservación de los datos entre reinicios.

## Pruebas automatizadas

```bash
mvn clean test
```

| Clase de prueba | Cobertura |
|---|---|
| `PreguntaTest` | Reglas, estados y objetos de valor del dominio |
| `IntercambiabilidadTest` | Caso de uso y repositorio en memoria sin dependencia de Spring |
| `ApiJpaIntegrationTest` | API, inyección del adaptador JPA/H2 y errores |
| `ApiMemoryIntegrationTest` | API con HashMap, consulta, revisión y rechazos |

**Ejecución documentada en Cloud Shell:** `Tests run: 23, Failures: 0, Errors: 0, Skipped: 0` — **`BUILD SUCCESS`**.

## Diagramas y evidencias

Los tres diagramas están en [docs/diagramas.md](docs/diagramas.md) (Mermaid) y en la carpeta de figuras.

![Flujo de envío a revisión](docs/figurasYEvidencias/flujo_revision.png)

| Captura real | Qué demuestra |
|---|---|
| [Pruebas JUnit 5](docs/figurasYEvidencias/Pruebas%20JUnit%205.PNG) | 23 pruebas aprobadas y BUILD SUCCESS |
| [Crear pregunta con JPA](docs/figurasYEvidencias/Creaci%C3%B3n%20de%20una%20pregunta%20con%20JPA%20y%20H2.PNG) | HTTP 201 y BORRADOR |
| [Enviar a revisión con JPA](docs/figurasYEvidencias/Cambio%20de%20estado%20de%20una%20pregunta%20mediante%20el%20adaptador%20JPA.PNG) | HTTP 200 y PENDIENTE_REVISION |
| [Validación de invariantes](docs/figurasYEvidencias/Validaci%C3%B3n%20de%20invariantes%20del%20dominio%20mediante%20DDD..PNG) | HTTP 400 por justificación corta |
| [Activación del adaptador en memoria](docs/figurasYEvidencias/Activaci%C3%B3n%20del%20adaptador%20en%20memoria.PNG) | Inicio con el modo memory |
| [Verificación del argumento memory](docs/figurasYEvidencias/Evidencia_05_Adaptador_Memoria.PNG) | Argumento `--app.repository=memory` |
| [Crear pregunta con memoria](docs/figurasYEvidencias/Crear%20una%20pregunta%20usando%20el%20adaptador%20en%20memoria.PNG) | HTTP 201 y BORRADOR |
| [Enviar a revisión con memoria](docs/figurasYEvidencias/Env%C3%ADo%20de%20una%20pregunta%20a%20revisi%C3%B3n%20utilizando%20el%20adaptador%20en%20memoria..PNG) | HTTP 200 y PENDIENTE_REVISION |

[**Ver todas las figuras y evidencias**](docs/figurasYEvidencias/)

## Entrega académica

Este README documenta el repositorio y su uso. El **informe técnico** es un entregable independiente, preparado en la plantilla oficial `Formato-Documento.docx`, con explicaciones, diagramas y capturas. El puerto de notificaciones registra mensajes en el log; **no integra un servicio de correo o mensajería real**.

---

**Universidad del Cauca — Laboratorio de Ingeniería de Software II — 2026.2**  
**Repositorio:** https://github.com/britneybenitez-gif/saberpro-hexagonal-taller12
