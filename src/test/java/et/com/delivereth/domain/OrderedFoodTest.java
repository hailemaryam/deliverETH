package et.com.delivereth.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class OrderedFoodTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderedFood.class);
        OrderedFood orderedFood1 = new OrderedFood();
        orderedFood1.setId(1L);
        OrderedFood orderedFood2 = new OrderedFood();
        orderedFood2.setId(orderedFood1.getId());
        assertThat(orderedFood1).isEqualTo(orderedFood2);
        orderedFood2.setId(2L);
        assertThat(orderedFood1).isNotEqualTo(orderedFood2);
        orderedFood1.setId(null);
        assertThat(orderedFood1).isNotEqualTo(orderedFood2);
    }
}
