package et.com.delivereth.Telegram.telegramRestorant.processor;

import et.com.delivereth.Telegram.DbUtility.TelegramUserDbUtility;
import et.com.delivereth.Telegram.telegramUser.requests.RequestContact;
import et.com.delivereth.Telegram.telegramUser.requests.RequestForMenu;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RestaurantWaitingForContactResponseProcessor {
    private final RequestContact requestContact;
    private final RequestForMenu requestForMenu;
    private final TelegramUserDbUtility telegramUserDbUtility;

    public RestaurantWaitingForContactResponseProcessor(RequestContact requestContact, RequestForMenu requestForMenu, TelegramUserDbUtility telegramUserDbUtility) {
        this.requestContact = requestContact;
        this.requestForMenu = requestForMenu;
        this.telegramUserDbUtility = telegramUserDbUtility;
    }

    void processContactAndProceedToOrder(Update update, TelegramUserDTO telegramUser) {
        if (update.getMessage().getContact() != null) {
            telegramUserDbUtility.registerUserPhone(update, telegramUser);
            requestForMenu.requestForMenu(update, telegramUser);
        } else {
            requestContact.requestContactAgain(update);
        }
    }

}
