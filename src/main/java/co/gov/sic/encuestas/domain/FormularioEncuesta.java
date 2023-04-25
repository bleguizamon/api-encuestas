package co.gov.sic.encuestas.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A FormularioEncuesta.
 */
@Entity
@Table(name = "formulario_encuesta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormularioEncuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_documento", nullable = false, unique = true)
    private Integer numeroDocumento;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "comentarios")
    private String comentarios;

    @Column(name = "fecha_respuesta")
    private Instant fechaRespuesta;

    @ManyToOne
    private Computador marcaFavoritaPC;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FormularioEncuesta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroDocumento() {
        return this.numeroDocumento;
    }

    public FormularioEncuesta numeroDocumento(Integer numeroDocumento) {
        this.setNumeroDocumento(numeroDocumento);
        return this;
    }

    public void setNumeroDocumento(Integer numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getEmail() {
        return this.email;
    }

    public FormularioEncuesta email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComentarios() {
        return this.comentarios;
    }

    public FormularioEncuesta comentarios(String comentarios) {
        this.setComentarios(comentarios);
        return this;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Instant getFechaRespuesta() {
        return this.fechaRespuesta;
    }

    public FormularioEncuesta fechaRespuesta(Instant fechaRespuesta) {
        this.setFechaRespuesta(fechaRespuesta);
        return this;
    }

    public void setFechaRespuesta(Instant fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public Computador getMarcaFavoritaPC() {
        return this.marcaFavoritaPC;
    }

    public void setMarcaFavoritaPC(Computador computador) {
        this.marcaFavoritaPC = computador;
    }

    public FormularioEncuesta marcaFavoritaPC(Computador computador) {
        this.setMarcaFavoritaPC(computador);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormularioEncuesta)) {
            return false;
        }
        return id != null && id.equals(((FormularioEncuesta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormularioEncuesta{" +
            "id=" + getId() +
            ", numeroDocumento=" + getNumeroDocumento() +
            ", email='" + getEmail() + "'" +
            ", comentarios='" + getComentarios() + "'" +
            ", fechaRespuesta='" + getFechaRespuesta() + "'" +
            "}";
    }
}
