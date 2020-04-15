package et.com.delivereth.Telegram.telegramUser.processor;

import et.com.delivereth.Telegram.DbUtility.OrderDbUtility;
import et.com.delivereth.Telegram.telegramUser.requests.*;
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
            if (inAddis(update)) {
                orderDbUtility.registerOrder(update, telegramUser);
                requestRestorantSelection.sendTitle(update);
                requestRestorantSelection.requestRestorantSelection(update, telegramUser);
            } else {
                requestLocation.requestForErrorOutofAddiss(update);
            }
        } else if ((update.hasMessage() && update.getMessage().getLocation() == null) || update.hasCallbackQuery()) {
            requestLocation.requestLocationAgain(update);
            commandProcessor.cancelOrder(update, telegramUser);
        } else {
            commandProcessor.requestForErrorResponder(update, telegramUser);
        }
    }
//    top 9.096078
//    botom 8.838826
//    right 38.915886
//    left 38.661270
    boolean inAddis(Update update){
        return update.getMessage().getLocation().getLatitude() <= 9.096078 &&
            update.getMessage().getLocation().getLatitude() >= 8.838826 &&
            update.getMessage().getLocation().getLongitude() <= 38.915886 &&
            update.getMessage().getLocation().getLongitude() >= 38.661270
         ?  true : false;
    }
}
