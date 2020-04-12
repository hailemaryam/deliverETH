package et.com.delivereth.Telegram.telegramTransport.main;

import et.com.delivereth.Telegram.Constants.TelegramBotConstantCredentials;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Component
public class TransportTelegramSender extends DefaultAbsSender {
    private String token = TelegramBotConstantCredentials.BOT_TOKEN_TRANSPORT;
    public TransportTelegramSender() {
        this((DefaultBotOptions) ApiContext.getInstance(DefaultBotOptions.class));
    }

    protected TransportTelegramSender(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
