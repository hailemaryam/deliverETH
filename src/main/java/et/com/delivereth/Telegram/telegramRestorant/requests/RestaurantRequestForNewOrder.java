package et.com.delivereth.Telegram.telegramRestorant.requests;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.DbUtility.*;
import et.com.delivereth.Telegram.OtherUtility.SendSMS;
import et.com.delivereth.Telegram.telegramRestorant.main.RestaurantTelegramSender;
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
public class RestaurantRequestForNewOrder {
    private final RestaurantTelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(RestaurantTelegramSender.class);
    private final RestorantDbUtitlity restorantDbUtitlity;
    private final FoodDbUtitility foodDbUtitility;
    private final OrderedFoodDbUtility orderedFoodDbUtility;
    private final TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility;
    private final TelegramUserDbUtility telegramUserDbUtility;
    private final SendSMS sendSMS;

    public RestaurantRequestForNewOrder(RestaurantTelegramSender telegramSender, RestorantDbUtitlity restorantDbUtitlity, FoodDbUtitility foodDbUtitility, OrderedFoodDbUtility orderedFoodDbUtility, TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility, TelegramUserDbUtility telegramUserDbUtility, SendSMS sendSMS) {
        this.telegramSender = telegramSender;
        this.restorantDbUtitlity = restorantDbUtitlity;
        this.foodDbUtitility = foodDbUtitility;
        this.orderedFoodDbUtility = orderedFoodDbUtility;
        this.telegramRestaurantUserDbUtility = telegramRestaurantUserDbUtility;
        this.telegramUserDbUtility = telegramUserDbUtility;
        this.sendSMS = sendSMS;
    }

    public void sendNewOrder(OrderDTO orderDTO) {
        List<OrderedFoodDTO> orderedFoodList = orderedFoodDbUtility.getOrderedFoods(orderDTO.getId());
        RestorantDTO restorant = restorantDbUtitlity.getRestorant(foodDbUtitility.getFood(orderedFoodList.get(0).getFoodId()).getRestorantId());
        List<TelegramRestaurantUserDTO> restaurantUsers = telegramRestaurantUserDbUtility.getRestaurantUsers(restorant);
        for (TelegramRestaurantUserDTO telegramRestaurantUserDTO : restaurantUsers) {
            sendNewOrder(telegramRestaurantUserDTO, restorant, orderedFoodList, orderDTO);
            sendSMS.sendSMS(
                telegramRestaurantUserDTO.getPhone().startsWith("+") ?
                    telegramRestaurantUserDTO.getPhone().substring(1): telegramRestaurantUserDTO.getPhone() ,
                StaticText.newOrderForRestauratnSms);
        }
    }

    public void editNewOrder(Update update, OrderDTO orderDTO, TelegramRestaurantUserDTO telegramRestaurantUserDTO) {
        List<OrderedFoodDTO> orderedFoodList = orderedFoodDbUtility.getOrderedFoods(orderDTO.getId());
        RestorantDTO restorant = restorantDbUtitlity.getRestorant(foodDbUtitility.getFood(orderedFoodList.get(0).getFoodId()).getRestorantId());
        sendEditOrder(telegramRestaurantUserDTO, restorant, orderedFoodList, orderDTO, update);
    }

