package co.gov.sic.encuestas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.gov.sic.encuestas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComputadorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Computador.class);
        Computador computador1 = new Computador();
        computador1.setId(1L);
        Computador computador2 = new Computador();
        computador2.setId(computador1.getId());
        assertThat(computador1).isEqualTo(computador2);
        computador2.setId(2L);
        assertThat(computador1).isNotEqualTo(computador2);
        computador1.setId(null);
        assertThat(computador1).isNotEqualTo(computador2);
    }
}
