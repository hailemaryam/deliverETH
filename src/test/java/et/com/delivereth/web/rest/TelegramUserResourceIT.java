package et.com.delivereth.web.rest;

import et.com.delivereth.DeliverEthApp;
import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.domain.Order;
import et.com.delivereth.repository.TelegramUserRepository;
import et.com.delivereth.service.TelegramUserService;
import et.com.delivereth.service.dto.TelegramUserDTO;
import et.com.delivereth.service.mapper.TelegramUserMapper;
import et.com.delivereth.service.dto.TelegramUserCriteria;
import et.com.delivereth.service.TelegramUserQueryService;

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
 * Integration tests for the {@link TelegramUserResource} REST controller.
 */
@SpringBootTest(classes = DeliverEthApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class TelegramUserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CHAT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CHAT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CONVERSATION_META_DATA = "AAAAAAAAAA";
    private static final String UPDATED_CONVERSATION_META_DATA = "BBBBBBBBBB";

    private static final Long DEFAULT_ORDER_ID_PAUSED = 1L;
    private static final Long UPDATED_ORDER_ID_PAUSED = 2L;
    private static final Long SMALLER_ORDER_ID_PAUSED = 1L - 1L;

    private static final Long DEFAULT_ORDERED_FOOD_ID_PAUSED = 1L;
    private static final Long UPDATED_ORDERED_FOOD_ID_PAUSED = 2L;
    private static final Long SMALLER_ORDERED_FOOD_ID_PAUSED = 1L - 1L;

    private static final Integer DEFAULT_LOADED_PAGE = 1;
    private static final Integer UPDATED_LOADED_PAGE = 2;
    private static final Integer SMALLER_LOADED_PAGE = 1 - 1;

    @Autowired
    private TelegramUserRepository telegramUserRepository;

    @Autowired
    private TelegramUserMapper telegramUserMapper;

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private TelegramUserQueryService telegramUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTelegramUserMockMvc;

    private TelegramUser telegramUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TelegramUser createEntity(EntityManager em) {
        TelegramUser telegramUser = new TelegramUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .userName(DEFAULT_USER_NAME)
            .chatId(DEFAULT_CHAT_ID)
            .phone(DEFAULT_PHONE)
            .conversationMetaData(DEFAULT_CONVERSATION_META_DATA)
            .orderIdPaused(DEFAULT_ORDER_ID_PAUSED)
            .orderedFoodIdPaused(DEFAULT_ORDERED_FOOD_ID_PAUSED)
            .loadedPage(DEFAULT_LOADED_PAGE);
        return telegramUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TelegramUser createUpdatedEntity(EntityManager em) {
        TelegramUser telegramUser = new TelegramUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .userName(UPDATED_USER_NAME)
            .chatId(UPDATED_CHAT_ID)
            .phone(UPDATED_PHONE)
            .conversationMetaData(UPDATED_CONVERSATION_META_DATA)
            .orderIdPaused(UPDATED_ORDER_ID_PAUSED)
            .orderedFoodIdPaused(UPDATED_ORDERED_FOOD_ID_PAUSED)
            .loadedPage(UPDATED_LOADED_PAGE);
        return telegramUser;
    }

    @BeforeEach
    public void initTest() {
        telegramUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createTelegramUser() throws Exception {
        int databaseSizeBeforeCreate = telegramUserRepository.findAll().size();

        // Create the TelegramUser
        TelegramUserDTO telegramUserDTO = telegramUserMapper.toDto(telegramUser);
        restTelegramUserMockMvc.perform(post("/api/telegram-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramUserDTO)))
            .andExpect(status().isCreated());

        // Validate the TelegramUser in the database
        List<TelegramUser> telegramUserList = telegramUserRepository.findAll();
        assertThat(telegramUserList).hasSize(databaseSizeBeforeCreate + 1);
        TelegramUser testTelegramUser = telegramUserList.get(telegramUserList.size() - 1);
        assertThat(testTelegramUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTelegramUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTelegramUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testTelegramUser.getChatId()).isEqualTo(DEFAULT_CHAT_ID);
        assertThat(testTelegramUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testTelegramUser.getConversationMetaData()).isEqualTo(DEFAULT_CONVERSATION_META_DATA);
        assertThat(testTelegramUser.getOrderIdPaused()).isEqualTo(DEFAULT_ORDER_ID_PAUSED);
        assertThat(testTelegramUser.getOrderedFoodIdPaused()).isEqualTo(DEFAULT_ORDERED_FOOD_ID_PAUSED);
        assertThat(testTelegramUser.getLoadedPage()).isEqualTo(DEFAULT_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void createTelegramUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = telegramUserRepository.findAll().size();

        // Create the TelegramUser with an existing ID
        telegramUser.setId(1L);
        TelegramUserDTO telegramUserDTO = telegramUserMapper.toDto(telegramUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelegramUserMockMvc.perform(post("/api/telegram-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TelegramUser in the database
        List<TelegramUser> telegramUserList = telegramUserRepository.findAll();
        assertThat(telegramUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTelegramUsers() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList
        restTelegramUserMockMvc.perform(get("/api/telegram-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telegramUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].chatId").value(hasItem(DEFAULT_CHAT_ID)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].conversationMetaData").value(hasItem(DEFAULT_CONVERSATION_META_DATA)))
            .andExpect(jsonPath("$.[*].orderIdPaused").value(hasItem(DEFAULT_ORDER_ID_PAUSED.intValue())))
            .andExpect(jsonPath("$.[*].orderedFoodIdPaused").value(hasItem(DEFAULT_ORDERED_FOOD_ID_PAUSED.intValue())))
            .andExpect(jsonPath("$.[*].loadedPage").value(hasItem(DEFAULT_LOADED_PAGE)));
    }
    
    @Test
    @Transactional
    public void getTelegramUser() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get the telegramUser
        restTelegramUserMockMvc.perform(get("/api/telegram-users/{id}", telegramUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(telegramUser.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.chatId").value(DEFAULT_CHAT_ID))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.conversationMetaData").value(DEFAULT_CONVERSATION_META_DATA))
            .andExpect(jsonPath("$.orderIdPaused").value(DEFAULT_ORDER_ID_PAUSED.intValue()))
            .andExpect(jsonPath("$.orderedFoodIdPaused").value(DEFAULT_ORDERED_FOOD_ID_PAUSED.intValue()))
            .andExpect(jsonPath("$.loadedPage").value(DEFAULT_LOADED_PAGE));
    }


    @Test
    @Transactional
    public void getTelegramUsersByIdFiltering() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        Long id = telegramUser.getId();

        defaultTelegramUserShouldBeFound("id.equals=" + id);
        defaultTelegramUserShouldNotBeFound("id.notEquals=" + id);

        defaultTelegramUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTelegramUserShouldNotBeFound("id.greaterThan=" + id);

        defaultTelegramUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTelegramUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTelegramUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultTelegramUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the telegramUserList where firstName equals to UPDATED_FIRST_NAME
        defaultTelegramUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where firstName not equals to DEFAULT_FIRST_NAME
        defaultTelegramUserShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the telegramUserList where firstName not equals to UPDATED_FIRST_NAME
        defaultTelegramUserShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultTelegramUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the telegramUserList where firstName equals to UPDATED_FIRST_NAME
        defaultTelegramUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where firstName is not null
        defaultTelegramUserShouldBeFound("firstName.specified=true");

        // Get all the telegramUserList where firstName is null
        defaultTelegramUserShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where firstName contains DEFAULT_FIRST_NAME
        defaultTelegramUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the telegramUserList where firstName contains UPDATED_FIRST_NAME
        defaultTelegramUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultTelegramUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the telegramUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultTelegramUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllTelegramUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where lastName equals to DEFAULT_LAST_NAME
        defaultTelegramUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the telegramUserList where lastName equals to UPDATED_LAST_NAME
        defaultTelegramUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where lastName not equals to DEFAULT_LAST_NAME
        defaultTelegramUserShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the telegramUserList where lastName not equals to UPDATED_LAST_NAME
        defaultTelegramUserShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultTelegramUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the telegramUserList where lastName equals to UPDATED_LAST_NAME
        defaultTelegramUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where lastName is not null
        defaultTelegramUserShouldBeFound("lastName.specified=true");

        // Get all the telegramUserList where lastName is null
        defaultTelegramUserShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where lastName contains DEFAULT_LAST_NAME
        defaultTelegramUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the telegramUserList where lastName contains UPDATED_LAST_NAME
        defaultTelegramUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultTelegramUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the telegramUserList where lastName does not contain UPDATED_LAST_NAME
        defaultTelegramUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllTelegramUsersByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where userName equals to DEFAULT_USER_NAME
        defaultTelegramUserShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the telegramUserList where userName equals to UPDATED_USER_NAME
        defaultTelegramUserShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByUserNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where userName not equals to DEFAULT_USER_NAME
        defaultTelegramUserShouldNotBeFound("userName.notEquals=" + DEFAULT_USER_NAME);

        // Get all the telegramUserList where userName not equals to UPDATED_USER_NAME
        defaultTelegramUserShouldBeFound("userName.notEquals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultTelegramUserShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the telegramUserList where userName equals to UPDATED_USER_NAME
        defaultTelegramUserShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where userName is not null
        defaultTelegramUserShouldBeFound("userName.specified=true");

        // Get all the telegramUserList where userName is null
        defaultTelegramUserShouldNotBeFound("userName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramUsersByUserNameContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where userName contains DEFAULT_USER_NAME
        defaultTelegramUserShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the telegramUserList where userName contains UPDATED_USER_NAME
        defaultTelegramUserShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where userName does not contain DEFAULT_USER_NAME
        defaultTelegramUserShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the telegramUserList where userName does not contain UPDATED_USER_NAME
        defaultTelegramUserShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }


    @Test
    @Transactional
    public void getAllTelegramUsersByChatIdIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where chatId equals to DEFAULT_CHAT_ID
        defaultTelegramUserShouldBeFound("chatId.equals=" + DEFAULT_CHAT_ID);

        // Get all the telegramUserList where chatId equals to UPDATED_CHAT_ID
        defaultTelegramUserShouldNotBeFound("chatId.equals=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByChatIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where chatId not equals to DEFAULT_CHAT_ID
        defaultTelegramUserShouldNotBeFound("chatId.notEquals=" + DEFAULT_CHAT_ID);

        // Get all the telegramUserList where chatId not equals to UPDATED_CHAT_ID
        defaultTelegramUserShouldBeFound("chatId.notEquals=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByChatIdIsInShouldWork() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where chatId in DEFAULT_CHAT_ID or UPDATED_CHAT_ID
        defaultTelegramUserShouldBeFound("chatId.in=" + DEFAULT_CHAT_ID + "," + UPDATED_CHAT_ID);

        // Get all the telegramUserList where chatId equals to UPDATED_CHAT_ID
        defaultTelegramUserShouldNotBeFound("chatId.in=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByChatIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where chatId is not null
        defaultTelegramUserShouldBeFound("chatId.specified=true");

        // Get all the telegramUserList where chatId is null
        defaultTelegramUserShouldNotBeFound("chatId.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramUsersByChatIdContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where chatId contains DEFAULT_CHAT_ID
        defaultTelegramUserShouldBeFound("chatId.contains=" + DEFAULT_CHAT_ID);

        // Get all the telegramUserList where chatId contains UPDATED_CHAT_ID
        defaultTelegramUserShouldNotBeFound("chatId.contains=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByChatIdNotContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where chatId does not contain DEFAULT_CHAT_ID
        defaultTelegramUserShouldNotBeFound("chatId.doesNotContain=" + DEFAULT_CHAT_ID);

        // Get all the telegramUserList where chatId does not contain UPDATED_CHAT_ID
        defaultTelegramUserShouldBeFound("chatId.doesNotContain=" + UPDATED_CHAT_ID);
    }


    @Test
    @Transactional
    public void getAllTelegramUsersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where phone equals to DEFAULT_PHONE
        defaultTelegramUserShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the telegramUserList where phone equals to UPDATED_PHONE
        defaultTelegramUserShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where phone not equals to DEFAULT_PHONE
        defaultTelegramUserShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the telegramUserList where phone not equals to UPDATED_PHONE
        defaultTelegramUserShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultTelegramUserShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the telegramUserList where phone equals to UPDATED_PHONE
        defaultTelegramUserShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where phone is not null
        defaultTelegramUserShouldBeFound("phone.specified=true");

        // Get all the telegramUserList where phone is null
        defaultTelegramUserShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramUsersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where phone contains DEFAULT_PHONE
        defaultTelegramUserShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the telegramUserList where phone contains UPDATED_PHONE
        defaultTelegramUserShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where phone does not contain DEFAULT_PHONE
        defaultTelegramUserShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the telegramUserList where phone does not contain UPDATED_PHONE
        defaultTelegramUserShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllTelegramUsersByConversationMetaDataIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where conversationMetaData equals to DEFAULT_CONVERSATION_META_DATA
        defaultTelegramUserShouldBeFound("conversationMetaData.equals=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramUserList where conversationMetaData equals to UPDATED_CONVERSATION_META_DATA
        defaultTelegramUserShouldNotBeFound("conversationMetaData.equals=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByConversationMetaDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where conversationMetaData not equals to DEFAULT_CONVERSATION_META_DATA
        defaultTelegramUserShouldNotBeFound("conversationMetaData.notEquals=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramUserList where conversationMetaData not equals to UPDATED_CONVERSATION_META_DATA
        defaultTelegramUserShouldBeFound("conversationMetaData.notEquals=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByConversationMetaDataIsInShouldWork() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where conversationMetaData in DEFAULT_CONVERSATION_META_DATA or UPDATED_CONVERSATION_META_DATA
        defaultTelegramUserShouldBeFound("conversationMetaData.in=" + DEFAULT_CONVERSATION_META_DATA + "," + UPDATED_CONVERSATION_META_DATA);

        // Get all the telegramUserList where conversationMetaData equals to UPDATED_CONVERSATION_META_DATA
        defaultTelegramUserShouldNotBeFound("conversationMetaData.in=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByConversationMetaDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where conversationMetaData is not null
        defaultTelegramUserShouldBeFound("conversationMetaData.specified=true");

        // Get all the telegramUserList where conversationMetaData is null
        defaultTelegramUserShouldNotBeFound("conversationMetaData.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramUsersByConversationMetaDataContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where conversationMetaData contains DEFAULT_CONVERSATION_META_DATA
        defaultTelegramUserShouldBeFound("conversationMetaData.contains=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramUserList where conversationMetaData contains UPDATED_CONVERSATION_META_DATA
        defaultTelegramUserShouldNotBeFound("conversationMetaData.contains=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByConversationMetaDataNotContainsSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where conversationMetaData does not contain DEFAULT_CONVERSATION_META_DATA
        defaultTelegramUserShouldNotBeFound("conversationMetaData.doesNotContain=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramUserList where conversationMetaData does not contain UPDATED_CONVERSATION_META_DATA
        defaultTelegramUserShouldBeFound("conversationMetaData.doesNotContain=" + UPDATED_CONVERSATION_META_DATA);
    }


    @Test
    @Transactional
    public void getAllTelegramUsersByOrderIdPausedIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderIdPaused equals to DEFAULT_ORDER_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderIdPaused.equals=" + DEFAULT_ORDER_ID_PAUSED);

        // Get all the telegramUserList where orderIdPaused equals to UPDATED_ORDER_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderIdPaused.equals=" + UPDATED_ORDER_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderIdPausedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderIdPaused not equals to DEFAULT_ORDER_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderIdPaused.notEquals=" + DEFAULT_ORDER_ID_PAUSED);

        // Get all the telegramUserList where orderIdPaused not equals to UPDATED_ORDER_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderIdPaused.notEquals=" + UPDATED_ORDER_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderIdPausedIsInShouldWork() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderIdPaused in DEFAULT_ORDER_ID_PAUSED or UPDATED_ORDER_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderIdPaused.in=" + DEFAULT_ORDER_ID_PAUSED + "," + UPDATED_ORDER_ID_PAUSED);

        // Get all the telegramUserList where orderIdPaused equals to UPDATED_ORDER_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderIdPaused.in=" + UPDATED_ORDER_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderIdPausedIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderIdPaused is not null
        defaultTelegramUserShouldBeFound("orderIdPaused.specified=true");

        // Get all the telegramUserList where orderIdPaused is null
        defaultTelegramUserShouldNotBeFound("orderIdPaused.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderIdPausedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderIdPaused is greater than or equal to DEFAULT_ORDER_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderIdPaused.greaterThanOrEqual=" + DEFAULT_ORDER_ID_PAUSED);

        // Get all the telegramUserList where orderIdPaused is greater than or equal to UPDATED_ORDER_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderIdPaused.greaterThanOrEqual=" + UPDATED_ORDER_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderIdPausedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderIdPaused is less than or equal to DEFAULT_ORDER_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderIdPaused.lessThanOrEqual=" + DEFAULT_ORDER_ID_PAUSED);

        // Get all the telegramUserList where orderIdPaused is less than or equal to SMALLER_ORDER_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderIdPaused.lessThanOrEqual=" + SMALLER_ORDER_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderIdPausedIsLessThanSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderIdPaused is less than DEFAULT_ORDER_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderIdPaused.lessThan=" + DEFAULT_ORDER_ID_PAUSED);

        // Get all the telegramUserList where orderIdPaused is less than UPDATED_ORDER_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderIdPaused.lessThan=" + UPDATED_ORDER_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderIdPausedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderIdPaused is greater than DEFAULT_ORDER_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderIdPaused.greaterThan=" + DEFAULT_ORDER_ID_PAUSED);

        // Get all the telegramUserList where orderIdPaused is greater than SMALLER_ORDER_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderIdPaused.greaterThan=" + SMALLER_ORDER_ID_PAUSED);
    }


    @Test
    @Transactional
    public void getAllTelegramUsersByOrderedFoodIdPausedIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderedFoodIdPaused equals to DEFAULT_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderedFoodIdPaused.equals=" + DEFAULT_ORDERED_FOOD_ID_PAUSED);

        // Get all the telegramUserList where orderedFoodIdPaused equals to UPDATED_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderedFoodIdPaused.equals=" + UPDATED_ORDERED_FOOD_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderedFoodIdPausedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderedFoodIdPaused not equals to DEFAULT_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderedFoodIdPaused.notEquals=" + DEFAULT_ORDERED_FOOD_ID_PAUSED);

        // Get all the telegramUserList where orderedFoodIdPaused not equals to UPDATED_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderedFoodIdPaused.notEquals=" + UPDATED_ORDERED_FOOD_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderedFoodIdPausedIsInShouldWork() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderedFoodIdPaused in DEFAULT_ORDERED_FOOD_ID_PAUSED or UPDATED_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderedFoodIdPaused.in=" + DEFAULT_ORDERED_FOOD_ID_PAUSED + "," + UPDATED_ORDERED_FOOD_ID_PAUSED);

        // Get all the telegramUserList where orderedFoodIdPaused equals to UPDATED_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderedFoodIdPaused.in=" + UPDATED_ORDERED_FOOD_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderedFoodIdPausedIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderedFoodIdPaused is not null
        defaultTelegramUserShouldBeFound("orderedFoodIdPaused.specified=true");

        // Get all the telegramUserList where orderedFoodIdPaused is null
        defaultTelegramUserShouldNotBeFound("orderedFoodIdPaused.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderedFoodIdPausedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderedFoodIdPaused is greater than or equal to DEFAULT_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderedFoodIdPaused.greaterThanOrEqual=" + DEFAULT_ORDERED_FOOD_ID_PAUSED);

        // Get all the telegramUserList where orderedFoodIdPaused is greater than or equal to UPDATED_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderedFoodIdPaused.greaterThanOrEqual=" + UPDATED_ORDERED_FOOD_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderedFoodIdPausedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderedFoodIdPaused is less than or equal to DEFAULT_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderedFoodIdPaused.lessThanOrEqual=" + DEFAULT_ORDERED_FOOD_ID_PAUSED);

        // Get all the telegramUserList where orderedFoodIdPaused is less than or equal to SMALLER_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderedFoodIdPaused.lessThanOrEqual=" + SMALLER_ORDERED_FOOD_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderedFoodIdPausedIsLessThanSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderedFoodIdPaused is less than DEFAULT_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderedFoodIdPaused.lessThan=" + DEFAULT_ORDERED_FOOD_ID_PAUSED);

        // Get all the telegramUserList where orderedFoodIdPaused is less than UPDATED_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderedFoodIdPaused.lessThan=" + UPDATED_ORDERED_FOOD_ID_PAUSED);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByOrderedFoodIdPausedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where orderedFoodIdPaused is greater than DEFAULT_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldNotBeFound("orderedFoodIdPaused.greaterThan=" + DEFAULT_ORDERED_FOOD_ID_PAUSED);

        // Get all the telegramUserList where orderedFoodIdPaused is greater than SMALLER_ORDERED_FOOD_ID_PAUSED
        defaultTelegramUserShouldBeFound("orderedFoodIdPaused.greaterThan=" + SMALLER_ORDERED_FOOD_ID_PAUSED);
    }


    @Test
    @Transactional
    public void getAllTelegramUsersByLoadedPageIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where loadedPage equals to DEFAULT_LOADED_PAGE
        defaultTelegramUserShouldBeFound("loadedPage.equals=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramUserList where loadedPage equals to UPDATED_LOADED_PAGE
        defaultTelegramUserShouldNotBeFound("loadedPage.equals=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByLoadedPageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where loadedPage not equals to DEFAULT_LOADED_PAGE
        defaultTelegramUserShouldNotBeFound("loadedPage.notEquals=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramUserList where loadedPage not equals to UPDATED_LOADED_PAGE
        defaultTelegramUserShouldBeFound("loadedPage.notEquals=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByLoadedPageIsInShouldWork() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where loadedPage in DEFAULT_LOADED_PAGE or UPDATED_LOADED_PAGE
        defaultTelegramUserShouldBeFound("loadedPage.in=" + DEFAULT_LOADED_PAGE + "," + UPDATED_LOADED_PAGE);

        // Get all the telegramUserList where loadedPage equals to UPDATED_LOADED_PAGE
        defaultTelegramUserShouldNotBeFound("loadedPage.in=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByLoadedPageIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where loadedPage is not null
        defaultTelegramUserShouldBeFound("loadedPage.specified=true");

        // Get all the telegramUserList where loadedPage is null
        defaultTelegramUserShouldNotBeFound("loadedPage.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByLoadedPageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where loadedPage is greater than or equal to DEFAULT_LOADED_PAGE
        defaultTelegramUserShouldBeFound("loadedPage.greaterThanOrEqual=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramUserList where loadedPage is greater than or equal to UPDATED_LOADED_PAGE
        defaultTelegramUserShouldNotBeFound("loadedPage.greaterThanOrEqual=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByLoadedPageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where loadedPage is less than or equal to DEFAULT_LOADED_PAGE
        defaultTelegramUserShouldBeFound("loadedPage.lessThanOrEqual=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramUserList where loadedPage is less than or equal to SMALLER_LOADED_PAGE
        defaultTelegramUserShouldNotBeFound("loadedPage.lessThanOrEqual=" + SMALLER_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByLoadedPageIsLessThanSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where loadedPage is less than DEFAULT_LOADED_PAGE
        defaultTelegramUserShouldNotBeFound("loadedPage.lessThan=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramUserList where loadedPage is less than UPDATED_LOADED_PAGE
        defaultTelegramUserShouldBeFound("loadedPage.lessThan=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramUsersByLoadedPageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        // Get all the telegramUserList where loadedPage is greater than DEFAULT_LOADED_PAGE
        defaultTelegramUserShouldNotBeFound("loadedPage.greaterThan=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramUserList where loadedPage is greater than SMALLER_LOADED_PAGE
        defaultTelegramUserShouldBeFound("loadedPage.greaterThan=" + SMALLER_LOADED_PAGE);
    }


    @Test
    @Transactional
    public void getAllTelegramUsersByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);
        Order order = OrderResourceIT.createEntity(em);
        em.persist(order);
        em.flush();
        telegramUser.addOrder(order);
        telegramUserRepository.saveAndFlush(telegramUser);
        Long orderId = order.getId();

        // Get all the telegramUserList where order equals to orderId
        defaultTelegramUserShouldBeFound("orderId.equals=" + orderId);

        // Get all the telegramUserList where order equals to orderId + 1
        defaultTelegramUserShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTelegramUserShouldBeFound(String filter) throws Exception {
        restTelegramUserMockMvc.perform(get("/api/telegram-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telegramUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].chatId").value(hasItem(DEFAULT_CHAT_ID)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].conversationMetaData").value(hasItem(DEFAULT_CONVERSATION_META_DATA)))
            .andExpect(jsonPath("$.[*].orderIdPaused").value(hasItem(DEFAULT_ORDER_ID_PAUSED.intValue())))
            .andExpect(jsonPath("$.[*].orderedFoodIdPaused").value(hasItem(DEFAULT_ORDERED_FOOD_ID_PAUSED.intValue())))
            .andExpect(jsonPath("$.[*].loadedPage").value(hasItem(DEFAULT_LOADED_PAGE)));

        // Check, that the count call also returns 1
        restTelegramUserMockMvc.perform(get("/api/telegram-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTelegramUserShouldNotBeFound(String filter) throws Exception {
        restTelegramUserMockMvc.perform(get("/api/telegram-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTelegramUserMockMvc.perform(get("/api/telegram-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTelegramUser() throws Exception {
        // Get the telegramUser
        restTelegramUserMockMvc.perform(get("/api/telegram-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelegramUser() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        int databaseSizeBeforeUpdate = telegramUserRepository.findAll().size();

        // Update the telegramUser
        TelegramUser updatedTelegramUser = telegramUserRepository.findById(telegramUser.getId()).get();
        // Disconnect from session so that the updates on updatedTelegramUser are not directly saved in db
        em.detach(updatedTelegramUser);
        updatedTelegramUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .userName(UPDATED_USER_NAME)
            .chatId(UPDATED_CHAT_ID)
            .phone(UPDATED_PHONE)
            .conversationMetaData(UPDATED_CONVERSATION_META_DATA)
            .orderIdPaused(UPDATED_ORDER_ID_PAUSED)
            .orderedFoodIdPaused(UPDATED_ORDERED_FOOD_ID_PAUSED)
            .loadedPage(UPDATED_LOADED_PAGE);
        TelegramUserDTO telegramUserDTO = telegramUserMapper.toDto(updatedTelegramUser);

        restTelegramUserMockMvc.perform(put("/api/telegram-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramUserDTO)))
            .andExpect(status().isOk());

        // Validate the TelegramUser in the database
        List<TelegramUser> telegramUserList = telegramUserRepository.findAll();
        assertThat(telegramUserList).hasSize(databaseSizeBeforeUpdate);
        TelegramUser testTelegramUser = telegramUserList.get(telegramUserList.size() - 1);
        assertThat(testTelegramUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTelegramUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTelegramUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testTelegramUser.getChatId()).isEqualTo(UPDATED_CHAT_ID);
        assertThat(testTelegramUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTelegramUser.getConversationMetaData()).isEqualTo(UPDATED_CONVERSATION_META_DATA);
        assertThat(testTelegramUser.getOrderIdPaused()).isEqualTo(UPDATED_ORDER_ID_PAUSED);
        assertThat(testTelegramUser.getOrderedFoodIdPaused()).isEqualTo(UPDATED_ORDERED_FOOD_ID_PAUSED);
        assertThat(testTelegramUser.getLoadedPage()).isEqualTo(UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingTelegramUser() throws Exception {
        int databaseSizeBeforeUpdate = telegramUserRepository.findAll().size();

        // Create the TelegramUser
        TelegramUserDTO telegramUserDTO = telegramUserMapper.toDto(telegramUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTelegramUserMockMvc.perform(put("/api/telegram-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TelegramUser in the database
        List<TelegramUser> telegramUserList = telegramUserRepository.findAll();
        assertThat(telegramUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTelegramUser() throws Exception {
        // Initialize the database
        telegramUserRepository.saveAndFlush(telegramUser);

        int databaseSizeBeforeDelete = telegramUserRepository.findAll().size();

        // Delete the telegramUser
        restTelegramUserMockMvc.perform(delete("/api/telegram-users/{id}", telegramUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TelegramUser> telegramUserList = telegramUserRepository.findAll();
        assertThat(telegramUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
