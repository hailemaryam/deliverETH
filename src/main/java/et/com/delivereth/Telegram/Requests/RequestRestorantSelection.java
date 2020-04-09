package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.Telegram.DbUtility;
import et.com.delivereth.Telegram.TelegramHome;
import et.com.delivereth.Telegram.TelegramSender;
import et.com.delivereth.domain.Order;
import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.service.dto.RestorantDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestRestorantSelection {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final DbUtility dbUtility;

    public RequestRestorantSelection(TelegramSender telegramSender, DbUtility dbUtility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
    }

    public void requestRestorantSelection(Update update, TelegramUserDTO telegramUser) {
        Page<RestorantDTO> restorantList = dbUtility.getRestorantList(telegramUser);
        restorantList.toList().forEach(restorantDTO -> {
            sendRestorant(restorantDTO, update);
        });
        if (restorantList.toList().size() == 0) {
            sendNoMoreItem(update);
            dbUtility.cancelOrder(telegramUser);
        } else if (restorantList.hasNext()) {
            sendLoadMoreButton(update);
        }
    }
    private void sendRestorant(RestorantDTO restorantDTO, Update update){
        SendPhoto response = new SendPhoto();
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        rowInline.add(new InlineKeyboardButton().setText("Show Menu").setCallbackData("menu_" + restorantDTO.getId()));
//        rowsInline.add(rowInline);
//        markupInline.setKeyboard(rowsInline);
//        response.setReplyMarkup(markupInline);
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        InputStream inputStream = new ByteArrayInputStream(restorantDTO.getIconImage());
        response.setPhoto(restorantDTO.getName(), inputStream);
        String caption = "<b>"+restorantDTO.getName() + "</b>\n" +
            "<i>To View Menu: </i><a>/show_menu_" + restorantDTO.getId() + "</a>";
        response.setCaption(caption);
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    private void sendLoadMoreButton(Update update){
        SendMessage response = new SendMessage();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Load More >>").setCallbackData("loadMore"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("Want to list more restaurant.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    private void sendNoMoreItem(Update update){
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("There are no restaurant list around you delivering at this time.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void sendTitle(Update update){
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("<b>Choose restaurant from this list</b>");
        response.setParseMode("HTML");
        response.setReplyMarkup(orderKeyBoardMenu());
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

    public ReplyKeyboardMarkup orderKeyBoardMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setResizeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardButtons1 = new KeyboardRow();
        keyboardButtons1.add(new KeyboardButton()
            .setText("Cancel Order"));
//        keyboardButtons1.add(new KeyboardButton()
//            .setText("My Orders"));
//        KeyboardRow keyboardButtons2 = new KeyboardRow();
//        keyboardButtons2.add(new KeyboardButton()
//            .setText("Help"));
//        keyboardButtons2.add(new KeyboardButton()
//            .setText("Setting"));
        keyboardRowList.add(keyboardButtons1);
//        keyboardRowList.add(keyboardButtons2);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return  replyKeyboardMarkup;
    }

}
