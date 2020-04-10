package et.com.delivereth.Telegram.telegramRestorant.requests;

import et.com.delivereth.Telegram.telegramRestorant.main.RestaurantTelegramSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class RestaurantRequestErrorResponder {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantTelegramSender.class);
    private final RestaurantTelegramSender telegramSender;

    public RestaurantRequestErrorResponder(RestaurantTelegramSender telegramSender) {
        this.telegramSender = telegramSender;
    }

    public void userErrorResponseResponder(Update update) {
        Long chatId = null;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()){
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText("<b>❗️ Improper Command</b>\n" +
            "Your request could not be processed. you need to choose or write proper commands.");
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
