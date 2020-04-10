package et.com.delivereth.Telegram.telegramRestorant.requests;

import et.com.delivereth.Telegram.telegramRestorant.main.RestaurantTelegramSender;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class RestaurantRequestForAccountLiking {
    private final RestaurantTelegramSender restaurantTelegramSender;
    private static final Logger logger = LoggerFactory.getLogger(RestaurantRequestForAccountLiking.class);

    public RestaurantRequestForAccountLiking(RestaurantTelegramSender restaurantTelegramSender) {
        this.restaurantTelegramSender = restaurantTelegramSender;
    }

    public void requestForMenu(Update update, TelegramRestaurantUserDTO telegramUser) {
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("<b>Your contact has been successfully registerd. Contact deliver eth admin to link your account to your restaurant</b>\n");
        response.setParseMode("HTML");
        try {
            restaurantTelegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
