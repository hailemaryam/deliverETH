package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.service.RestorantQueryService;
import et.com.delivereth.service.RestorantService;
import et.com.delivereth.service.dto.RestorantCriteria;
import et.com.delivereth.service.dto.RestorantDTO;
import io.github.jhipster.service.filter.StringFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestorantDbUtitlity {
    private static final Logger logger = LoggerFactory.getLogger(DbUtility.class);
    private final RestorantService restorantService;
    private final RestorantQueryService restorantQueryService;

    public RestorantDbUtitlity(RestorantService restorantService, RestorantQueryService restorantQueryService) {
        this.restorantService = restorantService;
        this.restorantQueryService = restorantQueryService;
    }

    public RestorantDTO getRestorant(String restaurantUserName) {
        RestorantCriteria restorantCriteria = new RestorantCriteria();
        StringFilter stringFilter = new StringFilter();
        stringFilter.equals(restaurantUserName);
        restorantCriteria.setUserName(stringFilter);
        List<RestorantDTO> restorantDTOList = restorantQueryService.findByCriteria(restorantCriteria);
        return restorantDTOList.isEmpty() ? null : restorantDTOList.get(0);
    }
}
