package co.edu.unicauca.saberpro.lifecycle;

import co.edu.unicauca.saberpro.lifecycle.messaging.QuestionReviewMessage;
import co.edu.unicauca.saberpro.lifecycle.persistence.ReviewReceiptRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "app.messaging.review-queue=pregunta.revisar.queue.integration-lifecycle",
        "spring.datasource.url=jdbc:h2:mem:lifecycle-it;DB_CLOSE_DELAY=-1"})
class LifecycleIntegrationTest {
    private static final String QUEUE = "pregunta.revisar.queue.integration-lifecycle";
    @Autowired RabbitTemplate rabbit;
    @Autowired ObjectMapper mapper;
    @Autowired ReviewReceiptRepository repository;
    @Autowired TestRestTemplate http;

    @Test void consumeJsonRealYExponeComprobanteUnaSolaVez() throws Exception {
        var event = new QuestionReviewMessage(UUID.randomUUID(), 1, "PreguntaEnviadaARevision",
                UUID.randomUUID(), "PENDIENTE_REVISION", Instant.now());
        var properties = new MessageProperties();
        properties.setContentType("application/json");
        var message = new Message(mapper.writeValueAsBytes(event), properties);
        rabbit.send("", QUEUE, message);
        await().atMost(Duration.ofSeconds(15)).untilAsserted(() ->
                assertTrue(repository.existsById(event.eventId())));
        var processedAt = repository.findById(event.eventId()).orElseThrow().getProcessedAt();
        rabbit.send("", QUEUE, message);
        // Un mensaje posterior funciona como barrera: el consumidor es secuencial (concurrency=1).
        var barrier = new QuestionReviewMessage(UUID.randomUUID(), 1, "PreguntaEnviadaARevision",
                UUID.randomUUID(), "PENDIENTE_REVISION", Instant.now());
        rabbit.send("", QUEUE, new Message(mapper.writeValueAsBytes(barrier), properties));
        await().atMost(Duration.ofSeconds(15)).untilAsserted(() ->
                assertTrue(repository.existsById(barrier.eventId())));
        assertEquals(processedAt, repository.findById(event.eventId()).orElseThrow().getProcessedAt());
        var response = http.getForEntity("/api/revisiones/" + event.preguntaId(), java.util.Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event.eventId().toString(), response.getBody().get("eventId"));
        assertEquals(HttpStatus.OK, http.getForEntity("/actuator/health", String.class).getStatusCode());
    }

    @Test void contratoInvalidoTerminaEnDlqSinBucle() {
        byte[] invalid = "{\"schemaVersion\":999}".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        rabbit.send("", QUEUE, new Message(invalid));
        Message rejected = rabbit.receive(QUEUE + ".dlq", 15000);
        assertNotNull(rejected);
        assertArrayEquals(invalid, rejected.getBody());
        assertNotNull(rejected.getMessageProperties().getHeaders().get("x-death"));
    }
}
