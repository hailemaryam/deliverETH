package et.com.delivereth.service.mapper;


import et.com.delivereth.domain.*;
import et.com.delivereth.service.dto.KeyValuPairHolderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link KeyValuPairHolder} and its DTO {@link KeyValuPairHolderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface KeyValuPairHolderMapper extends EntityMapper<KeyValuPairHolderDTO, KeyValuPairHolder> {



    default KeyValuPairHolder fromId(Long id) {
        if (id == null) {
            return null;
        }
        KeyValuPairHolder keyValuPairHolder = new KeyValuPairHolder();
        keyValuPairHolder.setId(id);
        return keyValuPairHolder;
    }
}
