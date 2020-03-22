package et.com.delivereth.web.rest;

import et.com.delivereth.DeliverEthApp;
import et.com.delivereth.domain.OrderedFood;
import et.com.delivereth.domain.Food;
import et.com.delivereth.domain.Order;
import et.com.delivereth.repository.OrderedFoodRepository;
import et.com.delivereth.service.OrderedFoodService;
import et.com.delivereth.service.dto.OrderedFoodDTO;
import et.com.delivereth.service.mapper.OrderedFoodMapper;
import et.com.delivereth.service.dto.OrderedFoodCriteria;
import et.com.delivereth.service.OrderedFoodQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OrderedFoodResource} REST controller.
 */
@SpringBootTest(classes = DeliverEthApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class OrderedFoodResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    @Autowired
    private OrderedFoodRepository orderedFoodRepository;

    @Autowired
    private OrderedFoodMapper orderedFoodMapper;

    @Autowired
    private OrderedFoodService orderedFoodService;

    @Autowired
    private OrderedFoodQueryService orderedFoodQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderedFoodMockMvc;

    private OrderedFood orderedFood;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderedFood createEntity(EntityManager em) {
        OrderedFood orderedFood = new OrderedFood()
            .quantity(DEFAULT_QUANTITY);
        return orderedFood;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderedFood createUpdatedEntity(EntityManager em) {
        OrderedFood orderedFood = new OrderedFood()
            .quantity(UPDATED_QUANTITY);
        return orderedFood;
    }

    @BeforeEach
    public void initTest() {
        orderedFood = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderedFood() throws Exception {
        int databaseSizeBeforeCreate = orderedFoodRepository.findAll().size();

        // Create the OrderedFood
        OrderedFoodDTO orderedFoodDTO = orderedFoodMapper.toDto(orderedFood);
        restOrderedFoodMockMvc.perform(post("/api/ordered-foods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderedFoodDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderedFood in the database
        List<OrderedFood> orderedFoodList = orderedFoodRepository.findAll();
        assertThat(orderedFoodList).hasSize(databaseSizeBeforeCreate + 1);
        OrderedFood testOrderedFood = orderedFoodList.get(orderedFoodList.size() - 1);
        assertThat(testOrderedFood.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createOrderedFoodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderedFoodRepository.findAll().size();

        // Create the OrderedFood with an existing ID
        orderedFood.setId(1L);
        OrderedFoodDTO orderedFoodDTO = orderedFoodMapper.toDto(orderedFood);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderedFoodMockMvc.perform(post("/api/ordered-foods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderedFoodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderedFood in the database
        List<OrderedFood> orderedFoodList = orderedFoodRepository.findAll();
        assertThat(orderedFoodList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOrderedFoods() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        // Get all the orderedFoodList
        restOrderedFoodMockMvc.perform(get("/api/ordered-foods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderedFood.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @Test
    @Transactional
    public void getOrderedFood() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        // Get the orderedFood
        restOrderedFoodMockMvc.perform(get("/api/ordered-foods/{id}", orderedFood.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderedFood.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }


    @Test
    @Transactional
    public void getOrderedFoodsByIdFiltering() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        Long id = orderedFood.getId();

        defaultOrderedFoodShouldBeFound("id.equals=" + id);
        defaultOrderedFoodShouldNotBeFound("id.notEquals=" + id);

        defaultOrderedFoodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderedFoodShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderedFoodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderedFoodShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrderedFoodsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        // Get all the orderedFoodList where quantity equals to DEFAULT_QUANTITY
        defaultOrderedFoodShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the orderedFoodList where quantity equals to UPDATED_QUANTITY
        defaultOrderedFoodShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrderedFoodsByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        // Get all the orderedFoodList where quantity not equals to DEFAULT_QUANTITY
        defaultOrderedFoodShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the orderedFoodList where quantity not equals to UPDATED_QUANTITY
        defaultOrderedFoodShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrderedFoodsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        // Get all the orderedFoodList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultOrderedFoodShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the orderedFoodList where quantity equals to UPDATED_QUANTITY
        defaultOrderedFoodShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrderedFoodsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        // Get all the orderedFoodList where quantity is not null
        defaultOrderedFoodShouldBeFound("quantity.specified=true");

        // Get all the orderedFoodList where quantity is null
        defaultOrderedFoodShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrderedFoodsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        // Get all the orderedFoodList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultOrderedFoodShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the orderedFoodList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultOrderedFoodShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrderedFoodsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        // Get all the orderedFoodList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultOrderedFoodShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the orderedFoodList where quantity is less than or equal to SMALLER_QUANTITY
        defaultOrderedFoodShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrderedFoodsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        // Get all the orderedFoodList where quantity is less than DEFAULT_QUANTITY
        defaultOrderedFoodShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the orderedFoodList where quantity is less than UPDATED_QUANTITY
        defaultOrderedFoodShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrderedFoodsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        // Get all the orderedFoodList where quantity is greater than DEFAULT_QUANTITY
        defaultOrderedFoodShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the orderedFoodList where quantity is greater than SMALLER_QUANTITY
        defaultOrderedFoodShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllOrderedFoodsByFoodIsEqualToSomething() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);
        Food food = FoodResourceIT.createEntity(em);
        em.persist(food);
        em.flush();
        orderedFood.setFood(food);
        orderedFoodRepository.saveAndFlush(orderedFood);
        Long foodId = food.getId();

        // Get all the orderedFoodList where food equals to foodId
        defaultOrderedFoodShouldBeFound("foodId.equals=" + foodId);

        // Get all the orderedFoodList where food equals to foodId + 1
        defaultOrderedFoodShouldNotBeFound("foodId.equals=" + (foodId + 1));
    }


    @Test
    @Transactional
    public void getAllOrderedFoodsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);
        Order order = OrderResourceIT.createEntity(em);
        em.persist(order);
        em.flush();
        orderedFood.setOrder(order);
        orderedFoodRepository.saveAndFlush(orderedFood);
        Long orderId = order.getId();

        // Get all the orderedFoodList where order equals to orderId
        defaultOrderedFoodShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderedFoodList where order equals to orderId + 1
        defaultOrderedFoodShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderedFoodShouldBeFound(String filter) throws Exception {
        restOrderedFoodMockMvc.perform(get("/api/ordered-foods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderedFood.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));

        // Check, that the count call also returns 1
        restOrderedFoodMockMvc.perform(get("/api/ordered-foods/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderedFoodShouldNotBeFound(String filter) throws Exception {
        restOrderedFoodMockMvc.perform(get("/api/ordered-foods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderedFoodMockMvc.perform(get("/api/ordered-foods/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOrderedFood() throws Exception {
        // Get the orderedFood
        restOrderedFoodMockMvc.perform(get("/api/ordered-foods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderedFood() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        int databaseSizeBeforeUpdate = orderedFoodRepository.findAll().size();

        // Update the orderedFood
        OrderedFood updatedOrderedFood = orderedFoodRepository.findById(orderedFood.getId()).get();
        // Disconnect from session so that the updates on updatedOrderedFood are not directly saved in db
        em.detach(updatedOrderedFood);
        updatedOrderedFood
            .quantity(UPDATED_QUANTITY);
        OrderedFoodDTO orderedFoodDTO = orderedFoodMapper.toDto(updatedOrderedFood);

        restOrderedFoodMockMvc.perform(put("/api/ordered-foods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderedFoodDTO)))
            .andExpect(status().isOk());

        // Validate the OrderedFood in the database
        List<OrderedFood> orderedFoodList = orderedFoodRepository.findAll();
        assertThat(orderedFoodList).hasSize(databaseSizeBeforeUpdate);
        OrderedFood testOrderedFood = orderedFoodList.get(orderedFoodList.size() - 1);
        assertThat(testOrderedFood.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderedFood() throws Exception {
        int databaseSizeBeforeUpdate = orderedFoodRepository.findAll().size();

        // Create the OrderedFood
        OrderedFoodDTO orderedFoodDTO = orderedFoodMapper.toDto(orderedFood);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderedFoodMockMvc.perform(put("/api/ordered-foods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderedFoodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderedFood in the database
        List<OrderedFood> orderedFoodList = orderedFoodRepository.findAll();
        assertThat(orderedFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrderedFood() throws Exception {
        // Initialize the database
        orderedFoodRepository.saveAndFlush(orderedFood);

        int databaseSizeBeforeDelete = orderedFoodRepository.findAll().size();

        // Delete the orderedFood
        restOrderedFoodMockMvc.perform(delete("/api/ordered-foods/{id}", orderedFood.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderedFood> orderedFoodList = orderedFoodRepository.findAll();
        assertThat(orderedFoodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
