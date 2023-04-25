package co.gov.sic.encuestas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.gov.sic.encuestas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormularioEncuestaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormularioEncuesta.class);
        FormularioEncuesta formularioEncuesta1 = new FormularioEncuesta();
        formularioEncuesta1.setId(1L);
        FormularioEncuesta formularioEncuesta2 = new FormularioEncuesta();
        formularioEncuesta2.setId(formularioEncuesta1.getId());
        assertThat(formularioEncuesta1).isEqualTo(formularioEncuesta2);
        formularioEncuesta2.setId(2L);
        assertThat(formularioEncuesta1).isNotEqualTo(formularioEncuesta2);
        formularioEncuesta1.setId(null);
        assertThat(formularioEncuesta1).isNotEqualTo(formularioEncuesta2);
    }
}
