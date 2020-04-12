package et.com.delivereth.Telegram.telegramTransport.processor;

import et.com.delivereth.Telegram.DbUtility.TelegramRestaurantUserDbUtility;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestContact;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestForAccountLiking;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TransportRestaurantWaitingForAccountLinkProcessor {
    private final TransportRequestContact transportRequestContact;
    private final TransportRequestForAccountLiking transportRequestForAccountLiking;
    private final TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility;

    public TransportRestaurantWaitingForAccountLinkProcessor(TransportRequestContact transportRequestContact, TransportRequestForAccountLiking transportRequestForAccountLiking, TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility) {
        this.transportRequestContact = transportRequestContact;
        this.transportRequestForAccountLiking = transportRequestForAccountLiking;
        this.telegramRestaurantUserDbUtility = telegramRestaurantUserDbUtility;
    }

    void requestUserForAccountLinking(Update update, TelegramRestaurantUserDTO telegramUser) {
        transportRequestForAccountLiking.requestUserToWaitAgain(update, telegramUser);
    }

}
