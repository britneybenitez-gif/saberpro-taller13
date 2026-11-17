package co.edu.unicauca.saberpro.question.domain.model;

import co.edu.unicauca.saberpro.question.domain.exception.ReglaNegocioException;

/** Value Object inmutable con sustentacion de al menos 15 caracteres. */
public record Justificacion(String texto) {
    public Justificacion {
        if (texto == null || texto.trim().length() < 15) {
            throw new ReglaNegocioException("La justificacion debe tener al menos 15 caracteres");
        }
        texto = texto.trim();
    }
}
