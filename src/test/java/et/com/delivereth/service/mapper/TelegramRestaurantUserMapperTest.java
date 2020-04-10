package et.com.delivereth.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TelegramRestaurantUserMapperTest {

    private TelegramRestaurantUserMapper telegramRestaurantUserMapper;

    @BeforeEach
    public void setUp() {
        telegramRestaurantUserMapper = new TelegramRestaurantUserMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(telegramRestaurantUserMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(telegramRestaurantUserMapper.fromId(null)).isNull();
    }
}
