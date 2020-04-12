package et.com.delivereth.service.mapper;


import et.com.delivereth.domain.*;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TelegramDeliveryUser} and its DTO {@link TelegramDeliveryUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TelegramDeliveryUserMapper extends EntityMapper<TelegramDeliveryUserDTO, TelegramDeliveryUser> {


    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "removeOrder", ignore = true)
    TelegramDeliveryUser toEntity(TelegramDeliveryUserDTO telegramDeliveryUserDTO);

    default TelegramDeliveryUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        TelegramDeliveryUser telegramDeliveryUser = new TelegramDeliveryUser();
        telegramDeliveryUser.setId(id);
        return telegramDeliveryUser;
    }
}
