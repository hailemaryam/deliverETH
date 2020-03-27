package et.com.delivereth.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class KeyValuPairHolerDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyValuPairHolerDTO.class);
        KeyValuPairHolerDTO keyValuPairHolerDTO1 = new KeyValuPairHolerDTO();
        keyValuPairHolerDTO1.setId(1L);
        KeyValuPairHolerDTO keyValuPairHolerDTO2 = new KeyValuPairHolerDTO();
        assertThat(keyValuPairHolerDTO1).isNotEqualTo(keyValuPairHolerDTO2);
        keyValuPairHolerDTO2.setId(keyValuPairHolerDTO1.getId());
        assertThat(keyValuPairHolerDTO1).isEqualTo(keyValuPairHolerDTO2);
        keyValuPairHolerDTO2.setId(2L);
        assertThat(keyValuPairHolerDTO1).isNotEqualTo(keyValuPairHolerDTO2);
        keyValuPairHolerDTO1.setId(null);
        assertThat(keyValuPairHolerDTO1).isNotEqualTo(keyValuPairHolerDTO2);
    }
}
