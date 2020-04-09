package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.service.*;
import et.com.delivereth.service.dto.*;
import io.github.jhipster.service.filter.LongFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodDbUtitility {
    private final FoodQueryService foodQueryService;
    private final FoodService foodService;
    private final TelegramUserDbUtility telegramUserDbUtility;
    private final TelegramUserService telegramUserService;
    private final OrderService orderService;
    private final OrderedFoodService orderedFoodService;
    private final OrderedFoodQueryService orderedFoodQueryService;


    public FoodDbUtitility(FoodQueryService foodQueryService, FoodService foodService, TelegramUserDbUtility telegramUserDbUtility, TelegramUserService telegramUserService, OrderService orderService, OrderedFoodService orderedFoodService, OrderedFoodQueryService orderedFoodQueryService) {
        this.foodQueryService = foodQueryService;
        this.foodService = foodService;
        this.telegramUserDbUtility = telegramUserDbUtility;
        this.telegramUserService = telegramUserService;
        this.orderService = orderService;
        this.orderedFoodService = orderedFoodService;
        this.orderedFoodQueryService = orderedFoodQueryService;
    }

    public Page<FoodDTO> getFoodList(TelegramUserDTO telegramUser){
        FoodCriteria foodCriteria = new FoodCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(telegramUser.getSelectedRestorant());
        foodCriteria.setRestorantId(longFilter);
        telegramUserDbUtility.updateTelegramUser(telegramUser);
        return foodQueryService.findByCriteria(foodCriteria, PageRequest.of(telegramUser.getLoadedPage(), 10));
    }
    public void addFoodToOrder(TelegramUserDTO telegramUser, Long foodId){
        OrderDTO order = orderService.findOne(telegramUser.getOrderIdPaused()).get();
        FoodDTO food = foodService.findOne(foodId).get();
        OrderedFoodDTO orderedFood = new OrderedFoodDTO();
        orderedFood.setFoodId(food.getId());
        orderedFood.setQuantity(1);
        orderedFood.setOrderId(order.getId());
        orderedFood = orderedFoodService.save(orderedFood);
        telegramUser.setOrderedFoodIdPaused(orderedFood.getId());
        telegramUserService.save(telegramUser);
    }
    public FoodDTO getFood(Long id){
        return foodService.findOne(id).get();
    }
    public OrderedFoodDTO getSelectedFood(TelegramUserDTO telegramUser){
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(telegramUser.getOrderedFoodIdPaused());
        OrderedFoodCriteria orderedFoodCriteria = new OrderedFoodCriteria();
        orderedFoodCriteria.setId(longFilter);
        List<OrderedFoodDTO> orderFoodList = orderedFoodQueryService.findByCriteria(orderedFoodCriteria);
        return orderFoodList.size() > 0? orderFoodList.get(0): null;
    }

}
