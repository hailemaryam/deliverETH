package et.com.delivereth.web.rest;

import et.com.delivereth.DeliverEthApp;
import et.com.delivereth.domain.Food;
import et.com.delivereth.domain.OrderedFood;
import et.com.delivereth.domain.Restorant;
import et.com.delivereth.repository.FoodRepository;
import et.com.delivereth.service.FoodService;
import et.com.delivereth.service.dto.FoodDTO;
import et.com.delivereth.service.mapper.FoodMapper;
import et.com.delivereth.service.dto.FoodCriteria;
import et.com.delivereth.service.FoodQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FoodResource} REST controller.
 */
@SpringBootTest(classes = DeliverEthApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class FoodResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ICON_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ICON_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ICON_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ICON_IMAGE_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodQueryService foodQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFoodMockMvc;

    private Food food;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Food createEntity(EntityManager em) {
        Food food = new Food()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .iconImage(DEFAULT_ICON_IMAGE)
            .iconImageContentType(DEFAULT_ICON_IMAGE_CONTENT_TYPE)
            .price(DEFAULT_PRICE);
        return food;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Food createUpdatedEntity(EntityManager em) {
        Food food = new Food()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .iconImage(UPDATED_ICON_IMAGE)
            .iconImageContentType(UPDATED_ICON_IMAGE_CONTENT_TYPE)
            .price(UPDATED_PRICE);
        return food;
    }

    @BeforeEach
    public void initTest() {
        food = createEntity(em);
    }

    @Test
    @Transactional
    public void createFood() throws Exception {
        int databaseSizeBeforeCreate = foodRepository.findAll().size();

        // Create the Food
        FoodDTO foodDTO = foodMapper.toDto(food);
        restFoodMockMvc.perform(post("/api/foods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isCreated());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeCreate + 1);
        Food testFood = foodList.get(foodList.size() - 1);
        assertThat(testFood.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFood.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFood.getIconImage()).isEqualTo(DEFAULT_ICON_IMAGE);
        assertThat(testFood.getIconImageContentType()).isEqualTo(DEFAULT_ICON_IMAGE_CONTENT_TYPE);
        assertThat(testFood.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createFoodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = foodRepository.findAll().size();

        // Create the Food with an existing ID
        food.setId(1L);
        FoodDTO foodDTO = foodMapper.toDto(food);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodMockMvc.perform(post("/api/foods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFoods() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList
        restFoodMockMvc.perform(get("/api/foods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(food.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].iconImageContentType").value(hasItem(DEFAULT_ICON_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].iconImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICON_IMAGE))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get the food
        restFoodMockMvc.perform(get("/api/foods/{id}", food.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(food.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.iconImageContentType").value(DEFAULT_ICON_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.iconImage").value(Base64Utils.encodeToString(DEFAULT_ICON_IMAGE)))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }


    @Test
    @Transactional
    public void getFoodsByIdFiltering() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        Long id = food.getId();

        defaultFoodShouldBeFound("id.equals=" + id);
        defaultFoodShouldNotBeFound("id.notEquals=" + id);

        defaultFoodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFoodShouldNotBeFound("id.greaterThan=" + id);

        defaultFoodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFoodShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFoodsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where name equals to DEFAULT_NAME
        defaultFoodShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the foodList where name equals to UPDATED_NAME
        defaultFoodShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFoodsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where name not equals to DEFAULT_NAME
        defaultFoodShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the foodList where name not equals to UPDATED_NAME
        defaultFoodShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFoodsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFoodShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the foodList where name equals to UPDATED_NAME
        defaultFoodShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFoodsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where name is not null
        defaultFoodShouldBeFound("name.specified=true");

        // Get all the foodList where name is null
        defaultFoodShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllFoodsByNameContainsSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where name contains DEFAULT_NAME
        defaultFoodShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the foodList where name contains UPDATED_NAME
        defaultFoodShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFoodsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where name does not contain DEFAULT_NAME
        defaultFoodShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the foodList where name does not contain UPDATED_NAME
        defaultFoodShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllFoodsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where price equals to DEFAULT_PRICE
        defaultFoodShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the foodList where price equals to UPDATED_PRICE
        defaultFoodShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllFoodsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where price not equals to DEFAULT_PRICE
        defaultFoodShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the foodList where price not equals to UPDATED_PRICE
        defaultFoodShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllFoodsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultFoodShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the foodList where price equals to UPDATED_PRICE
        defaultFoodShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllFoodsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where price is not null
        defaultFoodShouldBeFound("price.specified=true");

        // Get all the foodList where price is null
        defaultFoodShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllFoodsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where price is greater than or equal to DEFAULT_PRICE
        defaultFoodShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the foodList where price is greater than or equal to UPDATED_PRICE
        defaultFoodShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllFoodsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where price is less than or equal to DEFAULT_PRICE
        defaultFoodShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the foodList where price is less than or equal to SMALLER_PRICE
        defaultFoodShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllFoodsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where price is less than DEFAULT_PRICE
        defaultFoodShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the foodList where price is less than UPDATED_PRICE
        defaultFoodShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllFoodsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList where price is greater than DEFAULT_PRICE
        defaultFoodShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the foodList where price is greater than SMALLER_PRICE
        defaultFoodShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllFoodsByOrderedFoodIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);
        OrderedFood orderedFood = OrderedFoodResourceIT.createEntity(em);
        em.persist(orderedFood);
        em.flush();
        food.addOrderedFood(orderedFood);
        foodRepository.saveAndFlush(food);
        Long orderedFoodId = orderedFood.getId();

        // Get all the foodList where orderedFood equals to orderedFoodId
        defaultFoodShouldBeFound("orderedFoodId.equals=" + orderedFoodId);

        // Get all the foodList where orderedFood equals to orderedFoodId + 1
        defaultFoodShouldNotBeFound("orderedFoodId.equals=" + (orderedFoodId + 1));
    }


    @Test
    @Transactional
    public void getAllFoodsByRestorantIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);
        Restorant restorant = RestorantResourceIT.createEntity(em);
        em.persist(restorant);
        em.flush();
        food.setRestorant(restorant);
        foodRepository.saveAndFlush(food);
        Long restorantId = restorant.getId();

        // Get all the foodList where restorant equals to restorantId
        defaultFoodShouldBeFound("restorantId.equals=" + restorantId);

        // Get all the foodList where restorant equals to restorantId + 1
        defaultFoodShouldNotBeFound("restorantId.equals=" + (restorantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFoodShouldBeFound(String filter) throws Exception {
        restFoodMockMvc.perform(get("/api/foods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(food.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].iconImageContentType").value(hasItem(DEFAULT_ICON_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].iconImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICON_IMAGE))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restFoodMockMvc.perform(get("/api/foods/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFoodShouldNotBeFound(String filter) throws Exception {
        restFoodMockMvc.perform(get("/api/foods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFoodMockMvc.perform(get("/api/foods/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFood() throws Exception {
        // Get the food
        restFoodMockMvc.perform(get("/api/foods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        int databaseSizeBeforeUpdate = foodRepository.findAll().size();

        // Update the food
        Food updatedFood = foodRepository.findById(food.getId()).get();
        // Disconnect from session so that the updates on updatedFood are not directly saved in db
        em.detach(updatedFood);
        updatedFood
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .iconImage(UPDATED_ICON_IMAGE)
            .iconImageContentType(UPDATED_ICON_IMAGE_CONTENT_TYPE)
            .price(UPDATED_PRICE);
        FoodDTO foodDTO = foodMapper.toDto(updatedFood);

        restFoodMockMvc.perform(put("/api/foods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isOk());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
        Food testFood = foodList.get(foodList.size() - 1);
        assertThat(testFood.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFood.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFood.getIconImage()).isEqualTo(UPDATED_ICON_IMAGE);
        assertThat(testFood.getIconImageContentType()).isEqualTo(UPDATED_ICON_IMAGE_CONTENT_TYPE);
        assertThat(testFood.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingFood() throws Exception {
        int databaseSizeBeforeUpdate = foodRepository.findAll().size();

        // Create the Food
        FoodDTO foodDTO = foodMapper.toDto(food);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodMockMvc.perform(put("/api/foods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        int databaseSizeBeforeDelete = foodRepository.findAll().size();

        // Delete the food
        restFoodMockMvc.perform(delete("/api/foods/{id}", food.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
