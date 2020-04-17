package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.service.CustomRestorantService;
import et.com.delivereth.service.OrderService;
import et.com.delivereth.service.RestorantQueryService;
import et.com.delivereth.service.RestorantService;
import et.com.delivereth.service.dto.OrderDTO;
import et.com.delivereth.service.dto.RestorantCriteria;
import et.com.delivereth.service.dto.RestorantDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.StringFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestorantDbUtitlity {
    private static final Logger logger = LoggerFactory.getLogger(DbUtility.class);
    private final RestorantService restorantService;
    private final RestorantQueryService restorantQueryService;
    private final CustomRestorantService customRestorantService;
    private final OrderService orderService;
    private final TelegramUserDbUtility telegramUserDbUtility;


    public RestorantDbUtitlity(RestorantService restorantService, RestorantQueryService restorantQueryService, CustomRestorantService customRestorantService, OrderService orderService, TelegramUserDbUtility telegramUserDbUtility) {
        this.restorantService = restorantService;
        this.restorantQueryService = restorantQueryService;
        this.customRestorantService = customRestorantService;
        this.orderService = orderService;
        this.telegramUserDbUtility = telegramUserDbUtility;
    }

    public RestorantDTO getRestorant(String restaurantUserName) {
        RestorantCriteria restorantCriteria = new RestorantCriteria();
        StringFilter stringFilter = new StringFilter();
        stringFilter.setEquals(restaurantUserName);
        restorantCriteria.setUserName(stringFilter);
        List<RestorantDTO> restorantDTOList = restorantQueryService.findByCriteria(restorantCriteria);
        return restorantDTOList.isEmpty() ? null : restorantDTOList.get(0);
    }

    public RestorantDTO getRestorant(Long id) {
        Optional<RestorantDTO> restaurant = restorantService.findOne(id);
        return restaurant.isPresent() ? restaurant.get() : null;
    }
    public Page<RestorantDTO> getRestorantList(TelegramUserDTO telegramUser) {
        OrderDTO order = orderService.findOne(telegramUser.getOrderIdPaused()).get();
        Float latitude = order.getLatitude();
        Float longtitude = order.getLongtude();
        if (telegramUser.getLoadedPage() == null) {
            telegramUser.setLoadedPage(0);
        } else {
            telegramUser.setLoadedPage(telegramUser.getLoadedPage() + 1);
        }
        telegramUserDbUtility.updateTelegramUser(telegramUser);
        return customRestorantService.findAllSorByProximity(latitude, longtitude, 40000F, PageRequest.of(telegramUser.getLoadedPage(), 2));
    }

}
