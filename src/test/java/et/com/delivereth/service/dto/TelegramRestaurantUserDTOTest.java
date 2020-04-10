package et.com.delivereth.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class TelegramRestaurantUserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelegramRestaurantUserDTO.class);
        TelegramRestaurantUserDTO telegramRestaurantUserDTO1 = new TelegramRestaurantUserDTO();
        telegramRestaurantUserDTO1.setId(1L);
        TelegramRestaurantUserDTO telegramRestaurantUserDTO2 = new TelegramRestaurantUserDTO();
        assertThat(telegramRestaurantUserDTO1).isNotEqualTo(telegramRestaurantUserDTO2);
        telegramRestaurantUserDTO2.setId(telegramRestaurantUserDTO1.getId());
        assertThat(telegramRestaurantUserDTO1).isEqualTo(telegramRestaurantUserDTO2);
        telegramRestaurantUserDTO2.setId(2L);
        assertThat(telegramRestaurantUserDTO1).isNotEqualTo(telegramRestaurantUserDTO2);
        telegramRestaurantUserDTO1.setId(null);
        assertThat(telegramRestaurantUserDTO1).isNotEqualTo(telegramRestaurantUserDTO2);
    }
}
