package et.com.delivereth.Telegram;

import et.com.delivereth.domain.*;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.repository.*;
import et.com.delivereth.service.TelegramUserService;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class DbUtility {
    private final TelegramUserService telegramUserService;
    private final CustomTelegramUserRepository customTelegramUserRepository;
    private final CustomOrderRepository customOrderRepository;
    private final OrderedFoodRepository orderedFoodRepository;
    private final KeyValuPairHolderRepository keyValuPairHolderRepository;

    public DbUtility(TelegramUserService telegramUserService, CustomTelegramUserRepository customTelegramUserRepository, CustomOrderRepository customOrderRepository, OrderedFoodRepository orderedFoodRepository, KeyValuPairHolderRepository keyValuPairHolderRepository) {
        this.telegramUserService = telegramUserService;
        this.customTelegramUserRepository = customTelegramUserRepository;
        this.customOrderRepository = customOrderRepository;
        this.orderedFoodRepository = orderedFoodRepository;
        this.keyValuPairHolderRepository = keyValuPairHolderRepository;
    }

    public void registerTelegramUser(Update update){
        TelegramUserDTO telegramUserDTO = new TelegramUserDTO();
        if (update.hasMessage()){
            telegramUserDTO.setChatId(update.getMessage().getChatId().toString());
            telegramUserDTO.setFirstName(update.getMessage().getFrom().getFirstName());
            telegramUserDTO.setLastName(update.getMessage().getFrom().getLastName());
            telegramUserDTO.setUserName(update.getMessage().getFrom().getUserName());
            if (update.getMessage().getContact() != null) {
                telegramUserDTO.setPhone(update.getMessage().getContact().getPhoneNumber());
            }
            telegramUserDTO.setConversationMetaData(ChatStepConstants.WAITING_FOR_CONTACT_RESPONSE);
        }
        telegramUserService.save(telegramUserDTO);
    }
    public void registerUserPhone(Update update) {
        Optional<TelegramUser> telegramUserByUserNameEquals =
            customTelegramUserRepository
                .findTelegramUserByUserNameEquals(update.getMessage().getFrom().getUserName());
        TelegramUser telegramUser = telegramUserByUserNameEquals.get();
        telegramUser.setPhone(update.getMessage().getContact().getPhoneNumber());
        telegramUser.setConversationMetaData(ChatStepConstants.WAITING_FOR_ORDER_RESPONSE);
        customTelegramUserRepository.save(telegramUser);
    }
    public TelegramUser getTelegramUser(Update update) {
        Optional<TelegramUser> telegramUserByUserNameEquals =
            customTelegramUserRepository
                .findTelegramUserByUserNameEquals(update.getMessage().getFrom().getUserName());
        return telegramUserByUserNameEquals.isPresent()? telegramUserByUserNameEquals.get(): null;
    }
    public Order registerOrder(TelegramUser telegramUser) {
        Order order = new Order();
        order.setTelegramUser(telegramUser);
        order.setOrderStatus(OrderStatus.STARTED);
        return customOrderRepository.save(order);
    }
    public OrderedFood addItemToOrder(Order order, Food food, Integer quantity){
        OrderedFood orderedFood = new OrderedFood();
        orderedFood.setOrder(order);
        orderedFood.setFood(food);
        orderedFood.setQuantity(quantity);
        return orderedFoodRepository.save(orderedFood);
    }
    public Order updateOrder(Order order) {
        return customOrderRepository.save(order);
    }
    public Order findOrder(TelegramUser telegramUser, OrderStatus orderStatus){
        Optional<Order> byOrderStatusAndTelegramUser = customOrderRepository.findByOrderStatusAndTelegramUser(orderStatus, telegramUser);
        return  byOrderStatusAndTelegramUser.isPresent()? byOrderStatusAndTelegramUser.get(): null;
    }

    public KeyValuPairHolder getKeyValuPairHolderRepository(String Key) {
        return keyValuPairHolderRepository.getOne(1l);
    }
}
