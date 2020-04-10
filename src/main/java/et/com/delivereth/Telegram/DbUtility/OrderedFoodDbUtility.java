package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.service.*;
import et.com.delivereth.service.dto.FoodDTO;
import et.com.delivereth.service.dto.OrderedFoodCriteria;
import et.com.delivereth.service.dto.OrderedFoodDTO;
import io.github.jhipster.service.filter.LongFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderedFoodDbUtility {
    private final OrderedFoodQueryService orderedFoodQueryService;
    private final FoodDbUtitility foodDbUtitility;

    public OrderedFoodDbUtility(OrderedFoodQueryService orderedFoodQueryService, FoodDbUtitility foodDbUtitility) {
        this.orderedFoodQueryService = orderedFoodQueryService;
        this.foodDbUtitility = foodDbUtitility;
    }

    public List<OrderedFoodDTO> getOrderedFoods(Long orderId){
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(orderId);
        OrderedFoodCriteria orderedFoodCriteria = new OrderedFoodCriteria();
        orderedFoodCriteria.setOrderId(longFilter);
        return orderedFoodQueryService.findByCriteria(orderedFoodCriteria);
    }
    public Float getTotalFee(Long orderId){
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(orderId);
        OrderedFoodCriteria orderedFoodCriteria = new OrderedFoodCriteria();
        orderedFoodCriteria.setOrderId(longFilter);
        List<OrderedFoodDTO> orderdFood = orderedFoodQueryService.findByCriteria(orderedFoodCriteria);
        Double total = 0D;
        for (OrderedFoodDTO orderedFoodDTO: orderdFood){
            FoodDTO food = foodDbUtitility.getFood(orderedFoodDTO.getFoodId());
            total += orderedFoodDTO.getQuantity() * food.getPrice();
        }
        return total.floatValue();
    }
}
