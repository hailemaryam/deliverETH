package et.com.delivereth.Telegram.telegramUser.requests;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.DbUtility.*;
import et.com.delivereth.Telegram.telegramUser.main.TelegramSender;
import et.com.delivereth.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class RequestForFinishOrder {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(RequestForFinishOrder.class);
    public final RestorantDbUtitlity restorantDbUtitlity;
    private final FoodDbUtitility foodDbUtitility;
    private final OrderedFoodDbUtility orderedFoodDbUtility;
    private final DbUtility dbUtility;

    public RequestForFinishOrder(TelegramSender telegramSender, RestorantDbUtitlity restorantDbUtitlity, FoodDbUtitility foodDbUtitility, OrderedFoodDbUtility orderedFoodDbUtility, DbUtility dbUtility) {
        this.telegramSender = telegramSender;
        this.restorantDbUtitlity = restorantDbUtitlity;
        this.foodDbUtitility = foodDbUtitility;
        this.orderedFoodDbUtility = orderedFoodDbUtility;
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
        List<OrderedFoodDTO> orderedFoodList = orderedFoodDbUtility.getOrderedFoods(telegramUser.getOrderIdPaused());
        RestorantDTO restorant = restorantDbUtitlity.getRestorant(telegramUser.getSelectedRestorant());
        Double transportaionFee = dbUtility.transportaionFee(telegramUser, restorant);
        String invoice = "<strong>\uD83C\uDFE1 Restaurant Name: " +
             restorant.getName() +
            "</strong>\n";
        Double total = orderedFoodDbUtility.getTotalFee(telegramUser.getOrderIdPaused()).doubleValue();
        for (OrderedFoodDTO orderedFood : orderedFoodList) {
            FoodDTO food = foodDbUtitility.getFood(orderedFood.getFoodId());
            invoice = invoice + "<code>" + orderedFood.getFoodName() + " x " + orderedFood.getQuantity() + " = " + orderedFood.getQuantity() * food.getPrice() + "ETB</code>\n";
        }
        invoice = invoice + "\uD83D\uDCB5 Food subtotal = " + String.format("%.2f", total)  +"ETB\n";
        invoice = invoice + "\uD83D\uDCB5 Delivery service fee = " +  String.format("%.2f", transportaionFee) +"ETB\n";
        invoice = invoice + "\uD83D\uDCB5 Grand Total = " + String.format("%.2f", (transportaionFee + total)) +"ETB \n";
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
        response.setReplyMarkup(Menu.orderKeyBoardMenu(false));
        response.setText(StaticText.orderSuccessfullyRegistered);
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
        response.setText(StaticText.orderSuccessfullyRegistered);
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
        response.setText(StaticText.orderSuccessfullyCanceled);
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
