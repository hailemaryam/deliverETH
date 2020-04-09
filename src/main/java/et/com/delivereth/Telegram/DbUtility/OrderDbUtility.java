package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.service.*;
import et.com.delivereth.service.dto.OrderCriteria;
import et.com.delivereth.service.dto.OrderDTO;
import io.github.jhipster.service.filter.StringFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderDbUtility {
    private static final Logger logger = LoggerFactory.getLogger(DbUtility.class);
    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    public OrderDbUtility(OrderService orderService, OrderQueryService orderQueryService) {
        this.orderService = orderService;
        this.orderQueryService = orderQueryService;
    }
}
