package co.gov.sic.encuestas.service.mapper;

import co.gov.sic.encuestas.domain.Computador;
import co.gov.sic.encuestas.domain.FormularioEncuesta;
import co.gov.sic.encuestas.service.dto.ComputadorDTO;
import co.gov.sic.encuestas.service.dto.FormularioEncuestaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormularioEncuesta} and its DTO {@link FormularioEncuestaDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormularioEncuestaMapper extends EntityMapper<FormularioEncuestaDTO, FormularioEncuesta> {
    @Mapping(target = "marcaFavoritaPC", source = "marcaFavoritaPC", qualifiedByName = "computadorMarca")
    FormularioEncuestaDTO toDto(FormularioEncuesta s);

    @Named("computadorMarca")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "marca", source = "marca")
    ComputadorDTO toDtoComputadorMarca(Computador computador);
}
