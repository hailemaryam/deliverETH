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
            accept(update, telegramRestaurantUserDTO);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("reject_")) {
            reject(update, telegramRestaurantUserDTO);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("ready_")) {
            readyForDelivery(update, telegramRestaurantUserDTO);
        } else {
            restaurantCommandProcessor.requestForErrorResponder(update, telegramRestaurantUserDTO);
        }
    }
    public void accept(Update update, TelegramRestaurantUserDTO telegramRestaurantUserDTO) {
        OrderDTO orderDTO = orderDbUtility.getOrderById(Long.valueOf(update.getCallbackQuery().getData().substring(7)));
        if (orderDTO.getOrderStatus().equals(OrderStatus.ORDERED)){
            orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(7)),
                OrderStatus.ACCEPTED_BY_RESTAURANT);
            requestForNewOrder.editNewOrder(update, orderDTO, telegramRestaurantUserDTO);
            requestForNewOrder.responsePopUpForEditOrder(update);
            TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
            requestForMyOrdersList.sendOrderStatus(orderDTO, telegramUserDTO.getChatId());
        } else if (orderDTO.getOrderStatus().equals(OrderStatus.CANCELED_BY_USER)) {
            requestForNewOrder.responsePopUpForAlreadyCanceledByUser(update);
            requestForNewOrder.editNewOrder(update, orderDTO, telegramRestaurantUserDTO);
        } else {
            requestForNewOrder.responsePopUpForAlreadyProccesedByOtherUser(update);
            requestForNewOrder.editNewOrder(update, orderDTO, telegramRestaurantUserDTO);
        }
    }
    public void reject(Update update, TelegramRestaurantUserDTO telegramRestaurantUserDTO) {
        OrderDTO orderDTO = orderDbUtility.getOrderById(Long.valueOf(update.getCallbackQuery().getData().substring(7)));
        if(orderDTO.getOrderStatus().equals(OrderStatus.ORDERED)){
            orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(7)),
                OrderStatus.CANCELED_BY_RESTAURANT);
            requestForNewOrder.editNewOrder(update, orderDTO, telegramRestaurantUserDTO);
            requestForNewOrder.responsePopUpForEditOrder(update);
            TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
            requestForMyOrdersList.sendOrderStatus(orderDTO, telegramUserDTO.getChatId());
        } else if (orderDTO.getOrderStatus().equals(OrderStatus.CANCELED_BY_USER)) {
            requestForNewOrder.responsePopUpForAlreadyCanceledByUser(update);
            requestForNewOrder.editNewOrder(update, orderDTO, telegramRestaurantUserDTO);
        } else if (orderDTO.getOrderStatus().equals(OrderStatus.ACCEPTED_BY_RESTAURANT)){
            requestForNewOrder.responsePopUpForAlreadyAccepted(update);
            requestForNewOrder.editNewOrder(update, orderDTO, telegramRestaurantUserDTO);
        } else {
            requestForNewOrder.responsePopUpForAlreadyProccesedByOtherUser(update);
            requestForNewOrder.editNewOrder(update, orderDTO, telegramRestaurantUserDTO);
        }
    }
    public void readyForDelivery(Update update, TelegramRestaurantUserDTO telegramRestaurantUserDTO){
        OrderDTO orderDTO = orderDbUtility.getOrderById(Long.valueOf(update.getCallbackQuery().getData().substring(6)));
        if (orderDTO.getOrderStatus().equals(OrderStatus.ACCEPTED_BY_RESTAURANT)) {
            orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(6)),
                OrderStatus.READY_FOR_DELIVERY);
            requestForNewOrder.editNewOrder(update, orderDTO, telegramRestaurantUserDTO);
            requestForNewOrder.responsePopUpForEditOrder(update);
            TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
            requestForMyOrdersList.sendOrderStatus(orderDTO, telegramUserDTO.getChatId());
        }  else {
            requestForNewOrder.responsePopUpForAlreadyProccesedByOtherUser(update);
            requestForNewOrder.editNewOrder(update, orderDTO, telegramRestaurantUserDTO);
        }
    }
}
