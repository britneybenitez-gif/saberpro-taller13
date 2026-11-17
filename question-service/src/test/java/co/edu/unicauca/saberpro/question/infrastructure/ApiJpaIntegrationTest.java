package co.edu.unicauca.saberpro.question.infrastructure;

import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.JpaQuestionAdapter;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"app.repository=jpa",
                "spring.datasource.url=jdbc:h2:mem:api-jpa-it;DB_CLOSE_DELAY=-1"})
class ApiJpaIntegrationTest extends AdditionalApiContract {
    @Autowired private TestRestTemplate cliente;
    @Autowired private PreguntaRepositoryPort repositorio;

    private static final String JSON_VALIDO = """
            {"enunciado":"Pregunta de integracion","justificacion":"Explicacion tecnica suficientemente larga",
             "opciones":[{"texto":"A","correcta":true},{"texto":"B","correcta":false},
                         {"texto":"C","correcta":false},{"texto":"D","correcta":false}]}
            """;

    private ResponseEntity<Map> crear(String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return cliente.postForEntity("/api/preguntas", new HttpEntity<>(json, headers), Map.class);
    }

    @Test void crearConsultarYEnviarRevision() {
        assertInstanceOf(JpaQuestionAdapter.class, repositorio);
        ResponseEntity<Map> creada = crear(JSON_VALIDO);
        assertEquals(HttpStatus.CREATED, creada.getStatusCode());
        assertNotNull(creada.getBody());
        String id = (String) creada.getBody().get("id");
        assertNotNull(id);
        assertEquals("BORRADOR", creada.getBody().get("estado"));

        ResponseEntity<Map> consultada = cliente.getForEntity("/api/preguntas/" + id, Map.class);
        assertEquals(HttpStatus.OK, consultada.getStatusCode());
        assertNotNull(consultada.getBody());
        assertEquals("BORRADOR", consultada.getBody().get("estado"));
        assertEquals("Pregunta de integracion", ((Map<?, ?>) consultada.getBody().get("enunciado")).get("texto"));
        assertEquals(4, ((java.util.List<?>) consultada.getBody().get("opciones")).size());

        ResponseEntity<Map> revisada = cliente.postForEntity(
                "/api/preguntas/" + id + "/enviar-revision", null, Map.class);
        assertEquals(HttpStatus.ACCEPTED, revisada.getStatusCode());
        assertEquals("PENDIENTE_REVISION", revisada.getBody().get("estado"));
        assertEquals("PENDIENTE_REVISION", repositorio.findById(UUID.fromString(id)).orElseThrow().getEstado().name());

        ResponseEntity<Map> segundaRevision = cliente.postForEntity(
                "/api/preguntas/" + id + "/enviar-revision", null, Map.class);
        assertEquals(HttpStatus.BAD_REQUEST, segundaRevision.getStatusCode());
    }

    @Test void noPermiteRevisionConTresOpciones() {
        String invalido = """
                {"enunciado":"Pregunta incompleta","justificacion":"Explicacion tecnica suficientemente larga",
                 "opciones":[{"texto":"A","correcta":true},{"texto":"B","correcta":false},
                             {"texto":"C","correcta":false}]}
                """;
        ResponseEntity<Map> creada = crear(invalido);
        assertEquals(HttpStatus.CREATED, creada.getStatusCode());
        String id = (String) creada.getBody().get("id");
        ResponseEntity<Map> revision = cliente.postForEntity(
                "/api/preguntas/" + id + "/enviar-revision", null, Map.class);
        assertEquals(HttpStatus.BAD_REQUEST, revision.getStatusCode());
        assertEquals("BORRADOR", repositorio.findById(UUID.fromString(id)).orElseThrow().getEstado().name());
    }

    @Test void rechazaJustificacionCortaYResponde404SiNoExiste() {
        ResponseEntity<Map> invalida = crear(JSON_VALIDO.replace(
                "Explicacion tecnica suficientemente larga", "breve"));
        assertEquals(HttpStatus.BAD_REQUEST, invalida.getStatusCode());
        assertNotNull(invalida.getBody().get("error"));
        assertEquals(HttpStatus.NOT_FOUND, cliente.getForEntity(
                "/api/preguntas/" + UUID.randomUUID(), Map.class).getStatusCode());
    }
}
