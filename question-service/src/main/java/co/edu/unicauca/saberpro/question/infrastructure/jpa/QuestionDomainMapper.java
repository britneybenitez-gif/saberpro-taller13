package co.edu.unicauca.saberpro.question.infrastructure.jpa;

import co.edu.unicauca.saberpro.question.domain.model.*;

/** La entidad JPA y el agregado tienen ciclos de vida independientes. */
public final class QuestionDomainMapper {
    private QuestionDomainMapper() {}

    public static QuestionRecord toRecord(Pregunta pregunta) {
        QuestionRecord entidad = new QuestionRecord();
        entidad.setId(pregunta.getId());
        entidad.setEnunciado(pregunta.getEnunciado().texto());
        entidad.setJustificacion(pregunta.getJustificacion().texto());
        entidad.setEstado(pregunta.getEstado().name());
        entidad.setOpciones(pregunta.getOpciones().stream()
                .map(o -> new OpcionEmbeddable(o.texto(), o.correcta())).toList());
        return entidad;
    }

    public static Pregunta toDomain(QuestionRecord entidad) {
        return new Pregunta(entidad.getId(), new Enunciado(entidad.getEnunciado()),
                new Justificacion(entidad.getJustificacion()), entidad.getOpciones().stream()
                .map(o -> new OpcionRespuesta(o.getTexto(), o.isCorrecta())).toList(),
                EstadoPregunta.valueOf(entidad.getEstado()));
    }
}
