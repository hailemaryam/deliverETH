package et.com.delivereth.Telegram.telegramUser.processor;

import et.com.delivereth.Telegram.DbUtility.TelegramUserDbUtility;
import et.com.delivereth.Telegram.telegramUser.requests.*;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class WaitingForContactResponseProcessor {
    private final RequestContact requestContact;
    private final RequestForMenu requestForMenu;
    private final TelegramUserDbUtility telegramUserDbUtility;

    public WaitingForContactResponseProcessor(RequestContact requestContact, RequestForMenu requestForMenu, TelegramUserDbUtility telegramUserDbUtility) {
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
