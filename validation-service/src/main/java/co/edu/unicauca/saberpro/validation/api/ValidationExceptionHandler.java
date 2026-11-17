package co.edu.unicauca.saberpro.validation.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationRestController.Resultado> invalid(MethodArgumentNotValidException error) {
        List<String> errores = error.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage()).sorted().toList();
        return ResponseEntity.badRequest().body(new ValidationRestController.Resultado(false, errores));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationRestController.Resultado> unreadable(HttpMessageNotReadableException error) {
        return ResponseEntity.badRequest().body(new ValidationRestController.Resultado(false,
                List.of("El cuerpo debe ser un JSON válido con los tipos requeridos")));
    }
}
