package co.edu.unicauca.saberpro.question.domain.model;

import co.edu.unicauca.saberpro.question.domain.exception.ReglaNegocioException;

/** Value Object inmutable de una alternativa de respuesta. */
public record OpcionRespuesta(String texto, boolean correcta) {
    public OpcionRespuesta {
        if (texto == null || texto.isBlank()) {
            throw new ReglaNegocioException("El texto de la opcion es obligatorio");
        }
        texto = texto.trim();
    }
}
