package co.gov.sic.encuestas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.gov.sic.encuestas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComputadorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComputadorDTO.class);
        ComputadorDTO computadorDTO1 = new ComputadorDTO();
        computadorDTO1.setId(1L);
        ComputadorDTO computadorDTO2 = new ComputadorDTO();
        assertThat(computadorDTO1).isNotEqualTo(computadorDTO2);
        computadorDTO2.setId(computadorDTO1.getId());
        assertThat(computadorDTO1).isEqualTo(computadorDTO2);
        computadorDTO2.setId(2L);
        assertThat(computadorDTO1).isNotEqualTo(computadorDTO2);
        computadorDTO1.setId(null);
        assertThat(computadorDTO1).isNotEqualTo(computadorDTO2);
    }
}
