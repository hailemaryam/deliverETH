package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.Telegram.DbUtility;
import et.com.delivereth.Telegram.TelegramHome;
import et.com.delivereth.Telegram.TelegramSender;
import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.service.dto.FoodDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
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
public class RequestFoodList {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final DbUtility dbUtility;
    public RequestFoodList(TelegramSender telegramSender, DbUtility dbUtility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
    }
    public void requestFoodList(Update update, TelegramUserDTO telegramUser) {
        telegramUser.setLoadedPage(0);
        dbUtility.updateTelegramUser(telegramUser);
        Page<FoodDTO> foodList = dbUtility.getFoodList(telegramUser);
        if (foodList.toList().isEmpty()) {
            sendNoMoreItem(update);
            dbUtility.cancelOrder(telegramUser);
        } else {
            sendFood(update,foodList);
        }
    }
    public void requestFoodListNext(Update update, TelegramUserDTO telegramUser) {
        telegramUser.setLoadedPage(telegramUser.getLoadedPage() + 1);
        dbUtility.updateTelegramUser(telegramUser);
        Page<FoodDTO> foodList = dbUtility.getFoodList(telegramUser);
        if (foodList.toList().isEmpty()) {
            sendNoMoreItem(update);
            dbUtility.cancelOrder(telegramUser);
        } else {
            sendFoodEdit(update,foodList);
        }
    }
    public void requestFoodListPrev(Update update, TelegramUserDTO telegramUser) {
        telegramUser.setLoadedPage(telegramUser.getLoadedPage() - 1);
        dbUtility.updateTelegramUser(telegramUser);
        Page<FoodDTO> foodList = dbUtility.getFoodList(telegramUser);
        if (foodList.toList().isEmpty()) {
            sendNoMoreItem(update);
            dbUtility.cancelOrder(telegramUser);
        } else {
            sendFoodEdit(update,foodList);
        }
    }
    public String prepareMenu(Page<FoodDTO> foodDTOList){
        String text = "<b>Menu of "+ dbUtility.getRestorant(foodDTOList.toList().get(0).getRestorantId()).getName() + " Page " + (foodDTOList.getPageable().getPageNumber()+1) + "</b>\n";
        for (FoodDTO foodDTO: foodDTOList){
            text += "<i>" + foodDTO.getName() + prepareTick(foodDTO) + foodDTO.getPrice() + ": </i><a>/ADD_TO_CART_" + foodDTO.getId() + "</a>\n";
        }
        text += "<b>click the add to cart link to select the food.</b>";
        return text;
    }
    public String prepareTick(FoodDTO foodDTO){
        int size = 40 - foodDTO.getName().length() - foodDTO.getPrice().toString().length();
        String tickes = "";
        for (int i = 0;i  < size; i++ ){
            tickes += "-";
        }
        return tickes;
    }
    public void sendFood(Update update, Page<FoodDTO> foodDTOPage){
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        if (foodDTOPage.hasPrevious()) {
            rowInline.add(new InlineKeyboardButton().setText("<< Prev").setCallbackData("prev"));
        }
        if (foodDTOPage.hasNext()) {
            rowInline.add(new InlineKeyboardButton().setText("Next >>").setCallbackData("next"));
        }
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        response.setText(prepareMenu(foodDTOPage));
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void sendFoodEdit(Update update, Page<FoodDTO> foodDTOPage){
        EditMessageText response = new EditMessageText();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
            response.setMessageId(update.getMessage().getMessageId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
            response.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        }
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        if (foodDTOPage.hasPrevious()) {
            rowInline.add(new InlineKeyboardButton().setText("<< Prev").setCallbackData("prev"));
        }
        if (foodDTOPage.hasNext()) {
            rowInline.add(new InlineKeyboardButton().setText("Next >>").setCallbackData("next"));
        }
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        response.setText(prepareMenu(foodDTOPage));
        response.setParseMode("HTML");
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
        response.setText("Menu are not added in selected restaurant.");
        response.setReplyMarkup(Menu.orderKeyBoardMenu());
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
