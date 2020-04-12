package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.Telegram.telegramTransport.ChatStepConstants;
import et.com.delivereth.service.TelegramDeliveryUserQueryService;
import et.com.delivereth.service.TelegramDeliveryUserService;
import et.com.delivereth.service.dto.TelegramDeliveryUserCriteria;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import io.github.jhipster.service.filter.StringFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelegramDeliveryUserDbUtility {
    private final TelegramDeliveryUserService telegramDeliveryUserService;
    private final TelegramDeliveryUserQueryService telegramDeliveryUserQueryService;

    public TelegramDeliveryUserDbUtility(TelegramDeliveryUserService telegramDeliveryUserService, TelegramDeliveryUserQueryService telegramDeliveryUserQueryService) {
        this.telegramDeliveryUserService = telegramDeliveryUserService;
        this.telegramDeliveryUserQueryService = telegramDeliveryUserQueryService;
    }

    public void registerTelegramUser(Update update){
        TelegramDeliveryUserDTO telegramUserDTO = new TelegramDeliveryUserDTO();
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
        telegramDeliveryUserService.save(telegramUserDTO);
    }
    public void registerUserPhone(Update update, TelegramDeliveryUserDTO telegramUser) {
        telegramUser.setPhone(update.getMessage().getContact().getPhoneNumber());
        telegramUser.setConversationMetaData(ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE);
        telegramDeliveryUserService.save(telegramUser);
    }
    public TelegramDeliveryUserDTO getTelegramUser(Update update) {
        List<TelegramDeliveryUserDTO> telegramUserDTOList = new ArrayList<>();
        TelegramDeliveryUserCriteria telegramUserCriteria = new TelegramDeliveryUserCriteria();
        StringFilter stringFilter = new StringFilter();
        if (update.hasMessage()) {
            stringFilter.setEquals(update.getMessage().getFrom().getUserName());
            telegramUserCriteria.setUserName(stringFilter);
            telegramUserDTOList = telegramDeliveryUserQueryService.findByCriteria(telegramUserCriteria);
        } else if (update.hasCallbackQuery()){
            stringFilter.setEquals(update.getCallbackQuery().getFrom().getUserName());
            telegramUserCriteria.setUserName(stringFilter);
            telegramUserDTOList = telegramDeliveryUserQueryService.findByCriteria(telegramUserCriteria);
        }
        return telegramUserDTOList.size() > 0 ? telegramUserDTOList.get(0): null;
    }
    public void updateTelegramUser(TelegramDeliveryUserDTO telegramUser){
        telegramDeliveryUserService.save(telegramUser);
    }

    public List<TelegramDeliveryUserDTO> getDeliveryUser(Float latitude, Float longitude){
        return telegramDeliveryUserService.findAll(PageRequest.of(0, 7)).toList();
    }

}
