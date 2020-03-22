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
            .phone(DEFAULT_PHONE);
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
            .phone(UPDATED_PHONE);
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
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
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
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
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
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));

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
            .phone(UPDATED_PHONE);
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
