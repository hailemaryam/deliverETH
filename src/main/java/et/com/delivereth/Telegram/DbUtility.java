package et.com.delivereth.Telegram;

import et.com.delivereth.domain.*;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.repository.*;
import et.com.delivereth.service.*;
import et.com.delivereth.service.dto.*;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbUtility {
    private static final Logger logger = LoggerFactory.getLogger(DbUtility.class);
    private final TelegramUserService telegramUserService;
    private final TelegramUserQueryService telegramUserQueryService;
    private final OrderService orderService;
    private final OrderQueryService orderQueryService;
    private final OrderedFoodService orderedFoodService;
    private final OrderedFoodQueryService orderedFoodQueryService;
    private final KeyValuPairHolderQueryService keyValuPairHolderQueryService;
    private final RestorantService restorantService;
    private final RestorantQueryService restorantQueryService;
    private final FoodQueryService foodQueryService;
    private final FoodService foodService;

    public DbUtility(TelegramUserService telegramUserService, TelegramUserQueryService telegramUserQueryService, OrderService orderService, OrderQueryService orderQueryService, OrderedFoodService orderedFoodService, OrderedFoodQueryService orderedFoodQueryService, KeyValuPairHolderQueryService keyValuPairHolderQueryService, RestorantService restorantService, RestorantQueryService restorantQueryService, FoodQueryService foodQueryService, FoodService foodService) {
        this.telegramUserService = telegramUserService;
        this.telegramUserQueryService = telegramUserQueryService;
        this.orderService = orderService;
        this.orderQueryService = orderQueryService;
        this.orderedFoodService = orderedFoodService;
        this.orderedFoodQueryService = orderedFoodQueryService;
        this.keyValuPairHolderQueryService = keyValuPairHolderQueryService;
        this.restorantService = restorantService;
        this.restorantQueryService = restorantQueryService;
        this.foodQueryService = foodQueryService;
        this.foodService = foodService;
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
    public void registerUserPhone(Update update, TelegramUserDTO telegramUser) {
        telegramUser.setPhone(update.getMessage().getContact().getPhoneNumber());
        telegramUser.setConversationMetaData(ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE);
        telegramUserService.save(telegramUser);
    }
    public TelegramUserDTO getTelegramUser(Update update) {
        List<TelegramUserDTO> telegramUserDTOList = new ArrayList<>();
        TelegramUserCriteria telegramUserCriteria = new TelegramUserCriteria();
        StringFilter stringFilter = new StringFilter();
        if (update.hasMessage()) {
            stringFilter.setEquals(update.getMessage().getFrom().getUserName());
            telegramUserCriteria.setUserName(stringFilter);
            telegramUserDTOList = telegramUserQueryService.findByCriteria(telegramUserCriteria);
        } else if (update.hasCallbackQuery()){
            stringFilter.setEquals(update.getCallbackQuery().getFrom().getUserName());
            telegramUserCriteria.setUserName(stringFilter);
            telegramUserDTOList = telegramUserQueryService.findByCriteria(telegramUserCriteria);
        }
        return telegramUserDTOList.size() > 0 ? telegramUserDTOList.get(0): null;
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
    public void changeOrderStatusById(Long id, OrderStatus orderStatus) {
        Optional<OrderDTO> order = orderService.findOne(id);
        if (order.isPresent()) {
            OrderDTO orderTobeUpdated = order.get();
            orderTobeUpdated.setOrderStatus(orderStatus);
            orderTobeUpdated.setDate(Instant.now());
            orderService.save(orderTobeUpdated);
        }
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
    /*Not Finished Request Restorant Based On Location*/
    public Page<RestorantDTO> getRestorantList(TelegramUserDTO telegramUser) {
        RestorantCriteria restorantCriteria = new RestorantCriteria();
        OrderDTO order = orderService.findOne(telegramUser.getOrderIdPaused()).get();
        Float latitude = order.getLatitude();
        Float longtitude = order.getLongtude();
        if (telegramUser.getLoadedPage() == null) {
            telegramUser.setLoadedPage(0);
        } else {
            telegramUser.setLoadedPage(telegramUser.getLoadedPage() + 1);
        }
        updateTelegramUser(telegramUser);
        return restorantQueryService.findByCriteria(restorantCriteria, PageRequest.of(telegramUser.getLoadedPage(), 2));
    }
    public Page<FoodDTO> getFoodList(TelegramUserDTO telegramUser){
        FoodCriteria foodCriteria = new FoodCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(telegramUser.getSelectedRestorant());
        foodCriteria.setRestorantId(longFilter);
        updateTelegramUser(telegramUser);
        return foodQueryService.findByCriteria(foodCriteria, PageRequest.of(telegramUser.getLoadedPage(), 10));
    }
    public void addFoodToOrder(TelegramUserDTO telegramUser, Long foodId){
        OrderDTO order = orderService.findOne(telegramUser.getOrderIdPaused()).get();
        FoodDTO food = foodService.findOne(foodId).get();
        OrderedFoodDTO orderedFood = new OrderedFoodDTO();
        orderedFood.setFoodId(food.getId());
        orderedFood.setQuantity(1);
        orderedFood.setOrderId(order.getId());
        orderedFood = orderedFoodService.save(orderedFood);
        telegramUser.setOrderedFoodIdPaused(orderedFood.getId());
        telegramUserService.save(telegramUser);
    }
    public void setQuantity(Update update, TelegramUserDTO telegramUser){
        OrderedFoodDTO orderedFood = orderedFoodService.findOne(telegramUser.getOrderedFoodIdPaused()).get();
        if (update.hasCallbackQuery()) {
            orderedFood.setQuantity(Integer.valueOf(update.getCallbackQuery().getData().substring(9)));
        } else if (update.hasMessage()){
            orderedFood.setQuantity(Integer.valueOf(update.getMessage().getText()));
        }
        orderedFoodService.save(orderedFood);
        telegramUser.setOrderedFoodIdPaused(null);
        telegramUserService.save(telegramUser);
    }
    public void finishOrder(TelegramUserDTO telegramUser){
        OrderDTO order = orderService.findOne(telegramUser.getOrderIdPaused()).get();
        order.setOrderStatus(OrderStatus.ORDERED);
        order.setDate(Instant.now());
        orderService.save(order);
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
    public void updateTelegramUser(TelegramUserDTO telegramUser){
        telegramUserService.save(telegramUser);
    }
    public KeyValuPairHolderDTO getKeyValuPairHolderRepository(String key) {
        KeyValuPairHolderCriteria keyValuPairHolderCriteria = new KeyValuPairHolderCriteria();
        StringFilter stringFilter = new StringFilter();
        stringFilter.setEquals(key);
        keyValuPairHolderCriteria.setKey(stringFilter);
        List<KeyValuPairHolderDTO> keyValuPairHolderDTOList = keyValuPairHolderQueryService.findByCriteria(keyValuPairHolderCriteria);
        return keyValuPairHolderDTOList.size() > 0? keyValuPairHolderDTOList.get(0): null;
    }
    public List<OrderedFoodDTO> getOrderedFoods(Long orderId){
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(orderId);
        OrderedFoodCriteria orderedFoodCriteria = new OrderedFoodCriteria();
        orderedFoodCriteria.setOrderId(longFilter);
        return orderedFoodQueryService.findByCriteria(orderedFoodCriteria);
    }
    public OrderedFoodDTO getSelectedFood(TelegramUserDTO telegramUser){
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(telegramUser.getOrderedFoodIdPaused());
        OrderedFoodCriteria orderedFoodCriteria = new OrderedFoodCriteria();
        orderedFoodCriteria.setId(longFilter);
        List<OrderedFoodDTO> orderFoodList = orderedFoodQueryService.findByCriteria(orderedFoodCriteria);
        return orderFoodList.size() > 0? orderFoodList.get(0): null;
    }
    public FoodDTO getFood(Long id){
        return foodService.findOne(id).get();
    }
    public RestorantDTO getRestorant(Long id){
        Optional<RestorantDTO> restaurant = restorantService.findOne(id);
        return restaurant.isPresent()? restaurant.get(): null;
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
        updateTelegramUser(telegramUser);
        return orderQueryService.findByCriteria(orderCriteria, PageRequest.of(telegramUser.getLoadedPage(), 5));
    }
}
