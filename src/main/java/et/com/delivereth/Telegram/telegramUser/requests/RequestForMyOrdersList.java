package et.com.delivereth.Telegram.telegramUser.requests;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.DbUtility.*;
import et.com.delivereth.Telegram.telegramUser.main.TelegramHome;
import et.com.delivereth.Telegram.telegramUser.main.TelegramSender;
import et.com.delivereth.domain.TelegramDeliveryUser;
import et.com.delivereth.domain.TelegramRestaurantUser;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class RequestForMyOrdersList {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final RestorantDbUtitlity restorantDbUtitlity;
    private final FoodDbUtitility foodDbUtitility;
    private final OrderedFoodDbUtility orderedFoodDbUtility;
    private final OrderDbUtility orderDbUtility;

    public RequestForMyOrdersList(TelegramSender telegramSender, RestorantDbUtitlity restorantDbUtitlity, FoodDbUtitility foodDbUtitility, OrderedFoodDbUtility orderedFoodDbUtility, OrderDbUtility orderDbUtility) {
        this.telegramSender = telegramSender;
        this.restorantDbUtitlity = restorantDbUtitlity;
        this.foodDbUtitility = foodDbUtitility;
        this.orderedFoodDbUtility = orderedFoodDbUtility;
        this.orderDbUtility = orderDbUtility;
    }

    public void requestForMyOrdersList(Update update, TelegramUserDTO telegramUser) {
        Page<OrderDTO> orderList = orderDbUtility.getMyOrders(telegramUser);
        if (orderList.toList().size() >0) {
            sendTitle(update);
        }
        if (update.hasMessage()) {
            orderList.toList().forEach(orderDTO -> sendMyOrders(orderDTO, update.getMessage().getChatId().toString()));
        } else if (update.hasCallbackQuery()) {
            orderList.toList().forEach(orderDTO -> sendMyOrders(orderDTO, update.getCallbackQuery().getMessage().getChatId().toString()));
        }
        if (orderList.toList().size() == 0) {
            sendNoMoreItem(update);
        } else if (orderList.hasNext()) {
            sendLoadMoreButton(update);
        }
    }

    public void sendMyOrders(OrderDTO orderDTO, String chatId) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(Menu.myOrderInlineKeyBoard(orderDTO));
        response.setChatId(chatId);
        List<OrderedFoodDTO> orderedFoodList = orderedFoodDbUtility.getOrderedFoods(orderDTO.getId());
        String invoice = "";
        if (orderedFoodList.size() > 0) {
            invoice = invoice +  "<strong>\uD83C\uDFE1 Restaurant Name: " +
                restorantDbUtitlity.getRestorant(foodDbUtitility.getFood(orderedFoodList.get(0).getFoodId()).getRestorantId()).getName() +
                "</strong>\n";
        }
        for (OrderedFoodDTO orderedFood : orderedFoodList) {
            FoodDTO food = foodDbUtitility.getFood(orderedFood.getFoodId());
            invoice = invoice + (orderedFood.getFoodName() + " * " + orderedFood.getQuantity() + " = " + orderedFood.getQuantity() * food.getPrice() + "ETB\n");
        }
        invoice = invoice + "\uD83D\uDCB5 Food subtotal = " + String.format("%.2f", orderDTO.getTotalPrice())  +"ETB \n";
        invoice = invoice + "\uD83D\uDCB5 Delivery fee = " + String.format("%.2f", orderDTO.getTransportationFee())  +"ETB \n";
        invoice = invoice + "\uD83D\uDCB5 Grand Total = " + String.format("%.2f", (orderDTO.getTotalPrice() + orderDTO.getTransportationFee()))  +"ETB \n";
        invoice = invoice + "<b>Status = " + orderDTO.getOrderStatus() + "</b>\n";
        invoice = invoice + "<b>Order Id : #" + orderDTO.getId() + "</b>\n";
        response.setText(invoice);
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void sendOrderStatus(OrderDTO orderDTO, String chatId, TelegramRestaurantUserDTO telegramRestaurantUserDTO, TelegramDeliveryUserDTO telegramDeliveryUserDTO) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        String invoice = "";
        if (orderDTO.getOrderStatus().equals(OrderStatus.CANCELED_BY_RESTAURANT)) {
            invoice = invoice + StaticText.orderRejectedText;
        } else if(orderDTO.getOrderStatus().equals(OrderStatus.EXPIRED_AND_CANCELED_BY_SYSTEM)){
            invoice = invoice + StaticText.orderCancelBySystemText;
        } else {
            invoice = invoice + StaticText.orderStatuChanged;
        }
        invoice = invoice + "Status = " + orderDTO.getOrderStatus() + "\n";
        invoice = invoice + "Order Id : #" + orderDTO.getId() + "\n";
        if (telegramDeliveryUserDTO != null) {
            invoice = invoice + "Delivery user  : @" + telegramRestaurantUserDTO.getUserName();
            invoice = invoice + "Delivery user phone : @" + telegramRestaurantUserDTO.getPhone();
        } else if (telegramRestaurantUserDTO != null) {
            invoice = invoice + "Service provider : @" + telegramRestaurantUserDTO.getUserName();
            invoice = invoice + "Service provider phone : " + telegramRestaurantUserDTO.getPhone();
        }
        response.setText(invoice);
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

    private void sendLoadMoreButton(Update update) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(Menu.loadMore());
        if (update.hasMessage()) {
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("Want to list more orders.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

    private void sendNoMoreItem(Update update) {
        SendMessage response = new SendMessage();
        if (update.hasMessage()) {
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText(StaticText.noMoreOrder);
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void sendSuccefulCancel(Update update){
        sendPoup(update, StaticText.successfullyCanceld );
        delete(update);
    }
    public void sendSuccessfulRemove(Update update){
        sendPoup(update, StaticText.succesfullyRemoved);
        delete(update);
    }
    public void delete(Update update){
        DeleteMessage response = new DeleteMessage();
        if (update.hasMessage()) {
            response.setChatId(update.getMessage().getChatId());
            response.setMessageId(update.getMessage().getMessageId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
            response.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        }
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void sendPoup(Update update, String text) {
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

    public void sendTitle(Update update) {
        SendMessage response = new SendMessage();
        if (update.hasMessage()) {
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText(StaticText.yourOrderListTitle);
        response.setParseMode("HTML");
        response.setReplyMarkup(Menu.orderKeyBoardMenu(false));
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
