package et.com.delivereth.Telegram.telegramRestorant;

import et.com.delivereth.Telegram.DbUtility.TelegramUserDbUtility;
import et.com.delivereth.Telegram.telegramRestorant.processor.RestaurantMainCommandProccessor;
import et.com.delivereth.Telegram.telegramRestorant.processor.RestaurantMainStepProccessor;
import et.com.delivereth.Telegram.telegramRestorant.requests.RestaurantRequestContact;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RestaurantResponseBuilder {
    private final RestaurantRequestContact requestContact;
    private final RestaurantMainCommandProccessor mainCommandProccessor;
    private final RestaurantMainStepProccessor mainStepProcessor;
    private final TelegramUserDbUtility telegramUserDbUtility;

    public RestaurantResponseBuilder(RestaurantRequestContact requestContact, RestaurantMainCommandProccessor mainCommandProccessor, RestaurantMainStepProccessor mainStepProcessor, TelegramUserDbUtility telegramUserDbUtility) {
        this.requestContact = requestContact;
        this.mainCommandProccessor = mainCommandProccessor;
        this.mainStepProcessor = mainStepProcessor;
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
                mainStepProcessor.mainStepProcessor(update, telegramUser);
            }
        } else {
            mainStepProcessor.mainStepProcessor(update, telegramUser);
        }
    }
}
