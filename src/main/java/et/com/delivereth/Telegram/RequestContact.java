package et.com.delivereth.Telegram;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestContact {
    public SendMessage requestContact(Message message) {
        SendMessage response = new SendMessage();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
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
        response.setReplyMarkup(replyKeyboardMarkup);
        response.setChatId(message.getChatId());
        response.setText("share your contact?");
        return response;
    }
}
