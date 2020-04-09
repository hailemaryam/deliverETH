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
        Page<FoodDTO> foodList = dbUtility.getFoodList(telegramUser);
        if (!foodList.toList().isEmpty()) {
            sendFood(update, prepareMenu(foodList));
        }
        if (foodList.toList().isEmpty()) {
            sendNoMoreItem(update);
            dbUtility.cancelOrder(telegramUser);
        } else if (foodList.hasNext()) {
            sendLoadMoreButton(update);
        }
    }
    public String prepareMenu(Page<FoodDTO> foodDTOList){
        String text = "<b>Menu of "+ dbUtility.getRestorant(foodDTOList.toList().get(0).getRestorantId()).getName() + " Page " + (foodDTOList.getPageable().getPageNumber()+1) + "</b>\n";
        for (FoodDTO foodDTO: foodDTOList){
            text += "<i>" + foodDTO.getName() + prepareTick(foodDTO) + foodDTO.getPrice() + ": </i><a>/add_to_cart_" + foodDTO.getId() + "</a>\n";
        }
        text += "<b>click the add to cart link to select the food.</b>";
        return text;
    }
    public String prepareTick(FoodDTO foodDTO){
        int size = 60 - foodDTO.getName().length() - foodDTO.getPrice().toString().length();
        String tickes = "";
        for (int i = 0;i  < size; i++ ){
            tickes += "-";
        }
        return tickes;
    }
    public void sendFood(Update update, String message){
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText(message);
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
        rowInline.add(new InlineKeyboardButton().setText("Load More").setCallbackData("loadMore"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("Want to list more item.");
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
        response.setText("There are no food list.");
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
