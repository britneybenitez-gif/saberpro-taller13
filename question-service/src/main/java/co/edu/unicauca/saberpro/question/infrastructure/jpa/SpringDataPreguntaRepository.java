package co.edu.unicauca.saberpro.question.infrastructure.jpa;

import java.util.UUID;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Repositorio Spring Data: solo existe dentro de infraestructura. */
public interface SpringDataPreguntaRepository extends JpaRepository<QuestionRecord, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from QuestionRecord p where p.id = :id")
    Optional<QuestionRecord> findForUpdate(@Param("id") UUID id);
}
