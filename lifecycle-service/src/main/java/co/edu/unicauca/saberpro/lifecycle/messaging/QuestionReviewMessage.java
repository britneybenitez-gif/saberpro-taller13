package co.edu.unicauca.saberpro.lifecycle.messaging;

import java.time.Instant;
import java.util.UUID;

/** DTO del contrato JSON, sin dependencia de las clases del productor. */
public record QuestionReviewMessage(UUID eventId, int schemaVersion, String type,
                                    UUID preguntaId, String estado, Instant occurredAt) {
    public void validate() {
        if (eventId == null || preguntaId == null || occurredAt == null || schemaVersion != 1
                || !"PreguntaEnviadaARevision".equals(type) || !"PENDIENTE_REVISION".equals(estado)) {
            throw new IllegalArgumentException("Evento de revisión inválido o versión no soportada");
        }
    }
}
