package et.com.delivereth.Telegram.processor;

import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.requests.*;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class WaitingForLocationResponseProcessor {
    private final RequestLocation requestLocation;
    private final RequestRestorantSelection requestRestorantSelection;
    private final DbUtility dbUtility;
    private final CommandProcessor commandProcessor;

    public WaitingForLocationResponseProcessor(RequestLocation requestLocation, RequestRestorantSelection requestRestorantSelection, DbUtility dbUtility, CommandProcessor commandProcessor) {
        this.requestLocation = requestLocation;
        this.requestRestorantSelection = requestRestorantSelection;
        this.dbUtility = dbUtility;
        this.commandProcessor = commandProcessor;
    }

    void processLocationRequestAndProceedToRestorantRequest(Update update, TelegramUserDTO telegramUser) {
        if (update.hasMessage() && update.getMessage().getLocation() != null) {
            dbUtility.registerOrder(update, telegramUser);
            requestRestorantSelection.sendTitle(update);
            requestRestorantSelection.requestRestorantSelection(update, telegramUser);
        } else if ((update.hasMessage() && update.getMessage().getLocation() == null) || update.hasCallbackQuery()) {
            requestLocation.requestLocationAgain(update);
        } else {
            commandProcessor.requestForErrorResponder(update, telegramUser);
        }
    }
}
