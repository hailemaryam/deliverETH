package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.Telegram.DbUtility;
import et.com.delivereth.Telegram.TelegramSender;
import et.com.delivereth.domain.Food;
import et.com.delivereth.service.dto.FoodDTO;
import et.com.delivereth.service.dto.OrderedFoodDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    public void requestForFinishOrder(Update update, TelegramUserDTO telegramUser) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(Menu.finishOrAddMore());
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        List<OrderedFoodDTO> orderedFoodList = dbUtility.getOrderedFoods(telegramUser.getOrderIdPaused());
        String invoice = "<strong>Restaurant Name: " +
             dbUtility.getRestorant(telegramUser.getSelectedRestorant()).getName() +
            "</strong>\n";
        Double total = 0D;
        for (OrderedFoodDTO orderedFood : orderedFoodList) {
            FoodDTO food = dbUtility.getFood(orderedFood.getFoodId());
            invoice = invoice + (orderedFood.getFoodName() + " * " + orderedFood.getQuantity() + " = " + orderedFood.getQuantity() * food.getPrice() + "\n" );
            total += orderedFood.getQuantity() * food.getPrice();
        }
        invoice = invoice + "<b>\uD83D\uDCB5 Total = " + total +"</b>";
        response.setText(invoice);
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void responseForFinishOrder(Update update){
        responsePopUpForCancelOrder(update);
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("\uD83D\uDC68\u200D\uD83C\uDF73 Your order has been successfully registered.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void responsePopUpForCancelOrder(Update update){
        AnswerCallbackQuery response = new AnswerCallbackQuery();
        response.setCallbackQueryId(update.getCallbackQuery().getId());
        response.setShowAlert(true);
        response.setText("\uD83D\uDC68\u200D\uD83C\uDF73 Your order has been successfully registered.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

    public void responseForCancelOrder(Update update){
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("\uD83D\uDC68\u200D\uD83C\uDF73 Your order has been successfully canceled.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
