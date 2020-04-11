package et.com.delivereth.Telegram.telegramRestorant.requests;

import et.com.delivereth.Telegram.DbUtility.*;
import et.com.delivereth.Telegram.telegramRestorant.main.RestaurantTelegramSender;
import et.com.delivereth.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

    public RestaurantRequestForNewOrder(RestaurantTelegramSender telegramSender, RestorantDbUtitlity restorantDbUtitlity, FoodDbUtitility foodDbUtitility, OrderedFoodDbUtility orderedFoodDbUtility, TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility) {
        this.telegramSender = telegramSender;
        this.restorantDbUtitlity = restorantDbUtitlity;
        this.foodDbUtitility = foodDbUtitility;
        this.orderedFoodDbUtility = orderedFoodDbUtility;
        this.telegramRestaurantUserDbUtility = telegramRestaurantUserDbUtility;
    }

    private void sendNewOrder(OrderDTO orderDTO) {
        List<OrderedFoodDTO> orderedFoodList = orderedFoodDbUtility.getOrderedFoods(orderDTO.getId());
        RestorantDTO restorant = restorantDbUtitlity.getRestorant(foodDbUtitility.getFood(orderedFoodList.get(0).getFoodId()).getRestorantId());
        List<TelegramRestaurantUserDTO> restaurantUsers = telegramRestaurantUserDbUtility.getRestaurantUsers(restorant);
        for (TelegramRestaurantUserDTO telegramRestaurantUserDTO: restaurantUsers){
            sendNewOrder(telegramRestaurantUserDTO, restorant, orderedFoodList, orderDTO);
        }
    }
    private void sendNewOrder(TelegramRestaurantUserDTO telegramRestaurantUserDTO, RestorantDTO restorant, List<OrderedFoodDTO> orderedFoodList, OrderDTO orderDTO){
        SendMessage response = new SendMessage();
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

        response.setText(invoice);
        response.setParseMode("HTML");
        response.setChatId(telegramRestaurantUserDTO.getChatId());
        response.setReplyMarkup(RestaurantMenu.orderActionMenu(orderDTO));
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
