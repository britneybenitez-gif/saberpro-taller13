package co.edu.unicauca.saberpro.lifecycle.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReceiptRepository extends JpaRepository<ReviewReceipt, UUID> {
    Optional<ReviewReceipt> findByPreguntaId(UUID preguntaId);
}
