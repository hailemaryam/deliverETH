package et.com.delivereth.Telegram.telegramUser.requests;

import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.telegramUser.main.TelegramSender;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestForMenu {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(RequestForMenu.class);
    private final DbUtility dbUtility;
    public RequestForMenu(TelegramSender telegramSender, DbUtility dbUtility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
    }

    public void requestForMenu(Update update, TelegramUserDTO telegramUser) {
        SendMessage response = new SendMessage();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(StaticText.newOrder).setCallbackData("order"));
        rowInline.add(new InlineKeyboardButton().setText("\uD83D\uDCDD My Orders").setCallbackData("myOrder"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("<b>We are currently delivering to everywhere in Addis Ababa, Ethiopia \uD83C\uDDEA\uD83C\uDDF9</b>\n");
//            "<i>* hayat</i>\n" +
//            "<i>* semit</i>\n" +
//            "<i>* bole</i>\n");
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
