package et.com.delivereth.Telegram;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class ErrorResponder {

    public ErrorResponder() {
    }

    public SendMessage userErrorResponseResponder(Message message) {
        SendMessage response = new SendMessage();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Help").setCallbackData("update_msg_text"));
        rowInline.add(new InlineKeyboardButton().setText("let me continue. i will use proper commands").setCallbackData("update_msg_text"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        response.setChatId(message.getChatId());
        response.setText("Your request could not be processed. you need to choose or write proper comands.");
        return response;
    }

}
