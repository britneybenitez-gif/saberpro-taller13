package co.edu.unicauca.saberpro.question.infrastructure.jpa;

import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import org.springframework.transaction.annotation.Transactional;

/** Persistencia y límite transaccional local; el dominio permanece independiente. */
public class JpaQuestionAdapter implements PreguntaRepositoryPort {
    private final SpringDataPreguntaRepository repositorio;

    public JpaQuestionAdapter(SpringDataPreguntaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    @Transactional
    public Pregunta save(Pregunta pregunta) {
        return QuestionDomainMapper.toDomain(repositorio.saveAndFlush(QuestionDomainMapper.toRecord(pregunta)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pregunta> findById(UUID id) {
        return repositorio.findById(id).map(QuestionDomainMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pregunta> findAll() {
        return repositorio.findAll().stream().map(QuestionDomainMapper::toDomain)
                .sorted(java.util.Comparator.comparing(p -> p.getId().toString())).toList();
    }

    @Override
    @Transactional
    public Pregunta actualizar(UUID id, Consumer<Pregunta> cambio, Consumer<Pregunta> despuesDeGuardar) {
        Pregunta pregunta = repositorio.findForUpdate(id).map(QuestionDomainMapper::toDomain)
                .orElseThrow(() -> new NoSuchElementException("Pregunta no encontrada: " + id));
        cambio.accept(pregunta);
        repositorio.saveAndFlush(QuestionDomainMapper.toRecord(pregunta));
        despuesDeGuardar.accept(pregunta); // Un fallo revierte la transacción y libera el bloqueo.
        return pregunta;
    }
}
