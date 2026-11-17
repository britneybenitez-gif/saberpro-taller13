package co.edu.unicauca.saberpro.question.infrastructure.messaging;

import co.edu.unicauca.saberpro.question.application.exception.DependenciaNoDisponibleException;
import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.NotificacionRevisionPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/** Publica JSON persistente y espera confirmación positiva y ausencia de basic.return. */
public final class RabbitMQPublisher implements NotificacionRevisionPort {
    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper;
    private final String queue;
    private final Duration timeout;

    public RabbitMQPublisher(RabbitTemplate rabbit, ObjectMapper mapper, String queue, Duration timeout) {
        this.rabbit = rabbit;
        this.mapper = mapper;
        this.queue = queue;
        this.timeout = timeout;
    }

    @Override
    public void notificarPreguntaPendiente(Pregunta pregunta) {
        try {
            QuestionReviewEvent evento = QuestionReviewEvent.from(pregunta);
            MessageProperties properties = new MessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            properties.setContentEncoding("UTF-8");
            properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            properties.setMessageId(evento.eventId().toString());
            // Correlación única por intento; eventId estable para deduplicación del consumidor.
            CorrelationData correlation = new CorrelationData();
            rabbit.send("", queue, new Message(mapper.writeValueAsBytes(evento), properties), correlation);
            CorrelationData.Confirm confirm = correlation.getFuture().get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            if (!confirm.isAck() || correlation.getReturned() != null) {
                throw new IllegalStateException("RabbitMQ rechazó el mensaje o no existe una ruta a la cola");
            }
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            throw new DependenciaNoDisponibleException("Publicación de revisión interrumpida", error);
        } catch (Exception error) {
            throw new DependenciaNoDisponibleException("No se pudo confirmar la publicación en RabbitMQ", error);
        }
    }
}
