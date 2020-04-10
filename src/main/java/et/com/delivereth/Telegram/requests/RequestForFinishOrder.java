package et.com.delivereth.Telegram.requests;

import et.com.delivereth.Telegram.DbUtility.*;
import et.com.delivereth.Telegram.OtherUtility.DistanceCalculator;
import et.com.delivereth.Telegram.TelegramSender;
import et.com.delivereth.domain.TelegramUser;
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
    private final OrderDbUtility orderDbUtility;

    public RequestForFinishOrder(TelegramSender telegramSender, RestorantDbUtitlity restorantDbUtitlity, FoodDbUtitility foodDbUtitility, OrderedFoodDbUtility orderedFoodDbUtility, OrderDbUtility orderDbUtility) {
        this.telegramSender = telegramSender;
        this.restorantDbUtitlity = restorantDbUtitlity;
        this.foodDbUtitility = foodDbUtitility;
        this.orderedFoodDbUtility = orderedFoodDbUtility;
        this.orderDbUtility = orderDbUtility;
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
        Double transportaionFee = transportaionFee(telegramUser, restorant);
        String invoice = "<strong>Restaurant Name: " +
             restorant.getName() +
            "</strong>\n";
        Double total = 0D;
        for (OrderedFoodDTO orderedFood : orderedFoodList) {
            FoodDTO food = foodDbUtitility.getFood(orderedFood.getFoodId());
            invoice = invoice + (orderedFood.getFoodName() + " * " + orderedFood.getQuantity() + " = " + orderedFood.getQuantity() * food.getPrice() + "\n" );
            total += orderedFood.getQuantity() * food.getPrice();
        }
        invoice = invoice + "<b>\uD83D\uDCB5 Total = " + total +"</b>\n";
        invoice = invoice + "<b>\uD83D\uDCB5 Transportation Fee = " + transportaionFee +"</b>\n";
        invoice = invoice + "<b>\uD83D\uDCB5 Grand Total = " + (transportaionFee + total) +"</b>\n";
        response.setText(invoice);
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    private Double transportaionFee(TelegramUserDTO telegramUser, RestorantDTO restorantDTO){
        OrderDTO orderDTO;
        if (telegramUser.getOrderIdPaused() != null) {
            orderDTO = orderDbUtility.getOrderById(telegramUser.getOrderIdPaused());
            double distance = DistanceCalculator.distance(
                orderDTO.getLatitude().doubleValue(),
                restorantDTO.getLatitude().doubleValue(),
                orderDTO.getLongtude().doubleValue(),
                restorantDTO.getLongtude().doubleValue()
            ,0,0);
            return distance < 5000 ? 75 * 1.12 : (75 + (distance - 5000) * 10) * 1.12;
        }
        return null;
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
