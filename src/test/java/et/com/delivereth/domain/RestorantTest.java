package et.com.delivereth.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class RestorantTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Restorant.class);
        Restorant restorant1 = new Restorant();
        restorant1.setId(1L);
        Restorant restorant2 = new Restorant();
        restorant2.setId(restorant1.getId());
        assertThat(restorant1).isEqualTo(restorant2);
        restorant2.setId(2L);
        assertThat(restorant1).isNotEqualTo(restorant2);
        restorant1.setId(null);
        assertThat(restorant1).isNotEqualTo(restorant2);
    }
}
