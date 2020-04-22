package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.service.KeyValuPairHolderQueryService;
import et.com.delivereth.service.dto.KeyValuPairHolderCriteria;
import et.com.delivereth.service.dto.KeyValuPairHolderDTO;
import io.github.jhipster.service.filter.StringFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyValuePairDbUtility {
    private final KeyValuPairHolderQueryService keyValuPairHolderQueryService;

    public KeyValuePairDbUtility(KeyValuPairHolderQueryService keyValuPairHolderQueryService) {
        this.keyValuPairHolderQueryService = keyValuPairHolderQueryService;
    }

    public KeyValuPairHolderDTO getByKey(String key){
        StringFilter stringFilter = new StringFilter();
        stringFilter.setEquals(key);
        KeyValuPairHolderCriteria keyValuPairHolderCriteria = new KeyValuPairHolderCriteria();
        keyValuPairHolderCriteria.setKey(stringFilter);
        List<KeyValuPairHolderDTO> keyValuPairHolderDTOList = keyValuPairHolderQueryService.findByCriteria(keyValuPairHolderCriteria);
        return keyValuPairHolderDTOList.size() > 0 ? keyValuPairHolderDTOList.get(0) : null;
    }
}
