package et.com.delivereth.Telegram.telegramTransport.processor;

import et.com.delivereth.Telegram.DbUtility.OrderDbUtility;
import et.com.delivereth.Telegram.DbUtility.TelegramUserDbUtility;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestForNewOrder;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportSendLocation;
import et.com.delivereth.Telegram.telegramUser.requests.RequestForMyOrdersList;
import et.com.delivereth.domain.Order;
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

    public void processOrder(Update update, TelegramDeliveryUserDTO telegramDeliveryUserDTO) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("accept_")) {
            accept(update, telegramDeliveryUserDTO);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("delivered_")) {
            delivered(update, telegramDeliveryUserDTO);
        } else if (update.hasMessage() && update.getMessage().getText().startsWith("/Restaurant_Location_")) {
            transportSendLocation.sendRestaurantLocation(update, Long.valueOf(update.getMessage().getText().substring(21)));
        } else if (update.hasMessage() && update.getMessage().getText().startsWith("/User_Location_")) {
            transportSendLocation.sendOrderLocation(update, Long.valueOf(update.getMessage().getText().substring(15)));
        } else {
            transportCommandProcessor.requestForErrorResponder(update, telegramDeliveryUserDTO);
        }
    }
    void accept(Update update, TelegramDeliveryUserDTO telegramDeliveryUserDTO) {
        OrderDTO orderById = orderDbUtility.getOrderById(Long.valueOf(update.getCallbackQuery().getData().substring(7)));
        if (orderById.getOrderStatus().equals(OrderStatus.READY_FOR_DELIVERY)){
            OrderDTO orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(7)),
                OrderStatus.ACCEPTED_BY_DRIVER);
            requestForNewOrder.editNewOrder(update, orderDTO, false, telegramDeliveryUserDTO);
            TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
            requestForMyOrdersList.sendOrderStatus(orderDTO, telegramUserDTO.getChatId());
        } else {
            requestForNewOrder.editNewOrder(update, orderById,true, telegramDeliveryUserDTO);
        }
    }
    void delivered(Update update, TelegramDeliveryUserDTO telegramDeliveryUserDTO){
        OrderDTO orderDTO = orderDbUtility.getOrderById(Long.valueOf(update.getCallbackQuery().getData().substring(10)));
        if (orderDTO.getOrderStatus().equals(OrderStatus.ACCEPTED_BY_DRIVER)) {
            orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(10)),
                OrderStatus.DELIVERED);
            requestForNewOrder.editNewOrder(update, orderDTO, false, telegramDeliveryUserDTO);
            TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
            requestForMyOrdersList.sendOrderStatus(orderDTO, telegramUserDTO.getChatId());
        } else{
            requestForNewOrder.editNewOrder(update, orderDTO,true, telegramDeliveryUserDTO);
        }
    }
}
