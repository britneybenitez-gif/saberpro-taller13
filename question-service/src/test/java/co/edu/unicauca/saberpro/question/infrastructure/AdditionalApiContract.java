package co.edu.unicauca.saberpro.question.infrastructure;

import co.edu.unicauca.saberpro.question.application.exception.DependenciaNoDisponibleException;
import co.edu.unicauca.saberpro.question.domain.model.*;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.question.infrastructure.messaging.RabbitMQPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.awaitility.Awaitility.await;

/** Se ejecuta completo con JPA y HashMap, usando REST y AMQP reales. */
abstract class AdditionalApiContract {
    @Autowired TestRestTemplate http;
    @Autowired PreguntaRepositoryPort repository;
    @Autowired RabbitTemplate rabbit;
    @Autowired ObjectMapper mapper;

    private Map<String, Object> body() {
        return Map.of("enunciado", "Pregunta adicional", "justificacion", "Explicación suficientemente detallada",
                "opciones", List.of(Map.of("texto", "A", "correcta", true), Map.of("texto", "B", "correcta", false),
                Map.of("texto", "C", "correcta", false), Map.of("texto", "D", "correcta", false)));
    }

    private String create() {
        var response = http.postForEntity("/api/preguntas", body(), Map.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String id = (String) response.getBody().get("id");
        assertTrue(response.getHeaders().getLocation().toString().endsWith("/api/preguntas/" + id));
        return id;
    }

    @Test void listaYConservaContratoDelTaller12() {
        String id = create();
        var response = http.getForEntity("/api/preguntas", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<?> preguntas = response.getBody();
        assertTrue(preguntas.stream().map(o -> (Map<?, ?>) o).anyMatch(p -> id.equals(p.get("id"))));
        var single = http.getForEntity("/api/preguntas/" + id, Map.class).getBody();
        assertEquals("Pregunta adicional", ((Map<?, ?>) single.get("enunciado")).get("texto"));
    }

    @Test void rechazaEstructurasIncorrectasYUuidInvalido() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        for (String json : List.of("{", "null", "{}",
                "{\"enunciado\":\"Pregunta\",\"justificacion\":\"Explicacion suficiente\",\"opciones\":[null]}",
                "{\"enunciado\":42,\"justificacion\":\"Explicacion suficiente\",\"opciones\":[]}",
                "{\"enunciado\":\"Pregunta\",\"justificacion\":\"Explicacion suficiente\",\"opciones\":[{\"texto\":\"A\"}]}")) {
            assertEquals(HttpStatus.BAD_REQUEST, http.postForEntity("/api/preguntas",
                    new HttpEntity<>(json, headers), Map.class).getStatusCode(), json);
        }
        assertEquals(HttpStatus.BAD_REQUEST, http.getForEntity("/api/preguntas/no-es-uuid", Map.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, http.postForEntity("/api/preguntas/" + UUID.randomUUID()
                + "/enviar-revision", null, Map.class).getStatusCode());
    }

    @Test void validacionRestRealRechazaContenidoExcesivo() {
        var invalid = new java.util.HashMap<>(body());
        invalid.put("enunciado", "x".repeat(4001));
        int before = repository.findAll().size();
        assertEquals(HttpStatus.BAD_REQUEST, http.postForEntity("/api/preguntas", invalid, Map.class).getStatusCode());
        assertEquals(before, repository.findAll().size());
    }

    @Test void rechazaMultiplesClavesSinCambiarEstado() {
        var invalid = new java.util.HashMap<>(body());
        invalid.put("opciones", List.of(Map.of("texto", "A", "correcta", true), Map.of("texto", "B", "correcta", true),
                Map.of("texto", "C", "correcta", false), Map.of("texto", "D", "correcta", false)));
        var created = http.postForEntity("/api/preguntas", invalid, Map.class);
        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        String id = (String) created.getBody().get("id");
        assertEquals(HttpStatus.BAD_REQUEST, http.postForEntity("/api/preguntas/" + id + "/enviar-revision",
                null, Map.class).getStatusCode());
        assertEquals(EstadoPregunta.BORRADOR, repository.findById(UUID.fromString(id)).orElseThrow().getEstado());
    }

    @Test void confirmaPublicacionYConsumoRealEnLifecycle() {
        String id = create();
        assertEquals(HttpStatus.ACCEPTED, http.postForEntity("/api/preguntas/" + id + "/enviar-revision",
                null, Map.class).getStatusCode());
        TestRestTemplate lifecycle = new TestRestTemplate();
        String url = System.getProperty("test.lifecycle-url", "http://localhost:8084") + "/api/revisiones/" + id;
        await().atMost(Duration.ofSeconds(15)).untilAsserted(() -> {
            var receipt = lifecycle.getForEntity(url, Map.class);
            assertEquals(HttpStatus.OK, receipt.getStatusCode());
            assertEquals(id, receipt.getBody().get("preguntaId"));
            assertNotNull(receipt.getBody().get("eventId"));
            assertNotNull(receipt.getBody().get("processedAt"));
        });
    }

    @Test void reviertePersistenciaCuandoUnMensajeRealNoTieneRuta() {
        UUID id = UUID.fromString(create());
        var publisher = new RabbitMQPublisher(rabbit, mapper, "inexistente-" + UUID.randomUUID(), Duration.ofSeconds(5));
        assertThrows(DependenciaNoDisponibleException.class, () -> repository.actualizar(id,
                Pregunta::enviarRevision, publisher::notificarPreguntaPendiente));
        assertEquals(EstadoPregunta.BORRADOR, repository.findById(id).orElseThrow().getEstado());
    }

    @Test void dosEnviosConcurrentesProducenSoloUnaAceptacion() throws Exception {
        String url = "/api/preguntas/" + create() + "/enviar-revision";
        var executor = Executors.newFixedThreadPool(2);
        var start = new CountDownLatch(1);
        Callable<Integer> request = () -> {
            start.await();
            return http.postForEntity(url, null, Map.class).getStatusCode().value();
        };
        try {
            var first = executor.submit(request);
            var second = executor.submit(request);
            start.countDown();
            var statuses = java.util.stream.Stream.of(first.get(20, TimeUnit.SECONDS), second.get(20, TimeUnit.SECONDS))
                    .sorted().toList();
            assertEquals(List.of(202, 400), statuses);
        } finally {
            executor.shutdownNow();
        }
    }
}
