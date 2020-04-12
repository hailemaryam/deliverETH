package et.com.delivereth.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class TelegramDeliveryUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelegramDeliveryUser.class);
        TelegramDeliveryUser telegramDeliveryUser1 = new TelegramDeliveryUser();
        telegramDeliveryUser1.setId(1L);
        TelegramDeliveryUser telegramDeliveryUser2 = new TelegramDeliveryUser();
        telegramDeliveryUser2.setId(telegramDeliveryUser1.getId());
        assertThat(telegramDeliveryUser1).isEqualTo(telegramDeliveryUser2);
        telegramDeliveryUser2.setId(2L);
        assertThat(telegramDeliveryUser1).isNotEqualTo(telegramDeliveryUser2);
        telegramDeliveryUser1.setId(null);
        assertThat(telegramDeliveryUser1).isNotEqualTo(telegramDeliveryUser2);
    }
}
