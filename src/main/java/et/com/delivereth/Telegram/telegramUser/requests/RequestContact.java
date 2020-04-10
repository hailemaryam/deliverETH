package et.com.delivereth.Telegram.telegramUser.requests;

import et.com.delivereth.Telegram.telegramUser.main.TelegramHome;
import et.com.delivereth.Telegram.telegramUser.main.TelegramSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class RequestContact {
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);

    private final TelegramSender telegramSender;

    public RequestContact(TelegramSender telegramSender) {
        this.telegramSender = telegramSender;
    }

    public void requestContact(Update update) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(Menu.prepareShareContactReplyButton());
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
            response.setText("Welcome " + update.getMessage().getFrom().getUserName() + ".we need your contact for registration. click share button to share your contact.");
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
            response.setText("Welcome " + update.getCallbackQuery().getFrom().getUserName() + ".we need your contact for registration. click share button to share your contact.");
        }
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void requestContactAgain(Update update) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(Menu.prepareShareContactReplyButton());
        if (update.hasMessage()) {
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()){
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("we can't deliver our service with out your contact. click share button to share your contact.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
