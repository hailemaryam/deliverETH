package et.com.delivereth.Telegram;

import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.repository.TelegramUserRepository;
import et.com.delivereth.service.TelegramUserService;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class DbUtility {
    private final TelegramUserService telegramUserService;
    private final TelegramUserRepository telegramUserRepository;

    public DbUtility(
        TelegramUserService telegramUserService,
        TelegramUserRepository telegramUserRepository) {
        this.telegramUserService = telegramUserService;
        this.telegramUserRepository = telegramUserRepository;
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
    public void registerUserPhone(Update update) {
        Optional<TelegramUser> telegramUserByUserNameEquals =
            this.telegramUserRepository
                .findTelegramUserByUserNameEquals(update.getMessage().getFrom().getUserName());
        TelegramUser telegramUser = telegramUserByUserNameEquals.get();
        telegramUser.setPhone(update.getMessage().getContact().getPhoneNumber());
        telegramUser.setConversationMetaData(ChatStepConstants.WAITING_FOR_ORDER_RESPONSE);
        telegramUserRepository.save(telegramUser);
    }
    public TelegramUser getTelegramUser(Update update) {
        Optional<TelegramUser> telegramUserByUserNameEquals =
            this.telegramUserRepository
                .findTelegramUserByUserNameEquals(update.getMessage().getFrom().getUserName());
        return telegramUserByUserNameEquals.isPresent()? telegramUserByUserNameEquals.get(): null;
    }
}
