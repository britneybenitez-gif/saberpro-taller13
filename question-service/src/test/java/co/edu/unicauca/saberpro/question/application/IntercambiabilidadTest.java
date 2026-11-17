package co.edu.unicauca.saberpro.question.application;

import co.edu.unicauca.saberpro.question.domain.model.EstadoPregunta;
import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.NotificacionRevisionPort;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.question.domain.model.OpcionRespuesta;
import co.edu.unicauca.saberpro.question.infrastructure.memory.InMemoryQuestionAdapter;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IntercambiabilidadTest {
    private final PreguntaRepositoryPort repositorio = new InMemoryQuestionAdapter();
    private final RegistroNotificaciones notificaciones = new RegistroNotificaciones();
    private final QuestionApplicationService servicio = new QuestionApplicationService(repositorio, notificaciones, pregunta -> {});

    private static List<OpcionRespuesta> opciones() {
        return List.of(new OpcionRespuesta("A", true), new OpcionRespuesta("B", false),
                new OpcionRespuesta("C", false), new OpcionRespuesta("D", false));
    }

    @Test void guardaBuscaYEnviaRevisionPorPuertosSinSpring() {
        Pregunta creada = servicio.crear("Pregunta de prueba", "Explicacion suficientemente larga", opciones());
        assertEquals(EstadoPregunta.BORRADOR, servicio.buscar(creada.getId()).getEstado());
        assertEquals(EstadoPregunta.PENDIENTE_REVISION,
                servicio.enviarRevision(creada.getId()).getEstado());
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, servicio.buscar(creada.getId()).getEstado());
        assertEquals(1, notificaciones.cantidad);
    }

    @Test void noNotificaCuandoReglasDeRevisionFallan() {
        Pregunta creada = servicio.crear("Pregunta incompleta", "Explicacion suficientemente larga",
                opciones().subList(0, 3));
        assertThrows(RuntimeException.class, () -> servicio.enviarRevision(creada.getId()));
        assertEquals(0, notificaciones.cantidad);
        assertEquals(EstadoPregunta.BORRADOR, servicio.buscar(creada.getId()).getEstado());
    }

    @Test void repositorioEnMemoriaGuardaCopiasYNoReferenciasMutables() {
        Pregunta creada = servicio.crear("Pregunta en memoria", "Explicacion suficientemente larga", opciones());
        Pregunta cargada = repositorio.findById(creada.getId()).orElseThrow();
        cargada.enviarRevision(); // No se ha hecho save: el repositorio mantiene BORRADOR.
        assertEquals(EstadoPregunta.BORRADOR, repositorio.findById(creada.getId()).orElseThrow().getEstado());
    }

    private static final class RegistroNotificaciones implements NotificacionRevisionPort {
        private int cantidad;
        @Override public void notificarPreguntaPendiente(Pregunta pregunta) { cantidad++; }
    }
}
