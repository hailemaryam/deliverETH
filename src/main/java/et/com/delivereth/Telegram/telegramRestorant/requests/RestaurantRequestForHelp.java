package et.com.delivereth.Telegram.telegramRestorant.requests;

import et.com.delivereth.Telegram.telegramRestorant.main.RestaurantTelegramSender;
import et.com.delivereth.Telegram.telegramUser.main.TelegramHome;
import et.com.delivereth.Telegram.telegramUser.main.TelegramSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class RestaurantRequestForHelp {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantTelegramSender.class);
    private final RestaurantTelegramSender telegramSender;

    public RestaurantRequestForHelp(RestaurantTelegramSender telegramSender) {
        this.telegramSender = telegramSender;
    }

    public void helpResponse(Update update) {
        Long chatId = null;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()){
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText("<b>Help</b>\n" +
            "DeliverEth is the first Ethiopian telegram bot to bring you fresh foods where ever you are.\n" +
            "Order your favorite dish from your favorite restaurant and it will be delivered at your door steps.\n");
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

}
