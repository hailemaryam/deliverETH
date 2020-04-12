package et.com.delivereth.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TelegramDeliveryUserMapperTest {

    private TelegramDeliveryUserMapper telegramDeliveryUserMapper;

    @BeforeEach
    public void setUp() {
        telegramDeliveryUserMapper = new TelegramDeliveryUserMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(telegramDeliveryUserMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(telegramDeliveryUserMapper.fromId(null)).isNull();
    }
}
