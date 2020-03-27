package et.com.delivereth.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class KeyValuPairHolerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyValuPairHoler.class);
        KeyValuPairHoler keyValuPairHoler1 = new KeyValuPairHoler();
        keyValuPairHoler1.setId(1L);
        KeyValuPairHoler keyValuPairHoler2 = new KeyValuPairHoler();
        keyValuPairHoler2.setId(keyValuPairHoler1.getId());
        assertThat(keyValuPairHoler1).isEqualTo(keyValuPairHoler2);
        keyValuPairHoler2.setId(2L);
        assertThat(keyValuPairHoler1).isNotEqualTo(keyValuPairHoler2);
        keyValuPairHoler1.setId(null);
        assertThat(keyValuPairHoler1).isNotEqualTo(keyValuPairHoler2);
    }
}
