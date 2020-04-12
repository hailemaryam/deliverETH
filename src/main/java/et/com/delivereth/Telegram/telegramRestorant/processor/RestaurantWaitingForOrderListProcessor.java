package et.com.delivereth.Telegram.telegramRestorant.processor;

import et.com.delivereth.Telegram.DbUtility.OrderDbUtility;
import et.com.delivereth.Telegram.DbUtility.TelegramUserDbUtility;
import et.com.delivereth.Telegram.telegramRestorant.requests.RestaurantRequestForNewOrder;
import et.com.delivereth.Telegram.telegramUser.requests.RequestForMyOrdersList;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.service.dto.OrderDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RestaurantWaitingForOrderListProcessor {
    private final OrderDbUtility orderDbUtility;
    private final RestaurantRequestForNewOrder requestForNewOrder;
    private final RestaurantCommandProcessor restaurantCommandProcessor;
    private final RequestForMyOrdersList requestForMyOrdersList;
    private final TelegramUserDbUtility telegramUserDbUtility;


    public RestaurantWaitingForOrderListProcessor(OrderDbUtility orderDbUtility, RestaurantRequestForNewOrder requestForNewOrder, RestaurantCommandProcessor restaurantCommandProcessor, RequestForMyOrdersList requestForMyOrdersList, TelegramUserDbUtility telegramUserDbUtility) {
        this.orderDbUtility = orderDbUtility;
        this.requestForNewOrder = requestForNewOrder;
        this.restaurantCommandProcessor = restaurantCommandProcessor;
        this.requestForMyOrdersList = requestForMyOrdersList;
        this.telegramUserDbUtility = telegramUserDbUtility;
    }

    public void processOrder(Update update, TelegramRestaurantUserDTO telegramRestaurantUserDTO) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("accept_")) {
            OrderDTO orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(7)),
                OrderStatus.ACCEPTED_BY_RESTAURANT);
            requestForNewOrder.editNewOrder(update, orderDTO);
            TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
            requestForMyOrdersList.sendMyOrders(orderDTO, telegramUserDTO.getChatId());
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("reject_")) {
            OrderDTO orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(7)),
                OrderStatus.CANCELED_BY_RESTAURANT);
            requestForNewOrder.editNewOrder(update, orderDTO);
            TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
            requestForMyOrdersList.sendMyOrders(orderDTO, telegramUserDTO.getChatId());
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("ready_")) {
            OrderDTO orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(6)),
                OrderStatus.READY_FOR_DELIVERY);
            requestForNewOrder.editNewOrder(update, orderDTO);
            TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
            requestForMyOrdersList.sendMyOrders(orderDTO, telegramUserDTO.getChatId());
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("send_")) {
            OrderDTO orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(5)),
                OrderStatus.ACCEPTED_BY_DRIVER);
            requestForNewOrder.editNewOrder(update, orderDTO);
            TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
            requestForMyOrdersList.sendMyOrders(orderDTO, telegramUserDTO.getChatId());
        } else {
            restaurantCommandProcessor.requestForErrorResponder(update, telegramRestaurantUserDTO);
        }
    }
}
