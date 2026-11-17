package co.edu.unicauca.saberpro.question.api;

import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import co.edu.unicauca.saberpro.question.domain.model.OpcionRespuesta;
import co.edu.unicauca.saberpro.question.domain.ports.inbound.CrearPreguntaUseCase;
import co.edu.unicauca.saberpro.question.domain.ports.inbound.EvaluarPreguntaUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Valida la estructura HTTP y delega las reglas a los puertos de entrada. */
@RestController
@RequestMapping("/api/preguntas")
public class QuestionRestController {
    private final CrearPreguntaUseCase crearUseCase;
    private final EvaluarPreguntaUseCase evaluarUseCase;

    public QuestionRestController(CrearPreguntaUseCase crearUseCase, EvaluarPreguntaUseCase evaluarUseCase) {
        this.crearUseCase = crearUseCase;
        this.evaluarUseCase = evaluarUseCase;
    }

    public record OpcionRequest(String texto, @NotNull Boolean correcta) {}
    public record CrearRequest(String enunciado, String justificacion,
                               @NotNull List<@NotNull @Valid OpcionRequest> opciones) {}

    @PostMapping
    public ResponseEntity<Pregunta> crear(@Valid @RequestBody CrearRequest request) {
        Pregunta creada = crearUseCase.crear(request.enunciado(), request.justificacion(),
                request.opciones().stream().map(o -> new OpcionRespuesta(o.texto(), o.correcta())).toList());
        return ResponseEntity.created(URI.create("/api/preguntas/" + creada.getId())).body(creada);
    }

    @GetMapping
    public List<Pregunta> listar() { return crearUseCase.listar(); }

    @GetMapping("/{id}")
    public Pregunta buscar(@PathVariable UUID id) { return crearUseCase.buscar(id); }

    @PostMapping("/{id}/enviar-revision")
    public ResponseEntity<Pregunta> enviarRevision(@PathVariable UUID id) {
        return ResponseEntity.accepted().body(evaluarUseCase.enviarRevision(id));
    }
}
