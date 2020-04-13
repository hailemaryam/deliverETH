package et.com.delivereth.Telegram.telegramRestorant.requests;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.telegramRestorant.main.RestaurantTelegramHome;
import et.com.delivereth.Telegram.telegramRestorant.main.RestaurantTelegramSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class RestaurantRequestContact {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantTelegramHome.class);

    private final RestaurantTelegramSender telegramSender;

    public RestaurantRequestContact(RestaurantTelegramSender telegramSender) {
        this.telegramSender = telegramSender;
    }

    public void requestContact(Update update) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(RestaurantMenu.prepareShareContactReplyButton());
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
            response.setText("Welcome " + update.getMessage().getFrom().getUserName() + "." + StaticText.weNeedContact);
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
            response.setText("Welcome " + update.getCallbackQuery().getFrom().getUserName() + "." + StaticText.weNeedContact);
        }
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void requestContactAgain(Update update) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(RestaurantMenu.prepareShareContactReplyButton());
        if (update.hasMessage()) {
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()){
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText(StaticText.getWeNeedContactAgain);
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
