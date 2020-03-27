package et.com.delivereth.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class KeyValuPairHolderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyValuPairHolder.class);
        KeyValuPairHolder keyValuPairHolder1 = new KeyValuPairHolder();
        keyValuPairHolder1.setId(1L);
        KeyValuPairHolder keyValuPairHolder2 = new KeyValuPairHolder();
        keyValuPairHolder2.setId(keyValuPairHolder1.getId());
        assertThat(keyValuPairHolder1).isEqualTo(keyValuPairHolder2);
        keyValuPairHolder2.setId(2L);
        assertThat(keyValuPairHolder1).isNotEqualTo(keyValuPairHolder2);
        keyValuPairHolder1.setId(null);
        assertThat(keyValuPairHolder1).isNotEqualTo(keyValuPairHolder2);
    }
}
