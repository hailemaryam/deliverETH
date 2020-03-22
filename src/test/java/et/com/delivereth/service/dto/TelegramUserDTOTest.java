package et.com.delivereth.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import et.com.delivereth.web.rest.TestUtil;

public class TelegramUserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelegramUserDTO.class);
        TelegramUserDTO telegramUserDTO1 = new TelegramUserDTO();
        telegramUserDTO1.setId(1L);
        TelegramUserDTO telegramUserDTO2 = new TelegramUserDTO();
        assertThat(telegramUserDTO1).isNotEqualTo(telegramUserDTO2);
        telegramUserDTO2.setId(telegramUserDTO1.getId());
        assertThat(telegramUserDTO1).isEqualTo(telegramUserDTO2);
        telegramUserDTO2.setId(2L);
        assertThat(telegramUserDTO1).isNotEqualTo(telegramUserDTO2);
        telegramUserDTO1.setId(null);
        assertThat(telegramUserDTO1).isNotEqualTo(telegramUserDTO2);
    }
}
