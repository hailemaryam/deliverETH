package et.com.delivereth.Telegram.telegramTransport.requests;

import et.com.delivereth.Telegram.telegramTransport.main.TransportTelegramSender;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
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
        response.setText("<b>\uD83D\uDC68\u200D\uD83C\uDF73 Your contact has been successfully registerd. We will send order detail when it arrives.</b>\n");
        response.setParseMode("HTML");
        try {
            transportTelegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void requestUserToWaitAgain(Update update, TelegramDeliveryUserDTO telegramUser) {
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("<b>❗️ Before you continue the next step please wait deliverEth admin to link your account with your restaurant.</b>\n");
        response.setParseMode("HTML");
        try {
            transportTelegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
