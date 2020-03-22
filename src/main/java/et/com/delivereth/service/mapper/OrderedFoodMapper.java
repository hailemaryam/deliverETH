package et.com.delivereth.service.mapper;


import et.com.delivereth.domain.*;
import et.com.delivereth.service.dto.OrderedFoodDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderedFood} and its DTO {@link OrderedFoodDTO}.
 */
@Mapper(componentModel = "spring", uses = {FoodMapper.class, OrderMapper.class})
public interface OrderedFoodMapper extends EntityMapper<OrderedFoodDTO, OrderedFood> {

    @Mapping(source = "food.id", target = "foodId")
    @Mapping(source = "food.name", target = "foodName")
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.date", target = "orderDate")
    OrderedFoodDTO toDto(OrderedFood orderedFood);

    @Mapping(source = "foodId", target = "food")
    @Mapping(source = "orderId", target = "order")
    OrderedFood toEntity(OrderedFoodDTO orderedFoodDTO);

    default OrderedFood fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderedFood orderedFood = new OrderedFood();
        orderedFood.setId(id);
        return orderedFood;
    }
}
