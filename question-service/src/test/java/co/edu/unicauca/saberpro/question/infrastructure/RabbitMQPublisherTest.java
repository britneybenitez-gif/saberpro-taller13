package co.edu.unicauca.saberpro.question.infrastructure;

import co.edu.unicauca.saberpro.question.application.exception.DependenciaNoDisponibleException;
import co.edu.unicauca.saberpro.question.domain.model.*;
import co.edu.unicauca.saberpro.question.infrastructure.messaging.RabbitMQPublisher;
import co.edu.unicauca.saberpro.question.infrastructure.messaging.QuestionReviewEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RabbitMQPublisherTest {
    private final RabbitTemplate rabbit = mock(RabbitTemplate.class);
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    private final RabbitMQPublisher publisher = new RabbitMQPublisher(rabbit, mapper,
            "pregunta.revisar.queue", Duration.ofMillis(100));

    private Pregunta question() {
        var p = Pregunta.crear(new Enunciado("Pregunta"), new Justificacion("Justificacion suficiente"),
                List.of(new OpcionRespuesta("A", true), new OpcionRespuesta("B", false),
                        new OpcionRespuesta("C", false), new OpcionRespuesta("D", false)));
        p.enviarRevision();
        return p;
    }

    private void reply(Consumer<CorrelationData> action) {
        doAnswer(invocation -> {
            action.accept(invocation.getArgument(3));
            return null;
        }).when(rabbit).send(eq(""), eq("pregunta.revisar.queue"), any(Message.class), any(CorrelationData.class));
    }

    @Test void publicaJsonPersistenteConIdentidadEstable() throws Exception {
        reply(c -> c.getFuture().complete(new CorrelationData.Confirm(true, null)));
        var p = question();
        publisher.notificarPreguntaPendiente(p);
        var message = org.mockito.ArgumentCaptor.forClass(Message.class);
        verify(rabbit).send(eq(""), eq("pregunta.revisar.queue"), message.capture(), any(CorrelationData.class));
        var event = mapper.readTree(message.getValue().getBody());
        assertEquals(p.getId().toString(), event.get("preguntaId").asText());
        assertEquals(1, event.get("schemaVersion").asInt());
        assertEquals("PENDIENTE_REVISION", event.get("estado").asText());
        assertEquals(QuestionReviewEvent.from(p).eventId().toString(), event.get("eventId").asText());
        assertEquals(MessageDeliveryMode.PERSISTENT, message.getValue().getMessageProperties().getDeliveryMode());
        assertEquals("application/json", message.getValue().getMessageProperties().getContentType());
    }

    @Test void nackNoSeConsideraExito() {
        reply(c -> c.getFuture().complete(new CorrelationData.Confirm(false, "rechazado")));
        assertThrows(DependenciaNoDisponibleException.class, () -> publisher.notificarPreguntaPendiente(question()));
    }

    @Test void returnAunqueTengaAckNoSeConsideraExito() {
        reply(c -> {
            c.setReturned(new ReturnedMessage(new Message(new byte[0]), 312, "NO_ROUTE", "", "cola"));
            c.getFuture().complete(new CorrelationData.Confirm(true, null));
        });
        assertThrows(DependenciaNoDisponibleException.class, () -> publisher.notificarPreguntaPendiente(question()));
    }

    @Test void timeoutNoSeConsideraExito() {
        assertThrows(DependenciaNoDisponibleException.class, () -> publisher.notificarPreguntaPendiente(question()));
    }
}
