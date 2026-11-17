import co.edu.unicauca.saberpro.question.domain.model.*;
import java.util.List;
public class DomainBoundaryProbe {
    public static void main(String[] args) {
        String input = "\u0000\t ";
        Enunciado e = new Enunciado(input);
        OpcionRespuesta o = new OpcionRespuesta(input, true);
        var p = Pregunta.crear(e, new Justificacion("Justificación válida y suficientemente larga"),
            List.of(o, new OpcionRespuesta("B",false),new OpcionRespuesta("C",false),new OpcionRespuesta("D",false)));
        p.enviarRevision();
        System.out.println("Enunciado normalizado: longitud="+e.texto().length());
        System.out.println("Opcion normalizada: longitud="+o.texto().length());
        System.out.println("Estado="+p.getEstado());
        if(e.texto().isEmpty() || o.texto().isEmpty()) throw new AssertionError("Se aceptó texto vacío tras normalización");
    }
}
