package et.com.delivereth.service.mapper;


import et.com.delivereth.domain.*;
import et.com.delivereth.service.dto.OrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {TelegramUserMapper.class})
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {

    @Mapping(source = "telegramUser.id", target = "telegramUserId")
    @Mapping(source = "telegramUser.userName", target = "telegramUserUserName")
    OrderDTO toDto(Order order);

    @Mapping(target = "orderedFoods", ignore = true)
    @Mapping(target = "removeOrderedFood", ignore = true)
    @Mapping(source = "telegramUserId", target = "telegramUser")
    Order toEntity(OrderDTO orderDTO);

    default Order fromId(Long id) {
        if (id == null) {
            return null;
        }
        Order order = new Order();
        order.setId(id);
        return order;
    }
}
