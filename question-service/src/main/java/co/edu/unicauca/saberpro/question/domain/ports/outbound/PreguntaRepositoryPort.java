package co.edu.unicauca.saberpro.question.domain.ports.outbound;

import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.function.Consumer;

/** Puerto secundario (salida) de persistencia, independiente de JPA. */
public interface PreguntaRepositoryPort {
    Pregunta save(Pregunta pregunta);
    Optional<Pregunta> findById(UUID id);
    List<Pregunta> findAll();

    /**
     * Serializa la actualización, guarda y ejecuta el efecto antes de confirmar.
     * Si el cambio o el efecto fallan, revierte la persistencia local.
     * No garantiza una transacción distribuida con un sistema externo.
     */
    Pregunta actualizar(UUID id, Consumer<Pregunta> cambio, Consumer<Pregunta> despuesDeGuardar);
}
