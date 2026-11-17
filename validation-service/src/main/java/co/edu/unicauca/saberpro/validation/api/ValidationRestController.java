package co.edu.unicauca.saberpro.validation.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/** Contrato de transporte/almacenamiento; no evalúa las invariantes del agregado. */
@RestController
@RequestMapping("/api/validaciones")
public class ValidationRestController {
    public record Opcion(@NotNull @Size(max = 2000) String texto, @NotNull Boolean correcta) {}
    public record Solicitud(@NotNull @Size(max = 4000) String enunciado,
                            @NotNull @Size(max = 4000) String justificacion,
                            @NotNull List<@NotNull @Valid Opcion> opciones) {}
    public record Resultado(boolean valida, List<String> errores) {}

    @PostMapping("/preguntas")
    public Resultado validar(@Valid @RequestBody Solicitud solicitud) {
        return new Resultado(true, List.of());
    }
}
