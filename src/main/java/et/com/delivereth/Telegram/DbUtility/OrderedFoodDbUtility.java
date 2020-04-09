package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.service.*;
import et.com.delivereth.service.dto.OrderedFoodCriteria;
import et.com.delivereth.service.dto.OrderedFoodDTO;
import io.github.jhipster.service.filter.LongFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderedFoodDbUtility {
    private final OrderedFoodQueryService orderedFoodQueryService;

    public OrderedFoodDbUtility(OrderedFoodQueryService orderedFoodQueryService) {
        this.orderedFoodQueryService = orderedFoodQueryService;
    }

    public List<OrderedFoodDTO> getOrderedFoods(Long orderId){
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(orderId);
        OrderedFoodCriteria orderedFoodCriteria = new OrderedFoodCriteria();
        orderedFoodCriteria.setOrderId(longFilter);
        return orderedFoodQueryService.findByCriteria(orderedFoodCriteria);
    }

}
