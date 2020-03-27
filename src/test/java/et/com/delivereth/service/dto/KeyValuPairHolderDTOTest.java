package et.com.delivereth.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class KeyValuPairHolderDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyValuPairHolderDTO.class);
        KeyValuPairHolderDTO keyValuPairHolderDTO1 = new KeyValuPairHolderDTO();
        keyValuPairHolderDTO1.setId(1L);
        KeyValuPairHolderDTO keyValuPairHolderDTO2 = new KeyValuPairHolderDTO();
        assertThat(keyValuPairHolderDTO1).isNotEqualTo(keyValuPairHolderDTO2);
        keyValuPairHolderDTO2.setId(keyValuPairHolderDTO1.getId());
        assertThat(keyValuPairHolderDTO1).isEqualTo(keyValuPairHolderDTO2);
        keyValuPairHolderDTO2.setId(2L);
        assertThat(keyValuPairHolderDTO1).isNotEqualTo(keyValuPairHolderDTO2);
        keyValuPairHolderDTO1.setId(null);
        assertThat(keyValuPairHolderDTO1).isNotEqualTo(keyValuPairHolderDTO2);
    }
}
