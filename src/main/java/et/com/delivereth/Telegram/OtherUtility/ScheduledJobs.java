package et.com.delivereth.Telegram.OtherUtility;

import et.com.delivereth.Telegram.DbUtility.*;
import et.com.delivereth.Telegram.telegramRestorant.requests.RestaurantRequestForNewOrder;
import et.com.delivereth.Telegram.telegramUser.requests.RequestForMyOrdersList;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.service.dto.OrderDTO;
import et.com.delivereth.service.dto.OrderedFoodDTO;
import et.com.delivereth.service.dto.RestorantDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ScheduledJobs {
    private final OrderDbUtility orderDbUtility;
    private final TelegramUserDbUtility telegramUserDbUtility;
    private final RequestForMyOrdersList requestForMyOrdersList;
    private final RestorantDbUtitlity restorantDbUtitlity;
    private final OrderedFoodDbUtility orderedFoodDbUtility;
    private final FoodDbUtitility foodDbUtitility;
    private final TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility;
    private final RestaurantRequestForNewOrder restaurantRequestForNewOrder;

    public ScheduledJobs(OrderDbUtility orderDbUtility, TelegramUserDbUtility telegramUserDbUtility, RequestForMyOrdersList requestForMyOrdersList, RestorantDbUtitlity restorantDbUtitlity, OrderedFoodDbUtility orderedFoodDbUtility, FoodDbUtitility foodDbUtitility, TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility, RestaurantRequestForNewOrder restaurantRequestForNewOrder) {
        this.orderDbUtility = orderDbUtility;
        this.telegramUserDbUtility = telegramUserDbUtility;
        this.requestForMyOrdersList = requestForMyOrdersList;
        this.restorantDbUtitlity = restorantDbUtitlity;
        this.orderedFoodDbUtility = orderedFoodDbUtility;
        this.foodDbUtitility = foodDbUtitility;
        this.telegramRestaurantUserDbUtility = telegramRestaurantUserDbUtility;
        this.restaurantRequestForNewOrder = restaurantRequestForNewOrder;
    }

    @Scheduled(cron = "0 0/7 * * * ?")
    public void orderExpire() {
        System.out.println("cron job for expiring order is running");
        for (OrderDTO orderDTO: orderDbUtility.getActiveOrder()){
            if (orderDTO.getOrderStatus().equals(OrderStatus.ORDERED) && orderDTO.getDate().isBefore(Instant.now().minusSeconds(420))){
                orderDTO.setOrderStatus(OrderStatus.EXPIRED_AND_CANCELED_BY_SYSTEM);
                orderDTO = orderDbUtility.updateOrder(orderDTO);
                requestForMyOrdersList.sendOrderStatus(orderDTO, telegramUserDbUtility.getTelegramUser(orderDTO.getTelegramUserId()).getChatId());
                for (TelegramRestaurantUserDTO telegramRestaurantUserDTO: getRestaurantUser(orderDTO)){
                    restaurantRequestForNewOrder.sendOrderStatus(orderDTO, telegramRestaurantUserDTO.getChatId());
                }
            }
        }
    }
    List<TelegramRestaurantUserDTO> getRestaurantUser(OrderDTO orderDTO){
        List<OrderedFoodDTO> orderedFoodList = orderedFoodDbUtility.getOrderedFoods(orderDTO.getId());
        RestorantDTO restorant = restorantDbUtitlity.getRestorant(foodDbUtitility.getFood(orderedFoodList.get(0).getFoodId()).getRestorantId());
        List<TelegramRestaurantUserDTO> restaurantUsers = telegramRestaurantUserDbUtility.getRestaurantUsers(restorant);
        return restaurantUsers;
    }
}
