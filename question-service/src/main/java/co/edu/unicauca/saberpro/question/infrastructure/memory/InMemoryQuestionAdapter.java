package co.edu.unicauca.saberpro.question.infrastructure.memory;

import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/** HashMap con copias defensivas y exclusión mutua para lectura/actualización. */
public final class InMemoryQuestionAdapter implements PreguntaRepositoryPort {
    private final Map<UUID, Pregunta> preguntas = new HashMap<>();

    @Override
    public synchronized Pregunta save(Pregunta pregunta) {
        preguntas.put(pregunta.getId(), copiar(pregunta));
        return copiar(pregunta);
    }

    @Override
    public synchronized Optional<Pregunta> findById(UUID id) {
        return Optional.ofNullable(preguntas.get(id)).map(InMemoryQuestionAdapter::copiar);
    }

    @Override
    public synchronized List<Pregunta> findAll() {
        return preguntas.values().stream().map(InMemoryQuestionAdapter::copiar)
                .sorted(java.util.Comparator.comparing(p -> p.getId().toString())).toList();
    }

    @Override
    public synchronized Pregunta actualizar(UUID id, Consumer<Pregunta> cambio,
                                             Consumer<Pregunta> despuesDeGuardar) {
        Pregunta anterior = findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pregunta no encontrada: " + id));
        Pregunta nueva = copiar(anterior);
        cambio.accept(nueva);
        save(nueva);
        try {
            despuesDeGuardar.accept(nueva);
        } catch (RuntimeException | Error error) {
            preguntas.put(id, anterior);
            throw error;
        }
        return copiar(nueva);
    }

    private static Pregunta copiar(Pregunta p) {
        return new Pregunta(p.getId(), p.getEnunciado(), p.getJustificacion(), p.getOpciones(), p.getEstado());
    }
}
