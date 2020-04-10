package et.com.delivereth.Telegram.telegramRestorant;

import et.com.delivereth.Telegram.DbUtility.TelegramUserDbUtility;
import et.com.delivereth.Telegram.telegramUser.processor.MainCommandProccessor;
import et.com.delivereth.Telegram.telegramUser.processor.MainStepProccessor;
import et.com.delivereth.Telegram.telegramUser.requests.RequestContact;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RestaurantResponseBuilder {
    private final RequestContact requestContact;
    private final MainStepProccessor mainStepProccessor;
    private final MainCommandProccessor mainCommandProccessor;
    private final TelegramUserDbUtility telegramUserDbUtility;

    public RestaurantResponseBuilder(RequestContact requestContact, MainStepProccessor mainStepProccessor, MainCommandProccessor mainCommandProccessor, TelegramUserDbUtility telegramUserDbUtility) {
        this.requestContact = requestContact;
        this.mainStepProccessor = mainStepProccessor;
        this.mainCommandProccessor = mainCommandProccessor;
        this.telegramUserDbUtility = telegramUserDbUtility;
    }

    public void getResponse(Update update) {
        TelegramUserDTO telegramUser = telegramUserDbUtility.getTelegramUser(update);
        if (telegramUser == null) {
            telegramUserDbUtility.registerTelegramUser(update);
            requestContact.requestContact(update);
        } else if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                mainCommandProccessor.commandProccessor(update, telegramUser);
            } else {
                mainStepProccessor.mainStepProcessor(update, telegramUser);
            }
        } else {
            mainStepProccessor.mainStepProcessor(update, telegramUser);
        }
    }
}
