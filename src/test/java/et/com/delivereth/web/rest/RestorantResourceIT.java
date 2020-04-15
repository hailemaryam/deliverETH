package et.com.delivereth.web.rest;

import et.com.delivereth.DeliverEthApp;
import et.com.delivereth.domain.Restorant;
import et.com.delivereth.domain.Food;
import et.com.delivereth.domain.TelegramRestaurantUser;
import et.com.delivereth.domain.TelegramDeliveryUser;
import et.com.delivereth.repository.RestorantRepository;
import et.com.delivereth.service.RestorantService;
import et.com.delivereth.service.dto.RestorantDTO;
import et.com.delivereth.service.mapper.RestorantMapper;
import et.com.delivereth.service.dto.RestorantCriteria;
import et.com.delivereth.service.RestorantQueryService;

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
 * Integration tests for the {@link RestorantResource} REST controller.
 */
@SpringBootTest(classes = DeliverEthApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class RestorantResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ICON_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ICON_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ICON_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ICON_IMAGE_CONTENT_TYPE = "image/png";

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;
    private static final Float SMALLER_LATITUDE = 1F - 1F;

    private static final Float DEFAULT_LONGTUDE = 1F;
    private static final Float UPDATED_LONGTUDE = 2F;
    private static final Float SMALLER_LONGTUDE = 1F - 1F;

    private static final Integer DEFAULT_AVAILABLE_ORDER_CAP = 1;
    private static final Integer UPDATED_AVAILABLE_ORDER_CAP = 2;
    private static final Integer SMALLER_AVAILABLE_ORDER_CAP = 1 - 1;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private RestorantRepository restorantRepository;

    @Autowired
    private RestorantMapper restorantMapper;

    @Autowired
    private RestorantService restorantService;

    @Autowired
    private RestorantQueryService restorantQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestorantMockMvc;

    private Restorant restorant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restorant createEntity(EntityManager em) {
        Restorant restorant = new Restorant()
            .name(DEFAULT_NAME)
            .userName(DEFAULT_USER_NAME)
            .description(DEFAULT_DESCRIPTION)
            .iconImage(DEFAULT_ICON_IMAGE)
            .iconImageContentType(DEFAULT_ICON_IMAGE_CONTENT_TYPE)
            .latitude(DEFAULT_LATITUDE)
            .longtude(DEFAULT_LONGTUDE)
            .availableOrderCap(DEFAULT_AVAILABLE_ORDER_CAP)
            .status(DEFAULT_STATUS);
        return restorant;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restorant createUpdatedEntity(EntityManager em) {
        Restorant restorant = new Restorant()
            .name(UPDATED_NAME)
            .userName(UPDATED_USER_NAME)
            .description(UPDATED_DESCRIPTION)
            .iconImage(UPDATED_ICON_IMAGE)
            .iconImageContentType(UPDATED_ICON_IMAGE_CONTENT_TYPE)
            .latitude(UPDATED_LATITUDE)
            .longtude(UPDATED_LONGTUDE)
            .availableOrderCap(UPDATED_AVAILABLE_ORDER_CAP)
            .status(UPDATED_STATUS);
        return restorant;
    }

    @BeforeEach
    public void initTest() {
        restorant = createEntity(em);
    }

    @Test
    @Transactional
    public void createRestorant() throws Exception {
        int databaseSizeBeforeCreate = restorantRepository.findAll().size();

        // Create the Restorant
        RestorantDTO restorantDTO = restorantMapper.toDto(restorant);
        restRestorantMockMvc.perform(post("/api/restorants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(restorantDTO)))
            .andExpect(status().isCreated());

        // Validate the Restorant in the database
        List<Restorant> restorantList = restorantRepository.findAll();
        assertThat(restorantList).hasSize(databaseSizeBeforeCreate + 1);
        Restorant testRestorant = restorantList.get(restorantList.size() - 1);
        assertThat(testRestorant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestorant.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testRestorant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRestorant.getIconImage()).isEqualTo(DEFAULT_ICON_IMAGE);
        assertThat(testRestorant.getIconImageContentType()).isEqualTo(DEFAULT_ICON_IMAGE_CONTENT_TYPE);
        assertThat(testRestorant.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testRestorant.getLongtude()).isEqualTo(DEFAULT_LONGTUDE);
        assertThat(testRestorant.getAvailableOrderCap()).isEqualTo(DEFAULT_AVAILABLE_ORDER_CAP);
        assertThat(testRestorant.isStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createRestorantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = restorantRepository.findAll().size();

        // Create the Restorant with an existing ID
        restorant.setId(1L);
        RestorantDTO restorantDTO = restorantMapper.toDto(restorant);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestorantMockMvc.perform(post("/api/restorants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(restorantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Restorant in the database
        List<Restorant> restorantList = restorantRepository.findAll();
        assertThat(restorantList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRestorants() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList
        restRestorantMockMvc.perform(get("/api/restorants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restorant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].iconImageContentType").value(hasItem(DEFAULT_ICON_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].iconImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICON_IMAGE))))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longtude").value(hasItem(DEFAULT_LONGTUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].availableOrderCap").value(hasItem(DEFAULT_AVAILABLE_ORDER_CAP)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getRestorant() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get the restorant
        restRestorantMockMvc.perform(get("/api/restorants/{id}", restorant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restorant.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.iconImageContentType").value(DEFAULT_ICON_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.iconImage").value(Base64Utils.encodeToString(DEFAULT_ICON_IMAGE)))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longtude").value(DEFAULT_LONGTUDE.doubleValue()))
            .andExpect(jsonPath("$.availableOrderCap").value(DEFAULT_AVAILABLE_ORDER_CAP))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }


    @Test
    @Transactional
    public void getRestorantsByIdFiltering() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        Long id = restorant.getId();

        defaultRestorantShouldBeFound("id.equals=" + id);
        defaultRestorantShouldNotBeFound("id.notEquals=" + id);

        defaultRestorantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRestorantShouldNotBeFound("id.greaterThan=" + id);

        defaultRestorantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRestorantShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRestorantsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where name equals to DEFAULT_NAME
        defaultRestorantShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the restorantList where name equals to UPDATED_NAME
        defaultRestorantShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRestorantsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where name not equals to DEFAULT_NAME
        defaultRestorantShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the restorantList where name not equals to UPDATED_NAME
        defaultRestorantShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRestorantsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRestorantShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the restorantList where name equals to UPDATED_NAME
        defaultRestorantShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRestorantsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where name is not null
        defaultRestorantShouldBeFound("name.specified=true");

        // Get all the restorantList where name is null
        defaultRestorantShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllRestorantsByNameContainsSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where name contains DEFAULT_NAME
        defaultRestorantShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the restorantList where name contains UPDATED_NAME
        defaultRestorantShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRestorantsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where name does not contain DEFAULT_NAME
        defaultRestorantShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the restorantList where name does not contain UPDATED_NAME
        defaultRestorantShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllRestorantsByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where userName equals to DEFAULT_USER_NAME
        defaultRestorantShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the restorantList where userName equals to UPDATED_USER_NAME
        defaultRestorantShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllRestorantsByUserNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where userName not equals to DEFAULT_USER_NAME
        defaultRestorantShouldNotBeFound("userName.notEquals=" + DEFAULT_USER_NAME);

        // Get all the restorantList where userName not equals to UPDATED_USER_NAME
        defaultRestorantShouldBeFound("userName.notEquals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllRestorantsByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultRestorantShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the restorantList where userName equals to UPDATED_USER_NAME
        defaultRestorantShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllRestorantsByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where userName is not null
        defaultRestorantShouldBeFound("userName.specified=true");

        // Get all the restorantList where userName is null
        defaultRestorantShouldNotBeFound("userName.specified=false");
    }
                @Test
    @Transactional
    public void getAllRestorantsByUserNameContainsSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where userName contains DEFAULT_USER_NAME
        defaultRestorantShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the restorantList where userName contains UPDATED_USER_NAME
        defaultRestorantShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllRestorantsByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where userName does not contain DEFAULT_USER_NAME
        defaultRestorantShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the restorantList where userName does not contain UPDATED_USER_NAME
        defaultRestorantShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }


    @Test
    @Transactional
    public void getAllRestorantsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where latitude equals to DEFAULT_LATITUDE
        defaultRestorantShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the restorantList where latitude equals to UPDATED_LATITUDE
        defaultRestorantShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where latitude not equals to DEFAULT_LATITUDE
        defaultRestorantShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the restorantList where latitude not equals to UPDATED_LATITUDE
        defaultRestorantShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultRestorantShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the restorantList where latitude equals to UPDATED_LATITUDE
        defaultRestorantShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where latitude is not null
        defaultRestorantShouldBeFound("latitude.specified=true");

        // Get all the restorantList where latitude is null
        defaultRestorantShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllRestorantsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultRestorantShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the restorantList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultRestorantShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultRestorantShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the restorantList where latitude is less than or equal to SMALLER_LATITUDE
        defaultRestorantShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where latitude is less than DEFAULT_LATITUDE
        defaultRestorantShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the restorantList where latitude is less than UPDATED_LATITUDE
        defaultRestorantShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where latitude is greater than DEFAULT_LATITUDE
        defaultRestorantShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the restorantList where latitude is greater than SMALLER_LATITUDE
        defaultRestorantShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }


    @Test
    @Transactional
    public void getAllRestorantsByLongtudeIsEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where longtude equals to DEFAULT_LONGTUDE
        defaultRestorantShouldBeFound("longtude.equals=" + DEFAULT_LONGTUDE);

        // Get all the restorantList where longtude equals to UPDATED_LONGTUDE
        defaultRestorantShouldNotBeFound("longtude.equals=" + UPDATED_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLongtudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where longtude not equals to DEFAULT_LONGTUDE
        defaultRestorantShouldNotBeFound("longtude.notEquals=" + DEFAULT_LONGTUDE);

        // Get all the restorantList where longtude not equals to UPDATED_LONGTUDE
        defaultRestorantShouldBeFound("longtude.notEquals=" + UPDATED_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLongtudeIsInShouldWork() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where longtude in DEFAULT_LONGTUDE or UPDATED_LONGTUDE
        defaultRestorantShouldBeFound("longtude.in=" + DEFAULT_LONGTUDE + "," + UPDATED_LONGTUDE);

        // Get all the restorantList where longtude equals to UPDATED_LONGTUDE
        defaultRestorantShouldNotBeFound("longtude.in=" + UPDATED_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLongtudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where longtude is not null
        defaultRestorantShouldBeFound("longtude.specified=true");

        // Get all the restorantList where longtude is null
        defaultRestorantShouldNotBeFound("longtude.specified=false");
    }

    @Test
    @Transactional
    public void getAllRestorantsByLongtudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where longtude is greater than or equal to DEFAULT_LONGTUDE
        defaultRestorantShouldBeFound("longtude.greaterThanOrEqual=" + DEFAULT_LONGTUDE);

        // Get all the restorantList where longtude is greater than or equal to UPDATED_LONGTUDE
        defaultRestorantShouldNotBeFound("longtude.greaterThanOrEqual=" + UPDATED_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLongtudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where longtude is less than or equal to DEFAULT_LONGTUDE
        defaultRestorantShouldBeFound("longtude.lessThanOrEqual=" + DEFAULT_LONGTUDE);

        // Get all the restorantList where longtude is less than or equal to SMALLER_LONGTUDE
        defaultRestorantShouldNotBeFound("longtude.lessThanOrEqual=" + SMALLER_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLongtudeIsLessThanSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where longtude is less than DEFAULT_LONGTUDE
        defaultRestorantShouldNotBeFound("longtude.lessThan=" + DEFAULT_LONGTUDE);

        // Get all the restorantList where longtude is less than UPDATED_LONGTUDE
        defaultRestorantShouldBeFound("longtude.lessThan=" + UPDATED_LONGTUDE);
    }

    @Test
    @Transactional
    public void getAllRestorantsByLongtudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where longtude is greater than DEFAULT_LONGTUDE
        defaultRestorantShouldNotBeFound("longtude.greaterThan=" + DEFAULT_LONGTUDE);

        // Get all the restorantList where longtude is greater than SMALLER_LONGTUDE
        defaultRestorantShouldBeFound("longtude.greaterThan=" + SMALLER_LONGTUDE);
    }


    @Test
    @Transactional
    public void getAllRestorantsByAvailableOrderCapIsEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where availableOrderCap equals to DEFAULT_AVAILABLE_ORDER_CAP
        defaultRestorantShouldBeFound("availableOrderCap.equals=" + DEFAULT_AVAILABLE_ORDER_CAP);

        // Get all the restorantList where availableOrderCap equals to UPDATED_AVAILABLE_ORDER_CAP
        defaultRestorantShouldNotBeFound("availableOrderCap.equals=" + UPDATED_AVAILABLE_ORDER_CAP);
    }

    @Test
    @Transactional
    public void getAllRestorantsByAvailableOrderCapIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where availableOrderCap not equals to DEFAULT_AVAILABLE_ORDER_CAP
        defaultRestorantShouldNotBeFound("availableOrderCap.notEquals=" + DEFAULT_AVAILABLE_ORDER_CAP);

        // Get all the restorantList where availableOrderCap not equals to UPDATED_AVAILABLE_ORDER_CAP
        defaultRestorantShouldBeFound("availableOrderCap.notEquals=" + UPDATED_AVAILABLE_ORDER_CAP);
    }

    @Test
    @Transactional
    public void getAllRestorantsByAvailableOrderCapIsInShouldWork() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where availableOrderCap in DEFAULT_AVAILABLE_ORDER_CAP or UPDATED_AVAILABLE_ORDER_CAP
        defaultRestorantShouldBeFound("availableOrderCap.in=" + DEFAULT_AVAILABLE_ORDER_CAP + "," + UPDATED_AVAILABLE_ORDER_CAP);

        // Get all the restorantList where availableOrderCap equals to UPDATED_AVAILABLE_ORDER_CAP
        defaultRestorantShouldNotBeFound("availableOrderCap.in=" + UPDATED_AVAILABLE_ORDER_CAP);
    }

    @Test
    @Transactional
    public void getAllRestorantsByAvailableOrderCapIsNullOrNotNull() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where availableOrderCap is not null
        defaultRestorantShouldBeFound("availableOrderCap.specified=true");

        // Get all the restorantList where availableOrderCap is null
        defaultRestorantShouldNotBeFound("availableOrderCap.specified=false");
    }

    @Test
    @Transactional
    public void getAllRestorantsByAvailableOrderCapIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where availableOrderCap is greater than or equal to DEFAULT_AVAILABLE_ORDER_CAP
        defaultRestorantShouldBeFound("availableOrderCap.greaterThanOrEqual=" + DEFAULT_AVAILABLE_ORDER_CAP);

        // Get all the restorantList where availableOrderCap is greater than or equal to UPDATED_AVAILABLE_ORDER_CAP
        defaultRestorantShouldNotBeFound("availableOrderCap.greaterThanOrEqual=" + UPDATED_AVAILABLE_ORDER_CAP);
    }

    @Test
    @Transactional
    public void getAllRestorantsByAvailableOrderCapIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where availableOrderCap is less than or equal to DEFAULT_AVAILABLE_ORDER_CAP
        defaultRestorantShouldBeFound("availableOrderCap.lessThanOrEqual=" + DEFAULT_AVAILABLE_ORDER_CAP);

        // Get all the restorantList where availableOrderCap is less than or equal to SMALLER_AVAILABLE_ORDER_CAP
        defaultRestorantShouldNotBeFound("availableOrderCap.lessThanOrEqual=" + SMALLER_AVAILABLE_ORDER_CAP);
    }

    @Test
    @Transactional
    public void getAllRestorantsByAvailableOrderCapIsLessThanSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where availableOrderCap is less than DEFAULT_AVAILABLE_ORDER_CAP
        defaultRestorantShouldNotBeFound("availableOrderCap.lessThan=" + DEFAULT_AVAILABLE_ORDER_CAP);

        // Get all the restorantList where availableOrderCap is less than UPDATED_AVAILABLE_ORDER_CAP
        defaultRestorantShouldBeFound("availableOrderCap.lessThan=" + UPDATED_AVAILABLE_ORDER_CAP);
    }

    @Test
    @Transactional
    public void getAllRestorantsByAvailableOrderCapIsGreaterThanSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where availableOrderCap is greater than DEFAULT_AVAILABLE_ORDER_CAP
        defaultRestorantShouldNotBeFound("availableOrderCap.greaterThan=" + DEFAULT_AVAILABLE_ORDER_CAP);

        // Get all the restorantList where availableOrderCap is greater than SMALLER_AVAILABLE_ORDER_CAP
        defaultRestorantShouldBeFound("availableOrderCap.greaterThan=" + SMALLER_AVAILABLE_ORDER_CAP);
    }


    @Test
    @Transactional
    public void getAllRestorantsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where status equals to DEFAULT_STATUS
        defaultRestorantShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the restorantList where status equals to UPDATED_STATUS
        defaultRestorantShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllRestorantsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where status not equals to DEFAULT_STATUS
        defaultRestorantShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the restorantList where status not equals to UPDATED_STATUS
        defaultRestorantShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllRestorantsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultRestorantShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the restorantList where status equals to UPDATED_STATUS
        defaultRestorantShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllRestorantsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        // Get all the restorantList where status is not null
        defaultRestorantShouldBeFound("status.specified=true");

        // Get all the restorantList where status is null
        defaultRestorantShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllRestorantsByFoodIsEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);
        Food food = FoodResourceIT.createEntity(em);
        em.persist(food);
        em.flush();
        restorant.addFood(food);
        restorantRepository.saveAndFlush(restorant);
        Long foodId = food.getId();

        // Get all the restorantList where food equals to foodId
        defaultRestorantShouldBeFound("foodId.equals=" + foodId);

        // Get all the restorantList where food equals to foodId + 1
        defaultRestorantShouldNotBeFound("foodId.equals=" + (foodId + 1));
    }


    @Test
    @Transactional
    public void getAllRestorantsByTelegramRestaurantUserIsEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);
        TelegramRestaurantUser telegramRestaurantUser = TelegramRestaurantUserResourceIT.createEntity(em);
        em.persist(telegramRestaurantUser);
        em.flush();
        restorant.addTelegramRestaurantUser(telegramRestaurantUser);
        restorantRepository.saveAndFlush(restorant);
        Long telegramRestaurantUserId = telegramRestaurantUser.getId();

        // Get all the restorantList where telegramRestaurantUser equals to telegramRestaurantUserId
        defaultRestorantShouldBeFound("telegramRestaurantUserId.equals=" + telegramRestaurantUserId);

        // Get all the restorantList where telegramRestaurantUser equals to telegramRestaurantUserId + 1
        defaultRestorantShouldNotBeFound("telegramRestaurantUserId.equals=" + (telegramRestaurantUserId + 1));
    }


    @Test
    @Transactional
    public void getAllRestorantsByTelegramDeliveryUserIsEqualToSomething() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);
        TelegramDeliveryUser telegramDeliveryUser = TelegramDeliveryUserResourceIT.createEntity(em);
        em.persist(telegramDeliveryUser);
        em.flush();
        restorant.addTelegramDeliveryUser(telegramDeliveryUser);
        restorantRepository.saveAndFlush(restorant);
        Long telegramDeliveryUserId = telegramDeliveryUser.getId();

        // Get all the restorantList where telegramDeliveryUser equals to telegramDeliveryUserId
        defaultRestorantShouldBeFound("telegramDeliveryUserId.equals=" + telegramDeliveryUserId);

        // Get all the restorantList where telegramDeliveryUser equals to telegramDeliveryUserId + 1
        defaultRestorantShouldNotBeFound("telegramDeliveryUserId.equals=" + (telegramDeliveryUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRestorantShouldBeFound(String filter) throws Exception {
        restRestorantMockMvc.perform(get("/api/restorants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restorant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].iconImageContentType").value(hasItem(DEFAULT_ICON_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].iconImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICON_IMAGE))))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longtude").value(hasItem(DEFAULT_LONGTUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].availableOrderCap").value(hasItem(DEFAULT_AVAILABLE_ORDER_CAP)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));

        // Check, that the count call also returns 1
        restRestorantMockMvc.perform(get("/api/restorants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRestorantShouldNotBeFound(String filter) throws Exception {
        restRestorantMockMvc.perform(get("/api/restorants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRestorantMockMvc.perform(get("/api/restorants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRestorant() throws Exception {
        // Get the restorant
        restRestorantMockMvc.perform(get("/api/restorants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRestorant() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        int databaseSizeBeforeUpdate = restorantRepository.findAll().size();

        // Update the restorant
        Restorant updatedRestorant = restorantRepository.findById(restorant.getId()).get();
        // Disconnect from session so that the updates on updatedRestorant are not directly saved in db
        em.detach(updatedRestorant);
        updatedRestorant
            .name(UPDATED_NAME)
            .userName(UPDATED_USER_NAME)
            .description(UPDATED_DESCRIPTION)
            .iconImage(UPDATED_ICON_IMAGE)
            .iconImageContentType(UPDATED_ICON_IMAGE_CONTENT_TYPE)
            .latitude(UPDATED_LATITUDE)
            .longtude(UPDATED_LONGTUDE)
            .availableOrderCap(UPDATED_AVAILABLE_ORDER_CAP)
            .status(UPDATED_STATUS);
        RestorantDTO restorantDTO = restorantMapper.toDto(updatedRestorant);

        restRestorantMockMvc.perform(put("/api/restorants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(restorantDTO)))
            .andExpect(status().isOk());

        // Validate the Restorant in the database
        List<Restorant> restorantList = restorantRepository.findAll();
        assertThat(restorantList).hasSize(databaseSizeBeforeUpdate);
        Restorant testRestorant = restorantList.get(restorantList.size() - 1);
        assertThat(testRestorant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestorant.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testRestorant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRestorant.getIconImage()).isEqualTo(UPDATED_ICON_IMAGE);
        assertThat(testRestorant.getIconImageContentType()).isEqualTo(UPDATED_ICON_IMAGE_CONTENT_TYPE);
        assertThat(testRestorant.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testRestorant.getLongtude()).isEqualTo(UPDATED_LONGTUDE);
        assertThat(testRestorant.getAvailableOrderCap()).isEqualTo(UPDATED_AVAILABLE_ORDER_CAP);
        assertThat(testRestorant.isStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingRestorant() throws Exception {
        int databaseSizeBeforeUpdate = restorantRepository.findAll().size();

        // Create the Restorant
        RestorantDTO restorantDTO = restorantMapper.toDto(restorant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestorantMockMvc.perform(put("/api/restorants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(restorantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Restorant in the database
        List<Restorant> restorantList = restorantRepository.findAll();
        assertThat(restorantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRestorant() throws Exception {
        // Initialize the database
        restorantRepository.saveAndFlush(restorant);

        int databaseSizeBeforeDelete = restorantRepository.findAll().size();

        // Delete the restorant
        restRestorantMockMvc.perform(delete("/api/restorants/{id}", restorant.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restorant> restorantList = restorantRepository.findAll();
        assertThat(restorantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
