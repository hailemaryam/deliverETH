package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.Telegram.DbUtility;
import et.com.delivereth.Telegram.TelegramHome;
import et.com.delivereth.Telegram.TelegramSender;
import et.com.delivereth.domain.KeyValuPairHolder;
import et.com.delivereth.service.dto.KeyValuPairHolderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestForOrder {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final DbUtility dbUtility;
    public RequestForOrder(TelegramSender telegramSender, DbUtility dbUtility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
    }

    public SendPhoto requestForOrder(Update update) {
        Message message = update.getMessage();
        SendPhoto response = new SendPhoto();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Order").setCallbackData("order"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        response.setChatId(message.getChatId());
        KeyValuPairHolderDTO orderImage = dbUtility.getKeyValuPairHolderRepository("OrderImage");
        InputStream inputStream = new ByteArrayInputStream(orderImage.getValueImage());
        response.setPhoto(orderImage.getKey(), inputStream);
        response.setCaption("currently we are delivering @ hayat, semit and bole");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
        return response;
    }
}
