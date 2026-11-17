package co.edu.unicauca.saberpro.lifecycle.application;

import co.edu.unicauca.saberpro.lifecycle.messaging.QuestionReviewMessage;
import co.edu.unicauca.saberpro.lifecycle.persistence.ReviewReceipt;
import co.edu.unicauca.saberpro.lifecycle.persistence.ReviewReceiptRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewProcessingService {
    private final ReviewReceiptRepository repository;
    public ReviewProcessingService(ReviewReceiptRepository repository) { this.repository = repository; }

    @Transactional
    public boolean registrar(QuestionReviewMessage event) {
        event.validate();
        if (repository.existsById(event.eventId())) return false;
        repository.saveAndFlush(new ReviewReceipt(event.eventId(), event.preguntaId(), event.estado(), Instant.now()));
        return true;
    }
}
