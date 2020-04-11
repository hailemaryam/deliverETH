package et.com.delivereth.Telegram.telegramRestorant.requests;

import et.com.delivereth.Telegram.DbUtility.*;
import et.com.delivereth.Telegram.telegramRestorant.main.RestaurantTelegramSender;
import et.com.delivereth.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class RestaurantRequestForNewOrder {
    private final RestaurantTelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(RestaurantTelegramSender.class);
    private final RestorantDbUtitlity restorantDbUtitlity;
    private final FoodDbUtitility foodDbUtitility;
    private final OrderedFoodDbUtility orderedFoodDbUtility;
    private final TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility;
    private final TelegramUserDbUtility telegramUserDbUtility;

    public RestaurantRequestForNewOrder(RestaurantTelegramSender telegramSender, RestorantDbUtitlity restorantDbUtitlity, FoodDbUtitility foodDbUtitility, OrderedFoodDbUtility orderedFoodDbUtility, TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility, TelegramUserDbUtility telegramUserDbUtility) {
        this.telegramSender = telegramSender;
        this.restorantDbUtitlity = restorantDbUtitlity;
        this.foodDbUtitility = foodDbUtitility;
        this.orderedFoodDbUtility = orderedFoodDbUtility;
        this.telegramRestaurantUserDbUtility = telegramRestaurantUserDbUtility;
        this.telegramUserDbUtility = telegramUserDbUtility;
    }

    public void sendNewOrder(OrderDTO orderDTO) {
        List<OrderedFoodDTO> orderedFoodList = orderedFoodDbUtility.getOrderedFoods(orderDTO.getId());
        RestorantDTO restorant = restorantDbUtitlity.getRestorant(foodDbUtitility.getFood(orderedFoodList.get(0).getFoodId()).getRestorantId());
        List<TelegramRestaurantUserDTO> restaurantUsers = telegramRestaurantUserDbUtility.getRestaurantUsers(restorant);
        for (TelegramRestaurantUserDTO telegramRestaurantUserDTO: restaurantUsers){
            sendNewOrder(telegramRestaurantUserDTO, restorant, orderedFoodList, orderDTO);

        }
    }
    public void editNewOrder(Update update, OrderDTO orderDTO) {
        List<OrderedFoodDTO> orderedFoodList = orderedFoodDbUtility.getOrderedFoods(orderDTO.getId());
        RestorantDTO restorant = restorantDbUtitlity.getRestorant(foodDbUtitility.getFood(orderedFoodList.get(0).getFoodId()).getRestorantId());
        List<TelegramRestaurantUserDTO> restaurantUsers = telegramRestaurantUserDbUtility.getRestaurantUsers(restorant);
        for (TelegramRestaurantUserDTO telegramRestaurantUserDTO: restaurantUsers){
            sendEditOrder(telegramRestaurantUserDTO, restorant, orderedFoodList, orderDTO, update);
            responsePopUpForEditOrder(update);
        }
    }
    public void sendNewOrder(TelegramRestaurantUserDTO telegramRestaurantUserDTO, RestorantDTO restorant, List<OrderedFoodDTO> orderedFoodList, OrderDTO orderDTO){
        SendMessage response = new SendMessage();
        response.setText(prepareInvoice(restorant, orderedFoodList,orderDTO));
        response.setParseMode("HTML");
        response.setChatId(telegramRestaurantUserDTO.getChatId());
        response.setReplyMarkup(RestaurantMenu.orderActionMenu(orderDTO));
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void sendEditOrder(TelegramRestaurantUserDTO telegramRestaurantUserDTO, RestorantDTO restorant, List<OrderedFoodDTO> orderedFoodList, OrderDTO orderDTO, Update update){
        EditMessageText response = new EditMessageText();
        response.setText(prepareInvoice(restorant, orderedFoodList,orderDTO));
        response.setParseMode("HTML");
        response.setChatId(telegramRestaurantUserDTO.getChatId());
        response.setReplyMarkup(RestaurantMenu.orderActionMenu(orderDTO));
        if (update.hasCallbackQuery()) {
            response.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        } else if (update.hasMessage()){
            response.setMessageId(update.getMessage().getMessageId());
        }
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void responsePopUpForEditOrder(Update update){
        AnswerCallbackQuery response = new AnswerCallbackQuery();
        response.setCallbackQueryId(update.getCallbackQuery().getId());
        response.setShowAlert(true);
        response.setText("\uD83D\uDC68\u200D\uD83C\uDF73 Your order status has been successfully changed.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public String prepareInvoice(RestorantDTO restorant, List<OrderedFoodDTO> orderedFoodList, OrderDTO orderDTO){
        TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
        String invoice = "";
        invoice = invoice +  "<strong>\uD83C\uDFE1 Restaurant Name: " +
            restorant.getName() +
            "</strong>\n";
        double total = 0D;
        for (OrderedFoodDTO orderedFood : orderedFoodList) {
            FoodDTO food = foodDbUtitility.getFood(orderedFood.getFoodId());
            invoice = invoice + (orderedFood.getFoodName() + " * " + orderedFood.getQuantity() + " = " + orderedFood.getQuantity() * food.getPrice() + "\n");
            total += orderedFood.getQuantity() * food.getPrice();
        }
        invoice = invoice + "<b>\uD83D\uDCB5 Total = " + String.format("%.2f", total)  +"</b> \n";
        invoice = invoice + "\n";
        invoice = invoice +  "<strong>\uD83D\uDC68\u200D\uD83E\uDDB2 User Information</strong>\n";
        invoice = invoice + "User Name: " + telegramUserDTO.getFirstName() + " " + telegramUserDTO.getLastName() + "\n";
        invoice = invoice + "Phone: " + telegramUserDTO.getPhone() + "\n";
        invoice = invoice + "Telegram Chat: @" + telegramUserDTO.getUserName() + "\n";
        invoice = invoice + "\n";
        invoice = invoice + "<b>Order Status : " + orderDTO.getOrderStatus() + "</b>\n";
        return invoice;
    }
}
