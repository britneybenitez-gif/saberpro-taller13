package co.edu.unicauca.saberpro.question.domain.ports.outbound;

import co.edu.unicauca.saberpro.question.domain.model.Pregunta;

/** Verificación externa del contrato técnico; las invariantes pertenecen a Pregunta. */
public interface ValidacionContenidoPort {
    void validar(Pregunta pregunta);
}
