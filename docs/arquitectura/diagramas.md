# Diagramas de la implementación — C4 y secuencia
# Diagramas de la implementación — C4 y secuencia

Los bloques Mermaid son fuentes editables para renderizar y exportar a SVG/PNG. Los nodos describen componentes que existen en el repositorio. El Nivel 4 se concentra en el agregado y los puertos de Question. Estos diagramas no son capturas de ejecución.

## C4 Nivel 1 — Contexto del sistema

```mermaid
flowchart LR
    autor["Docente / autor de preguntas<br/>Persona"]
    operador["Estudiante / operador<br/>Persona"]
    sistema["Banco de Preguntas Saber PRO<br/>Sistema: crea borradores y solicita revisión"]
    autor -->|"Crear, consultar y enviar mediante HTTP"| sistema
    operador -->|"Desplegar y verificar procesamiento"| sistema
```

No se representan autenticación, correos ni un evaluador humano automatizado porque no están implementados.

## C4 Nivel 2 — Contenedores

```mermaid
flowchart TB
    autor["Autor / Thunder Client"]
    operador["Operador"]
    subgraph sistema["Banco de Preguntas Saber PRO · red bridge saberpro-net"]
      q["question-service :8083<br/>Spring Boot / Java 17<br/>Agregado y casos de uso"]
      v["validation-service :8082<br/>Spring Boot / Bean Validation<br/>Límites técnicos del contenido"]
      r["RabbitMQ :5672 / Management :15672<br/>Cola pregunta.revisar.queue + DLQ"]
      l["lifecycle-service :8084<br/>Spring Boot / RabbitListener<br/>Procesamiento idempotente"]
      qdb[("H2 preguntas<br/>volumen question-data")]
      ldb[("H2 comprobantes<br/>volumen lifecycle-data")]
      mem[("HashMap alternativo<br/>dentro de Question")]
      rvol[("Volumen rabbitmq-data")]
      q -->|"REST JSON / validar"| v
      q -->|"JPA si app.repository=jpa"| qdb
      q -->|"Si app.repository=memory"| mem
      q -->|"AMQP JSON persistente + confirm"| r
      r -->|"Entrega / ACK"| l
      r --> rvol
      l -->|JPA| ldb
    end
    autor -->|HTTP| q
    autor -->|"Consultar comprobantes"| l
    operador -->|"Inspección web"| r
```

H2 es embebido en cada JVM, representado como almacenamiento, no como un contenedor Docker independiente. Hay cuatro contenedores Docker: tres aplicaciones y RabbitMQ.

## C4 Nivel 3 — Componentes de Question Service

```mermaid
flowchart TB
    http["Cliente HTTP"] --> api["QuestionRestController<br/>Adaptador de entrada"]
    api --> create["CrearPreguntaUseCase"]
    api --> review["EvaluarPreguntaUseCase"]
    subgraph core["Dominio y aplicación · Java puro"]
      create
      review
      app["QuestionApplicationService"]
      model["Pregunta / EstadoPregunta / Value Objects"]
      repo["PreguntaRepositoryPort"]
      notify["NotificacionRevisionPort"]
      validate["ValidacionContenidoPort"]
      app -.->|implementa| create
      app -.->|implementa| review
      app --> model
      app --> repo
      app --> notify
      app --> validate
    end
    jpa["JpaQuestionAdapter<br/>QuestionDomainMapper / QuestionRecord"] -.->|implementa| repo
    memory["InMemoryQuestionAdapter<br/>HashMap / copias / monitor"] -.->|implementa| repo
    rabbit["RabbitMQPublisher<br/>JSON / confirm / return"] -.->|implementa| notify
    rest["RestValidationAdapter<br/>RestClient / timeout"] -.->|implementa| validate
    config["HexagonalConfiguration<br/>Beans según app.repository"] --> app
    config --> jpa
    config --> memory
    jpa --> db[("H2")]
    rabbit --> broker["RabbitMQ"]
    rest --> validation["Validation Service"]
```

Las flechas punteadas indican implementación de contratos; los adaptadores dependen de los puertos del núcleo.

## C4 Nivel 4 — Código del agregado y casos de uso

