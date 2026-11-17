package co.edu.unicauca.saberpro.lifecycle.messaging;

import co.edu.unicauca.saberpro.lifecycle.application.ReviewProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QuestionReviewListener {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionReviewListener.class);
    private final ObjectMapper mapper;
    private final ReviewProcessingService processing;

    public QuestionReviewListener(ObjectMapper mapper, ReviewProcessingService processing) {
        this.mapper = mapper;
        this.processing = processing;
    }

    @RabbitListener(queues = "${app.messaging.review-queue}")
    public void recibir(Message message) {
        QuestionReviewMessage event;
        try {
            event = mapper.readValue(message.getBody(), QuestionReviewMessage.class);
            if (event == null) throw new IllegalArgumentException("Evento nulo");
            event.validate();
        } catch (Exception error) {
            LOG.warn("Evento inválido enviado a la cola de errores: {}", error.getMessage());
            throw new AmqpRejectAndDontRequeueException("Contrato JSON de revisión inválido", error);
        }
        boolean nuevo = processing.registrar(event);
        LOG.info("Revisión {}: preguntaId={} eventId={}", nuevo ? "procesada" : "duplicada ignorada",
                event.preguntaId(), event.eventId());
        // AUTO ACK después de retornar; el servicio ya confirmó su transacción H2.
    }
}
