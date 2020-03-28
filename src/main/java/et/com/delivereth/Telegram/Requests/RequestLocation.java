package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.Telegram.DbUtility;
import et.com.delivereth.Telegram.TelegramHome;
import et.com.delivereth.Telegram.TelegramSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
@Service
public class RequestLocation {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final DbUtility dbUtility;

    public RequestLocation(TelegramSender telegramSender, DbUtility dbUtility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
    }

    public void requestLocation(Update update) {
        requestLocation(update, "we need your location for order delivery. click share location to share your location.");
    }
    public void requestLocationAgain(Update update) {
        requestLocation(update, "we can't process your order with out knowing your location. please click share location to share your location.");
    }
    public void requestLocation(Update update, String text){
        SendMessage response = new SendMessage();
        response.setReplyMarkup(prepareShareLocationReplyButton());
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText(text);
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    private ReplyKeyboardMarkup prepareShareLocationReplyButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow shareLocationButton = new KeyboardRow();
        shareLocationButton.add(new KeyboardButton()
            .setText("Share Location")
            .setRequestLocation(true));
        keyboardRowList.add(shareLocationButton);
        KeyboardRow cancelButton = new KeyboardRow();
        cancelButton.add(new KeyboardButton()
            .setText("Cancel"));
        keyboardRowList.add(cancelButton);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return  replyKeyboardMarkup;
    }

}
