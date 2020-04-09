package et.com.delivereth.Telegram.DbUtility;

import et.com.delivereth.Telegram.Constants.ChatStepConstants;
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

    public DbUtility(TelegramUserService telegramUserService, OrderService orderService, OrderedFoodService orderedFoodService) {
        this.telegramUserService = telegramUserService;
        this.orderService = orderService;
        this.orderedFoodService = orderedFoodService;
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
        OrderDTO order = orderDto.orElse(null);
        if (order != null) {
            order.setOrderStatus(OrderStatus.ORDERED);
            order.setDate(Instant.now());
            orderService.save(order);
        }
        telegramUser.setOrderedFoodIdPaused(null);
        telegramUser.setOrderIdPaused(null);
        telegramUser.setSelectedRestorant(null);
        telegramUser.setLoadedPage(null);
        telegramUser.setConversationMetaData(ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE);
        telegramUserService.save(telegramUser);
    }
    public void updateStep(TelegramUserDTO telegramUser, String step){
        telegramUser.setConversationMetaData(step);
        telegramUser.setLoadedPage(null);
        telegramUserService.save(telegramUser);
    }
}
