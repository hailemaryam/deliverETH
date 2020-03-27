package et.com.delivereth.Telegram.Requests;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestContact {
    public BotApiMethod<Message> requestContact(Message message) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(prepareShareContactReplyButton());
        response.setChatId(message.getChatId());
        response.setText("share your contact?");
        return response;
    }
    public BotApiMethod<Message> requestContactAgain(Message message) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(prepareShareContactReplyButton());
        response.setChatId(message.getChatId());
        response.setText("you need to share your contact to continue.");
        return response;
    }
    private ReplyKeyboardMarkup prepareShareContactReplyButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow shareContactButton = new KeyboardRow();
        shareContactButton.add(new KeyboardButton()
            .setText("Share Contact").setRequestContact(true));
        keyboardRowList.add(shareContactButton);
        KeyboardRow cancelButton = new KeyboardRow();
        cancelButton.add(new KeyboardButton()
            .setText("Cancel"));
        keyboardRowList.add(cancelButton);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return  replyKeyboardMarkup;
    }
}
