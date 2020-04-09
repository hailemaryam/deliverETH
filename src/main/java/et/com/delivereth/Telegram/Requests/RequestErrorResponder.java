package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.Telegram.TelegramHome;
import et.com.delivereth.Telegram.TelegramSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestErrorResponder {

    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final TelegramSender telegramSender;
    public RequestErrorResponder(TelegramSender telegramSender) {
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
