package et.com.delivereth.web.rest;

import et.com.delivereth.DeliverEthApp;
import et.com.delivereth.domain.Order;
import et.com.delivereth.domain.OrderedFood;
import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.repository.OrderRepository;
import et.com.delivereth.service.OrderService;
import et.com.delivereth.service.dto.OrderDTO;
import et.com.delivereth.service.mapper.OrderMapper;
import et.com.delivereth.service.dto.OrderCriteria;
import et.com.delivereth.service.OrderQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import et.com.delivereth.domain.enumeration.OrderStatus;
/**
 * Integration tests for the {@link OrderResource} REST controller.
 */
@SpringBootTest(classes = DeliverEthApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class OrderResourceIT {

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;
    private static final Float SMALLER_LATITUDE = 1F - 1F;

    private static final Float DEFAULT_LONGTUDE = 1F;
    private static final Float UPDATED_LONGTUDE = 2F;
    private static final Float SMALLER_LONGTUDE = 1F - 1F;

    private static final String DEFAULT_LOCATION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_PRICE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADDITIONAL_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_NOTE = "BBBBBBBBBB";

    private static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.STARTED;
    private static final OrderStatus UPDATED_ORDER_STATUS = OrderStatus.ORDERED;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .latitude(DEFAULT_LATITUDE)
            .longtude(DEFAULT_LONGTUDE)
            .locationDescription(DEFAULT_LOCATION_DESCRIPTION)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .date(DEFAULT_DATE)
            .additionalNote(DEFAULT_ADDITIONAL_NOTE)
            .orderStatus(DEFAULT_ORDER_STATUS);
        return order;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .latitude(UPDATED_LATITUDE)
            .longtude(UPDATED_LONGTUDE)
            .locationDescription(UPDATED_LOCATION_DESCRIPTION)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .date(UPDATED_DATE)
            .additionalNote(UPDATED_ADDITIONAL_NOTE)
            .orderStatus(UPDATED_ORDER_STATUS);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testOrder.getLongtude()).isEqualTo(DEFAULT_LONGTUDE);
        assertThat(testOrder.getLocationDescription()).isEqualTo(DEFAULT_LOCATION_DESCRIPTION);
        assertThat(testOrder.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testOrder.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOrder.getAdditionalNote()).isEqualTo(DEFAULT_ADDITIONAL_NOTE);
        assertThat(testOrder.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void createOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order with an existing ID
        order.setId(1L);
        OrderDTO orderDTO = orderMapper.toDto(order);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longtude").value(hasItem(DEFAULT_LONGTUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].locationDescription").value(hasItem(DEFAULT_LOCATION_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].additionalNote").value(hasItem(DEFAULT_ADDITIONAL_NOTE.toString())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longtude").value(DEFAULT_LONGTUDE.doubleValue()))
            .andExpect(jsonPath("$.locationDescription").value(DEFAULT_LOCATION_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.additionalNote").value(DEFAULT_ADDITIONAL_NOTE.toString()))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()));
    }


    @Test
    @Transactional
    public void getOrdersByIdFiltering() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        Long id = order.getId();

        defaultOrderShouldBeFound("id.equals=" + id);
        defaultOrderShouldNotBeFound("id.notEquals=" + id);

        defaultOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrdersByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where latitude equals to DEFAULT_LATITUDE
        defaultOrderShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the orderList where latitude equals to UPDATED_LATITUDE
        defaultOrderShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where latitude not equals to DEFAULT_LATITUDE
        defaultOrderShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the orderList where latitude not equals to UPDATED_LATITUDE
        defaultOrderShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultOrderShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the orderList where latitude equals to UPDATED_LATITUDE
        defaultOrderShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where latitude is not null
        defaultOrderShouldBeFound("latitude.specified=true");

        // Get all the orderList where latitude is null
        defaultOrderShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultOrderShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the orderList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultOrderShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultOrderShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the orderList where latitude is less than or equal to SMALLER_LATITUDE
        defaultOrderShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where latitude is less than DEFAULT_LATITUDE
        defaultOrderShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the orderList where latitude is less than UPDATED_LATITUDE
        defaultOrderShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where latitude is greater than DEFAULT_LATITUDE
        defaultOrderShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the orderList where latitude is greater than SMALLER_LATITUDE
        defaultOrderShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }


    @Test
    @Transactional
    public void getAllOrdersByLongtudeIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where longtude equals to DEFAULT_LONGTUDE
        defaultOrderShouldBeFound("longtude.equals=" + DEFAULT_LONGTUDE);

        // Get all the orderList where longtude equals to UPDATED_LONGTUDE
        defaultOrderShouldNotBeFound("longtude.equals=" + UPDATED_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLongtudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where longtude not equals to DEFAULT_LONGTUDE
        defaultOrderShouldNotBeFound("longtude.notEquals=" + DEFAULT_LONGTUDE);

        // Get all the orderList where longtude not equals to UPDATED_LONGTUDE
        defaultOrderShouldBeFound("longtude.notEquals=" + UPDATED_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLongtudeIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where longtude in DEFAULT_LONGTUDE or UPDATED_LONGTUDE
        defaultOrderShouldBeFound("longtude.in=" + DEFAULT_LONGTUDE + "," + UPDATED_LONGTUDE);

        // Get all the orderList where longtude equals to UPDATED_LONGTUDE
        defaultOrderShouldNotBeFound("longtude.in=" + UPDATED_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLongtudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where longtude is not null
        defaultOrderShouldBeFound("longtude.specified=true");

        // Get all the orderList where longtude is null
        defaultOrderShouldNotBeFound("longtude.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByLongtudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where longtude is greater than or equal to DEFAULT_LONGTUDE
        defaultOrderShouldBeFound("longtude.greaterThanOrEqual=" + DEFAULT_LONGTUDE);

        // Get all the orderList where longtude is greater than or equal to UPDATED_LONGTUDE
        defaultOrderShouldNotBeFound("longtude.greaterThanOrEqual=" + UPDATED_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLongtudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where longtude is less than or equal to DEFAULT_LONGTUDE
        defaultOrderShouldBeFound("longtude.lessThanOrEqual=" + DEFAULT_LONGTUDE);

        // Get all the orderList where longtude is less than or equal to SMALLER_LONGTUDE
        defaultOrderShouldNotBeFound("longtude.lessThanOrEqual=" + SMALLER_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLongtudeIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where longtude is less than DEFAULT_LONGTUDE
        defaultOrderShouldNotBeFound("longtude.lessThan=" + DEFAULT_LONGTUDE);

        // Get all the orderList where longtude is less than UPDATED_LONGTUDE
        defaultOrderShouldBeFound("longtude.lessThan=" + UPDATED_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllOrdersByLongtudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where longtude is greater than DEFAULT_LONGTUDE
        defaultOrderShouldNotBeFound("longtude.greaterThan=" + DEFAULT_LONGTUDE);

        // Get all the orderList where longtude is greater than SMALLER_LONGTUDE
        defaultOrderShouldBeFound("longtude.greaterThan=" + SMALLER_LONGTUDE);
    }


    @Test
    @Transactional
    public void getAllOrdersByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultOrderShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultOrderShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByTotalPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice not equals to DEFAULT_TOTAL_PRICE
        defaultOrderShouldNotBeFound("totalPrice.notEquals=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderList where totalPrice not equals to UPDATED_TOTAL_PRICE
        defaultOrderShouldBeFound("totalPrice.notEquals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultOrderShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the orderList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultOrderShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice is not null
        defaultOrderShouldBeFound("totalPrice.specified=true");

        // Get all the orderList where totalPrice is null
        defaultOrderShouldNotBeFound("totalPrice.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrdersByTotalPriceContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice contains DEFAULT_TOTAL_PRICE
        defaultOrderShouldBeFound("totalPrice.contains=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderList where totalPrice contains UPDATED_TOTAL_PRICE
        defaultOrderShouldNotBeFound("totalPrice.contains=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByTotalPriceNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice does not contain DEFAULT_TOTAL_PRICE
        defaultOrderShouldNotBeFound("totalPrice.doesNotContain=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderList where totalPrice does not contain UPDATED_TOTAL_PRICE
        defaultOrderShouldBeFound("totalPrice.doesNotContain=" + UPDATED_TOTAL_PRICE);
    }


    @Test
    @Transactional
    public void getAllOrdersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where date equals to DEFAULT_DATE
        defaultOrderShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the orderList where date equals to UPDATED_DATE
        defaultOrderShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where date not equals to DEFAULT_DATE
        defaultOrderShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the orderList where date not equals to UPDATED_DATE
        defaultOrderShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where date in DEFAULT_DATE or UPDATED_DATE
        defaultOrderShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the orderList where date equals to UPDATED_DATE
        defaultOrderShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where date is not null
        defaultOrderShouldBeFound("date.specified=true");

        // Get all the orderList where date is null
        defaultOrderShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderStatus equals to DEFAULT_ORDER_STATUS
        defaultOrderShouldBeFound("orderStatus.equals=" + DEFAULT_ORDER_STATUS);

        // Get all the orderList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultOrderShouldNotBeFound("orderStatus.equals=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderStatus not equals to DEFAULT_ORDER_STATUS
        defaultOrderShouldNotBeFound("orderStatus.notEquals=" + DEFAULT_ORDER_STATUS);

        // Get all the orderList where orderStatus not equals to UPDATED_ORDER_STATUS
        defaultOrderShouldBeFound("orderStatus.notEquals=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderStatusIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderStatus in DEFAULT_ORDER_STATUS or UPDATED_ORDER_STATUS
        defaultOrderShouldBeFound("orderStatus.in=" + DEFAULT_ORDER_STATUS + "," + UPDATED_ORDER_STATUS);

        // Get all the orderList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultOrderShouldNotBeFound("orderStatus.in=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderStatus is not null
        defaultOrderShouldBeFound("orderStatus.specified=true");

        // Get all the orderList where orderStatus is null
        defaultOrderShouldNotBeFound("orderStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderedFoodIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);
        OrderedFood orderedFood = OrderedFoodResourceIT.createEntity(em);
        em.persist(orderedFood);
        em.flush();
        order.addOrderedFood(orderedFood);
        orderRepository.saveAndFlush(order);
        Long orderedFoodId = orderedFood.getId();

        // Get all the orderList where orderedFood equals to orderedFoodId
        defaultOrderShouldBeFound("orderedFoodId.equals=" + orderedFoodId);

        // Get all the orderList where orderedFood equals to orderedFoodId + 1
        defaultOrderShouldNotBeFound("orderedFoodId.equals=" + (orderedFoodId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersByTelegramUserIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);
        TelegramUser telegramUser = TelegramUserResourceIT.createEntity(em);
        em.persist(telegramUser);
        em.flush();
        order.setTelegramUser(telegramUser);
        orderRepository.saveAndFlush(order);
        Long telegramUserId = telegramUser.getId();

        // Get all the orderList where telegramUser equals to telegramUserId
        defaultOrderShouldBeFound("telegramUserId.equals=" + telegramUserId);

        // Get all the orderList where telegramUser equals to telegramUserId + 1
        defaultOrderShouldNotBeFound("telegramUserId.equals=" + (telegramUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderShouldBeFound(String filter) throws Exception {
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longtude").value(hasItem(DEFAULT_LONGTUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].locationDescription").value(hasItem(DEFAULT_LOCATION_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].additionalNote").value(hasItem(DEFAULT_ADDITIONAL_NOTE.toString())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())));

        // Check, that the count call also returns 1
        restOrderMockMvc.perform(get("/api/orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderShouldNotBeFound(String filter) throws Exception {
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderMockMvc.perform(get("/api/orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .latitude(UPDATED_LATITUDE)
            .longtude(UPDATED_LONGTUDE)
            .locationDescription(UPDATED_LOCATION_DESCRIPTION)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .date(UPDATED_DATE)
            .additionalNote(UPDATED_ADDITIONAL_NOTE)
            .orderStatus(UPDATED_ORDER_STATUS);
        OrderDTO orderDTO = orderMapper.toDto(updatedOrder);

        restOrderMockMvc.perform(put("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testOrder.getLongtude()).isEqualTo(UPDATED_LONGTUDE);
        assertThat(testOrder.getLocationDescription()).isEqualTo(UPDATED_LOCATION_DESCRIPTION);
        assertThat(testOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOrder.getAdditionalNote()).isEqualTo(UPDATED_ADDITIONAL_NOTE);
        assertThat(testOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc.perform(put("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc.perform(delete("/api/orders/{id}", order.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
