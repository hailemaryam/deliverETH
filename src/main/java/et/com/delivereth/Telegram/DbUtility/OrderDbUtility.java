package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.Telegram.Constants.ChatStepConstants;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.service.*;
import et.com.delivereth.service.dto.OrderCriteria;
import et.com.delivereth.service.dto.OrderDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDbUtility {
    private static final Logger logger = LoggerFactory.getLogger(DbUtility.class);
    private final OrderService orderService;
    private final OrderQueryService orderQueryService;
    private final TelegramUserService telegramUserService;
    private final TelegramUserDbUtility telegramUserDbUtility;
    public OrderDbUtility(OrderService orderService, OrderQueryService orderQueryService, TelegramUserService telegramUserService, TelegramUserDbUtility telegramUserDbUtility) {
        this.orderService = orderService;
        this.orderQueryService = orderQueryService;
        this.telegramUserService = telegramUserService;
        this.telegramUserDbUtility = telegramUserDbUtility;
    }

    public OrderDTO registerOrder(Update update, TelegramUserDTO telegramUser) {
        OrderDTO order = new OrderDTO();
        order.setLatitude(update.getMessage().getLocation().getLatitude());
        order.setLongtude(update.getMessage().getLocation().getLongitude());
        order.setTelegramUserId(telegramUser.getId());
        order.setTelegramUserUserName(telegramUser.getUserName());
        order.setOrderStatus(OrderStatus.STARTED);
        order = orderService.save(order);
        telegramUser.setOrderIdPaused(order.getId());
        telegramUser.setConversationMetaData(ChatStepConstants.WAITING_FOR_RESTORANT_SELECTION);
        telegramUser.setLoadedPage(null);
        telegramUserService.save(telegramUser);
        return order;
    }
    public void orderRemove(Long id) {
        Optional<OrderDTO> order = orderService.findOne(id);
        if (order.isPresent()) {
            OrderDTO orderTobeUpdated = order.get();
            if (orderTobeUpdated.getOrderStatus().equals(OrderStatus.DELIVERED)){
                orderTobeUpdated.setOrderStatus(OrderStatus.DELIVERED_AND_REMOVED);
            } else if (orderTobeUpdated.getOrderStatus().equals(OrderStatus.CANCELED_BY_RESTAURANT)) {
                orderTobeUpdated.setOrderStatus(OrderStatus.CANCELED_BY_RESTAURANT_AND_REMOVED);
            }
            orderTobeUpdated.setDate(Instant.now());
            orderService.save(orderTobeUpdated);
        }
    }
    public void changeOrderStatusById(Long id, OrderStatus orderStatus) {
        Optional<OrderDTO> order = orderService.findOne(id);
        if (order.isPresent()) {
            OrderDTO orderTobeUpdated = order.get();
            orderTobeUpdated.setOrderStatus(orderStatus);
            orderTobeUpdated.setDate(Instant.now());
            orderService.save(orderTobeUpdated);
        }
    }
    public Page<OrderDTO> getMyOrders(TelegramUserDTO telegramUser){
        List<OrderStatus> orderStatusList = new ArrayList<>();
        orderStatusList.add(OrderStatus.ORDERED);
        orderStatusList.add(OrderStatus.ACCEPTED_BY_RESTAURANT);
        orderStatusList.add(OrderStatus.ACCEPTED_BY_DRIVER);
        orderStatusList.add(OrderStatus.DELIVERED);
        orderStatusList.add(OrderStatus.CANCELED_BY_RESTAURANT);
        OrderCriteria.OrderStatusFilter orderStatusFilter = new OrderCriteria.OrderStatusFilter();
        orderStatusFilter.setIn(orderStatusList);
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(telegramUser.getId());
        OrderCriteria orderCriteria = new OrderCriteria();
        orderCriteria.setOrderStatus(orderStatusFilter);
        orderCriteria.setTelegramUserId(longFilter);
        if (telegramUser.getLoadedPage() == null) {
            telegramUser.setLoadedPage(0);
        } else {
            telegramUser.setLoadedPage(telegramUser.getLoadedPage() + 1);
        }
        telegramUserDbUtility.updateTelegramUser(telegramUser);
        return orderQueryService.findByCriteria(orderCriteria, PageRequest.of(telegramUser.getLoadedPage(), 5));
    }

}
