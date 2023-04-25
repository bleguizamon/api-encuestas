package co.gov.sic.encuestas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.gov.sic.encuestas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormularioEncuestaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormularioEncuestaDTO.class);
        FormularioEncuestaDTO formularioEncuestaDTO1 = new FormularioEncuestaDTO();
        formularioEncuestaDTO1.setId(1L);
        FormularioEncuestaDTO formularioEncuestaDTO2 = new FormularioEncuestaDTO();
        assertThat(formularioEncuestaDTO1).isNotEqualTo(formularioEncuestaDTO2);
        formularioEncuestaDTO2.setId(formularioEncuestaDTO1.getId());
        assertThat(formularioEncuestaDTO1).isEqualTo(formularioEncuestaDTO2);
        formularioEncuestaDTO2.setId(2L);
        assertThat(formularioEncuestaDTO1).isNotEqualTo(formularioEncuestaDTO2);
        formularioEncuestaDTO1.setId(null);
        assertThat(formularioEncuestaDTO1).isNotEqualTo(formularioEncuestaDTO2);
    }
}
