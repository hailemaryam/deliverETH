package et.com.delivereth.Telegram;

import et.com.delivereth.domain.*;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.repository.*;
import et.com.delivereth.service.*;
import et.com.delivereth.service.dto.*;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
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
    public void cancelOrder(TelegramUser telegramUser) {
        Optional<Order> order = customOrderRepository.findById(telegramUser.getOrderIdPaused());
        if (order.isPresent()) {
            Order orderTobeUpdated = order.get();
            orderTobeUpdated.setOrderStatus(OrderStatus.CANCELED_BY_USER);
            customOrderRepository.save(orderTobeUpdated);
            telegramUser.setOrderIdPaused(null);
            customTelegramUserRepository.save(telegramUser);
        }
    }
    public Order registerOrder(Update update, TelegramUser telegramUser) {
        Order order = new Order();
        order.setLatitude(update.getMessage().getLocation().getLatitude());
        order.setLongtude(update.getMessage().getLocation().getLongitude());
        order.setTelegramUser(telegramUser);
        order.setOrderStatus(OrderStatus.STARTED);
        order = customOrderRepository.save(order);
        telegramUser.setOrderIdPaused(order.getId());
        customTelegramUserRepository.save(telegramUser);
        return order;
    }
    /*Not Finished Request Restorant Based On Location*/
    public List<RestorantDTO> getRestorantList(TelegramUser telegramUser) {
        RestorantCriteria restorantCriteria = new RestorantCriteria();
        Order order = customOrderRepository.getOne(telegramUser.getOrderIdPaused());
        Float latitude = order.getLatitude();
        Float longtitude = order.getLongtude();
        if (telegramUser.getLoadedPage() == null) {
            telegramUser.setLoadedPage(1);
        } else {
            telegramUser.setLoadedPage(telegramUser.getLoadedPage() + 1);
        }
        return restorantQueryService.findByCriteria(restorantCriteria, PageRequest.of(telegramUser.getLoadedPage(), 2)).toList();
    }
    public List<FoodDTO> getFoodList(TelegramUser telegramUser){
        FoodCriteria foodCriteria = new FoodCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(telegramUser.getSelectedRestorant());
        foodCriteria.setRestorantId(longFilter);
        if (telegramUser.getLoadedPage() == null) {
            telegramUser.setLoadedPage(1);
        } else {
            telegramUser.setLoadedPage(telegramUser.getLoadedPage() + 1);
        }
        return foodQueryService.findByCriteria(foodCriteria, PageRequest.of(telegramUser.getLoadedPage(), 2)).toList();
    }
    public void addFoodToOrder(Update update, TelegramUser telegramUser){
        Order order = customOrderRepository.findById(telegramUser.getOrderIdPaused()).get();
        Food food = foodRepository.getOne(Long.valueOf(update.getCallbackQuery().getData().substring(5)));
        OrderedFood orderedFood = new OrderedFood();
        orderedFood.setFood(food);
        orderedFood.setQuantity(1);
        orderedFood.setOrder(order);
        orderedFood =orderedFoodRepository.save(orderedFood);
        telegramUser.setOrderedFoodIdPaused(orderedFood.getId());
        customTelegramUserRepository.save(telegramUser);
    }
    public void setQuantity(Update update, TelegramUser telegramUser){
        OrderedFood orderedFood = orderedFoodRepository.getOne(telegramUser.getOrderIdPaused());
        if (update.hasCallbackQuery()) {
            orderedFood.setQuantity(Integer.valueOf(update.getCallbackQuery().getData().substring(9)));
        } else if (update.hasMessage()){
            orderedFood.setQuantity(Integer.valueOf(update.getMessage().getText()));
        }
        orderedFoodRepository.save(orderedFood);
        telegramUser.setOrderedFoodIdPaused(null);
        customTelegramUserRepository.save(telegramUser);
    }
    public void finishOrder(TelegramUser telegramUser){
        Order order = customOrderRepository.getOne(telegramUser.getOrderIdPaused());
        order.setOrderStatus(OrderStatus.ORDERED);
        customOrderRepository.save(order);
        telegramUser.setOrderedFoodIdPaused(null);
        telegramUser.setOrderIdPaused(null);
        telegramUser.setSelectedRestorant(null);
        telegramUser.setLoadedPage(null);
        customTelegramUserRepository.save(telegramUser);
    }
    public void updateStep(TelegramUser telegramUser, String step){
        telegramUser.setConversationMetaData(step);
        telegramUser.setLoadedPage(null);
        customTelegramUserRepository.save(telegramUser);
    }
    public KeyValuPairHolderDTO getKeyValuPairHolderRepository(String Key) {
        Optional<KeyValuPairHolderDTO> one = keyValuPairHolderService.findOne(1L);
        return one.isPresent()? one.get(): null;
    }
}
