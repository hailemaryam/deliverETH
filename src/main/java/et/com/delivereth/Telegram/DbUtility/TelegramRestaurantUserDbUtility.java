package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.Telegram.telegramRestorant.ChatStepConstants;
import et.com.delivereth.service.TelegramRestaurantUserQueryService;
import et.com.delivereth.service.TelegramRestaurantUserService;
import et.com.delivereth.service.dto.RestorantDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserCriteria;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
@Service
public class TelegramRestaurantUserDbUtility {
    private final TelegramRestaurantUserService telegramRestaurantUserService;
    private final TelegramRestaurantUserQueryService telegramRestaurantUserQueryService;

    public TelegramRestaurantUserDbUtility(TelegramRestaurantUserService telegramRestaurantUserService, TelegramRestaurantUserQueryService telegramRestaurantUserQueryService) {
        this.telegramRestaurantUserService = telegramRestaurantUserService;
        this.telegramRestaurantUserQueryService = telegramRestaurantUserQueryService;
    }

    public void registerTelegramUser(Update update){
        TelegramRestaurantUserDTO telegramUserDTO = new TelegramRestaurantUserDTO();
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
        telegramRestaurantUserService.save(telegramUserDTO);
    }
    public void registerUserPhone(Update update, TelegramRestaurantUserDTO telegramUser) {
        telegramUser.setPhone(update.getMessage().getContact().getPhoneNumber());
        telegramUser.setConversationMetaData(ChatStepConstants.WAITING_FOR_ACCOUNT_LINKING_RESPONSE);
        telegramRestaurantUserService.save(telegramUser);
    }
    public TelegramRestaurantUserDTO getTelegramUser(Update update) {
        List<TelegramRestaurantUserDTO> telegramUserDTOList = new ArrayList<>();
        TelegramRestaurantUserCriteria telegramUserCriteria = new TelegramRestaurantUserCriteria();
        StringFilter stringFilter = new StringFilter();
        if (update.hasMessage()) {
            stringFilter.setEquals(update.getMessage().getFrom().getUserName());
            telegramUserCriteria.setUserName(stringFilter);
            telegramUserDTOList = telegramRestaurantUserQueryService.findByCriteria(telegramUserCriteria);
        } else if (update.hasCallbackQuery()){
            stringFilter.setEquals(update.getCallbackQuery().getFrom().getUserName());
            telegramUserCriteria.setUserName(stringFilter);
            telegramUserDTOList = telegramRestaurantUserQueryService.findByCriteria(telegramUserCriteria);
        }
        return telegramUserDTOList.size() > 0 ? telegramUserDTOList.get(0): null;
    }
    public void updateTelegramUser(TelegramRestaurantUserDTO telegramUser){
        telegramRestaurantUserService.save(telegramUser);
    }

    public List<TelegramRestaurantUserDTO> getRestaurantUsers(RestorantDTO restorantDTO) {
        TelegramRestaurantUserCriteria telegramRestaurantUserCriteria = new TelegramRestaurantUserCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(restorantDTO.getId());
        telegramRestaurantUserCriteria.setRestorantId(longFilter);
        return telegramRestaurantUserQueryService.findByCriteria(telegramRestaurantUserCriteria);
    }
}
