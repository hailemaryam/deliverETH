package et.com.delivereth.Telegram;

import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.processor.MainStepProccessor;
import et.com.delivereth.Telegram.processor.MainCommandProccessor;
import et.com.delivereth.Telegram.requests.*;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class ResponseBuilder {
    private final RequestContact requestContact;
    private final DbUtility dbUtility;
    private final MainStepProccessor mainStepProccessor;
    private final MainCommandProccessor mainCommandProccessor;

    public ResponseBuilder(RequestContact requestContact, DbUtility dbUtility, MainStepProccessor mainStepProccessor, MainCommandProccessor mainCommandProccessor) {
        this.requestContact = requestContact;
        this.dbUtility = dbUtility;
        this.mainStepProccessor = mainStepProccessor;
        this.mainCommandProccessor = mainCommandProccessor;
    }

    void getResponse(Update update) {
        TelegramUserDTO telegramUser = dbUtility.getTelegramUser(update);
        if (telegramUser == null) {
            dbUtility.registerTelegramUser(update);
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
