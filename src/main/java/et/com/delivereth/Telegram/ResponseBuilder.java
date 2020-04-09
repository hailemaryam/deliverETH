package et.com.delivereth.Telegram;

import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.processor.MainStepProccessor;
import et.com.delivereth.Telegram.processor.StepLessCommandProccessor;
import et.com.delivereth.Telegram.processor.StepProcessors;
import et.com.delivereth.Telegram.requests.*;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class ResponseBuilder {
    private final RequestContact requestContact;
    private final DbUtility dbUtility;
    private final StepProcessors stepProcessors;
    private final MainStepProccessor mainStepProccessor;
    private final StepLessCommandProccessor stepLessCommandProccessor;

    public ResponseBuilder(RequestContact requestContact, DbUtility dbUtility, StepProcessors stepProcessors, MainStepProccessor mainStepProccessor, StepLessCommandProccessor stepLessCommandProccessor) {
        this.requestContact = requestContact;
        this.dbUtility = dbUtility;
        this.stepProcessors = stepProcessors;
        this.mainStepProccessor = mainStepProccessor;
        this.stepLessCommandProccessor = stepLessCommandProccessor;
    }

    public void getResponse(Update update) {
        TelegramUserDTO telegramUser = dbUtility.getTelegramUser(update);
        if (telegramUser == null) {
            dbUtility.registerTelegramUser(update);
            requestContact.requestContact(update);
        } else if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                stepLessCommandProccessor.commandProccessor(update, telegramUser);
            } else {
                mainStepProccessor.mainStepProcessor(update, telegramUser);
            }
        } else {
            mainStepProccessor.mainStepProcessor(update, telegramUser);
        }
    }
}
