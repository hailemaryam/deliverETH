package et.com.delivereth.Telegram.telegramTransport.main;

import et.com.delivereth.Telegram.Constants.TelegramBotConstantCredentials;
import et.com.delivereth.Telegram.telegramTransport.TransportResponseBuilder;
import et.com.delivereth.Telegram.telegramUser.requests.RequestErrorResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;

@Component
public class TransportTelegramHome extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TransportTelegramHome.class);
    private String token = TelegramBotConstantCredentials.BOT_TOKEN_TRANSPORT;
    private String username = TelegramBotConstantCredentials.BOT_USER_NAME;
    private final RequestErrorResponder requestErrorResponder;
    private final TransportResponseBuilder transportResponseBuilder;

    public TransportTelegramHome(RequestErrorResponder requestErrorResponder, TransportResponseBuilder transportResponseBuilder) {
        this.requestErrorResponder = requestErrorResponder;
        this.transportResponseBuilder = transportResponseBuilder;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("Received message {}", update);
        if (update.hasMessage() || update.hasCallbackQuery()) {
            transportResponseBuilder.getResponse(update);
        } else {
            requestErrorResponder.userErrorResponseResponder(update);
        }
    }
    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }

}
