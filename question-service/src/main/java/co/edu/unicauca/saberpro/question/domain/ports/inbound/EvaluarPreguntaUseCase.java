package co.edu.unicauca.saberpro.question.domain.ports.inbound;

import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import java.util.UUID;

/** Evalúa las invariantes de preparación y solicita revisión; no califica exámenes. */
public interface EvaluarPreguntaUseCase {
    Pregunta enviarRevision(UUID id);
}
