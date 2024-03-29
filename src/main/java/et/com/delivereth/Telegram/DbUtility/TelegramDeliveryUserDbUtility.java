package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.Telegram.telegramTransport.ChatStepConstants;
import et.com.delivereth.service.TelegramDeliveryUserQueryService;
import et.com.delivereth.service.TelegramDeliveryUserService;
import et.com.delivereth.service.dto.RestorantDTO;
import et.com.delivereth.service.dto.TelegramDeliveryUserCriteria;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
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
            telegramUserDTO.setUserId(update.getMessage().getFrom().getId());
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
        IntegerFilter integerFilter = new IntegerFilter();
        if (update.hasMessage()) {
            integerFilter.setEquals(update.getMessage().getFrom().getId());
            telegramUserCriteria.setUserId(integerFilter);
            telegramUserDTOList = telegramDeliveryUserQueryService.findByCriteria(telegramUserCriteria);
        } else if (update.hasCallbackQuery()){
            integerFilter.setEquals(update.getCallbackQuery().getFrom().getId());
            telegramUserCriteria.setUserId(integerFilter);
            telegramUserDTOList = telegramDeliveryUserQueryService.findByCriteria(telegramUserCriteria);
        }
        return telegramUserDTOList.size() > 0 ? telegramUserDTOList.get(0): null;
    }
    public void updateTelegramUser(TelegramDeliveryUserDTO telegramUser){
        telegramDeliveryUserService.save(telegramUser);
    }

    public List<TelegramDeliveryUserDTO> getDeliveryUser(RestorantDTO restorantDTO){
        TelegramDeliveryUserCriteria telegramDeliveryUserCriteria = new TelegramDeliveryUserCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(restorantDTO.getId());
        telegramDeliveryUserCriteria.setRestorantId(longFilter);
        return telegramDeliveryUserQueryService.findByCriteria(telegramDeliveryUserCriteria);
    }

}
