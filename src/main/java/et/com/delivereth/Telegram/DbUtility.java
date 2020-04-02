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
import org.springframework.data.domain.PageRequest;
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
    private final OrderedFoodService orderedFoodService;
    private final OrderedFoodQueryService orderedFoodQueryService;
    private final KeyValuPairHolderService keyValuPairHolderService;
    private final RestorantQueryService restorantQueryService;
    private final FoodQueryService foodQueryService;
    private final FoodService foodService;

    public DbUtility(TelegramUserService telegramUserService, TelegramUserQueryService telegramUserQueryService, OrderService orderService, OrderedFoodService orderedFoodService, OrderedFoodQueryService orderedFoodQueryService, KeyValuPairHolderService keyValuPairHolderService, RestorantQueryService restorantQueryService, FoodQueryService foodQueryService, FoodService foodService) {
        this.telegramUserService = telegramUserService;
        this.telegramUserQueryService = telegramUserQueryService;
        this.orderService = orderService;
        this.orderedFoodService = orderedFoodService;
        this.orderedFoodQueryService = orderedFoodQueryService;
        this.keyValuPairHolderService = keyValuPairHolderService;
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
        Optional<OrderDTO> order = orderService.findOne(telegramUser.getOrderIdPaused());
        if (order.isPresent()) {
            OrderDTO orderTobeUpdated = order.get();
            orderTobeUpdated.setOrderStatus(OrderStatus.CANCELED_BY_USER);
            orderTobeUpdated.setDate(Instant.now());
            orderService.save(orderTobeUpdated);
            telegramUser.setOrderIdPaused(null);
            telegramUserService.save(telegramUser);
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
        telegramUserService.save(telegramUser);
        return order;
    }
    /*Not Finished Request Restorant Based On Location*/
    public List<RestorantDTO> getRestorantList(TelegramUserDTO telegramUser) {
        RestorantCriteria restorantCriteria = new RestorantCriteria();
        OrderDTO order = orderService.findOne(telegramUser.getOrderIdPaused()).get();
        Float latitude = order.getLatitude();
        Float longtitude = order.getLongtude();
        if (telegramUser.getLoadedPage() == null) {
            telegramUser.setLoadedPage(1);
        } else {
            telegramUser.setLoadedPage(telegramUser.getLoadedPage() + 1);
        }
        return restorantQueryService.findByCriteria(restorantCriteria, PageRequest.of(telegramUser.getLoadedPage(), 2)).toList();
    }
    public List<FoodDTO> getFoodList(TelegramUserDTO telegramUser){
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
    public void addFoodToOrder(Update update, TelegramUserDTO telegramUser){
        OrderDTO order = orderService.findOne(telegramUser.getOrderIdPaused()).get();
        FoodDTO food = foodService.findOne(Long.valueOf(update.getCallbackQuery().getData().substring(5))).get();
        OrderedFoodDTO orderedFood = new OrderedFoodDTO();
        orderedFood.setFoodId(food.getId());
        orderedFood.setQuantity(1);
        orderedFood.setOrderId(order.getId());
        orderedFood = orderedFoodService.save(orderedFood);
        telegramUser.setOrderedFoodIdPaused(orderedFood.getId());
        telegramUserService.save(telegramUser);
    }
    public void setQuantity(Update update, TelegramUserDTO telegramUser){
        OrderedFoodDTO orderedFood = orderedFoodService.findOne(telegramUser.getOrderIdPaused()).get();
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
    public KeyValuPairHolderDTO getKeyValuPairHolderRepository(String Key) {
        Optional<KeyValuPairHolderDTO> one = keyValuPairHolderService.findOne(1L);
        return one.isPresent()? one.get(): null;
    }
    public List<OrderedFoodDTO> getOrderedFoods(TelegramUserDTO telegramUser){
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(telegramUser.getOrderIdPaused());
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
}
