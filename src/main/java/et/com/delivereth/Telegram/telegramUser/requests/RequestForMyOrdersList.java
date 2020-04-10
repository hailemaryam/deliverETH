package et.com.delivereth.Telegram.telegramUser.requests;

import et.com.delivereth.Telegram.DbUtility.*;
import et.com.delivereth.Telegram.telegramUser.main.TelegramHome;
import et.com.delivereth.Telegram.telegramUser.main.TelegramSender;
import et.com.delivereth.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
        orderList.toList().forEach(orderDTO -> sendMyOrders(orderDTO, update));
        if (orderList.toList().size() == 0) {
            sendNoMoreItem(update);
        } else if (orderList.hasNext()) {
            sendLoadMoreButton(update);
        }
    }

    private void sendMyOrders(OrderDTO orderDTO, Update update) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(Menu.myOrderInlineKeyBoard(orderDTO));
        if (update.hasMessage()) {
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        List<OrderedFoodDTO> orderedFoodList = orderedFoodDbUtility.getOrderedFoods(orderDTO.getId());
        String invoice = "";

        if (orderedFoodList.size() > 0) {
            invoice = invoice +  "<strong>Restaurant Name: " +
                restorantDbUtitlity.getRestorant(foodDbUtitility.getFood(orderedFoodList.get(0).getFoodId()).getRestorantId()).getName() +
                "</strong>\n";
        }
        double total = 0D;
        for (OrderedFoodDTO orderedFood : orderedFoodList) {
            FoodDTO food = foodDbUtitility.getFood(orderedFood.getFoodId());
            invoice = invoice + (orderedFood.getFoodName() + " * " + orderedFood.getQuantity() + " = " + orderedFood.getQuantity() * food.getPrice() + "\n");
            total += orderedFood.getQuantity() * food.getPrice();
        }
        invoice = invoice + "<b>Total = " + total + "</b> \n";
        invoice = invoice + "<b>Status = " + orderDTO.getOrderStatus() + "</b>";
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
        response.setText("There are no more orders to load.");
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
        response.setText("<b>Your Order List</b>");
        response.setParseMode("HTML");
        response.setReplyMarkup(Menu.orderKeyBoardMenu(false));
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
