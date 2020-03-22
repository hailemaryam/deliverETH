package et.com.delivereth.service.mapper;


import et.com.delivereth.domain.*;
import et.com.delivereth.service.dto.TelegramUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TelegramUser} and its DTO {@link TelegramUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TelegramUserMapper extends EntityMapper<TelegramUserDTO, TelegramUser> {


    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "removeOrder", ignore = true)
    TelegramUser toEntity(TelegramUserDTO telegramUserDTO);

    default TelegramUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setId(id);
        return telegramUser;
    }
}
