package co.edu.unicauca.saberpro.question.domain.ports.inbound;

import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import co.edu.unicauca.saberpro.question.domain.model.OpcionRespuesta;
import java.util.List;
import java.util.UUID;

/** Puerto primario (entrada) para interactuar con preguntas. */
public interface CrearPreguntaUseCase {
    Pregunta crear(String enunciado, String justificacion, List<OpcionRespuesta> opciones);
    Pregunta buscar(UUID id);
    List<Pregunta> listar();
}
