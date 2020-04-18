package et.com.delivereth.Telegram.telegramTransport.requests;

import et.com.delivereth.Telegram.DbUtility.*;
import et.com.delivereth.Telegram.telegramTransport.main.TransportTelegramSender;
import et.com.delivereth.domain.enumeration.OrderStatus;
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
public class TransportRequestForNewOrder {
    private final TransportTelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TransportTelegramSender.class);
    private final RestorantDbUtitlity restorantDbUtitlity;
    private final FoodDbUtitility foodDbUtitility;
    private final OrderedFoodDbUtility orderedFoodDbUtility;
    private final TelegramDeliveryUserDbUtility telegramDeliveryUserDbUtility;
    private final TelegramUserDbUtility telegramUserDbUtility;

    public TransportRequestForNewOrder(TransportTelegramSender telegramSender, RestorantDbUtitlity restorantDbUtitlity, FoodDbUtitility foodDbUtitility, OrderedFoodDbUtility orderedFoodDbUtility, TelegramDeliveryUserDbUtility telegramDeliveryUserDbUtility, TelegramUserDbUtility telegramUserDbUtility) {
        this.telegramSender = telegramSender;
        this.restorantDbUtitlity = restorantDbUtitlity;
        this.foodDbUtitility = foodDbUtitility;
        this.orderedFoodDbUtility = orderedFoodDbUtility;
        this.telegramDeliveryUserDbUtility = telegramDeliveryUserDbUtility;
        this.telegramUserDbUtility = telegramUserDbUtility;
    }

    public void sendNewOrder(OrderDTO orderDTO) {
        List<OrderedFoodDTO> orderedFoodList = orderedFoodDbUtility.getOrderedFoods(orderDTO.getId());
        RestorantDTO restorant = restorantDbUtitlity.getRestorant(foodDbUtitility.getFood(orderedFoodList.get(0).getFoodId()).getRestorantId());
        List<TelegramDeliveryUserDTO> restaurantUsers = telegramDeliveryUserDbUtility.getDeliveryUser(restorant);
        for (TelegramDeliveryUserDTO telegramRestaurantUserDTO: restaurantUsers){
            sendNewOrder(telegramRestaurantUserDTO, restorant, orderedFoodList, orderDTO);
        }
    }
    public void editNewOrder(Update update, OrderDTO orderDTO, Boolean alreadyAccepted, TelegramDeliveryUserDTO telegramDeliveryUserDTO) {
        List<OrderedFoodDTO> orderedFoodList = orderedFoodDbUtility.getOrderedFoods(orderDTO.getId());
        RestorantDTO restorant = restorantDbUtitlity.getRestorant(foodDbUtitility.getFood(orderedFoodList.get(0).getFoodId()).getRestorantId());
        sendEditOrder(telegramDeliveryUserDTO, restorant, orderedFoodList, orderDTO, update, alreadyAccepted);
        if (!alreadyAccepted) {
            responsePopUpForEditOrder(update);
        } else {
            responsePopUpForAlreadyTakenByOtherDriver(update);
        }
    }
    public void sendNewOrder(TelegramDeliveryUserDTO telegramRestaurantUserDTO, RestorantDTO restorant, List<OrderedFoodDTO> orderedFoodList, OrderDTO orderDTO){
        SendMessage response = new SendMessage();
        response.setText(prepareInvoice(restorant, orderedFoodList,orderDTO));
        response.setParseMode("HTML");
        response.setChatId(telegramRestaurantUserDTO.getChatId());
        response.setReplyMarkup(TransportMenu.orderActionMenu(orderDTO, false));
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void sendEditOrder(TelegramDeliveryUserDTO telegramRestaurantUserDTO, RestorantDTO restorant, List<OrderedFoodDTO> orderedFoodList, OrderDTO orderDTO, Update update, boolean alreadyAccepted){
        EditMessageText response = new EditMessageText();
        response.setText(prepareInvoice(restorant, orderedFoodList,orderDTO));
        response.setParseMode("HTML");
        response.setChatId(telegramRestaurantUserDTO.getChatId());
        response.setReplyMarkup(TransportMenu.orderActionMenu(orderDTO,alreadyAccepted));
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
    public void responsePopUpForAlreadyTakenByOtherDriver(Update update){
        AnswerCallbackQuery response = new AnswerCallbackQuery();
        response.setCallbackQueryId(update.getCallbackQuery().getId());
        response.setShowAlert(true);
        response.setText("❗️ This order has been accepted by other delivery person.");
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
        invoice = invoice + "Restaurant location on map : /Restaurant_Location_" + restorant.getId()  +" \n";
        invoice = invoice + "Latitude : " + restorant.getLatitude()  +" \n";
        invoice = invoice + "Longitude : " + restorant.getLongtude()  +" \n";
        invoice = invoice + "Location description : " + restorant.getDescription()  +" \n";
        invoice = invoice + "\n";
        invoice = invoice +  "<strong>\uD83D\uDC68\u200D\uD83E\uDDB2 User Information</strong>\n";
        invoice = invoice + "User name: " + telegramUserDTO.getFirstName() + " " + telegramUserDTO.getLastName() + "\n";
        invoice = invoice + "Phone: " + telegramUserDTO.getPhone() + "\n";
        invoice = invoice + "Telegram chat: @" + telegramUserDTO.getUserName() + "\n";
        invoice = invoice + "User location on map: /User_Location_" + orderDTO.getId() + "\n";
        invoice = invoice + "Latitude = " + orderDTO.getLatitude()  +" \n";
        invoice = invoice + "Longitude = " + orderDTO.getLongtude()  +" \n";
        invoice = invoice + "\n";
        invoice = invoice +  "<strong>\uD83D\uDCB5 Invoice</strong>\n";
        invoice = invoice + "Food subtotal = " + String.format("%.2f", orderDTO.getTotalPrice())  +" ETB\n";
        invoice = invoice + "Transportation fee = " + String.format("%.2f", orderDTO.getTransportationFee() * 0.91)  +"ETB \n";
        invoice = invoice + "Service charge = " + String.format("%.2f", orderDTO.getTransportationFee() * 0.09)  +"ETB \n";
        invoice = invoice + "Grand total = " + String.format("%.2f", (orderDTO.getTotalPrice() + orderDTO.getTransportationFee()))  +"ETB \n";
        invoice = invoice + "\n";
        invoice = invoice + "<b>Order status : " + orderDTO.getOrderStatus() + "</b>\n";
        invoice = invoice + "<b>Order id : #" + orderDTO.getId() + "</b>\n";
        return invoice;
    }
}
