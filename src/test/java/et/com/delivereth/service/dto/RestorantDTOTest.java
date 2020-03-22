package et.com.delivereth.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class RestorantDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RestorantDTO.class);
        RestorantDTO restorantDTO1 = new RestorantDTO();
        restorantDTO1.setId(1L);
        RestorantDTO restorantDTO2 = new RestorantDTO();
        assertThat(restorantDTO1).isNotEqualTo(restorantDTO2);
        restorantDTO2.setId(restorantDTO1.getId());
        assertThat(restorantDTO1).isEqualTo(restorantDTO2);
        restorantDTO2.setId(2L);
        assertThat(restorantDTO1).isNotEqualTo(restorantDTO2);
        restorantDTO1.setId(null);
        assertThat(restorantDTO1).isNotEqualTo(restorantDTO2);
    }
}
