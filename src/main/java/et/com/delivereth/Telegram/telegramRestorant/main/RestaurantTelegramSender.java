package et.com.delivereth.Telegram.telegramRestorant.main;

import et.com.delivereth.Telegram.Constants.TelegramBotConstantCredentials;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Component
public class RestaurantTelegramSender extends DefaultAbsSender {
    private String token = TelegramBotConstantCredentials.BOT_TOKEN_RESTAURANT;
    public RestaurantTelegramSender() {
        this((DefaultBotOptions) ApiContext.getInstance(DefaultBotOptions.class));
    }

    protected RestaurantTelegramSender(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
