# Diagramas de arquitectura - Taller 12

## Arquitectura hexagonal

```mermaid
flowchart TB
    cliente[Cliente HTTP / Postman] --> controller[PreguntaController - REST]
    subgraph nucleo[Hexagono: dominio y aplicacion]
      inbound[CrearPreguntaUseCase - puerto entrada]
      service[CrearPreguntaService - orquestacion]
      agregado[Pregunta - agregado]
      vo[Enunciado / Justificacion / OpcionRespuesta]
      repo[PreguntaRepositoryPort - salida]
      notificacion[NotificacionRevisionPort - salida]
      inbound -. implementado por .-> service
      service --> agregado
      agregado --> vo
      service --> repo
      service --> notificacion
    end
    controller --> inbound
    jpa[JpaQuestionAdapter] -. implementa .-> repo
    memoria[InMemoryQuestionAdapter] -. implementa .-> repo
    registro[LoggingNotificacionAdapter] -. implementa .-> notificacion
    jpa --> h2[(H2)]
    memoria --> hash[(HashMap)]
    registro --> log[(Log de aplicacion)]
```

## Secuencia de enviar a revision

```mermaid
sequenceDiagram
    actor Cliente
    participant REST as PreguntaController
    participant UC as CrearPreguntaUseCase
    participant S as CrearPreguntaService
    participant P as Pregunta
    participant R as PreguntaRepositoryPort
    participant N as NotificacionRevisionPort
    Cliente->>REST: POST /api/preguntas/{id}/enviar-revision
    REST->>UC: enviarRevision(id)
    UC->>S: enviarRevision(id)
    S->>R: findById(id)
    R-->>S: Pregunta en BORRADOR
    S->>P: enviarRevision()
    P->>P: validar 4 opciones y 1 correcta
    alt valida
        P-->>S: PENDIENTE_REVISION
        S->>R: save(pregunta)
        R-->>S: Pregunta guardada
        S->>N: notificarPreguntaPendiente(pregunta)
        S-->>REST: Pregunta guardada
        REST-->>Cliente: HTTP 200 JSON
    else invalida
        P-->>S: ReglaNegocioException
        REST-->>Cliente: HTTP 400 {error}
    end
```

## Modelo de dominio

```mermaid
classDiagram
    class Pregunta {
      -UUID id
      -Enunciado enunciado
      -Justificacion justificacion
      -List~OpcionRespuesta~ opciones
      -EstadoPregunta estado
      +crear()
      +enviarRevision()
      -validarInvariantes()
    }
    class Enunciado { +String texto }
    class Justificacion { +String texto }
    class OpcionRespuesta { +String texto +boolean correcta }
    class EstadoPregunta { <<enumeration>> BORRADOR PENDIENTE_REVISION }
    Pregunta *-- Enunciado
    Pregunta *-- Justificacion
    Pregunta *-- "0..*" OpcionRespuesta
    Pregunta --> EstadoPregunta
```

La multiplicidad `0..*` en BORRADOR es deliberada: la guia exige validar exactamente 4 al enviar a revision, no al crear el borrador.
