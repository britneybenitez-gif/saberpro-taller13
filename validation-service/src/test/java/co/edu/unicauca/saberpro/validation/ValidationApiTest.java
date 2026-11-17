package co.edu.unicauca.saberpro.validation;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ValidationApiTest {
    @Autowired TestRestTemplate http;

    private ResponseEntity<Map> post(Object body) {
        return http.postForEntity("/api/validaciones/preguntas", body, Map.class);
    }

    @Test void contextoYHttpDisponibles() {
        assertEquals(HttpStatus.OK, http.getForEntity("/actuator/health", Map.class).getStatusCode());
    }

    @Test void validaContenidoCompleto() {
        var response = post(Map.of("enunciado", "Pregunta", "justificacion", "Justificacion suficiente",
                "opciones", List.of(Map.of("texto", "A", "correcta", true), Map.of("texto", "B", "correcta", false),
                Map.of("texto", "C", "correcta", false), Map.of("texto", "D", "correcta", false))));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().get("valida"));
        assertEquals(List.of(), response.getBody().get("errores"));
    }

    @Test void permiteBorradoresSinDuplicarReglasDeRevision() {
        assertEquals(HttpStatus.OK, post(Map.of("enunciado", "Pregunta", "justificacion", "Sustento",
                "opciones", List.of())).getStatusCode());
    }

    @Test void rechazaLimitesDeAlmacenamiento() {
        var response = post(Map.of("enunciado", "x".repeat(4001), "justificacion", "Justificacion suficiente",
                "opciones", List.of(Map.of("texto", "y".repeat(2001), "correcta", true))));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(false, response.getBody().get("valida"));
        assertEquals(2, ((List<?>) response.getBody().get("errores")).size());
    }

    @Test void rechazaEstructuraIncompleta() {
        assertEquals(HttpStatus.BAD_REQUEST, post(Map.of("enunciado", "Pregunta")).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, post(Map.of("enunciado", "Pregunta", "justificacion", "Sustento",
                "opciones", List.of(Map.of("texto", "A")))).getStatusCode());
    }

    @Test void rechazaJsonMalformadoYTiposIncorrectos() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        assertEquals(HttpStatus.BAD_REQUEST, post(new HttpEntity<>("{", headers)).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, post(Map.of("enunciado", 23, "justificacion", "Sustento",
                "opciones", List.of())).getStatusCode());
    }
}
