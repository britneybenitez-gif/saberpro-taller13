package co.edu.unicauca.saberpro.question.infrastructure;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"app.repository=memory", "spring.datasource.url=jdbc:h2:mem:empty-it;DB_CLOSE_DELAY=-1"})
class ApiEmptyIntegrationTest {
    @Autowired TestRestTemplate http;
    @Test void devuelve200YListaVacia() {
        var response = http.getForEntity("/api/preguntas", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(), response.getBody());
    }
}
