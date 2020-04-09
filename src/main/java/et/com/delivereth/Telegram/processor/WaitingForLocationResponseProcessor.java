package et.com.delivereth.Telegram.processor;

import et.com.delivereth.Telegram.DbUtility.OrderDbUtility;
import et.com.delivereth.Telegram.requests.*;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class WaitingForLocationResponseProcessor {
    private final RequestLocation requestLocation;
    private final RequestRestorantSelection requestRestorantSelection;
    private final OrderDbUtility orderDbUtility;
    private final CommandProcessor commandProcessor;

    public WaitingForLocationResponseProcessor(RequestLocation requestLocation, RequestRestorantSelection requestRestorantSelection, OrderDbUtility orderDbUtility, CommandProcessor commandProcessor) {
        this.requestLocation = requestLocation;
        this.requestRestorantSelection = requestRestorantSelection;
        this.orderDbUtility = orderDbUtility;
        this.commandProcessor = commandProcessor;
    }

    void processLocationRequestAndProceedToRestorantRequest(Update update, TelegramUserDTO telegramUser) {
        if (update.hasMessage() && update.getMessage().getLocation() != null) {
            orderDbUtility.registerOrder(update, telegramUser);
            requestRestorantSelection.sendTitle(update);
            requestRestorantSelection.requestRestorantSelection(update, telegramUser);
        } else if ((update.hasMessage() && update.getMessage().getLocation() == null) || update.hasCallbackQuery()) {
            requestLocation.requestLocationAgain(update);
        } else {
            commandProcessor.requestForErrorResponder(update, telegramUser);
        }
    }
}
