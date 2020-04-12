package et.com.delivereth.Telegram.telegramTransport.processor;

import et.com.delivereth.Telegram.DbUtility.TelegramRestaurantUserDbUtility;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestContact;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestForAccountLiking;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TransportRestaurantWaitingForAccountLinkProcessor {
    private final TransportRequestForAccountLiking transportRequestForAccountLiking;

    public TransportRestaurantWaitingForAccountLinkProcessor(TransportRequestForAccountLiking transportRequestForAccountLiking) {

        this.transportRequestForAccountLiking = transportRequestForAccountLiking;
    }

    void requestUserForAccountLinking(Update update, TelegramDeliveryUserDTO telegramUser) {
        transportRequestForAccountLiking.requestUserToWaitAgain(update, telegramUser);
    }

}
