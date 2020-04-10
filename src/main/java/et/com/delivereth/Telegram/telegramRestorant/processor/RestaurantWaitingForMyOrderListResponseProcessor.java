package et.com.delivereth.Telegram.telegramRestorant.processor;

import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.DbUtility.OrderDbUtility;
import et.com.delivereth.Telegram.telegramUser.ChatStepConstants;
import et.com.delivereth.Telegram.telegramUser.requests.RequestForMyOrdersList;
import et.com.delivereth.Telegram.telegramUser.requests.RequestLocation;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RestaurantWaitingForMyOrderListResponseProcessor {
    private final RequestLocation requestLocation;
    private final RequestForMyOrdersList requestForMyOrdersList;
    private final DbUtility dbUtility;
    private final RestaurantCommandProcessor restaurantCommandProcessor;
    private final OrderDbUtility orderDbUtility;

    public RestaurantWaitingForMyOrderListResponseProcessor(RequestLocation requestLocation, RequestForMyOrdersList requestForMyOrdersList, DbUtility dbUtility, RestaurantCommandProcessor restaurantCommandProcessor, OrderDbUtility orderDbUtility) {
        this.requestLocation = requestLocation;
        this.requestForMyOrdersList = requestForMyOrdersList;
        this.dbUtility = dbUtility;
        this.restaurantCommandProcessor = restaurantCommandProcessor;
        this.orderDbUtility = orderDbUtility;
    }

    void processMyOrderResponse(Update update, TelegramUserDTO telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("C_")) {
            orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(2)), OrderStatus.CANCELED_BY_USER);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("R_")) {
            orderDbUtility.orderRemove(Long.valueOf(update.getCallbackQuery().getData().substring(2)));
        }  else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("order") ||
            (update.hasMessage() && update.getMessage().getText().equals("New Order"))) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE);
            requestLocation.requestLocation(update);
        } else if ((update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("myOrder")) ||
            (update.hasMessage() && update.getMessage().getText().equals("My Orders"))) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE);
            requestForMyOrdersList.sendTitle(update);
            requestForMyOrdersList.requestForMyOrdersList(update, telegramUser);
        } else {
            restaurantCommandProcessor.requestForErrorResponder(update, telegramUser);
        }
    }
}