    public void sendNewOrder(TelegramRestaurantUserDTO telegramRestaurantUserDTO, RestorantDTO restorant, List<OrderedFoodDTO> orderedFoodList, OrderDTO orderDTO) {
        SendMessage response = new SendMessage();
        response.setText(prepareInvoice(restorant, orderedFoodList, orderDTO));
        response.setParseMode("HTML");
        response.setChatId(telegramRestaurantUserDTO.getChatId());
        response.setReplyMarkup(RestaurantMenu.orderActionMenu(orderDTO));
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

    public void sendEditOrder(TelegramRestaurantUserDTO telegramRestaurantUserDTO, RestorantDTO restorant, List<OrderedFoodDTO> orderedFoodList, OrderDTO orderDTO, Update update) {
        EditMessageText response = new EditMessageText();
        response.setText(prepareInvoice(restorant, orderedFoodList, orderDTO));
        response.setParseMode("HTML");
        response.setChatId(telegramRestaurantUserDTO.getChatId());
        response.setReplyMarkup(RestaurantMenu.orderActionMenu(orderDTO));
        if (update.hasCallbackQuery()) {
            response.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        } else if (update.hasMessage()) {
            response.setMessageId(update.getMessage().getMessageId());
        }
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

    public void responsePopUpForEditOrder(Update update) {
        popUpMessage(update, StaticText.orderStatusSuccessfullyChanged);
    }

    public void responsePopUpForAlreadyCanceledByUser(Update update) {
        popUpMessage(update, StaticText.alreadyCanceledByUser);
    }

    public void responsePopUpForAlreadyAccepted(Update update) {
        popUpMessage(update, StaticText.alreadyAccepted);
    }
    public void responsePopUpForAlreadyProccesedByOtherUser(Update update) {
        popUpMessage(update, StaticText.alreadyProccedByOtherUser);
    }

    public void popUpMessage(Update update, String text) {
        AnswerCallbackQuery response = new AnswerCallbackQuery();
        response.setCallbackQueryId(update.getCallbackQuery().getId());
        response.setShowAlert(true);
        response.setText(text);
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

    public String prepareInvoice(RestorantDTO restorant, List<OrderedFoodDTO> orderedFoodList, OrderDTO orderDTO) {
        TelegramUserDTO telegramUserDTO = telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId());
        String invoice = "";
        invoice = invoice + "<strong>\uD83C\uDFE1 Restaurant Name: " +
            restorant.getName() +
            "</strong>\n";
        double total = 0D;
        for (OrderedFoodDTO orderedFood : orderedFoodList) {
            FoodDTO food = foodDbUtitility.getFood(orderedFood.getFoodId());
            invoice = invoice + (orderedFood.getFoodName() + " * " + orderedFood.getQuantity() + " = " + orderedFood.getQuantity() * food.getPrice() + "ETB\n");
            total += orderedFood.getQuantity() * food.getPrice();
        }
        invoice = invoice + "<b>\uD83D\uDCB5 Total = " + String.format("%.2f", total) + "ETB</b> \n";
        invoice = invoice + "\n";
        invoice = invoice + "<strong>\uD83D\uDC68\u200D\uD83E\uDDB2 User Information</strong>\n";
        invoice = invoice + "User Name: " + telegramUserDTO.getFirstName() + " " + telegramUserDTO.getLastName() + "\n";
        invoice = invoice + "Phone: " + telegramUserDTO.getPhone() + "\n";
        invoice = invoice + "Telegram Chat: @" + telegramUserDTO.getUserName() + "\n";
        invoice = invoice + "\n";
        invoice = invoice + "<b>Order Status : " + orderDTO.getOrderStatus() + "</b>\n";
        invoice = invoice + "<b>Order Id : #" + orderDTO.getId() + "</b>\n";
        return invoice;
    }

    public void sendOrderStatus(OrderDTO orderDTO, String chatId) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        String invoice = "";
        if (orderDTO.getOrderStatus().equals(OrderStatus.CANCELED_BY_RESTAURANT)) {
            invoice = invoice + StaticText.orderRejectedText;
        } else if (orderDTO.getOrderStatus().equals(OrderStatus.EXPIRED_AND_CANCELED_BY_SYSTEM)) {
            invoice = invoice + StaticText.orderCancelBySystemForRestaurantText;
        } else {
            invoice = invoice + StaticText.orderStatuChanged;
        }
        invoice = invoice + "Status = " + orderDTO.getOrderStatus() + "\n";
        invoice = invoice + "Order Id : #" + orderDTO.getId() + "\n";
        response.setText(invoice);
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

}
