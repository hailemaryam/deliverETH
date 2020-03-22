package et.com.delivereth.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TelegramUserMapperTest {

    private TelegramUserMapper telegramUserMapper;

    @BeforeEach
    public void setUp() {
        telegramUserMapper = new TelegramUserMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(telegramUserMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(telegramUserMapper.fromId(null)).isNull();
    }
}
