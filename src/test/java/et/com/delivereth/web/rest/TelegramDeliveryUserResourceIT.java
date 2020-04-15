package et.com.delivereth.web.rest;

import et.com.delivereth.DeliverEthApp;
import et.com.delivereth.domain.TelegramDeliveryUser;
import et.com.delivereth.domain.Order;
import et.com.delivereth.domain.Restorant;
import et.com.delivereth.repository.TelegramDeliveryUserRepository;
import et.com.delivereth.service.TelegramDeliveryUserService;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.mapper.TelegramDeliveryUserMapper;
import et.com.delivereth.service.dto.TelegramDeliveryUserCriteria;
import et.com.delivereth.service.TelegramDeliveryUserQueryService;

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
 * Integration tests for the {@link TelegramDeliveryUserResource} REST controller.
 */
@SpringBootTest(classes = DeliverEthApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TelegramDeliveryUserResourceIT {

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

    @Autowired
    private TelegramDeliveryUserRepository telegramDeliveryUserRepository;

    @Mock
    private TelegramDeliveryUserRepository telegramDeliveryUserRepositoryMock;

    @Autowired
    private TelegramDeliveryUserMapper telegramDeliveryUserMapper;

    @Mock
    private TelegramDeliveryUserService telegramDeliveryUserServiceMock;

    @Autowired
    private TelegramDeliveryUserService telegramDeliveryUserService;

    @Autowired
    private TelegramDeliveryUserQueryService telegramDeliveryUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTelegramDeliveryUserMockMvc;

    private TelegramDeliveryUser telegramDeliveryUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TelegramDeliveryUser createEntity(EntityManager em) {
        TelegramDeliveryUser telegramDeliveryUser = new TelegramDeliveryUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .userName(DEFAULT_USER_NAME)
            .userId(DEFAULT_USER_ID)
            .chatId(DEFAULT_CHAT_ID)
            .phone(DEFAULT_PHONE)
            .conversationMetaData(DEFAULT_CONVERSATION_META_DATA)
            .loadedPage(DEFAULT_LOADED_PAGE);
        return telegramDeliveryUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TelegramDeliveryUser createUpdatedEntity(EntityManager em) {
        TelegramDeliveryUser telegramDeliveryUser = new TelegramDeliveryUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .userName(UPDATED_USER_NAME)
            .userId(UPDATED_USER_ID)
            .chatId(UPDATED_CHAT_ID)
            .phone(UPDATED_PHONE)
            .conversationMetaData(UPDATED_CONVERSATION_META_DATA)
            .loadedPage(UPDATED_LOADED_PAGE);
        return telegramDeliveryUser;
    }

    @BeforeEach
    public void initTest() {
        telegramDeliveryUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createTelegramDeliveryUser() throws Exception {
        int databaseSizeBeforeCreate = telegramDeliveryUserRepository.findAll().size();

        // Create the TelegramDeliveryUser
        TelegramDeliveryUserDTO telegramDeliveryUserDTO = telegramDeliveryUserMapper.toDto(telegramDeliveryUser);
        restTelegramDeliveryUserMockMvc.perform(post("/api/telegram-delivery-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramDeliveryUserDTO)))
            .andExpect(status().isCreated());

        // Validate the TelegramDeliveryUser in the database
        List<TelegramDeliveryUser> telegramDeliveryUserList = telegramDeliveryUserRepository.findAll();
        assertThat(telegramDeliveryUserList).hasSize(databaseSizeBeforeCreate + 1);
        TelegramDeliveryUser testTelegramDeliveryUser = telegramDeliveryUserList.get(telegramDeliveryUserList.size() - 1);
        assertThat(testTelegramDeliveryUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTelegramDeliveryUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTelegramDeliveryUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testTelegramDeliveryUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testTelegramDeliveryUser.getChatId()).isEqualTo(DEFAULT_CHAT_ID);
        assertThat(testTelegramDeliveryUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testTelegramDeliveryUser.getConversationMetaData()).isEqualTo(DEFAULT_CONVERSATION_META_DATA);
        assertThat(testTelegramDeliveryUser.getLoadedPage()).isEqualTo(DEFAULT_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void createTelegramDeliveryUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = telegramDeliveryUserRepository.findAll().size();

        // Create the TelegramDeliveryUser with an existing ID
        telegramDeliveryUser.setId(1L);
        TelegramDeliveryUserDTO telegramDeliveryUserDTO = telegramDeliveryUserMapper.toDto(telegramDeliveryUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelegramDeliveryUserMockMvc.perform(post("/api/telegram-delivery-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramDeliveryUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TelegramDeliveryUser in the database
        List<TelegramDeliveryUser> telegramDeliveryUserList = telegramDeliveryUserRepository.findAll();
        assertThat(telegramDeliveryUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTelegramDeliveryUsers() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList
        restTelegramDeliveryUserMockMvc.perform(get("/api/telegram-delivery-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telegramDeliveryUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].chatId").value(hasItem(DEFAULT_CHAT_ID)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].conversationMetaData").value(hasItem(DEFAULT_CONVERSATION_META_DATA)))
            .andExpect(jsonPath("$.[*].loadedPage").value(hasItem(DEFAULT_LOADED_PAGE)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTelegramDeliveryUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(telegramDeliveryUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTelegramDeliveryUserMockMvc.perform(get("/api/telegram-delivery-users?eagerload=true"))
            .andExpect(status().isOk());

        verify(telegramDeliveryUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTelegramDeliveryUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(telegramDeliveryUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTelegramDeliveryUserMockMvc.perform(get("/api/telegram-delivery-users?eagerload=true"))
            .andExpect(status().isOk());

        verify(telegramDeliveryUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTelegramDeliveryUser() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get the telegramDeliveryUser
        restTelegramDeliveryUserMockMvc.perform(get("/api/telegram-delivery-users/{id}", telegramDeliveryUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(telegramDeliveryUser.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.chatId").value(DEFAULT_CHAT_ID))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.conversationMetaData").value(DEFAULT_CONVERSATION_META_DATA))
            .andExpect(jsonPath("$.loadedPage").value(DEFAULT_LOADED_PAGE));
    }


    @Test
    @Transactional
    public void getTelegramDeliveryUsersByIdFiltering() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        Long id = telegramDeliveryUser.getId();

        defaultTelegramDeliveryUserShouldBeFound("id.equals=" + id);
        defaultTelegramDeliveryUserShouldNotBeFound("id.notEquals=" + id);

        defaultTelegramDeliveryUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTelegramDeliveryUserShouldNotBeFound("id.greaterThan=" + id);

        defaultTelegramDeliveryUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTelegramDeliveryUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultTelegramDeliveryUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the telegramDeliveryUserList where firstName equals to UPDATED_FIRST_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where firstName not equals to DEFAULT_FIRST_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the telegramDeliveryUserList where firstName not equals to UPDATED_FIRST_NAME
        defaultTelegramDeliveryUserShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultTelegramDeliveryUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the telegramDeliveryUserList where firstName equals to UPDATED_FIRST_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where firstName is not null
        defaultTelegramDeliveryUserShouldBeFound("firstName.specified=true");

        // Get all the telegramDeliveryUserList where firstName is null
        defaultTelegramDeliveryUserShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where firstName contains DEFAULT_FIRST_NAME
        defaultTelegramDeliveryUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the telegramDeliveryUserList where firstName contains UPDATED_FIRST_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the telegramDeliveryUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultTelegramDeliveryUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where lastName equals to DEFAULT_LAST_NAME
        defaultTelegramDeliveryUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the telegramDeliveryUserList where lastName equals to UPDATED_LAST_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where lastName not equals to DEFAULT_LAST_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the telegramDeliveryUserList where lastName not equals to UPDATED_LAST_NAME
        defaultTelegramDeliveryUserShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultTelegramDeliveryUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the telegramDeliveryUserList where lastName equals to UPDATED_LAST_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where lastName is not null
        defaultTelegramDeliveryUserShouldBeFound("lastName.specified=true");

        // Get all the telegramDeliveryUserList where lastName is null
        defaultTelegramDeliveryUserShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where lastName contains DEFAULT_LAST_NAME
        defaultTelegramDeliveryUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the telegramDeliveryUserList where lastName contains UPDATED_LAST_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the telegramDeliveryUserList where lastName does not contain UPDATED_LAST_NAME
        defaultTelegramDeliveryUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userName equals to DEFAULT_USER_NAME
        defaultTelegramDeliveryUserShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the telegramDeliveryUserList where userName equals to UPDATED_USER_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userName not equals to DEFAULT_USER_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("userName.notEquals=" + DEFAULT_USER_NAME);

        // Get all the telegramDeliveryUserList where userName not equals to UPDATED_USER_NAME
        defaultTelegramDeliveryUserShouldBeFound("userName.notEquals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultTelegramDeliveryUserShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the telegramDeliveryUserList where userName equals to UPDATED_USER_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userName is not null
        defaultTelegramDeliveryUserShouldBeFound("userName.specified=true");

        // Get all the telegramDeliveryUserList where userName is null
        defaultTelegramDeliveryUserShouldNotBeFound("userName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserNameContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userName contains DEFAULT_USER_NAME
        defaultTelegramDeliveryUserShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the telegramDeliveryUserList where userName contains UPDATED_USER_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userName does not contain DEFAULT_USER_NAME
        defaultTelegramDeliveryUserShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the telegramDeliveryUserList where userName does not contain UPDATED_USER_NAME
        defaultTelegramDeliveryUserShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }


    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userId equals to DEFAULT_USER_ID
        defaultTelegramDeliveryUserShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the telegramDeliveryUserList where userId equals to UPDATED_USER_ID
        defaultTelegramDeliveryUserShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userId not equals to DEFAULT_USER_ID
        defaultTelegramDeliveryUserShouldNotBeFound("userId.notEquals=" + DEFAULT_USER_ID);

        // Get all the telegramDeliveryUserList where userId not equals to UPDATED_USER_ID
        defaultTelegramDeliveryUserShouldBeFound("userId.notEquals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultTelegramDeliveryUserShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the telegramDeliveryUserList where userId equals to UPDATED_USER_ID
        defaultTelegramDeliveryUserShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userId is not null
        defaultTelegramDeliveryUserShouldBeFound("userId.specified=true");

        // Get all the telegramDeliveryUserList where userId is null
        defaultTelegramDeliveryUserShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userId is greater than or equal to DEFAULT_USER_ID
        defaultTelegramDeliveryUserShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the telegramDeliveryUserList where userId is greater than or equal to UPDATED_USER_ID
        defaultTelegramDeliveryUserShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userId is less than or equal to DEFAULT_USER_ID
        defaultTelegramDeliveryUserShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the telegramDeliveryUserList where userId is less than or equal to SMALLER_USER_ID
        defaultTelegramDeliveryUserShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userId is less than DEFAULT_USER_ID
        defaultTelegramDeliveryUserShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the telegramDeliveryUserList where userId is less than UPDATED_USER_ID
        defaultTelegramDeliveryUserShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where userId is greater than DEFAULT_USER_ID
        defaultTelegramDeliveryUserShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the telegramDeliveryUserList where userId is greater than SMALLER_USER_ID
        defaultTelegramDeliveryUserShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByChatIdIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where chatId equals to DEFAULT_CHAT_ID
        defaultTelegramDeliveryUserShouldBeFound("chatId.equals=" + DEFAULT_CHAT_ID);

        // Get all the telegramDeliveryUserList where chatId equals to UPDATED_CHAT_ID
        defaultTelegramDeliveryUserShouldNotBeFound("chatId.equals=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByChatIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where chatId not equals to DEFAULT_CHAT_ID
        defaultTelegramDeliveryUserShouldNotBeFound("chatId.notEquals=" + DEFAULT_CHAT_ID);

        // Get all the telegramDeliveryUserList where chatId not equals to UPDATED_CHAT_ID
        defaultTelegramDeliveryUserShouldBeFound("chatId.notEquals=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByChatIdIsInShouldWork() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where chatId in DEFAULT_CHAT_ID or UPDATED_CHAT_ID
        defaultTelegramDeliveryUserShouldBeFound("chatId.in=" + DEFAULT_CHAT_ID + "," + UPDATED_CHAT_ID);

        // Get all the telegramDeliveryUserList where chatId equals to UPDATED_CHAT_ID
        defaultTelegramDeliveryUserShouldNotBeFound("chatId.in=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByChatIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where chatId is not null
        defaultTelegramDeliveryUserShouldBeFound("chatId.specified=true");

        // Get all the telegramDeliveryUserList where chatId is null
        defaultTelegramDeliveryUserShouldNotBeFound("chatId.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByChatIdContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where chatId contains DEFAULT_CHAT_ID
        defaultTelegramDeliveryUserShouldBeFound("chatId.contains=" + DEFAULT_CHAT_ID);

        // Get all the telegramDeliveryUserList where chatId contains UPDATED_CHAT_ID
        defaultTelegramDeliveryUserShouldNotBeFound("chatId.contains=" + UPDATED_CHAT_ID);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByChatIdNotContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where chatId does not contain DEFAULT_CHAT_ID
        defaultTelegramDeliveryUserShouldNotBeFound("chatId.doesNotContain=" + DEFAULT_CHAT_ID);

        // Get all the telegramDeliveryUserList where chatId does not contain UPDATED_CHAT_ID
        defaultTelegramDeliveryUserShouldBeFound("chatId.doesNotContain=" + UPDATED_CHAT_ID);
    }


    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where phone equals to DEFAULT_PHONE
        defaultTelegramDeliveryUserShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the telegramDeliveryUserList where phone equals to UPDATED_PHONE
        defaultTelegramDeliveryUserShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where phone not equals to DEFAULT_PHONE
        defaultTelegramDeliveryUserShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the telegramDeliveryUserList where phone not equals to UPDATED_PHONE
        defaultTelegramDeliveryUserShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultTelegramDeliveryUserShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the telegramDeliveryUserList where phone equals to UPDATED_PHONE
        defaultTelegramDeliveryUserShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where phone is not null
        defaultTelegramDeliveryUserShouldBeFound("phone.specified=true");

        // Get all the telegramDeliveryUserList where phone is null
        defaultTelegramDeliveryUserShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where phone contains DEFAULT_PHONE
        defaultTelegramDeliveryUserShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the telegramDeliveryUserList where phone contains UPDATED_PHONE
        defaultTelegramDeliveryUserShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where phone does not contain DEFAULT_PHONE
        defaultTelegramDeliveryUserShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the telegramDeliveryUserList where phone does not contain UPDATED_PHONE
        defaultTelegramDeliveryUserShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByConversationMetaDataIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where conversationMetaData equals to DEFAULT_CONVERSATION_META_DATA
        defaultTelegramDeliveryUserShouldBeFound("conversationMetaData.equals=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramDeliveryUserList where conversationMetaData equals to UPDATED_CONVERSATION_META_DATA
        defaultTelegramDeliveryUserShouldNotBeFound("conversationMetaData.equals=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByConversationMetaDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where conversationMetaData not equals to DEFAULT_CONVERSATION_META_DATA
        defaultTelegramDeliveryUserShouldNotBeFound("conversationMetaData.notEquals=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramDeliveryUserList where conversationMetaData not equals to UPDATED_CONVERSATION_META_DATA
        defaultTelegramDeliveryUserShouldBeFound("conversationMetaData.notEquals=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByConversationMetaDataIsInShouldWork() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where conversationMetaData in DEFAULT_CONVERSATION_META_DATA or UPDATED_CONVERSATION_META_DATA
        defaultTelegramDeliveryUserShouldBeFound("conversationMetaData.in=" + DEFAULT_CONVERSATION_META_DATA + "," + UPDATED_CONVERSATION_META_DATA);

        // Get all the telegramDeliveryUserList where conversationMetaData equals to UPDATED_CONVERSATION_META_DATA
        defaultTelegramDeliveryUserShouldNotBeFound("conversationMetaData.in=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByConversationMetaDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where conversationMetaData is not null
        defaultTelegramDeliveryUserShouldBeFound("conversationMetaData.specified=true");

        // Get all the telegramDeliveryUserList where conversationMetaData is null
        defaultTelegramDeliveryUserShouldNotBeFound("conversationMetaData.specified=false");
    }
                @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByConversationMetaDataContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where conversationMetaData contains DEFAULT_CONVERSATION_META_DATA
        defaultTelegramDeliveryUserShouldBeFound("conversationMetaData.contains=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramDeliveryUserList where conversationMetaData contains UPDATED_CONVERSATION_META_DATA
        defaultTelegramDeliveryUserShouldNotBeFound("conversationMetaData.contains=" + UPDATED_CONVERSATION_META_DATA);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByConversationMetaDataNotContainsSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where conversationMetaData does not contain DEFAULT_CONVERSATION_META_DATA
        defaultTelegramDeliveryUserShouldNotBeFound("conversationMetaData.doesNotContain=" + DEFAULT_CONVERSATION_META_DATA);

        // Get all the telegramDeliveryUserList where conversationMetaData does not contain UPDATED_CONVERSATION_META_DATA
        defaultTelegramDeliveryUserShouldBeFound("conversationMetaData.doesNotContain=" + UPDATED_CONVERSATION_META_DATA);
    }


    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLoadedPageIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where loadedPage equals to DEFAULT_LOADED_PAGE
        defaultTelegramDeliveryUserShouldBeFound("loadedPage.equals=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramDeliveryUserList where loadedPage equals to UPDATED_LOADED_PAGE
        defaultTelegramDeliveryUserShouldNotBeFound("loadedPage.equals=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLoadedPageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where loadedPage not equals to DEFAULT_LOADED_PAGE
        defaultTelegramDeliveryUserShouldNotBeFound("loadedPage.notEquals=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramDeliveryUserList where loadedPage not equals to UPDATED_LOADED_PAGE
        defaultTelegramDeliveryUserShouldBeFound("loadedPage.notEquals=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLoadedPageIsInShouldWork() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where loadedPage in DEFAULT_LOADED_PAGE or UPDATED_LOADED_PAGE
        defaultTelegramDeliveryUserShouldBeFound("loadedPage.in=" + DEFAULT_LOADED_PAGE + "," + UPDATED_LOADED_PAGE);

        // Get all the telegramDeliveryUserList where loadedPage equals to UPDATED_LOADED_PAGE
        defaultTelegramDeliveryUserShouldNotBeFound("loadedPage.in=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLoadedPageIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where loadedPage is not null
        defaultTelegramDeliveryUserShouldBeFound("loadedPage.specified=true");

        // Get all the telegramDeliveryUserList where loadedPage is null
        defaultTelegramDeliveryUserShouldNotBeFound("loadedPage.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLoadedPageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where loadedPage is greater than or equal to DEFAULT_LOADED_PAGE
        defaultTelegramDeliveryUserShouldBeFound("loadedPage.greaterThanOrEqual=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramDeliveryUserList where loadedPage is greater than or equal to UPDATED_LOADED_PAGE
        defaultTelegramDeliveryUserShouldNotBeFound("loadedPage.greaterThanOrEqual=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLoadedPageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where loadedPage is less than or equal to DEFAULT_LOADED_PAGE
        defaultTelegramDeliveryUserShouldBeFound("loadedPage.lessThanOrEqual=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramDeliveryUserList where loadedPage is less than or equal to SMALLER_LOADED_PAGE
        defaultTelegramDeliveryUserShouldNotBeFound("loadedPage.lessThanOrEqual=" + SMALLER_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLoadedPageIsLessThanSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where loadedPage is less than DEFAULT_LOADED_PAGE
        defaultTelegramDeliveryUserShouldNotBeFound("loadedPage.lessThan=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramDeliveryUserList where loadedPage is less than UPDATED_LOADED_PAGE
        defaultTelegramDeliveryUserShouldBeFound("loadedPage.lessThan=" + UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByLoadedPageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        // Get all the telegramDeliveryUserList where loadedPage is greater than DEFAULT_LOADED_PAGE
        defaultTelegramDeliveryUserShouldNotBeFound("loadedPage.greaterThan=" + DEFAULT_LOADED_PAGE);

        // Get all the telegramDeliveryUserList where loadedPage is greater than SMALLER_LOADED_PAGE
        defaultTelegramDeliveryUserShouldBeFound("loadedPage.greaterThan=" + SMALLER_LOADED_PAGE);
    }


    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);
        Order order = OrderResourceIT.createEntity(em);
        em.persist(order);
        em.flush();
        telegramDeliveryUser.addOrder(order);
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);
        Long orderId = order.getId();

        // Get all the telegramDeliveryUserList where order equals to orderId
        defaultTelegramDeliveryUserShouldBeFound("orderId.equals=" + orderId);

        // Get all the telegramDeliveryUserList where order equals to orderId + 1
        defaultTelegramDeliveryUserShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }


    @Test
    @Transactional
    public void getAllTelegramDeliveryUsersByRestorantIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);
        Restorant restorant = RestorantResourceIT.createEntity(em);
        em.persist(restorant);
        em.flush();
        telegramDeliveryUser.addRestorant(restorant);
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);
        Long restorantId = restorant.getId();

        // Get all the telegramDeliveryUserList where restorant equals to restorantId
        defaultTelegramDeliveryUserShouldBeFound("restorantId.equals=" + restorantId);

        // Get all the telegramDeliveryUserList where restorant equals to restorantId + 1
        defaultTelegramDeliveryUserShouldNotBeFound("restorantId.equals=" + (restorantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTelegramDeliveryUserShouldBeFound(String filter) throws Exception {
        restTelegramDeliveryUserMockMvc.perform(get("/api/telegram-delivery-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telegramDeliveryUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].chatId").value(hasItem(DEFAULT_CHAT_ID)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].conversationMetaData").value(hasItem(DEFAULT_CONVERSATION_META_DATA)))
            .andExpect(jsonPath("$.[*].loadedPage").value(hasItem(DEFAULT_LOADED_PAGE)));

        // Check, that the count call also returns 1
        restTelegramDeliveryUserMockMvc.perform(get("/api/telegram-delivery-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTelegramDeliveryUserShouldNotBeFound(String filter) throws Exception {
        restTelegramDeliveryUserMockMvc.perform(get("/api/telegram-delivery-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTelegramDeliveryUserMockMvc.perform(get("/api/telegram-delivery-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTelegramDeliveryUser() throws Exception {
        // Get the telegramDeliveryUser
        restTelegramDeliveryUserMockMvc.perform(get("/api/telegram-delivery-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelegramDeliveryUser() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        int databaseSizeBeforeUpdate = telegramDeliveryUserRepository.findAll().size();

        // Update the telegramDeliveryUser
        TelegramDeliveryUser updatedTelegramDeliveryUser = telegramDeliveryUserRepository.findById(telegramDeliveryUser.getId()).get();
        // Disconnect from session so that the updates on updatedTelegramDeliveryUser are not directly saved in db
        em.detach(updatedTelegramDeliveryUser);
        updatedTelegramDeliveryUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .userName(UPDATED_USER_NAME)
            .userId(UPDATED_USER_ID)
            .chatId(UPDATED_CHAT_ID)
            .phone(UPDATED_PHONE)
            .conversationMetaData(UPDATED_CONVERSATION_META_DATA)
            .loadedPage(UPDATED_LOADED_PAGE);
        TelegramDeliveryUserDTO telegramDeliveryUserDTO = telegramDeliveryUserMapper.toDto(updatedTelegramDeliveryUser);

        restTelegramDeliveryUserMockMvc.perform(put("/api/telegram-delivery-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramDeliveryUserDTO)))
            .andExpect(status().isOk());

        // Validate the TelegramDeliveryUser in the database
        List<TelegramDeliveryUser> telegramDeliveryUserList = telegramDeliveryUserRepository.findAll();
        assertThat(telegramDeliveryUserList).hasSize(databaseSizeBeforeUpdate);
        TelegramDeliveryUser testTelegramDeliveryUser = telegramDeliveryUserList.get(telegramDeliveryUserList.size() - 1);
        assertThat(testTelegramDeliveryUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTelegramDeliveryUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTelegramDeliveryUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testTelegramDeliveryUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testTelegramDeliveryUser.getChatId()).isEqualTo(UPDATED_CHAT_ID);
        assertThat(testTelegramDeliveryUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTelegramDeliveryUser.getConversationMetaData()).isEqualTo(UPDATED_CONVERSATION_META_DATA);
        assertThat(testTelegramDeliveryUser.getLoadedPage()).isEqualTo(UPDATED_LOADED_PAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingTelegramDeliveryUser() throws Exception {
        int databaseSizeBeforeUpdate = telegramDeliveryUserRepository.findAll().size();

        // Create the TelegramDeliveryUser
        TelegramDeliveryUserDTO telegramDeliveryUserDTO = telegramDeliveryUserMapper.toDto(telegramDeliveryUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTelegramDeliveryUserMockMvc.perform(put("/api/telegram-delivery-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telegramDeliveryUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TelegramDeliveryUser in the database
        List<TelegramDeliveryUser> telegramDeliveryUserList = telegramDeliveryUserRepository.findAll();
        assertThat(telegramDeliveryUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTelegramDeliveryUser() throws Exception {
        // Initialize the database
        telegramDeliveryUserRepository.saveAndFlush(telegramDeliveryUser);

        int databaseSizeBeforeDelete = telegramDeliveryUserRepository.findAll().size();

        // Delete the telegramDeliveryUser
        restTelegramDeliveryUserMockMvc.perform(delete("/api/telegram-delivery-users/{id}", telegramDeliveryUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TelegramDeliveryUser> telegramDeliveryUserList = telegramDeliveryUserRepository.findAll();
        assertThat(telegramDeliveryUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
