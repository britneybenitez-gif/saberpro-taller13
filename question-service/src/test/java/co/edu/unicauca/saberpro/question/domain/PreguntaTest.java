package co.edu.unicauca.saberpro.question.domain;

import co.edu.unicauca.saberpro.question.domain.exception.ReglaNegocioException;
import co.edu.unicauca.saberpro.question.domain.model.EstadoPregunta;
import co.edu.unicauca.saberpro.question.domain.model.Pregunta;
import co.edu.unicauca.saberpro.question.domain.model.Enunciado;
import co.edu.unicauca.saberpro.question.domain.model.Justificacion;
import co.edu.unicauca.saberpro.question.domain.model.OpcionRespuesta;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PreguntaTest {
    private List<OpcionRespuesta> opcionesValidas() {
        return List.of(new OpcionRespuesta("A", true), new OpcionRespuesta("B", false),
                new OpcionRespuesta("C", false), new OpcionRespuesta("D", false));
    }

    private Pregunta crear(List<OpcionRespuesta> opciones) {
        return Pregunta.crear(new Enunciado("Pregunta de prueba"),
                new Justificacion("Justificacion tecnica suficiente"), opciones);
    }

    @Test void iniciaEnBorrador() {
        assertEquals(EstadoPregunta.BORRADOR, crear(opcionesValidas()).getEstado());
    }

    @Test void cambiaAPendienteRevision() {
        Pregunta pregunta = crear(opcionesValidas());
        pregunta.enviarRevision();
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, pregunta.getEstado());
    }

    @Test void enunciadoNuloOVacioSeRechaza() {
        assertThrows(ReglaNegocioException.class, () -> new Enunciado(null));
        assertThrows(ReglaNegocioException.class, () -> new Enunciado("   "));
    }

    @Test void justificacionDe14CaracteresSeRechaza() {
        assertThrows(ReglaNegocioException.class, () -> new Justificacion("12345678901234"));
        assertThrows(ReglaNegocioException.class, () -> new Justificacion(null));
    }

    @Test void justificacionDe15CaracteresSeAcepta() {
        assertEquals(15, new Justificacion("123456789012345").texto().length());
    }

    @Test void opcionConTextoVacioSeRechaza() {
        assertThrows(ReglaNegocioException.class, () -> new OpcionRespuesta("  ", false));
    }

    @Test void unaPreguntaConTresOpcionesPuedeSerBorradorPeroNoEnviarse() {
        Pregunta pregunta = crear(opcionesValidas().subList(0, 3));
        assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
        assertThrows(ReglaNegocioException.class, pregunta::enviarRevision);
        assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
    }

    @Test void cincoOpcionesSeRechazanAlEnviar() {
        List<OpcionRespuesta> cinco = new ArrayList<>(opcionesValidas());
        cinco.add(new OpcionRespuesta("E", false));
        assertThrows(ReglaNegocioException.class, crear(cinco)::enviarRevision);
    }

    @Test void ceroRespuestasCorrectasSeRechazan() {
        assertThrows(ReglaNegocioException.class, crear(List.of(
                new OpcionRespuesta("A", false), new OpcionRespuesta("B", false),
                new OpcionRespuesta("C", false), new OpcionRespuesta("D", false)))::enviarRevision);
    }

    @Test void dosRespuestasCorrectasSeRechazan() {
        assertThrows(ReglaNegocioException.class, crear(List.of(
                new OpcionRespuesta("A", true), new OpcionRespuesta("B", true),
                new OpcionRespuesta("C", false), new OpcionRespuesta("D", false)))::enviarRevision);
    }

    @Test void noPermiteEnviarDosVeces() {
        Pregunta pregunta = crear(opcionesValidas());
        pregunta.enviarRevision();
        assertThrows(ReglaNegocioException.class, pregunta::enviarRevision);
    }

    @Test void opcionesSonInmutablesYNoCompartenListaOriginal() {
        List<OpcionRespuesta> original = new ArrayList<>(opcionesValidas());
        Pregunta pregunta = crear(original);
        original.clear();
        assertEquals(4, pregunta.getOpciones().size());
        assertThrows(UnsupportedOperationException.class, () -> pregunta.getOpciones().clear());
    }

    @Test void noPermiteOpcionNula() {
        List<OpcionRespuesta> opciones = new ArrayList<>(opcionesValidas());
        opciones.set(1, null);
        assertThrows(ReglaNegocioException.class, () -> crear(opciones));
    }

    @Test void noPermiteRehidratarPendienteConOpcionesInvalidas() {
        assertThrows(ReglaNegocioException.class, () -> new Pregunta(UUID.randomUUID(),
                new Enunciado("Enunciado"), new Justificacion("Justificacion suficiente"),
                opcionesValidas().subList(0, 2), EstadoPregunta.PENDIENTE_REVISION));
    }
}
