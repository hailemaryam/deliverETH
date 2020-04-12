package et.com.delivereth.Telegram.telegramTransport.processor;

import et.com.delivereth.Telegram.DbUtility.OrderDbUtility;
import et.com.delivereth.Telegram.DbUtility.TelegramUserDbUtility;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestForNewOrder;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportSendLocation;
import et.com.delivereth.Telegram.telegramUser.requests.RequestForMyOrdersList;
import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.service.dto.OrderDTO;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TransportWaitingForOrderListProcessor {
    private final OrderDbUtility orderDbUtility;
    private final TransportRequestForNewOrder requestForNewOrder;
    private final TransportCommandProcessor transportCommandProcessor;
    private final TransportSendLocation transportSendLocation;
    private final RequestForMyOrdersList requestForMyOrdersList;
    private final TelegramUserDbUtility telegramUserDbUtility;

    public TransportWaitingForOrderListProcessor(OrderDbUtility orderDbUtility, TransportRequestForNewOrder requestForNewOrder, TransportCommandProcessor transportCommandProcessor, TransportSendLocation transportSendLocation, RequestForMyOrdersList requestForMyOrdersList, TelegramUserDbUtility telegramUserDbUtility) {
        this.orderDbUtility = orderDbUtility;
        this.requestForNewOrder = requestForNewOrder;
        this.transportCommandProcessor = transportCommandProcessor;
        this.transportSendLocation = transportSendLocation;
        this.requestForMyOrdersList = requestForMyOrdersList;
        this.telegramUserDbUtility = telegramUserDbUtility;
    }

    public void processOrder(Update update, TelegramDeliveryUserDTO telegramRestaurantUserDTO) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("accept_")) {
            OrderDTO orderById = orderDbUtility.getOrderById(Long.valueOf(update.getCallbackQuery().getData().substring(7)));
            if (orderById.getOrderStatus().equals(OrderStatus.ACCEPTED_BY_DRIVER) || orderById.getOrderStatus().equals(OrderStatus.DELIVERED)) {
                requestForNewOrder.editNewOrder(update, orderById,true);
            } else {
                OrderDTO orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(7)),
                    OrderStatus.ACCEPTED_BY_DRIVER);
                requestForNewOrder.editNewOrder(update, orderDTO, false);
                TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
                requestForMyOrdersList.sendMyOrders(orderDTO, telegramUserDTO.getChatId());
            }
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("delivered_")) {
            OrderDTO orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(10)),
                OrderStatus.DELIVERED);
            requestForNewOrder.editNewOrder(update, orderDTO, false);
            TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
            requestForMyOrdersList.sendMyOrders(orderDTO, telegramUserDTO.getChatId());
        } else if (update.hasMessage() && update.getMessage().getText().startsWith("/Restaurant_Location_")) {
            transportSendLocation.sendRestaurantLocation(update, Long.valueOf(update.getMessage().getText().substring(21)));
        } else if (update.hasMessage() && update.getMessage().getText().startsWith("/User_Location_")) {
            transportSendLocation.sendOrderLocation(update, Long.valueOf(update.getMessage().getText().substring(15)));
        } else {
            transportCommandProcessor.requestForErrorResponder(update, telegramRestaurantUserDTO);
        }
    }
}
