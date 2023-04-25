package co.gov.sic.encuestas.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.gov.sic.encuestas.domain.FormularioEncuesta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormularioEncuestaDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer numeroDocumento;

    @NotNull
    private String email;

    private String comentarios;

    private Instant fechaRespuesta;

    private ComputadorDTO marcaFavoritaPC;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Integer numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Instant getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(Instant fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public ComputadorDTO getMarcaFavoritaPC() {
        return marcaFavoritaPC;
    }

    public void setMarcaFavoritaPC(ComputadorDTO marcaFavoritaPC) {
        this.marcaFavoritaPC = marcaFavoritaPC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormularioEncuestaDTO)) {
            return false;
        }

        FormularioEncuestaDTO formularioEncuestaDTO = (FormularioEncuestaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formularioEncuestaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormularioEncuestaDTO{" +
            "id=" + getId() +
            ", numeroDocumento=" + getNumeroDocumento() +
            ", email='" + getEmail() + "'" +
            ", comentarios='" + getComentarios() + "'" +
            ", fechaRespuesta='" + getFechaRespuesta() + "'" +
            ", marcaFavoritaPC=" + getMarcaFavoritaPC() +
            "}";
    }
}
