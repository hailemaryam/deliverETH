package et.com.delivereth.Telegram.processor;

import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.requests.*;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class WaitingForContactResponseProcessor {
    private final RequestContact requestContact;
    private final RequestForMenu requestForMenu;
    private final DbUtility dbUtility;

    public WaitingForContactResponseProcessor(RequestContact requestContact, RequestForMenu requestForMenu, DbUtility dbUtility) {
        this.requestContact = requestContact;
        this.requestForMenu = requestForMenu;
        this.dbUtility = dbUtility;
    }

    void processContactAndProceedToOrder(Update update, TelegramUserDTO telegramUser) {
        if (update.getMessage().getContact() != null) {
            dbUtility.registerUserPhone(update, telegramUser);
            requestForMenu.requestForMenu(update, telegramUser);
        } else {
            requestContact.requestContactAgain(update);
        }
    }

}
