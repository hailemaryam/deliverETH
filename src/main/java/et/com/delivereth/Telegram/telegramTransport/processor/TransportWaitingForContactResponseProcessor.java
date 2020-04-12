package et.com.delivereth.Telegram.telegramTransport.processor;

import et.com.delivereth.Telegram.DbUtility.TelegramDeliveryUserDbUtility;
import et.com.delivereth.Telegram.DbUtility.TelegramRestaurantUserDbUtility;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestContact;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestForAccountLiking;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TransportWaitingForContactResponseProcessor {
    private final TransportRequestContact transportRequestContact;
    private final TransportRequestForAccountLiking transportRequestForAccountLiking;
    private final TelegramDeliveryUserDbUtility telegramDeliveryUserDbUtility;

    public TransportWaitingForContactResponseProcessor(TransportRequestContact transportRequestContact, TransportRequestForAccountLiking transportRequestForAccountLiking, TelegramDeliveryUserDbUtility telegramDeliveryUserDbUtility) {
        this.transportRequestContact = transportRequestContact;
        this.transportRequestForAccountLiking = transportRequestForAccountLiking;
        this.telegramDeliveryUserDbUtility = telegramDeliveryUserDbUtility;
    }

    void processContactAndProceedToOrder(Update update, TelegramDeliveryUserDTO telegramUser) {
        if (update.getMessage().getContact() != null) {
            telegramDeliveryUserDbUtility.registerUserPhone(update, telegramUser);
            transportRequestForAccountLiking.requestUserToWait(update, telegramUser);
        } else {
            transportRequestContact.requestContactAgain(update);
        }
    }

}
