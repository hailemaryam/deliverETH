package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestForNewOrder;
import et.com.delivereth.Telegram.telegramUser.ChatStepConstants;
import et.com.delivereth.Telegram.OtherUtility.DistanceCalculator;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.service.*;
import et.com.delivereth.service.dto.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.Optional;

@Service
public class DbUtility {
    private final TelegramUserService telegramUserService;
    private final OrderService orderService;
    private final OrderedFoodService orderedFoodService;
    private final RestorantService restorantService;
    private final OrderedFoodDbUtility orderedFoodDbUtility;
    private final TransportRequestForNewOrder requestForNewOrder;

    public DbUtility(TelegramUserService telegramUserService, OrderService orderService, OrderedFoodService orderedFoodService, RestorantService restorantService, OrderedFoodDbUtility orderedFoodDbUtility, TransportRequestForNewOrder requestForNewOrder) {
        this.telegramUserService = telegramUserService;
        this.orderService = orderService;
        this.orderedFoodService = orderedFoodService;
        this.restorantService = restorantService;
        this.orderedFoodDbUtility = orderedFoodDbUtility;
        this.requestForNewOrder = requestForNewOrder;
    }

    public void cancelOrder(TelegramUserDTO telegramUser) {
        Optional<OrderDTO> order = Optional.empty();
        if (telegramUser.getOrderIdPaused() != null) {
            order = orderService.findOne(telegramUser.getOrderIdPaused());
        }
        if (order.isPresent()) {
            OrderDTO orderTobeUpdated = order.get();
            orderTobeUpdated.setOrderStatus(OrderStatus.CANCELED_BY_USER);
            orderTobeUpdated.setDate(Instant.now());
            orderService.save(orderTobeUpdated);
        }
        telegramUser.setConversationMetaData(ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE);
        telegramUser.setOrderedFoodIdPaused(null);
        telegramUser.setOrderIdPaused(null);
        telegramUser.setSelectedRestorant(null);
        telegramUser.setLoadedPage(null);
        telegramUserService.save(telegramUser);
    }
    public void setQuantity(Update update, TelegramUserDTO telegramUser){
        Optional<OrderedFoodDTO> orderedFoodDTO = orderedFoodService.findOne(telegramUser.getOrderedFoodIdPaused());
        OrderedFoodDTO orderedFood = orderedFoodDTO.orElse(null);
        if (orderedFood != null) {
            if (update.hasCallbackQuery()) {
                orderedFood.setQuantity(Integer.valueOf(update.getCallbackQuery().getData().substring(9)));
            } else if (update.hasMessage()){
                orderedFood.setQuantity(Integer.valueOf(update.getMessage().getText()));
            }
            orderedFoodService.save(orderedFood);
        }
        telegramUser.setOrderedFoodIdPaused(null);
        telegramUserService.save(telegramUser);
    }
    public void finishOrder(TelegramUserDTO telegramUser){
        Optional<OrderDTO> orderDto = orderService.findOne(telegramUser.getOrderIdPaused());
        Optional<RestorantDTO> restaurantOptional = restorantService.findOne(telegramUser.getSelectedRestorant());
        OrderDTO order = orderDto.orElse(null);
        RestorantDTO restorant = restaurantOptional.orElse(null);
        if (order != null && restaurantOptional != null) {
            order.setTotalPrice(orderedFoodDbUtility.getTotalFee(order.getId()));
            order.setTransportationFee(transportaionFee(telegramUser, restorant).floatValue());
            order.setOrderStatus(OrderStatus.ORDERED);
            order.setDate(Instant.now());
            order = orderService.save(order);
        }
        telegramUser.setOrderedFoodIdPaused(null);
        telegramUser.setOrderIdPaused(null);
        telegramUser.setSelectedRestorant(null);
        telegramUser.setLoadedPage(null);
        telegramUser.setConversationMetaData(ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE);
        telegramUserService.save(telegramUser);
        requestForNewOrder.sendNewOrder(order);
    }
    public Double transportaionFee(TelegramUserDTO telegramUser, RestorantDTO restorantDTO){
        OrderDTO orderDTO;
        if (telegramUser.getOrderIdPaused() != null) {
            orderDTO = orderService.findOne(telegramUser.getOrderIdPaused()).get();
            double distance = DistanceCalculator.distance(
                orderDTO.getLatitude().doubleValue(),
                restorantDTO.getLatitude().doubleValue(),
                orderDTO.getLongtude().doubleValue(),
                restorantDTO.getLongtude().doubleValue()
                ,0,0);
            return distance < 5000 ? (75 * 0.94) * 1.09 : ((75 + (distance - 5000) * 0.01) * 0.94)*1.09;
        }
        return null;
    }

    public void updateStep(TelegramUserDTO telegramUser, String step){
        telegramUser.setConversationMetaData(step);
        telegramUser.setLoadedPage(null);
        telegramUserService.save(telegramUser);
    }
}
