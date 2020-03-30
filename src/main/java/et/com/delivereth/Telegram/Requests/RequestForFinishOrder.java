package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.Telegram.DbUtility;
import et.com.delivereth.Telegram.TelegramSender;
import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.service.dto.KeyValuPairHolderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestForFinishOrder {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(RequestForFinishOrder.class);
    private final DbUtility dbUtility;

    public RequestForFinishOrder(TelegramSender telegramSender, DbUtility dbUtility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
    }

    public void requestForFinishOrder(Update update, TelegramUser telegramUser) {
        SendPhoto response = new SendPhoto();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Add More Item").setCallbackData("order"));
        rowInline.add(new InlineKeyboardButton().setText("Cancel").setCallbackData("order"));
        rowInline.add(new InlineKeyboardButton().setText("Place Order").setCallbackData("order"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        KeyValuPairHolderDTO orderImage = dbUtility.getKeyValuPairHolderRepository("OrderImage");
        InputStream inputStream = new ByteArrayInputStream(orderImage.getValueImage());
        response.setPhoto(orderImage.getKey(), inputStream);
        response.setCaption("order invoice under development");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}