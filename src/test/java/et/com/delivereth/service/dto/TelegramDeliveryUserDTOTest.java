package et.com.delivereth.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class TelegramDeliveryUserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelegramDeliveryUserDTO.class);
        TelegramDeliveryUserDTO telegramDeliveryUserDTO1 = new TelegramDeliveryUserDTO();
        telegramDeliveryUserDTO1.setId(1L);
        TelegramDeliveryUserDTO telegramDeliveryUserDTO2 = new TelegramDeliveryUserDTO();
        assertThat(telegramDeliveryUserDTO1).isNotEqualTo(telegramDeliveryUserDTO2);
        telegramDeliveryUserDTO2.setId(telegramDeliveryUserDTO1.getId());
        assertThat(telegramDeliveryUserDTO1).isEqualTo(telegramDeliveryUserDTO2);
        telegramDeliveryUserDTO2.setId(2L);
        assertThat(telegramDeliveryUserDTO1).isNotEqualTo(telegramDeliveryUserDTO2);
        telegramDeliveryUserDTO1.setId(null);
        assertThat(telegramDeliveryUserDTO1).isNotEqualTo(telegramDeliveryUserDTO2);
    }
}
