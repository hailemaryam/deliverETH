package et.com.delivereth.Telegram.telegramUser.main;

import et.com.delivereth.Telegram.Constants.TelegramBotConstantCredentials;
import et.com.delivereth.Telegram.telegramUser.ResponseBuilder;
import et.com.delivereth.Telegram.telegramUser.requests.RequestErrorResponder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;


import javax.annotation.PostConstruct;

@Component
public class TelegramHome extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private String token = TelegramBotConstantCredentials.BOT_TOKEN;
    private String username = TelegramBotConstantCredentials.BOT_USER_NAME;
    private final ResponseBuilder responseBuilder;
    private final RequestErrorResponder requestErrorResponder;

    public TelegramHome(ResponseBuilder responseBuilder, RequestErrorResponder requestErrorResponder) {
        this.responseBuilder = responseBuilder;
        this.requestErrorResponder = requestErrorResponder;
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
            responseBuilder.getResponse(update);
        } else {
            requestErrorResponder.userErrorResponseResponder(update);
        }
    }
    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }

}
