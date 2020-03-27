package et.com.delivereth.Telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import javax.annotation.PostConstruct;

@Component
public class TelegramHome extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;
    private final ResponseBuilder responseBuilder;

    public TelegramHome(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
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
        if (update.hasMessage() || update.hasCallbackQuery()) {
            BotApiMethod<Message> response = responseBuilder.getResponse(update);
            try {
                execute(response);
            } catch (TelegramApiException e) {
                logger.error("Error Sending Message {}", response);
            }
            logger.info("Sent message \"{}\" to {}", update, update.getMessage().getChatId());
        }
    }
    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }

}
