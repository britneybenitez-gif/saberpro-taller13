package co.edu.unicauca.saberpro.question.infrastructure.validation;

import co.edu.unicauca.saberpro.question.application.exception.DependenciaNoDisponibleException;
import co.edu.unicauca.saberpro.question.domain.exception.ReglaNegocioException;
import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.ValidacionContenidoPort;
import java.util.List;
import java.util.Map;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;

public final class RestValidationAdapter implements ValidacionContenidoPort {
    private final RestClient client;

    public RestValidationAdapter(RestClient client) { this.client = client; }

    public record Resultado(Boolean valida, List<String> errores) {}

    @Override
    public void validar(Pregunta pregunta) {
        try {
            Resultado respuesta = client.post().uri("/api/validaciones/preguntas")
                    .body(Map.of("enunciado", pregunta.getEnunciado().texto(),
                            "justificacion", pregunta.getJustificacion().texto(), "opciones", pregunta.getOpciones()))
                    .retrieve().body(Resultado.class);
            if (respuesta == null || respuesta.valida() == null || respuesta.errores() == null) {
                throw new DependenciaNoDisponibleException("Respuesta inválida de Validation Service", null);
            }
            if (!respuesta.valida()) {
                throw new ReglaNegocioException("El contenido excede los límites técnicos de validación");
            }
        } catch (HttpClientErrorException.BadRequest error) {
            throw new ReglaNegocioException("El contenido excede los límites técnicos de validación");
        } catch (RestClientException error) {
            throw new DependenciaNoDisponibleException("Validation Service no está disponible", error);
        }
    }
}
