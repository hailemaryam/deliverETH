package et.com.delivereth.Telegram.telegramRestorant.processor;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.telegramUser.ChatStepConstants;
import et.com.delivereth.Telegram.telegramUser.requests.RequestErrorResponder;
import et.com.delivereth.Telegram.telegramUser.requests.RequestForMyOrdersList;
import et.com.delivereth.Telegram.telegramUser.requests.RequestLocation;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RestaurantWaitingForMenuPageResponseProcessor {
    private final RequestLocation requestLocation;
    private final RequestErrorResponder requestErrorResponder;
    private final RequestForMyOrdersList requestForMyOrdersList;
    private final DbUtility dbUtility;

    public RestaurantWaitingForMenuPageResponseProcessor(RequestLocation requestLocation, RequestErrorResponder requestErrorResponder, RequestForMyOrdersList requestForMyOrdersList, DbUtility dbUtility) {
        this.requestLocation = requestLocation;
        this.requestErrorResponder = requestErrorResponder;
        this.requestForMyOrdersList = requestForMyOrdersList;
        this.dbUtility = dbUtility;
    }

    void processOrderMenuRequestAndProceedToLocationRequest(Update update, TelegramUserDTO telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("order") ||
            (update.hasMessage() && update.getMessage().getText().equals(StaticText.newOrder))) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE);
            requestLocation.requestLocation(update);
        } else if ((update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("myOrder")) ||
            (update.hasMessage() && update.getMessage().getText().equals(StaticText.myOrders))) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE);
            requestForMyOrdersList.requestForMyOrdersList(update, telegramUser);
        } else if (update.hasMessage() && update.getMessage().getText().equals(StaticText.help)) {

        } else if (update.hasMessage() && update.getMessage().getText().equals("Setting")) {

        } else {
            requestErrorResponder.userErrorResponseResponder(update);
        }
    }
}
