package et.com.delivereth.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class TelegramRestaurantUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelegramRestaurantUser.class);
        TelegramRestaurantUser telegramRestaurantUser1 = new TelegramRestaurantUser();
        telegramRestaurantUser1.setId(1L);
        TelegramRestaurantUser telegramRestaurantUser2 = new TelegramRestaurantUser();
        telegramRestaurantUser2.setId(telegramRestaurantUser1.getId());
        assertThat(telegramRestaurantUser1).isEqualTo(telegramRestaurantUser2);
        telegramRestaurantUser2.setId(2L);
        assertThat(telegramRestaurantUser1).isNotEqualTo(telegramRestaurantUser2);
        telegramRestaurantUser1.setId(null);
        assertThat(telegramRestaurantUser1).isNotEqualTo(telegramRestaurantUser2);
    }
}
