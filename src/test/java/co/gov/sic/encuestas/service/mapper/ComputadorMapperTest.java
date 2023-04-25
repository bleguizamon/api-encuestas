package co.gov.sic.encuestas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComputadorMapperTest {

    private ComputadorMapper computadorMapper;

    @BeforeEach
    public void setUp() {
        computadorMapper = new ComputadorMapperImpl();
    }
}
