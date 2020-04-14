package et.com.delivereth.Telegram.telegramUser.requests;

import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.DbUtility.FoodDbUtitility;
import et.com.delivereth.Telegram.DbUtility.OrderedFoodDbUtility;
import et.com.delivereth.Telegram.DbUtility.TelegramUserDbUtility;
import et.com.delivereth.Telegram.telegramUser.main.TelegramHome;
import et.com.delivereth.Telegram.telegramUser.main.TelegramSender;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestQuantity {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final DbUtility dbUtility;
    private final TelegramUserDbUtility telegramUserDbUtility;
    private final OrderedFoodDbUtility orderedFoodDbUtility;
    private final FoodDbUtitility foodDbUtitility;

    public RequestQuantity(TelegramSender telegramSender, DbUtility dbUtility, TelegramUserDbUtility telegramUserDbUtility, OrderedFoodDbUtility orderedFoodDbUtility, FoodDbUtitility foodDbUtitility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
        this.telegramUserDbUtility = telegramUserDbUtility;
        this.orderedFoodDbUtility = orderedFoodDbUtility;
        this.foodDbUtitility = foodDbUtitility;
    }

    public void requestQuantity(Update update, TelegramUserDTO telegramUser) {
        String selectedFoodName = foodDbUtitility.getSelectedFood(telegramUser).getFoodName();
        SendMessage response = new SendMessage();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        if (telegramUser.getLoadedPage() == null) {
            telegramUser.setLoadedPage(0);
            telegramUserDbUtility.updateTelegramUser(telegramUser);
        } else {
            rowInline.add(new InlineKeyboardButton().setText("<<").setCallbackData("prev"));
            telegramUser.setLoadedPage(telegramUser.getLoadedPage() + 1);
            telegramUserDbUtility.updateTelegramUser(telegramUser);
        }
        for (int i = telegramUser.getLoadedPage()*5; i < (telegramUser.getLoadedPage() + 1)*5; i++) {
            if (i != 0) {
                rowInline.add(new InlineKeyboardButton().setText("" + i).setCallbackData("quantity_" + i));
            }
        }
        rowInline.add(new InlineKeyboardButton().setText(">>").setCallbackData("next"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("How many " + selectedFoodName + ". do you want?");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void requestQuantityNextPage(Update update, TelegramUserDTO telegramUser) {
        String selectedFoodName = foodDbUtitility.getSelectedFood(telegramUser).getFoodName();
        EditMessageText response = new EditMessageText();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        if (telegramUser.getLoadedPage() == null) {
            telegramUser.setLoadedPage(0);
            telegramUserDbUtility.updateTelegramUser(telegramUser);
        } else {
            rowInline.add(new InlineKeyboardButton().setText("<<").setCallbackData("prev"));
            telegramUser.setLoadedPage(telegramUser.getLoadedPage() + 1);
            telegramUserDbUtility.updateTelegramUser(telegramUser);
        }
        for (int i = telegramUser.getLoadedPage()*5; i < (telegramUser.getLoadedPage() + 1)*5; i++) {
            if (i != 0) {
                rowInline.add(new InlineKeyboardButton().setText("" + i).setCallbackData("quantity_" + i));
            }
        }
        rowInline.add(new InlineKeyboardButton().setText(">>").setCallbackData("next"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        if (update.hasMessage()){
            response.setMessageId(update.getMessage().getMessageId());
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("How Many " + selectedFoodName + ". do you want?");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void requestQuantityPrevious(Update update, TelegramUserDTO telegramUser) {
        String selectedFoodName = foodDbUtitility.getSelectedFood(telegramUser).getFoodName();
        EditMessageText response = new EditMessageText();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        if (telegramUser.getLoadedPage() == null) {
            telegramUser.setLoadedPage(0);
            telegramUserDbUtility.updateTelegramUser(telegramUser);
        } else {
            if (telegramUser.getLoadedPage() != 0) {
                rowInline.add(new InlineKeyboardButton().setText("<<").setCallbackData("prev"));
                telegramUser.setLoadedPage(telegramUser.getLoadedPage() - 1);
                telegramUserDbUtility.updateTelegramUser(telegramUser);
            }
        }
        for (int i = telegramUser.getLoadedPage()*5; i < (telegramUser.getLoadedPage() + 1)*5; i++) {
            if (i != 0) {
                rowInline.add(new InlineKeyboardButton().setText("" + i).setCallbackData("quantity_" + i));
            }
        }
        rowInline.add(new InlineKeyboardButton().setText(">>").setCallbackData("next"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        if (update.hasMessage()){
            response.setMessageId(update.getMessage().getMessageId());
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("How Many " + selectedFoodName + ". do you want?");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
