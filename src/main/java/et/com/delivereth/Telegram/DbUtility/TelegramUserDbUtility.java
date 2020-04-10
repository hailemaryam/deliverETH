package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.Telegram.telegramUser.ChatStepConstants;
import et.com.delivereth.service.TelegramUserQueryService;
import et.com.delivereth.service.TelegramUserService;
import et.com.delivereth.service.dto.TelegramUserCriteria;
import et.com.delivereth.service.dto.TelegramUserDTO;
import io.github.jhipster.service.filter.StringFilter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelegramUserDbUtility {
    private final TelegramUserService telegramUserService;
    private final TelegramUserQueryService telegramUserQueryService;

    public TelegramUserDbUtility(TelegramUserService telegramUserService, TelegramUserQueryService telegramUserQueryService) {
        this.telegramUserService = telegramUserService;
        this.telegramUserQueryService = telegramUserQueryService;
    }
    public void registerTelegramUser(Update update){
        TelegramUserDTO telegramUserDTO = new TelegramUserDTO();
        if (update.hasMessage()){
            telegramUserDTO.setChatId(update.getMessage().getChatId().toString());
            telegramUserDTO.setFirstName(update.getMessage().getFrom().getFirstName());
            telegramUserDTO.setLastName(update.getMessage().getFrom().getLastName());
            telegramUserDTO.setUserName(update.getMessage().getFrom().getUserName());
            if (update.getMessage().getContact() != null) {
                telegramUserDTO.setPhone(update.getMessage().getContact().getPhoneNumber());
            }
            telegramUserDTO.setConversationMetaData(ChatStepConstants.WAITING_FOR_CONTACT_RESPONSE);
        }
        telegramUserService.save(telegramUserDTO);
    }
    public void registerUserPhone(Update update, TelegramUserDTO telegramUser) {
        telegramUser.setPhone(update.getMessage().getContact().getPhoneNumber());
        telegramUser.setConversationMetaData(ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE);
        telegramUserService.save(telegramUser);
    }
    public TelegramUserDTO getTelegramUser(Update update) {
        List<TelegramUserDTO> telegramUserDTOList = new ArrayList<>();
        TelegramUserCriteria telegramUserCriteria = new TelegramUserCriteria();
        StringFilter stringFilter = new StringFilter();
        if (update.hasMessage()) {
            stringFilter.setEquals(update.getMessage().getFrom().getUserName());
            telegramUserCriteria.setUserName(stringFilter);
            telegramUserDTOList = telegramUserQueryService.findByCriteria(telegramUserCriteria);
        } else if (update.hasCallbackQuery()){
            stringFilter.setEquals(update.getCallbackQuery().getFrom().getUserName());
            telegramUserCriteria.setUserName(stringFilter);
            telegramUserDTOList = telegramUserQueryService.findByCriteria(telegramUserCriteria);
        }
        return telegramUserDTOList.size() > 0 ? telegramUserDTOList.get(0): null;
    }
    public void updateTelegramUser(TelegramUserDTO telegramUser){
        telegramUserService.save(telegramUser);
    }

}
