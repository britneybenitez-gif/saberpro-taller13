package co.edu.unicauca.saberpro.lifecycle.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "revisiones_recibidas")
public class ReviewReceipt {
    @Id private UUID eventId;
    @Column(nullable = false, unique = true) private UUID preguntaId;
    @Column(nullable = false) private String estado;
    @Column(nullable = false) private Instant processedAt;

    protected ReviewReceipt() {}

    public ReviewReceipt(UUID eventId, UUID preguntaId, String estado, Instant processedAt) {
        this.eventId = eventId;
        this.preguntaId = preguntaId;
        this.estado = estado;
        this.processedAt = processedAt;
    }

    public UUID getEventId() { return eventId; }
    public UUID getPreguntaId() { return preguntaId; }
    public String getEstado() { return estado; }
    public Instant getProcessedAt() { return processedAt; }
}
