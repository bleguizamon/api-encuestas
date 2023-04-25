package co.gov.sic.encuestas.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.gov.sic.encuestas.domain.Computador} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComputadorDTO implements Serializable {

    private Long id;

    private String marca;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComputadorDTO)) {
            return false;
        }

        ComputadorDTO computadorDTO = (ComputadorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, computadorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComputadorDTO{" +
            "id=" + getId() +
            ", marca='" + getMarca() + "'" +
            "}";
    }
}
