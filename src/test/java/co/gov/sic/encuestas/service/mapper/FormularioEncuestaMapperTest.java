package co.gov.sic.encuestas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormularioEncuestaMapperTest {

    private FormularioEncuestaMapper formularioEncuestaMapper;

    @BeforeEach
    public void setUp() {
        formularioEncuestaMapper = new FormularioEncuestaMapperImpl();
    }
}