```mermaid
classDiagram
    class Pregunta {
      -UUID id
      -Enunciado enunciado
      -Justificacion justificacion
      -List~OpcionRespuesta~ opciones
      -EstadoPregunta estado
      +crear(Enunciado, Justificacion, List) Pregunta
      +enviarRevision() void
      -validarInvariantes() void
    }
    class Enunciado {
      <<record>>
      +String texto
    }
    class Justificacion {
      <<record>>
      +String texto
    }
    class OpcionRespuesta {
      <<record>>
      +String texto
      +boolean correcta
    }
    class EstadoPregunta {
      <<enumeration>>
      BORRADOR
      PENDIENTE_REVISION
    }
    class ReglaNegocioException
    class CrearPreguntaUseCase {
      <<interface>>
      +crear(String, String, List) Pregunta
      +buscar(UUID) Pregunta
      +listar() List
    }
    class EvaluarPreguntaUseCase {
      <<interface>>
      +enviarRevision(UUID) Pregunta
    }
    class PreguntaRepositoryPort {
      <<interface>>
      +save(Pregunta) Pregunta
      +findById(UUID) Optional
      +findAll() List
      +actualizar(UUID, Consumer, Consumer) Pregunta
    }
    class NotificacionRevisionPort {
      <<interface>>
      +notificarPreguntaPendiente(Pregunta) void
    }
    class ValidacionContenidoPort {
      <<interface>>
      +validar(Pregunta) void
    }
    class QuestionApplicationService
    Pregunta *-- Enunciado
    Pregunta *-- Justificacion
    Pregunta *-- "0..* en borrador" OpcionRespuesta
    Pregunta --> EstadoPregunta
    Pregunta ..> ReglaNegocioException
    Enunciado ..> ReglaNegocioException
    Justificacion ..> ReglaNegocioException
    OpcionRespuesta ..> ReglaNegocioException
    QuestionApplicationService ..|> CrearPreguntaUseCase
    QuestionApplicationService ..|> EvaluarPreguntaUseCase
    QuestionApplicationService --> PreguntaRepositoryPort
    QuestionApplicationService --> NotificacionRevisionPort
    QuestionApplicationService --> ValidacionContenidoPort
    QuestionApplicationService --> Pregunta
```

La multiplicidad permite borradores; el método de revisión exige exactamente cuatro opciones y una correcta. No se representa GoF State: el código utiliza un enum y un método del agregado.

## Secuencia REST síncrona de creación

```mermaid
sequenceDiagram
    actor Cliente
    participant API as QuestionRestController
    participant S as QuestionApplicationService
    participant P as Pregunta / Value Objects
    participant V as Validation Service
    participant R as Repositorio seleccionado
    Cliente->>API: POST /api/preguntas (JSON)
    API->>S: crear(textos, opciones)
    S->>P: crear (valida objetos de valor)
    P-->>S: BORRADOR
    S->>V: REST POST /api/validaciones/preguntas
    alt estructura y límites válidos
      V-->>S: 200 valida=true
      S->>R: save(pregunta)
      R-->>S: agregado guardado
      S-->>API: Pregunta
      API-->>Cliente: 201 + Location + JSON
    else contenido inválido
      V-->>S: 400
      S-->>API: ReglaNegocioException
      API-->>Cliente: 400
    else dependencia no disponible
      V--xS: timeout / fallo de conexión / 5xx
      S-->>API: DependenciaNoDisponibleException
      API-->>Cliente: 503 sin persistir
    end
```

## Secuencia de revisión y RabbitMQ

```mermaid
sequenceDiagram
    actor Cliente
    participant API as QuestionRestController
    participant S as QuestionApplicationService
    participant R as JpaQuestionAdapter / InMemoryQuestionAdapter
    participant P as Pregunta
    participant N as RabbitMQPublisher
    participant B as RabbitMQ
    participant L as QuestionReviewListener
    participant C as H2 comprobantes
    Cliente->>API: POST /api/preguntas/id/enviar-revision
    API->>S: EvaluarPreguntaUseCase.enviarRevision(id)
    S->>R: actualizar(id, cambio, notificación)
    R->>R: comenzar transacción/bloqueo y buscar
    R->>P: enviarRevision()
    P->>P: validar BORRADOR, 4 opciones, 1 correcta
    alt violación de invariantes
      P-->>R: ReglaNegocioException
      R-->>S: revertir / liberar bloqueo
      S-->>API: error de negocio
      API-->>Cliente: 400
    else transición válida
      R->>R: guardar / flush PENDIENTE_REVISION
      R->>N: notificarPreguntaPendiente(pregunta)
      N->>B: JSON persistente, mandatory
      alt ACK y sin basic.return
        B-->>N: confirm ACK
        N-->>R: publicación confirmada
        R->>R: commit / liberar bloqueo
        R-->>S: Pregunta
        S-->>API: Pregunta
        API-->>Cliente: 202 Accepted
      else fallo, NACK, return o timeout
        N-->>R: DependenciaNoDisponibleException
        R->>R: rollback / restaurar BORRADOR
        R-->>S: error
        S-->>API: error de dependencia
        API-->>Cliente: 503
      end
    end
    opt mensaje aceptado por RabbitMQ
      B->>L: entrega asíncrona JSON
      L->>L: validar contrato v1
      L->>C: guardar eventId si no existe
      C-->>L: commit o evento ya procesado
      L->>L: registrar preguntaId en log
      L-->>B: ACK
    end
```

El consumidor puede procesar antes del commit o antes de la respuesta HTTP: el orden de entrega asíncrona no está sincronizado con la transacción de Question. La secuencia agrupa ese tramo para facilitar su lectura; ver las ventanas de fallo documentadas en `decisiones.md`.
