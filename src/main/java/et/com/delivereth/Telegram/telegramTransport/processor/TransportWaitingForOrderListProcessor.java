package et.com.delivereth.Telegram.telegramTransport.processor;

import et.com.delivereth.Telegram.DbUtility.OrderDbUtility;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestForNewOrder;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.service.dto.OrderDTO;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TransportWaitingForOrderListProcessor {
    private final OrderDbUtility orderDbUtility;
    private final TransportRequestForNewOrder requestForNewOrder;
    private final TransportCommandProcessor transportCommandProcessor;

    public TransportWaitingForOrderListProcessor(OrderDbUtility orderDbUtility, TransportRequestForNewOrder requestForNewOrder, TransportCommandProcessor transportCommandProcessor) {
        this.orderDbUtility = orderDbUtility;
        this.requestForNewOrder = requestForNewOrder;
        this.transportCommandProcessor = transportCommandProcessor;
    }

    public void processOrder(Update update, TelegramDeliveryUserDTO telegramRestaurantUserDTO) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("accept_")) {
            OrderDTO orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(7)),
                OrderStatus.ACCEPTED_BY_DRIVER);
            requestForNewOrder.editNewOrder(update, orderDTO);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("delivered_")) {
            OrderDTO orderDTO = orderDbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(10)),
                OrderStatus.DELIVERED);
            requestForNewOrder.editNewOrder(update, orderDTO);
        } else {
            transportCommandProcessor.requestForErrorResponder(update, telegramRestaurantUserDTO);
        }
    }
}
