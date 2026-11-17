package co.edu.unicauca.saberpro.question.application;

import co.edu.unicauca.saberpro.question.application.exception.DependenciaNoDisponibleException;
import co.edu.unicauca.saberpro.question.domain.model.*;
import co.edu.unicauca.saberpro.question.infrastructure.memory.InMemoryQuestionAdapter;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuestionApplicationServiceTest {
    private final InMemoryQuestionAdapter repository = new InMemoryQuestionAdapter();
    private List<OpcionRespuesta> options() {
        return List.of(new OpcionRespuesta("A", true), new OpcionRespuesta("B", false),
                new OpcionRespuesta("C", false), new OpcionRespuesta("D", false));
    }

    @Test void falloDePublicacionRevierteHashMap() {
        var service = new QuestionApplicationService(repository, p -> {
            throw new DependenciaNoDisponibleException("Broker no disponible", null);
        }, p -> {});
        var created = service.crear("Pregunta", "Justificacion suficiente", options());
        assertThrows(DependenciaNoDisponibleException.class, () -> service.enviarRevision(created.getId()));
        assertEquals(EstadoPregunta.BORRADOR, service.buscar(created.getId()).getEstado());
    }

    @Test void falloDeValidacionNoPersiste() {
        var service = new QuestionApplicationService(repository, p -> {}, p -> {
            throw new DependenciaNoDisponibleException("Validacion no disponible", null);
        });
        assertThrows(DependenciaNoDisponibleException.class,
                () -> service.crear("Pregunta", "Justificacion suficiente", options()));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test void listadoNoExponeReferenciasMutables() {
        var service = new QuestionApplicationService(repository, p -> {}, p -> {});
        var created = service.crear("Pregunta", "Justificacion suficiente", options());
        service.listar().get(0).enviarRevision();
        assertEquals(EstadoPregunta.BORRADOR, service.buscar(created.getId()).getEstado());
        assertThrows(UnsupportedOperationException.class, () -> service.listar().clear());
    }
}
