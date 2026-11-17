package co.edu.unicauca.saberpro.question.infrastructure.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class OpcionEmbeddable {
    @Column(nullable = false, length = 2000)
    private String texto;
    @Column(nullable = false)
    private boolean correcta;

    protected OpcionEmbeddable() {} // Hibernate

    public OpcionEmbeddable(String texto, boolean correcta) {
        this.texto = texto;
        this.correcta = correcta;
    }

    public String getTexto() { return texto; }
    public boolean isCorrecta() { return correcta; }
}
