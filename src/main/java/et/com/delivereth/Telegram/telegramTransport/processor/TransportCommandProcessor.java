package et.com.delivereth.Telegram.telegramTransport.processor;

import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestErrorResponder;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestForHelp;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TransportCommandProcessor {
    private final TransportRequestErrorResponder transportRequestErrorResponder;
    private final TransportRequestForHelp transportRequestForHelp;

    public TransportCommandProcessor(TransportRequestErrorResponder transportRequestErrorResponder, TransportRequestForHelp transportRequestForHelp) {
        this.transportRequestErrorResponder = transportRequestErrorResponder;
        this.transportRequestForHelp = transportRequestForHelp;
    }

    void help(Update update, TelegramDeliveryUserDTO telegramUser){
        transportRequestForHelp.helpResponse(update);
    }
    void myOrder(Update update, TelegramRestaurantUserDTO telegramUser){
    }

    void requestForErrorResponder(Update update, TelegramDeliveryUserDTO telegramUser) {
        transportRequestErrorResponder.userErrorResponseResponder(update);
    }

}
