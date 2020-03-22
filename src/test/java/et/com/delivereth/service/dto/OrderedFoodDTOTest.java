package et.com.delivereth.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class OrderedFoodDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderedFoodDTO.class);
        OrderedFoodDTO orderedFoodDTO1 = new OrderedFoodDTO();
        orderedFoodDTO1.setId(1L);
        OrderedFoodDTO orderedFoodDTO2 = new OrderedFoodDTO();
        assertThat(orderedFoodDTO1).isNotEqualTo(orderedFoodDTO2);
        orderedFoodDTO2.setId(orderedFoodDTO1.getId());
        assertThat(orderedFoodDTO1).isEqualTo(orderedFoodDTO2);
        orderedFoodDTO2.setId(2L);
        assertThat(orderedFoodDTO1).isNotEqualTo(orderedFoodDTO2);
        orderedFoodDTO1.setId(null);
        assertThat(orderedFoodDTO1).isNotEqualTo(orderedFoodDTO2);
    }
}
