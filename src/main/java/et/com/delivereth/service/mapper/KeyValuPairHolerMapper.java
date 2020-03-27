package et.com.delivereth.service.mapper;


import et.com.delivereth.domain.*;
import et.com.delivereth.service.dto.KeyValuPairHolerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link KeyValuPairHoler} and its DTO {@link KeyValuPairHolerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface KeyValuPairHolerMapper extends EntityMapper<KeyValuPairHolerDTO, KeyValuPairHoler> {



    default KeyValuPairHoler fromId(Long id) {
        if (id == null) {
            return null;
        }
        KeyValuPairHoler keyValuPairHoler = new KeyValuPairHoler();
        keyValuPairHoler.setId(id);
        return keyValuPairHoler;
    }
}
