package et.com.delivereth.service.mapper;


import et.com.delivereth.domain.*;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TelegramRestaurantUser} and its DTO {@link TelegramRestaurantUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {RestorantMapper.class})
public interface TelegramRestaurantUserMapper extends EntityMapper<TelegramRestaurantUserDTO, TelegramRestaurantUser> {


    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "removeOrder", ignore = true)
    @Mapping(target = "removeRestorant", ignore = true)
    TelegramRestaurantUser toEntity(TelegramRestaurantUserDTO telegramRestaurantUserDTO);

    default TelegramRestaurantUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        TelegramRestaurantUser telegramRestaurantUser = new TelegramRestaurantUser();
        telegramRestaurantUser.setId(id);
        return telegramRestaurantUser;
    }
}
