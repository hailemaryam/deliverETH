package et.com.delivereth.service.mapper;


import et.com.delivereth.domain.*;
import et.com.delivereth.service.dto.RestorantDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restorant} and its DTO {@link RestorantDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RestorantMapper extends EntityMapper<RestorantDTO, Restorant> {


    @Mapping(target = "foods", ignore = true)
    @Mapping(target = "removeFood", ignore = true)
    @Mapping(target = "telegramRestaurantUsers", ignore = true)
    @Mapping(target = "removeTelegramRestaurantUser", ignore = true)
    @Mapping(target = "telegramDeliveryUsers", ignore = true)
    @Mapping(target = "removeTelegramDeliveryUser", ignore = true)
    Restorant toEntity(RestorantDTO restorantDTO);

    default Restorant fromId(Long id) {
        if (id == null) {
            return null;
        }
        Restorant restorant = new Restorant();
        restorant.setId(id);
        return restorant;
    }
}
