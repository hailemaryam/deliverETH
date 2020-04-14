package et.com.delivereth.Telegram.telegramTransport.requests;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.telegramTransport.main.TransportTelegramSender;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TransportRequestForAccountLiking {
    private final TransportTelegramSender transportTelegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TransportRequestForAccountLiking.class);

    public TransportRequestForAccountLiking(TransportTelegramSender transportTelegramSender) {
        this.transportTelegramSender = transportTelegramSender;
    }

    public void requestUserToWait(Update update, TelegramDeliveryUserDTO telegramUser) {
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText(StaticText.successfullyTransportUserRegistration);
        response.setParseMode("HTML");
        try {
            transportTelegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
