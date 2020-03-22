package et.com.delivereth.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class TelegramUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelegramUser.class);
        TelegramUser telegramUser1 = new TelegramUser();
        telegramUser1.setId(1L);
        TelegramUser telegramUser2 = new TelegramUser();
        telegramUser2.setId(telegramUser1.getId());
        assertThat(telegramUser1).isEqualTo(telegramUser2);
        telegramUser2.setId(2L);
        assertThat(telegramUser1).isNotEqualTo(telegramUser2);
        telegramUser1.setId(null);
        assertThat(telegramUser1).isNotEqualTo(telegramUser2);
    }
}
