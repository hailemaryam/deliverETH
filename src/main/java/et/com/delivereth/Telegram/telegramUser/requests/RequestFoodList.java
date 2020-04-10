package et.com.delivereth.Telegram.telegramUser.requests;

import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.DbUtility.FoodDbUtitility;
import et.com.delivereth.Telegram.DbUtility.RestorantDbUtitlity;
import et.com.delivereth.Telegram.DbUtility.TelegramUserDbUtility;
import et.com.delivereth.Telegram.telegramUser.main.TelegramHome;
import et.com.delivereth.Telegram.telegramUser.main.TelegramSender;
import et.com.delivereth.service.dto.FoodDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
public class RequestFoodList {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final DbUtility dbUtility;
    private final RestorantDbUtitlity restorantDbUtitlity;
    private final TelegramUserDbUtility telegramUserDbUtility;
    private final FoodDbUtitility foodDbUtitility;

    public RequestFoodList(TelegramSender telegramSender, DbUtility dbUtility, RestorantDbUtitlity restorantDbUtitlity, TelegramUserDbUtility telegramUserDbUtility, FoodDbUtitility foodDbUtitility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
        this.restorantDbUtitlity = restorantDbUtitlity;
        this.telegramUserDbUtility = telegramUserDbUtility;
        this.foodDbUtitility = foodDbUtitility;
    }

    public void requestFoodList(Update update, TelegramUserDTO telegramUser) {
        telegramUser.setLoadedPage(0);
        telegramUserDbUtility.updateTelegramUser(telegramUser);
        Page<FoodDTO> foodList = foodDbUtitility.getFoodList(telegramUser);
        if (foodList.toList().isEmpty()) {
            sendNoMoreItem(update);
            dbUtility.cancelOrder(telegramUser);
        } else {
            sendFood(update,foodList);
        }
    }
    public void requestFoodListNext(Update update, TelegramUserDTO telegramUser) {
        telegramUser.setLoadedPage(telegramUser.getLoadedPage() + 1);
        telegramUserDbUtility.updateTelegramUser(telegramUser);
        Page<FoodDTO> foodList = foodDbUtitility.getFoodList(telegramUser);
        if (foodList.toList().isEmpty()) {
            sendNoMoreItem(update);
            dbUtility.cancelOrder(telegramUser);
        } else {
            sendFoodEdit(update,foodList);
        }
    }
    public void requestFoodListPrev(Update update, TelegramUserDTO telegramUser) {
        telegramUser.setLoadedPage(telegramUser.getLoadedPage() - 1);
        telegramUserDbUtility.updateTelegramUser(telegramUser);
        Page<FoodDTO> foodList = foodDbUtitility.getFoodList(telegramUser);
        if (foodList.toList().isEmpty()) {
            sendNoMoreItem(update);
            dbUtility.cancelOrder(telegramUser);
        } else {
            sendFoodEdit(update,foodList);
        }
    }
    public String prepareMenu(Page<FoodDTO> foodDTOList){
        String text = "<b>Menu of "+ restorantDbUtitlity.getRestorant(foodDTOList.toList().get(0).getRestorantId()).getName() + " Page " + (foodDTOList.getPageable().getPageNumber()+1) + "</b>\n";
        for (FoodDTO foodDTO: foodDTOList){
            text += "<i>" + foodDTO.getId() + ". " + foodDTO.getName() + prepareTick(foodDTO) + foodDTO.getPrice() + ": </i><a>/ADD_TO_CART_" + foodDTO.getId() + "</a>\n";
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
        response.setReplyMarkup(Menu.orderKeyBoardMenu(true));
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
