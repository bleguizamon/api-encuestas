package co.gov.sic.encuestas.service.mapper;

import co.gov.sic.encuestas.domain.Computador;
import co.gov.sic.encuestas.service.dto.ComputadorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Computador} and its DTO {@link ComputadorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ComputadorMapper extends EntityMapper<ComputadorDTO, Computador> {}
