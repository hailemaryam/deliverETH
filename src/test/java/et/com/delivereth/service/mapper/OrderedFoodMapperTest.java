package et.com.delivereth.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderedFoodMapperTest {

    private OrderedFoodMapper orderedFoodMapper;

    @BeforeEach
    public void setUp() {
        orderedFoodMapper = new OrderedFoodMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(orderedFoodMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(orderedFoodMapper.fromId(null)).isNull();
    }
}
