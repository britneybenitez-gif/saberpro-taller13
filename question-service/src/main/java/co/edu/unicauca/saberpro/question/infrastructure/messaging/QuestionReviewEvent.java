package co.edu.unicauca.saberpro.question.infrastructure.messaging;

import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

/** Contrato JSON v1 independiente del nombre de clase del consumidor. */
public record QuestionReviewEvent(UUID eventId, int schemaVersion, String type,
                                  UUID preguntaId, String estado, Instant occurredAt) {
    public static QuestionReviewEvent from(Pregunta p) {
        UUID eventId = UUID.nameUUIDFromBytes(("revision:" + p.getId()).getBytes(StandardCharsets.UTF_8));
        return new QuestionReviewEvent(eventId, 1, "PreguntaEnviadaARevision", p.getId(),
                p.getEstado().name(), Instant.now());
    }
}
