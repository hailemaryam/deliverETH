package et.com.delivereth.Telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Component
public class TelegramSender extends DefaultAbsSender {
    private String token = TelegramBotConstant.BOT_TOKEN;
    public TelegramSender() {
        this((DefaultBotOptions) ApiContext.getInstance(DefaultBotOptions.class));
    }

    protected TelegramSender(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
