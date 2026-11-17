package co.edu.unicauca.saberpro.lifecycle;

import co.edu.unicauca.saberpro.lifecycle.application.ReviewProcessingService;
import co.edu.unicauca.saberpro.lifecycle.messaging.QuestionReviewMessage;
import co.edu.unicauca.saberpro.lifecycle.persistence.ReviewReceiptRepository;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ReviewProcessingService.class)
class ReviewProcessingTest {
    @Autowired ReviewProcessingService service;
    @Autowired ReviewReceiptRepository repository;

    @Test void registraYDeduplicaEnH2Real() {
        var message = new QuestionReviewMessage(UUID.randomUUID(), 1, "PreguntaEnviadaARevision",
                UUID.randomUUID(), "PENDIENTE_REVISION", Instant.now());
        assertTrue(service.registrar(message));
        assertFalse(service.registrar(message));
        assertEquals(1, repository.count());
        assertEquals(message.preguntaId(), repository.findById(message.eventId()).orElseThrow().getPreguntaId());
    }

    @Test void rechazaContratoInvalidoSinPersistir() {
        var message = new QuestionReviewMessage(UUID.randomUUID(), 2, "PreguntaEnviadaARevision",
                UUID.randomUUID(), "PENDIENTE_REVISION", Instant.now());
        assertThrows(IllegalArgumentException.class, () -> service.registrar(message));
        assertEquals(0, repository.count());
    }
}
