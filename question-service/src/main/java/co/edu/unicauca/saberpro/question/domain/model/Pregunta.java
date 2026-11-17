package co.edu.unicauca.saberpro.question.domain.model;

import co.edu.unicauca.saberpro.question.domain.exception.ReglaNegocioException;
import co.edu.unicauca.saberpro.question.domain.model.Enunciado;
import co.edu.unicauca.saberpro.question.domain.model.Justificacion;
import co.edu.unicauca.saberpro.question.domain.model.OpcionRespuesta;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Raiz del agregado Pregunta: contiene y protege sus invariantes. */
public final class Pregunta {
    private final UUID id;
    private final Enunciado enunciado;
    private final Justificacion justificacion;
    private final List<OpcionRespuesta> opciones;
    private EstadoPregunta estado;

    /** Tambien se usa al reconstruir el agregado desde persistencia. */
    public Pregunta(UUID id, Enunciado enunciado, Justificacion justificacion,
                    List<OpcionRespuesta> opciones, EstadoPregunta estado) {
        if (id == null || enunciado == null || justificacion == null
                || opciones == null || estado == null) {
            throw new ReglaNegocioException("Datos de pregunta incompletos");
        }
        if (opciones.stream().anyMatch(Objects::isNull)) {
            throw new ReglaNegocioException("No se permiten opciones nulas");
        }
        this.id = id;
        this.enunciado = enunciado;
        this.justificacion = justificacion;
        this.opciones = List.copyOf(opciones);
        this.estado = estado;

        // Nunca rehidratar una pregunta en revision con datos invalidos.
        if (estado == EstadoPregunta.PENDIENTE_REVISION) {
            validarInvariantes();
        }
    }

    public static Pregunta crear(Enunciado enunciado, Justificacion justificacion,
                                List<OpcionRespuesta> opciones) {
        return new Pregunta(UUID.randomUUID(), enunciado, justificacion,
                opciones, EstadoPregunta.BORRADOR);
    }

    public void enviarRevision() {
        if (estado != EstadoPregunta.BORRADOR) {
            throw new ReglaNegocioException("La pregunta ya fue enviada a revision");
        }
        validarInvariantes();
        estado = EstadoPregunta.PENDIENTE_REVISION;
    }

    private void validarInvariantes() {
        if (opciones.size() != 4) {
            throw new ReglaNegocioException("Se requieren exactamente 4 opciones");
        }
        long clavesCorrectas = opciones.stream().filter(OpcionRespuesta::correcta).count();
        if (clavesCorrectas != 1) {
            throw new ReglaNegocioException("Debe existir exactamente una respuesta correcta");
        }
    }

    public UUID getId() { return id; }
    public Enunciado getEnunciado() { return enunciado; }
    public Justificacion getJustificacion() { return justificacion; }
    public List<OpcionRespuesta> getOpciones() { return opciones; }
    public EstadoPregunta getEstado() { return estado; }
}
