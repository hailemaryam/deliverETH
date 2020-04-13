package et.com.delivereth.Telegram.telegramUser.requests;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.telegramUser.main.TelegramHome;
import et.com.delivereth.Telegram.telegramUser.main.TelegramSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class RequestForHelp {
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final TelegramSender telegramSender;

    public RequestForHelp(TelegramSender telegramSender) {
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
        response.setText(StaticText.userHelp);
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

}
