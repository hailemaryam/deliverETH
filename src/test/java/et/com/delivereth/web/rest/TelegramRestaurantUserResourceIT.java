package et.com.delivereth.web.rest;

import et.com.delivereth.DeliverEthApp;
import et.com.delivereth.domain.TelegramRestaurantUser;
import et.com.delivereth.domain.Order;
import et.com.delivereth.domain.Restorant;
import et.com.delivereth.repository.TelegramRestaurantUserRepository;
import et.com.delivereth.service.TelegramRestaurantUserService;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import et.com.delivereth.service.mapper.TelegramRestaurantUserMapper;
import et.com.delivereth.service.dto.TelegramRestaurantUserCriteria;
import et.com.delivereth.service.TelegramRestaurantUserQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TelegramRestaurantUserResource} REST controller.
 */
@SpringBootTest(classes = DeliverEthApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TelegramRestaurantUserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;
    private static final Integer SMALLER_USER_ID = 1 - 1;

    private static final String DEFAULT_CHAT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CHAT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CONVERSATION_META_DATA = "AAAAAAAAAA";
    private static final String UPDATED_CONVERSATION_META_DATA = "BBBBBBBBBB";

    private static final Integer DEFAULT_LOADED_PAGE = 1;
    private static final Integer UPDATED_LOADED_PAGE = 2;
    private static final Integer SMALLER_LOADED_PAGE = 1 - 1;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Double DEFAULT_CURRENT_BALANCE = 1D;
    private static final Double UPDATED_CURRENT_BALANCE = 2D;
    private static final Double SMALLER_CURRENT_BALANCE = 1D - 1D;

    @Autowired
    private TelegramRestaurantUserRepository telegramRestaurantUserRepository;

    @Mock
    private TelegramRestaurantUserRepository telegramRestaurantUserRepositoryMock;

    @Autowired
    private TelegramRestaurantUserMapper telegramRestaurantUserMapper;

    @Mock
    private TelegramRestaurantUserService telegramRestaurantUserServiceMock;

    @Autowired
    private TelegramRestaurantUserService telegramRestaurantUserService;

    @Autowired
    private TelegramRestaurantUserQueryService telegramRestaurantUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTelegramRestaurantUserMockMvc;

    private TelegramRestaurantUser telegramRestaurantUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TelegramRestaurantUser createEntity(EntityManager em) {
        TelegramRestaurantUser telegramRestaurantUser = new TelegramRestaurantUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .userName(DEFAULT_USER_NAME)
            .userId(DEFAULT_USER_ID)
            .chatId(DEFAULT_CHAT_ID)
            .phone(DEFAULT_PHONE)
            .conversationMetaData(DEFAULT_CONVERSATION_META_DATA)
            .loadedPage(DEFAULT_LOADED_PAGE)
            .status(DEFAULT_STATUS)
            .currentBalance(DEFAULT_CURRENT_BALANCE);
        return telegramRestaurantUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TelegramRestaurantUser createUpdatedEntity(EntityManager em) {
        TelegramRestaurantUser telegramRestaurantUser = new TelegramRestaurantUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .userName(UPDATED_USER_NAME)
            .userId(UPDATED_USER_ID)
            .chatId(UPDATED_CHAT_ID)
            .phone(UPDATED_PHONE)
            .conversationMetaData(UPDATED_CONVERSATION_META_DATA)
            .loadedPage(UPDATED_LOADED_PAGE)
            .status(UPDATED_STATUS)
            .currentBalance(UPDATED_CURRENT_BALANCE);
        return telegramRestaurantUser;
    }

    @BeforeEach
    public void initTest() {
        telegramRestaurantUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createTelegramRestaurantUser() throws Exception {
        int databaseSizeBeforeCreate = telegramRestaurantUserRepository.findAll().size();

        // Create the TelegramRestaurantUser
        TelegramRestaurantUserDTO telegramRestaurantUserDTO = telegramRestaurantUserMapper.toDto(telegramRestaurantUser);
        restTelegramRestaurantUserMockMvc.perform(post("/api/telegram-restaurant-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramRestaurantUserDTO)))
            .andExpect(status().isCreated());

        // Validate the TelegramRestaurantUser in the database
        List<TelegramRestaurantUser> telegramRestaurantUserList = telegramRestaurantUserRepository.findAll();
        assertThat(telegramRestaurantUserList).hasSize(databaseSizeBeforeCreate + 1);
        TelegramRestaurantUser testTelegramRestaurantUser = telegramRestaurantUserList.get(telegramRestaurantUserList.size() - 1);
        assertThat(testTelegramRestaurantUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTelegramRestaurantUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTelegramRestaurantUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testTelegramRestaurantUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testTelegramRestaurantUser.getChatId()).isEqualTo(DEFAULT_CHAT_ID);
        assertThat(testTelegramRestaurantUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testTelegramRestaurantUser.getConversationMetaData()).isEqualTo(DEFAULT_CONVERSATION_META_DATA);
        assertThat(testTelegramRestaurantUser.getLoadedPage()).isEqualTo(DEFAULT_LOADED_PAGE);
        assertThat(testTelegramRestaurantUser.isStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTelegramRestaurantUser.getCurrentBalance()).isEqualTo(DEFAULT_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    public void createTelegramRestaurantUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = telegramRestaurantUserRepository.findAll().size();

        // Create the TelegramRestaurantUser with an existing ID
        telegramRestaurantUser.setId(1L);
        TelegramRestaurantUserDTO telegramRestaurantUserDTO = telegramRestaurantUserMapper.toDto(telegramRestaurantUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelegramRestaurantUserMockMvc.perform(post("/api/telegram-restaurant-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramRestaurantUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TelegramRestaurantUser in the database
        List<TelegramRestaurantUser> telegramRestaurantUserList = telegramRestaurantUserRepository.findAll();
        assertThat(telegramRestaurantUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsers() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList
        restTelegramRestaurantUserMockMvc.perform(get("/api/telegram-restaurant-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telegramRestaurantUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].chatId").value(hasItem(DEFAULT_CHAT_ID)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].conversationMetaData").value(hasItem(DEFAULT_CONVERSATION_META_DATA)))
            .andExpect(jsonPath("$.[*].loadedPage").value(hasItem(DEFAULT_LOADED_PAGE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].currentBalance").value(hasItem(DEFAULT_CURRENT_BALANCE.doubleValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTelegramRestaurantUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(telegramRestaurantUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTelegramRestaurantUserMockMvc.perform(get("/api/telegram-restaurant-users?eagerload=true"))
            .andExpect(status().isOk());

        verify(telegramRestaurantUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTelegramRestaurantUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(telegramRestaurantUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTelegramRestaurantUserMockMvc.perform(get("/api/telegram-restaurant-users?eagerload=true"))
            .andExpect(status().isOk());

        verify(telegramRestaurantUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTelegramRestaurantUser() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get the telegramRestaurantUser
        restTelegramRestaurantUserMockMvc.perform(get("/api/telegram-restaurant-users/{id}", telegramRestaurantUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(telegramRestaurantUser.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.chatId").value(DEFAULT_CHAT_ID))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.conversationMetaData").value(DEFAULT_CONVERSATION_META_DATA))
            .andExpect(jsonPath("$.loadedPage").value(DEFAULT_LOADED_PAGE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.currentBalance").value(DEFAULT_CURRENT_BALANCE.doubleValue()));
    }


    @Test
    @Transactional
    public void getTelegramRestaurantUsersByIdFiltering() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        Long id = telegramRestaurantUser.getId();

        defaultTelegramRestaurantUserShouldBeFound("id.equals=" + id);
        defaultTelegramRestaurantUserShouldNotBeFound("id.notEquals=" + id);

        defaultTelegramRestaurantUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTelegramRestaurantUserShouldNotBeFound("id.greaterThan=" + id);

        defaultTelegramRestaurantUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTelegramRestaurantUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultTelegramRestaurantUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the telegramRestaurantUserList where firstName equals to UPDATED_FIRST_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where firstName not equals to DEFAULT_FIRST_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the telegramRestaurantUserList where firstName not equals to UPDATED_FIRST_NAME
        defaultTelegramRestaurantUserShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultTelegramRestaurantUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the telegramRestaurantUserList where firstName equals to UPDATED_FIRST_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where firstName is not null
        defaultTelegramRestaurantUserShouldBeFound("firstName.specified=true");

        // Get all the telegramRestaurantUserList where firstName is null
        defaultTelegramRestaurantUserShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where firstName contains DEFAULT_FIRST_NAME
        defaultTelegramRestaurantUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the telegramRestaurantUserList where firstName contains UPDATED_FIRST_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the telegramRestaurantUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultTelegramRestaurantUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where lastName equals to DEFAULT_LAST_NAME
        defaultTelegramRestaurantUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the telegramRestaurantUserList where lastName equals to UPDATED_LAST_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where lastName not equals to DEFAULT_LAST_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the telegramRestaurantUserList where lastName not equals to UPDATED_LAST_NAME
        defaultTelegramRestaurantUserShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultTelegramRestaurantUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the telegramRestaurantUserList where lastName equals to UPDATED_LAST_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where lastName is not null
        defaultTelegramRestaurantUserShouldBeFound("lastName.specified=true");

        // Get all the telegramRestaurantUserList where lastName is null
        defaultTelegramRestaurantUserShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where lastName contains DEFAULT_LAST_NAME
        defaultTelegramRestaurantUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the telegramRestaurantUserList where lastName contains UPDATED_LAST_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the telegramRestaurantUserList where lastName does not contain UPDATED_LAST_NAME
        defaultTelegramRestaurantUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userName equals to DEFAULT_USER_NAME
        defaultTelegramRestaurantUserShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the telegramRestaurantUserList where userName equals to UPDATED_USER_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userName not equals to DEFAULT_USER_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("userName.notEquals=" + DEFAULT_USER_NAME);

        // Get all the telegramRestaurantUserList where userName not equals to UPDATED_USER_NAME
        defaultTelegramRestaurantUserShouldBeFound("userName.notEquals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultTelegramRestaurantUserShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the telegramRestaurantUserList where userName equals to UPDATED_USER_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userName is not null
        defaultTelegramRestaurantUserShouldBeFound("userName.specified=true");

        // Get all the telegramRestaurantUserList where userName is null
        defaultTelegramRestaurantUserShouldNotBeFound("userName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserNameContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userName contains DEFAULT_USER_NAME
        defaultTelegramRestaurantUserShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the telegramRestaurantUserList where userName contains UPDATED_USER_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userName does not contain DEFAULT_USER_NAME
        defaultTelegramRestaurantUserShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the telegramRestaurantUserList where userName does not contain UPDATED_USER_NAME
        defaultTelegramRestaurantUserShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userId equals to DEFAULT_USER_ID
        defaultTelegramRestaurantUserShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the telegramRestaurantUserList where userId equals to UPDATED_USER_ID
        defaultTelegramRestaurantUserShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userId not equals to DEFAULT_USER_ID
        defaultTelegramRestaurantUserShouldNotBeFound("userId.notEquals=" + DEFAULT_USER_ID);

        // Get all the telegramRestaurantUserList where userId not equals to UPDATED_USER_ID
        defaultTelegramRestaurantUserShouldBeFound("userId.notEquals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultTelegramRestaurantUserShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the telegramRestaurantUserList where userId equals to UPDATED_USER_ID
        defaultTelegramRestaurantUserShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userId is not null
        defaultTelegramRestaurantUserShouldBeFound("userId.specified=true");

        // Get all the telegramRestaurantUserList where userId is null
        defaultTelegramRestaurantUserShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userId is greater than or equal to DEFAULT_USER_ID
        defaultTelegramRestaurantUserShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the telegramRestaurantUserList where userId is greater than or equal to UPDATED_USER_ID
        defaultTelegramRestaurantUserShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userId is less than or equal to DEFAULT_USER_ID
        defaultTelegramRestaurantUserShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the telegramRestaurantUserList where userId is less than or equal to SMALLER_USER_ID
        defaultTelegramRestaurantUserShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userId is less than DEFAULT_USER_ID
        defaultTelegramRestaurantUserShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the telegramRestaurantUserList where userId is less than UPDATED_USER_ID
        defaultTelegramRestaurantUserShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where userId is greater than DEFAULT_USER_ID
        defaultTelegramRestaurantUserShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the telegramRestaurantUserList where userId is greater than SMALLER_USER_ID
        defaultTelegramRestaurantUserShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByChatIdIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where chatId equals to DEFAULT_CHAT_ID
        defaultTelegramRestaurantUserShouldBeFound("chatId.equals=" + DEFAULT_CHAT_ID);

        // Get all the telegramRestaurantUserList where chatId equals to UPDATED_CHAT_ID
        defaultTelegramRestaurantUserShouldNotBeFound("chatId.equals=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByChatIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where chatId not equals to DEFAULT_CHAT_ID
        defaultTelegramRestaurantUserShouldNotBeFound("chatId.notEquals=" + DEFAULT_CHAT_ID);

        // Get all the telegramRestaurantUserList where chatId not equals to UPDATED_CHAT_ID
        defaultTelegramRestaurantUserShouldBeFound("chatId.notEquals=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByChatIdIsInShouldWork() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where chatId in DEFAULT_CHAT_ID or UPDATED_CHAT_ID
        defaultTelegramRestaurantUserShouldBeFound("chatId.in=" + DEFAULT_CHAT_ID + "," + UPDATED_CHAT_ID);

        // Get all the telegramRestaurantUserList where chatId equals to UPDATED_CHAT_ID
        defaultTelegramRestaurantUserShouldNotBeFound("chatId.in=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByChatIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where chatId is not null
        defaultTelegramRestaurantUserShouldBeFound("chatId.specified=true");

        // Get all the telegramRestaurantUserList where chatId is null
        defaultTelegramRestaurantUserShouldNotBeFound("chatId.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByChatIdContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where chatId contains DEFAULT_CHAT_ID
        defaultTelegramRestaurantUserShouldBeFound("chatId.contains=" + DEFAULT_CHAT_ID);

        // Get all the telegramRestaurantUserList where chatId contains UPDATED_CHAT_ID
        defaultTelegramRestaurantUserShouldNotBeFound("chatId.contains=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByChatIdNotContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where chatId does not contain DEFAULT_CHAT_ID
        defaultTelegramRestaurantUserShouldNotBeFound("chatId.doesNotContain=" + DEFAULT_CHAT_ID);

        // Get all the telegramRestaurantUserList where chatId does not contain UPDATED_CHAT_ID
        defaultTelegramRestaurantUserShouldBeFound("chatId.doesNotContain=" + UPDATED_CHAT_ID);
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where phone equals to DEFAULT_PHONE
        defaultTelegramRestaurantUserShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the telegramRestaurantUserList where phone equals to UPDATED_PHONE
        defaultTelegramRestaurantUserShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where phone not equals to DEFAULT_PHONE
        defaultTelegramRestaurantUserShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the telegramRestaurantUserList where phone not equals to UPDATED_PHONE
        defaultTelegramRestaurantUserShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultTelegramRestaurantUserShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the telegramRestaurantUserList where phone equals to UPDATED_PHONE
        defaultTelegramRestaurantUserShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where phone is not null
        defaultTelegramRestaurantUserShouldBeFound("phone.specified=true");

        // Get all the telegramRestaurantUserList where phone is null
        defaultTelegramRestaurantUserShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where phone contains DEFAULT_PHONE
        defaultTelegramRestaurantUserShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the telegramRestaurantUserList where phone contains UPDATED_PHONE
        defaultTelegramRestaurantUserShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where phone does not contain DEFAULT_PHONE
        defaultTelegramRestaurantUserShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the telegramRestaurantUserList where phone does not contain UPDATED_PHONE
        defaultTelegramRestaurantUserShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByConversationMetaDataIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where conversationMetaData equals to DEFAULT_CONVERSATION_META_DATA
        defaultTelegramRestaurantUserShouldBeFound("conversationMetaData.equals=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramRestaurantUserList where conversationMetaData equals to UPDATED_CONVERSATION_META_DATA
        defaultTelegramRestaurantUserShouldNotBeFound("conversationMetaData.equals=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByConversationMetaDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where conversationMetaData not equals to DEFAULT_CONVERSATION_META_DATA
        defaultTelegramRestaurantUserShouldNotBeFound("conversationMetaData.notEquals=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramRestaurantUserList where conversationMetaData not equals to UPDATED_CONVERSATION_META_DATA
        defaultTelegramRestaurantUserShouldBeFound("conversationMetaData.notEquals=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByConversationMetaDataIsInShouldWork() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where conversationMetaData in DEFAULT_CONVERSATION_META_DATA or UPDATED_CONVERSATION_META_DATA
        defaultTelegramRestaurantUserShouldBeFound("conversationMetaData.in=" + DEFAULT_CONVERSATION_META_DATA + "," + UPDATED_CONVERSATION_META_DATA);

        // Get all the telegramRestaurantUserList where conversationMetaData equals to UPDATED_CONVERSATION_META_DATA
        defaultTelegramRestaurantUserShouldNotBeFound("conversationMetaData.in=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByConversationMetaDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where conversationMetaData is not null
        defaultTelegramRestaurantUserShouldBeFound("conversationMetaData.specified=true");

        // Get all the telegramRestaurantUserList where conversationMetaData is null
        defaultTelegramRestaurantUserShouldNotBeFound("conversationMetaData.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByConversationMetaDataContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where conversationMetaData contains DEFAULT_CONVERSATION_META_DATA
        defaultTelegramRestaurantUserShouldBeFound("conversationMetaData.contains=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramRestaurantUserList where conversationMetaData contains UPDATED_CONVERSATION_META_DATA
        defaultTelegramRestaurantUserShouldNotBeFound("conversationMetaData.contains=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByConversationMetaDataNotContainsSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where conversationMetaData does not contain DEFAULT_CONVERSATION_META_DATA
        defaultTelegramRestaurantUserShouldNotBeFound("conversationMetaData.doesNotContain=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramRestaurantUserList where conversationMetaData does not contain UPDATED_CONVERSATION_META_DATA
        defaultTelegramRestaurantUserShouldBeFound("conversationMetaData.doesNotContain=" + UPDATED_CONVERSATION_META_DATA);
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLoadedPageIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where loadedPage equals to DEFAULT_LOADED_PAGE
        defaultTelegramRestaurantUserShouldBeFound("loadedPage.equals=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramRestaurantUserList where loadedPage equals to UPDATED_LOADED_PAGE
        defaultTelegramRestaurantUserShouldNotBeFound("loadedPage.equals=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLoadedPageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where loadedPage not equals to DEFAULT_LOADED_PAGE
        defaultTelegramRestaurantUserShouldNotBeFound("loadedPage.notEquals=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramRestaurantUserList where loadedPage not equals to UPDATED_LOADED_PAGE
        defaultTelegramRestaurantUserShouldBeFound("loadedPage.notEquals=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLoadedPageIsInShouldWork() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where loadedPage in DEFAULT_LOADED_PAGE or UPDATED_LOADED_PAGE
        defaultTelegramRestaurantUserShouldBeFound("loadedPage.in=" + DEFAULT_LOADED_PAGE + "," + UPDATED_LOADED_PAGE);

        // Get all the telegramRestaurantUserList where loadedPage equals to UPDATED_LOADED_PAGE
        defaultTelegramRestaurantUserShouldNotBeFound("loadedPage.in=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLoadedPageIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where loadedPage is not null
        defaultTelegramRestaurantUserShouldBeFound("loadedPage.specified=true");

        // Get all the telegramRestaurantUserList where loadedPage is null
        defaultTelegramRestaurantUserShouldNotBeFound("loadedPage.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLoadedPageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where loadedPage is greater than or equal to DEFAULT_LOADED_PAGE
        defaultTelegramRestaurantUserShouldBeFound("loadedPage.greaterThanOrEqual=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramRestaurantUserList where loadedPage is greater than or equal to UPDATED_LOADED_PAGE
        defaultTelegramRestaurantUserShouldNotBeFound("loadedPage.greaterThanOrEqual=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLoadedPageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where loadedPage is less than or equal to DEFAULT_LOADED_PAGE
        defaultTelegramRestaurantUserShouldBeFound("loadedPage.lessThanOrEqual=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramRestaurantUserList where loadedPage is less than or equal to SMALLER_LOADED_PAGE
        defaultTelegramRestaurantUserShouldNotBeFound("loadedPage.lessThanOrEqual=" + SMALLER_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLoadedPageIsLessThanSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where loadedPage is less than DEFAULT_LOADED_PAGE
        defaultTelegramRestaurantUserShouldNotBeFound("loadedPage.lessThan=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramRestaurantUserList where loadedPage is less than UPDATED_LOADED_PAGE
        defaultTelegramRestaurantUserShouldBeFound("loadedPage.lessThan=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByLoadedPageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where loadedPage is greater than DEFAULT_LOADED_PAGE
        defaultTelegramRestaurantUserShouldNotBeFound("loadedPage.greaterThan=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramRestaurantUserList where loadedPage is greater than SMALLER_LOADED_PAGE
        defaultTelegramRestaurantUserShouldBeFound("loadedPage.greaterThan=" + SMALLER_LOADED_PAGE);
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where status equals to DEFAULT_STATUS
        defaultTelegramRestaurantUserShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the telegramRestaurantUserList where status equals to UPDATED_STATUS
        defaultTelegramRestaurantUserShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where status not equals to DEFAULT_STATUS
        defaultTelegramRestaurantUserShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the telegramRestaurantUserList where status not equals to UPDATED_STATUS
        defaultTelegramRestaurantUserShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTelegramRestaurantUserShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the telegramRestaurantUserList where status equals to UPDATED_STATUS
        defaultTelegramRestaurantUserShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where status is not null
        defaultTelegramRestaurantUserShouldBeFound("status.specified=true");

        // Get all the telegramRestaurantUserList where status is null
        defaultTelegramRestaurantUserShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByCurrentBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where currentBalance equals to DEFAULT_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldBeFound("currentBalance.equals=" + DEFAULT_CURRENT_BALANCE);

        // Get all the telegramRestaurantUserList where currentBalance equals to UPDATED_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldNotBeFound("currentBalance.equals=" + UPDATED_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByCurrentBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where currentBalance not equals to DEFAULT_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldNotBeFound("currentBalance.notEquals=" + DEFAULT_CURRENT_BALANCE);

        // Get all the telegramRestaurantUserList where currentBalance not equals to UPDATED_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldBeFound("currentBalance.notEquals=" + UPDATED_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByCurrentBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where currentBalance in DEFAULT_CURRENT_BALANCE or UPDATED_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldBeFound("currentBalance.in=" + DEFAULT_CURRENT_BALANCE + "," + UPDATED_CURRENT_BALANCE);

        // Get all the telegramRestaurantUserList where currentBalance equals to UPDATED_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldNotBeFound("currentBalance.in=" + UPDATED_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByCurrentBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where currentBalance is not null
        defaultTelegramRestaurantUserShouldBeFound("currentBalance.specified=true");

        // Get all the telegramRestaurantUserList where currentBalance is null
        defaultTelegramRestaurantUserShouldNotBeFound("currentBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByCurrentBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where currentBalance is greater than or equal to DEFAULT_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldBeFound("currentBalance.greaterThanOrEqual=" + DEFAULT_CURRENT_BALANCE);

        // Get all the telegramRestaurantUserList where currentBalance is greater than or equal to UPDATED_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldNotBeFound("currentBalance.greaterThanOrEqual=" + UPDATED_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByCurrentBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where currentBalance is less than or equal to DEFAULT_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldBeFound("currentBalance.lessThanOrEqual=" + DEFAULT_CURRENT_BALANCE);

        // Get all the telegramRestaurantUserList where currentBalance is less than or equal to SMALLER_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldNotBeFound("currentBalance.lessThanOrEqual=" + SMALLER_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByCurrentBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where currentBalance is less than DEFAULT_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldNotBeFound("currentBalance.lessThan=" + DEFAULT_CURRENT_BALANCE);

        // Get all the telegramRestaurantUserList where currentBalance is less than UPDATED_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldBeFound("currentBalance.lessThan=" + UPDATED_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByCurrentBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        // Get all the telegramRestaurantUserList where currentBalance is greater than DEFAULT_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldNotBeFound("currentBalance.greaterThan=" + DEFAULT_CURRENT_BALANCE);

        // Get all the telegramRestaurantUserList where currentBalance is greater than SMALLER_CURRENT_BALANCE
        defaultTelegramRestaurantUserShouldBeFound("currentBalance.greaterThan=" + SMALLER_CURRENT_BALANCE);
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);
        Order order = OrderResourceIT.createEntity(em);
        em.persist(order);
        em.flush();
        telegramRestaurantUser.addOrder(order);
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);
        Long orderId = order.getId();

        // Get all the telegramRestaurantUserList where order equals to orderId
        defaultTelegramRestaurantUserShouldBeFound("orderId.equals=" + orderId);

        // Get all the telegramRestaurantUserList where order equals to orderId + 1
        defaultTelegramRestaurantUserShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }


    @Test
    @Transactional
    public void getAllTelegramRestaurantUsersByRestorantIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);
        Restorant restorant = RestorantResourceIT.createEntity(em);
        em.persist(restorant);
        em.flush();
        telegramRestaurantUser.addRestorant(restorant);
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);
        Long restorantId = restorant.getId();

        // Get all the telegramRestaurantUserList where restorant equals to restorantId
        defaultTelegramRestaurantUserShouldBeFound("restorantId.equals=" + restorantId);

        // Get all the telegramRestaurantUserList where restorant equals to restorantId + 1
        defaultTelegramRestaurantUserShouldNotBeFound("restorantId.equals=" + (restorantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTelegramRestaurantUserShouldBeFound(String filter) throws Exception {
        restTelegramRestaurantUserMockMvc.perform(get("/api/telegram-restaurant-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telegramRestaurantUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].chatId").value(hasItem(DEFAULT_CHAT_ID)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].conversationMetaData").value(hasItem(DEFAULT_CONVERSATION_META_DATA)))
            .andExpect(jsonPath("$.[*].loadedPage").value(hasItem(DEFAULT_LOADED_PAGE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].currentBalance").value(hasItem(DEFAULT_CURRENT_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restTelegramRestaurantUserMockMvc.perform(get("/api/telegram-restaurant-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTelegramRestaurantUserShouldNotBeFound(String filter) throws Exception {
        restTelegramRestaurantUserMockMvc.perform(get("/api/telegram-restaurant-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTelegramRestaurantUserMockMvc.perform(get("/api/telegram-restaurant-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTelegramRestaurantUser() throws Exception {
        // Get the telegramRestaurantUser
        restTelegramRestaurantUserMockMvc.perform(get("/api/telegram-restaurant-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelegramRestaurantUser() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        int databaseSizeBeforeUpdate = telegramRestaurantUserRepository.findAll().size();

        // Update the telegramRestaurantUser
        TelegramRestaurantUser updatedTelegramRestaurantUser = telegramRestaurantUserRepository.findById(telegramRestaurantUser.getId()).get();
        // Disconnect from session so that the updates on updatedTelegramRestaurantUser are not directly saved in db
        em.detach(updatedTelegramRestaurantUser);
        updatedTelegramRestaurantUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .userName(UPDATED_USER_NAME)
            .userId(UPDATED_USER_ID)
            .chatId(UPDATED_CHAT_ID)
            .phone(UPDATED_PHONE)
            .conversationMetaData(UPDATED_CONVERSATION_META_DATA)
            .loadedPage(UPDATED_LOADED_PAGE)
            .status(UPDATED_STATUS)
            .currentBalance(UPDATED_CURRENT_BALANCE);
        TelegramRestaurantUserDTO telegramRestaurantUserDTO = telegramRestaurantUserMapper.toDto(updatedTelegramRestaurantUser);

        restTelegramRestaurantUserMockMvc.perform(put("/api/telegram-restaurant-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramRestaurantUserDTO)))
            .andExpect(status().isOk());

        // Validate the TelegramRestaurantUser in the database
        List<TelegramRestaurantUser> telegramRestaurantUserList = telegramRestaurantUserRepository.findAll();
        assertThat(telegramRestaurantUserList).hasSize(databaseSizeBeforeUpdate);
        TelegramRestaurantUser testTelegramRestaurantUser = telegramRestaurantUserList.get(telegramRestaurantUserList.size() - 1);
        assertThat(testTelegramRestaurantUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTelegramRestaurantUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTelegramRestaurantUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testTelegramRestaurantUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testTelegramRestaurantUser.getChatId()).isEqualTo(UPDATED_CHAT_ID);
        assertThat(testTelegramRestaurantUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTelegramRestaurantUser.getConversationMetaData()).isEqualTo(UPDATED_CONVERSATION_META_DATA);
        assertThat(testTelegramRestaurantUser.getLoadedPage()).isEqualTo(UPDATED_LOADED_PAGE);
        assertThat(testTelegramRestaurantUser.isStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTelegramRestaurantUser.getCurrentBalance()).isEqualTo(UPDATED_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingTelegramRestaurantUser() throws Exception {
        int databaseSizeBeforeUpdate = telegramRestaurantUserRepository.findAll().size();

        // Create the TelegramRestaurantUser
        TelegramRestaurantUserDTO telegramRestaurantUserDTO = telegramRestaurantUserMapper.toDto(telegramRestaurantUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTelegramRestaurantUserMockMvc.perform(put("/api/telegram-restaurant-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramRestaurantUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TelegramRestaurantUser in the database
        List<TelegramRestaurantUser> telegramRestaurantUserList = telegramRestaurantUserRepository.findAll();
        assertThat(telegramRestaurantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTelegramRestaurantUser() throws Exception {
        // Initialize the database
        telegramRestaurantUserRepository.saveAndFlush(telegramRestaurantUser);

        int databaseSizeBeforeDelete = telegramRestaurantUserRepository.findAll().size();

        // Delete the telegramRestaurantUser
        restTelegramRestaurantUserMockMvc.perform(delete("/api/telegram-restaurant-users/{id}", telegramRestaurantUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TelegramRestaurantUser> telegramRestaurantUserList = telegramRestaurantUserRepository.findAll();
        assertThat(telegramRestaurantUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
