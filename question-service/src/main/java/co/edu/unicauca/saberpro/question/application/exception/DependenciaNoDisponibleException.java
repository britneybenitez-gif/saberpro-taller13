package co.edu.unicauca.saberpro.question.application.exception;

public class DependenciaNoDisponibleException extends RuntimeException {
    public DependenciaNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
