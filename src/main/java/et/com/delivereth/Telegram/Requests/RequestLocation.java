package et.com.delivereth.Telegram.Requests;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
@Service
public class RequestLocation {
    public SendMessage requestLocation(Message message) {
        SendMessage response = new SendMessage();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
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
        response.setReplyMarkup(replyKeyboardMarkup);
        response.setChatId(message.getChatId());
        response.setText("we need your location click share location to share.");
        return response;
    }
}
