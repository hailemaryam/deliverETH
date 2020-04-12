package et.com.delivereth.Telegram.telegramTransport.requests;

import et.com.delivereth.Telegram.DbUtility.OrderDbUtility;
import et.com.delivereth.Telegram.DbUtility.RestorantDbUtitlity;
import et.com.delivereth.Telegram.telegramTransport.main.TransportTelegramSender;
import et.com.delivereth.service.dto.OrderDTO;
import et.com.delivereth.service.dto.RestorantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TransportSendLocation {
    private static final Logger logger = LoggerFactory.getLogger(TransportTelegramSender.class);

    private final RestorantDbUtitlity restorantDbUtitlity;
    private final OrderDbUtility orderDbUtility;
    private final TransportTelegramSender telegramSender;

    public TransportSendLocation(RestorantDbUtitlity restorantDbUtitlity, OrderDbUtility orderDbUtility, TransportTelegramSender telegramSender) {
        this.restorantDbUtitlity = restorantDbUtitlity;
        this.orderDbUtility = orderDbUtility;
        this.telegramSender = telegramSender;
    }
    public void sendOrderLocation(Update update, Long id){
        OrderDTO orderById = orderDbUtility.getOrderById(id);
        sendLocation(update, orderById.getLatitude(), orderById.getLongtude());
    }
    public void sendRestaurantLocation(Update update, Long id){
        RestorantDTO restorant = restorantDbUtitlity.getRestorant(id);
        sendLocation(update, restorant.getLatitude(), restorant.getLongtude());
    }
    public void sendLocation(Update update, Float latitude, Float longitude) {
        SendLocation response = new SendLocation();
        if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
            response.setReplyToMessageId(update.getCallbackQuery().getMessage().getMessageId());
        } else if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
            response.setReplyToMessageId(update.getMessage().getMessageId());
        }
        response.setLatitude(latitude);
        response.setLongitude(longitude);
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
