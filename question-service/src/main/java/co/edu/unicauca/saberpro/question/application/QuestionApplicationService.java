package co.edu.unicauca.saberpro.question.application;

import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import co.edu.unicauca.saberpro.question.domain.ports.inbound.CrearPreguntaUseCase;
import co.edu.unicauca.saberpro.question.domain.ports.inbound.EvaluarPreguntaUseCase;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.ValidacionContenidoPort;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.NotificacionRevisionPort;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.question.domain.model.Enunciado;
import co.edu.unicauca.saberpro.question.domain.model.Justificacion;
import co.edu.unicauca.saberpro.question.domain.model.OpcionRespuesta;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

/** Orquestacion del caso de uso: no conoce HTTP, JPA ni Spring. */
public final class QuestionApplicationService implements CrearPreguntaUseCase, EvaluarPreguntaUseCase {
    private final PreguntaRepositoryPort repositorio;
    private final NotificacionRevisionPort notificaciones;
    private final ValidacionContenidoPort validacion;

    public QuestionApplicationService(PreguntaRepositoryPort repositorio,
                                NotificacionRevisionPort notificaciones,
                                ValidacionContenidoPort validacion) {
        this.repositorio = Objects.requireNonNull(repositorio);
        this.notificaciones = Objects.requireNonNull(notificaciones);
        this.validacion = Objects.requireNonNull(validacion);
    }

    @Override
    public Pregunta crear(String enunciado, String justificacion,
                         List<OpcionRespuesta> opciones) {
        Pregunta pregunta = Pregunta.crear(new Enunciado(enunciado),
                new Justificacion(justificacion), opciones);
        validacion.validar(pregunta);
        return repositorio.save(pregunta);
    }

    @Override
    public Pregunta buscar(UUID id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pregunta no encontrada: " + id));
    }

    @Override
    public List<Pregunta> listar() {
        return repositorio.findAll();
    }

    @Override
    public Pregunta enviarRevision(UUID id) {
        return repositorio.actualizar(id, Pregunta::enviarRevision,
                notificaciones::notificarPreguntaPendiente);
    }
}
