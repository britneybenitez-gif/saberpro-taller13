package co.edu.unicauca.saberpro.lifecycle.api;

import co.edu.unicauca.saberpro.lifecycle.persistence.ReviewReceipt;
import co.edu.unicauca.saberpro.lifecycle.persistence.ReviewReceiptRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Consulta del procesamiento real; no cambia ni aprueba el agregado remoto. */
@RestController
@RequestMapping("/api/revisiones")
public class ReviewRestController {
    private final ReviewReceiptRepository repository;
    public ReviewRestController(ReviewReceiptRepository repository) { this.repository = repository; }

    @GetMapping
    public List<ReviewReceipt> listar() { return repository.findAll(); }

    @GetMapping("/{preguntaId}")
    public ResponseEntity<ReviewReceipt> buscar(@PathVariable UUID preguntaId) {
        return ResponseEntity.of(repository.findByPreguntaId(preguntaId));
    }
}
