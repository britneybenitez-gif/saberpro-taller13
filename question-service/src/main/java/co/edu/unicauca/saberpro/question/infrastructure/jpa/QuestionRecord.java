package co.edu.unicauca.saberpro.question.infrastructure.jpa;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Entidad para infraestructura: NO es el agregado de dominio. */
@Entity
@Table(name = "preguntas")
public class QuestionRecord {
    @Id private UUID id;
    @Column(nullable = false, length = 4000) private String enunciado;
    @Column(nullable = false, length = 4000) private String justificacion;
    @Column(nullable = false) private String estado;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "opciones_pregunta", joinColumns = @JoinColumn(name = "pregunta_id"))
    @OrderColumn(name = "posicion")
    private List<OpcionEmbeddable> opciones = new ArrayList<>();

    protected QuestionRecord() {} // Hibernate

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getJustificacion() { return justificacion; }
    public void setJustificacion(String justificacion) { this.justificacion = justificacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<OpcionEmbeddable> getOpciones() { return opciones; }
    public void setOpciones(List<OpcionEmbeddable> opciones) {
        this.opciones = new ArrayList<>(opciones); // JPA necesita coleccion mutable.
    }
}
