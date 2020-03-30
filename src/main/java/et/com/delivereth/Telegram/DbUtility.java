package et.com.delivereth.Telegram;

import et.com.delivereth.Telegram.Requests.RequestForOrder;
import et.com.delivereth.domain.*;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.repository.*;
import et.com.delivereth.service.*;
import et.com.delivereth.service.dto.*;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Service
public class DbUtility {
    private static final Logger logger = LoggerFactory.getLogger(DbUtility.class);
    private final TelegramUserService telegramUserService;
    private final CustomTelegramUserRepository customTelegramUserRepository;
    private final CustomOrderRepository customOrderRepository;
    private final OrderedFoodRepository orderedFoodRepository;
    private final KeyValuPairHolderService keyValuPairHolderService;
    private final RestorantQueryService restorantQueryService;
    private final FoodQueryService foodQueryService;
    private final FoodRepository foodRepository;

    public DbUtility(TelegramUserService telegramUserService, CustomTelegramUserRepository customTelegramUserRepository, CustomOrderRepository customOrderRepository, OrderedFoodRepository orderedFoodRepository, KeyValuPairHolderService keyValuPairHolderService, RestorantQueryService restorantQueryService, FoodQueryService foodQueryService, FoodRepository foodRepository) {
        this.telegramUserService = telegramUserService;
        this.customTelegramUserRepository = customTelegramUserRepository;
        this.customOrderRepository = customOrderRepository;
        this.orderedFoodRepository = orderedFoodRepository;
        this.keyValuPairHolderService = keyValuPairHolderService;
        this.restorantQueryService = restorantQueryService;
        this.foodQueryService = foodQueryService;
        this.foodRepository = foodRepository;
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
    public void registerUserPhone(Update update, TelegramUser telegramUser) {
        telegramUser.setPhone(update.getMessage().getContact().getPhoneNumber());
        telegramUser.setConversationMetaData(ChatStepConstants.WAITING_FOR_ORDER_RESPONSE);
        customTelegramUserRepository.save(telegramUser);
    }
    public TelegramUser getTelegramUser(Update update) {
        Optional<TelegramUser> telegramUserByUserNameEquals = null;
        if (update.hasMessage()) {
            telegramUserByUserNameEquals =
                customTelegramUserRepository
                    .findTelegramUserByUserNameEquals(update.getMessage().getFrom().getUserName());
        } else if (update.hasCallbackQuery()){
            telegramUserByUserNameEquals =
                customTelegramUserRepository
                    .findTelegramUserByUserNameEquals(update.getCallbackQuery().getFrom().getUserName());
        }
        return telegramUserByUserNameEquals.isPresent()? telegramUserByUserNameEquals.get(): null;
    }
    public void updateStep(TelegramUser telegramUser, String step){
        telegramUser.setConversationMetaData(step);
        customTelegramUserRepository.save(telegramUser);
    }

    public Order updateOrder(Order order) {
        return customOrderRepository.save(order);
    }
    public Order findOrder(TelegramUser telegramUser, OrderStatus orderStatus){
        Optional<Order> byOrderStatusAndTelegramUser = customOrderRepository.findByOrderStatusAndTelegramUser(orderStatus, telegramUser);
        return  byOrderStatusAndTelegramUser.isPresent()? byOrderStatusAndTelegramUser.get(): null;
    }
    public KeyValuPairHolderDTO getKeyValuPairHolderRepository(String Key) {
        Optional<KeyValuPairHolderDTO> one = keyValuPairHolderService.findOne(1L);
        return one.isPresent()? one.get(): null;
    }

    public List<RestorantDTO> getRestorantList(String latitude, String longtude, Integer page, Integer pageSize) {
        RestorantCriteria restorantCriteria = new RestorantCriteria();
        return restorantQueryService.findByCriteria(restorantCriteria, PageRequest.of(page, pageSize)).toList();
    }
    public List<FoodDTO> getFoodList(Long restorantId, Integer page, Integer size){
        FoodCriteria foodCriteria = new FoodCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(restorantId);
        return foodQueryService.findByCriteria(foodCriteria, PageRequest.of(page, size)).toList();
    }
    public Food getFoodById(Long id){
        return foodRepository.getOne(id);
    }
    public OrderedFood addFoodToOrder(Order order, Food food){
        OrderedFood orderedFood = new OrderedFood();
        orderedFood.setFood(food);
        orderedFood.setOrder(order);
        orderedFood.setQuantity(1);
        return orderedFoodRepository.save(orderedFood);
    }
    public Order registerOrder(TelegramUser telegramUser, Float latitude, Float longtude) {
        Order order = new Order();
        order.setLatitude(latitude);
        order.setLongtude(longtude);
        order.setTelegramUser(telegramUser);
        order.setOrderStatus(OrderStatus.STARTED);
        return customOrderRepository.save(order);
    }
    public OrderedFood addToCart(Update update, TelegramUser telegramUser){
        Order order= findOrder(telegramUser, OrderStatus.STARTED);
        Food food = getFoodById(Long.valueOf(update.getCallbackQuery().getData().substring(5)));
        return addFoodToOrder(order, food);
    }
}
