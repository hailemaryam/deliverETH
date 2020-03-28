package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.Telegram.DbUtility;
import et.com.delivereth.Telegram.TelegramHome;
import et.com.delivereth.Telegram.TelegramSender;
import et.com.delivereth.service.dto.FoodDTO;
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
public class RequestFoodList {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final DbUtility dbUtility;

    public RequestFoodList(TelegramSender telegramSender, DbUtility dbUtility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
    }

    public void requestFoodList(Update update) {
        List<FoodDTO> foodList = dbUtility.getFoodList(1L);
        foodList.forEach(foodDTO -> {
            sendFood(foodDTO, update.getMessage());
        });
    }

    public void sendFood(FoodDTO foodDTO, Message message){
        SendPhoto response = new SendPhoto();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Add To Cart").setCallbackData("food_" + foodDTO.getId()));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        response.setChatId(message.getChatId());
        InputStream inputStream = new ByteArrayInputStream(foodDTO.getIconImage());
        response.setPhoto(foodDTO.getName(), inputStream);
        response.setCaption(foodDTO.getName() + " : " + foodDTO.getPrice() + " birr");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }


}