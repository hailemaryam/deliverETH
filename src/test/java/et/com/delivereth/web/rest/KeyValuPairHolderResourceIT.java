package et.com.delivereth.web.rest;

import et.com.delivereth.DeliverEthApp;
import et.com.delivereth.domain.KeyValuPairHolder;
import et.com.delivereth.repository.KeyValuPairHolderRepository;
import et.com.delivereth.service.KeyValuPairHolderService;
import et.com.delivereth.service.dto.KeyValuPairHolderDTO;
import et.com.delivereth.service.mapper.KeyValuPairHolderMapper;
import et.com.delivereth.service.dto.KeyValuPairHolderCriteria;
import et.com.delivereth.service.KeyValuPairHolderQueryService;

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
 * Integration tests for the {@link KeyValuPairHolderResource} REST controller.
 */
@SpringBootTest(classes = DeliverEthApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class KeyValuPairHolderResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_STRING = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_STRING = "BBBBBBBBBB";

    private static final Double DEFAULT_VALUE_NUMBER = 1D;
    private static final Double UPDATED_VALUE_NUMBER = 2D;
    private static final Double SMALLER_VALUE_NUMBER = 1D - 1D;

    private static final byte[] DEFAULT_VALUE_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VALUE_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VALUE_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VALUE_IMAGE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_VALUE_BLOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VALUE_BLOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VALUE_BLOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VALUE_BLOB_CONTENT_TYPE = "image/png";

    @Autowired
    private KeyValuPairHolderRepository keyValuPairHolderRepository;

    @Autowired
    private KeyValuPairHolderMapper keyValuPairHolderMapper;

    @Autowired
    private KeyValuPairHolderService keyValuPairHolderService;

    @Autowired
    private KeyValuPairHolderQueryService keyValuPairHolderQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKeyValuPairHolderMockMvc;

    private KeyValuPairHolder keyValuPairHolder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeyValuPairHolder createEntity(EntityManager em) {
        KeyValuPairHolder keyValuPairHolder = new KeyValuPairHolder()
            .key(DEFAULT_KEY)
            .valueString(DEFAULT_VALUE_STRING)
            .valueNumber(DEFAULT_VALUE_NUMBER)
            .valueImage(DEFAULT_VALUE_IMAGE)
            .valueImageContentType(DEFAULT_VALUE_IMAGE_CONTENT_TYPE)
            .valueBlob(DEFAULT_VALUE_BLOB)
            .valueBlobContentType(DEFAULT_VALUE_BLOB_CONTENT_TYPE);
        return keyValuPairHolder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeyValuPairHolder createUpdatedEntity(EntityManager em) {
        KeyValuPairHolder keyValuPairHolder = new KeyValuPairHolder()
            .key(UPDATED_KEY)
            .valueString(UPDATED_VALUE_STRING)
            .valueNumber(UPDATED_VALUE_NUMBER)
            .valueImage(UPDATED_VALUE_IMAGE)
            .valueImageContentType(UPDATED_VALUE_IMAGE_CONTENT_TYPE)
            .valueBlob(UPDATED_VALUE_BLOB)
            .valueBlobContentType(UPDATED_VALUE_BLOB_CONTENT_TYPE);
        return keyValuPairHolder;
    }

    @BeforeEach
    public void initTest() {
        keyValuPairHolder = createEntity(em);
    }

    @Test
    @Transactional
    public void createKeyValuPairHolder() throws Exception {
        int databaseSizeBeforeCreate = keyValuPairHolderRepository.findAll().size();

        // Create the KeyValuPairHolder
        KeyValuPairHolderDTO keyValuPairHolderDTO = keyValuPairHolderMapper.toDto(keyValuPairHolder);
        restKeyValuPairHolderMockMvc.perform(post("/api/key-valu-pair-holders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyValuPairHolderDTO)))
            .andExpect(status().isCreated());

        // Validate the KeyValuPairHolder in the database
        List<KeyValuPairHolder> keyValuPairHolderList = keyValuPairHolderRepository.findAll();
        assertThat(keyValuPairHolderList).hasSize(databaseSizeBeforeCreate + 1);
        KeyValuPairHolder testKeyValuPairHolder = keyValuPairHolderList.get(keyValuPairHolderList.size() - 1);
        assertThat(testKeyValuPairHolder.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testKeyValuPairHolder.getValueString()).isEqualTo(DEFAULT_VALUE_STRING);
        assertThat(testKeyValuPairHolder.getValueNumber()).isEqualTo(DEFAULT_VALUE_NUMBER);
        assertThat(testKeyValuPairHolder.getValueImage()).isEqualTo(DEFAULT_VALUE_IMAGE);
        assertThat(testKeyValuPairHolder.getValueImageContentType()).isEqualTo(DEFAULT_VALUE_IMAGE_CONTENT_TYPE);
        assertThat(testKeyValuPairHolder.getValueBlob()).isEqualTo(DEFAULT_VALUE_BLOB);
        assertThat(testKeyValuPairHolder.getValueBlobContentType()).isEqualTo(DEFAULT_VALUE_BLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createKeyValuPairHolderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = keyValuPairHolderRepository.findAll().size();

        // Create the KeyValuPairHolder with an existing ID
        keyValuPairHolder.setId(1L);
        KeyValuPairHolderDTO keyValuPairHolderDTO = keyValuPairHolderMapper.toDto(keyValuPairHolder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeyValuPairHolderMockMvc.perform(post("/api/key-valu-pair-holders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyValuPairHolderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KeyValuPairHolder in the database
        List<KeyValuPairHolder> keyValuPairHolderList = keyValuPairHolderRepository.findAll();
        assertThat(keyValuPairHolderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllKeyValuPairHolders() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList
        restKeyValuPairHolderMockMvc.perform(get("/api/key-valu-pair-holders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyValuPairHolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueString").value(hasItem(DEFAULT_VALUE_STRING)))
            .andExpect(jsonPath("$.[*].valueNumber").value(hasItem(DEFAULT_VALUE_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].valueImageContentType").value(hasItem(DEFAULT_VALUE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].valueImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_VALUE_IMAGE))))
            .andExpect(jsonPath("$.[*].valueBlobContentType").value(hasItem(DEFAULT_VALUE_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].valueBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_VALUE_BLOB))));
    }
    
    @Test
    @Transactional
    public void getKeyValuPairHolder() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get the keyValuPairHolder
        restKeyValuPairHolderMockMvc.perform(get("/api/key-valu-pair-holders/{id}", keyValuPairHolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(keyValuPairHolder.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.valueString").value(DEFAULT_VALUE_STRING))
            .andExpect(jsonPath("$.valueNumber").value(DEFAULT_VALUE_NUMBER.doubleValue()))
            .andExpect(jsonPath("$.valueImageContentType").value(DEFAULT_VALUE_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.valueImage").value(Base64Utils.encodeToString(DEFAULT_VALUE_IMAGE)))
            .andExpect(jsonPath("$.valueBlobContentType").value(DEFAULT_VALUE_BLOB_CONTENT_TYPE))
            .andExpect(jsonPath("$.valueBlob").value(Base64Utils.encodeToString(DEFAULT_VALUE_BLOB)));
    }


    @Test
    @Transactional
    public void getKeyValuPairHoldersByIdFiltering() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        Long id = keyValuPairHolder.getId();

        defaultKeyValuPairHolderShouldBeFound("id.equals=" + id);
        defaultKeyValuPairHolderShouldNotBeFound("id.notEquals=" + id);

        defaultKeyValuPairHolderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultKeyValuPairHolderShouldNotBeFound("id.greaterThan=" + id);

        defaultKeyValuPairHolderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultKeyValuPairHolderShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where key equals to DEFAULT_KEY
        defaultKeyValuPairHolderShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the keyValuPairHolderList where key equals to UPDATED_KEY
        defaultKeyValuPairHolderShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where key not equals to DEFAULT_KEY
        defaultKeyValuPairHolderShouldNotBeFound("key.notEquals=" + DEFAULT_KEY);

        // Get all the keyValuPairHolderList where key not equals to UPDATED_KEY
        defaultKeyValuPairHolderShouldBeFound("key.notEquals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where key in DEFAULT_KEY or UPDATED_KEY
        defaultKeyValuPairHolderShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the keyValuPairHolderList where key equals to UPDATED_KEY
        defaultKeyValuPairHolderShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where key is not null
        defaultKeyValuPairHolderShouldBeFound("key.specified=true");

        // Get all the keyValuPairHolderList where key is null
        defaultKeyValuPairHolderShouldNotBeFound("key.specified=false");
    }
                @Test
    @Transactional
    public void getAllKeyValuPairHoldersByKeyContainsSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where key contains DEFAULT_KEY
        defaultKeyValuPairHolderShouldBeFound("key.contains=" + DEFAULT_KEY);

        // Get all the keyValuPairHolderList where key contains UPDATED_KEY
        defaultKeyValuPairHolderShouldNotBeFound("key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where key does not contain DEFAULT_KEY
        defaultKeyValuPairHolderShouldNotBeFound("key.doesNotContain=" + DEFAULT_KEY);

        // Get all the keyValuPairHolderList where key does not contain UPDATED_KEY
        defaultKeyValuPairHolderShouldBeFound("key.doesNotContain=" + UPDATED_KEY);
    }


    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueStringIsEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueString equals to DEFAULT_VALUE_STRING
        defaultKeyValuPairHolderShouldBeFound("valueString.equals=" + DEFAULT_VALUE_STRING);

        // Get all the keyValuPairHolderList where valueString equals to UPDATED_VALUE_STRING
        defaultKeyValuPairHolderShouldNotBeFound("valueString.equals=" + UPDATED_VALUE_STRING);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueStringIsNotEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueString not equals to DEFAULT_VALUE_STRING
        defaultKeyValuPairHolderShouldNotBeFound("valueString.notEquals=" + DEFAULT_VALUE_STRING);

        // Get all the keyValuPairHolderList where valueString not equals to UPDATED_VALUE_STRING
        defaultKeyValuPairHolderShouldBeFound("valueString.notEquals=" + UPDATED_VALUE_STRING);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueStringIsInShouldWork() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueString in DEFAULT_VALUE_STRING or UPDATED_VALUE_STRING
        defaultKeyValuPairHolderShouldBeFound("valueString.in=" + DEFAULT_VALUE_STRING + "," + UPDATED_VALUE_STRING);

        // Get all the keyValuPairHolderList where valueString equals to UPDATED_VALUE_STRING
        defaultKeyValuPairHolderShouldNotBeFound("valueString.in=" + UPDATED_VALUE_STRING);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueStringIsNullOrNotNull() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueString is not null
        defaultKeyValuPairHolderShouldBeFound("valueString.specified=true");

        // Get all the keyValuPairHolderList where valueString is null
        defaultKeyValuPairHolderShouldNotBeFound("valueString.specified=false");
    }
                @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueStringContainsSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueString contains DEFAULT_VALUE_STRING
        defaultKeyValuPairHolderShouldBeFound("valueString.contains=" + DEFAULT_VALUE_STRING);

        // Get all the keyValuPairHolderList where valueString contains UPDATED_VALUE_STRING
        defaultKeyValuPairHolderShouldNotBeFound("valueString.contains=" + UPDATED_VALUE_STRING);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueStringNotContainsSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueString does not contain DEFAULT_VALUE_STRING
        defaultKeyValuPairHolderShouldNotBeFound("valueString.doesNotContain=" + DEFAULT_VALUE_STRING);

        // Get all the keyValuPairHolderList where valueString does not contain UPDATED_VALUE_STRING
        defaultKeyValuPairHolderShouldBeFound("valueString.doesNotContain=" + UPDATED_VALUE_STRING);
    }


    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueNumber equals to DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolderShouldBeFound("valueNumber.equals=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolderList where valueNumber equals to UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolderShouldNotBeFound("valueNumber.equals=" + UPDATED_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueNumber not equals to DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolderShouldNotBeFound("valueNumber.notEquals=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolderList where valueNumber not equals to UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolderShouldBeFound("valueNumber.notEquals=" + UPDATED_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueNumberIsInShouldWork() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueNumber in DEFAULT_VALUE_NUMBER or UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolderShouldBeFound("valueNumber.in=" + DEFAULT_VALUE_NUMBER + "," + UPDATED_VALUE_NUMBER);

        // Get all the keyValuPairHolderList where valueNumber equals to UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolderShouldNotBeFound("valueNumber.in=" + UPDATED_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueNumber is not null
        defaultKeyValuPairHolderShouldBeFound("valueNumber.specified=true");

        // Get all the keyValuPairHolderList where valueNumber is null
        defaultKeyValuPairHolderShouldNotBeFound("valueNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueNumber is greater than or equal to DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolderShouldBeFound("valueNumber.greaterThanOrEqual=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolderList where valueNumber is greater than or equal to UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolderShouldNotBeFound("valueNumber.greaterThanOrEqual=" + UPDATED_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueNumber is less than or equal to DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolderShouldBeFound("valueNumber.lessThanOrEqual=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolderList where valueNumber is less than or equal to SMALLER_VALUE_NUMBER
        defaultKeyValuPairHolderShouldNotBeFound("valueNumber.lessThanOrEqual=" + SMALLER_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueNumber is less than DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolderShouldNotBeFound("valueNumber.lessThan=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolderList where valueNumber is less than UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolderShouldBeFound("valueNumber.lessThan=" + UPDATED_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHoldersByValueNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        // Get all the keyValuPairHolderList where valueNumber is greater than DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolderShouldNotBeFound("valueNumber.greaterThan=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolderList where valueNumber is greater than SMALLER_VALUE_NUMBER
        defaultKeyValuPairHolderShouldBeFound("valueNumber.greaterThan=" + SMALLER_VALUE_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultKeyValuPairHolderShouldBeFound(String filter) throws Exception {
        restKeyValuPairHolderMockMvc.perform(get("/api/key-valu-pair-holders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyValuPairHolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueString").value(hasItem(DEFAULT_VALUE_STRING)))
            .andExpect(jsonPath("$.[*].valueNumber").value(hasItem(DEFAULT_VALUE_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].valueImageContentType").value(hasItem(DEFAULT_VALUE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].valueImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_VALUE_IMAGE))))
            .andExpect(jsonPath("$.[*].valueBlobContentType").value(hasItem(DEFAULT_VALUE_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].valueBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_VALUE_BLOB))));

        // Check, that the count call also returns 1
        restKeyValuPairHolderMockMvc.perform(get("/api/key-valu-pair-holders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultKeyValuPairHolderShouldNotBeFound(String filter) throws Exception {
        restKeyValuPairHolderMockMvc.perform(get("/api/key-valu-pair-holders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restKeyValuPairHolderMockMvc.perform(get("/api/key-valu-pair-holders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingKeyValuPairHolder() throws Exception {
        // Get the keyValuPairHolder
        restKeyValuPairHolderMockMvc.perform(get("/api/key-valu-pair-holders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKeyValuPairHolder() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        int databaseSizeBeforeUpdate = keyValuPairHolderRepository.findAll().size();

        // Update the keyValuPairHolder
        KeyValuPairHolder updatedKeyValuPairHolder = keyValuPairHolderRepository.findById(keyValuPairHolder.getId()).get();
        // Disconnect from session so that the updates on updatedKeyValuPairHolder are not directly saved in db
        em.detach(updatedKeyValuPairHolder);
        updatedKeyValuPairHolder
            .key(UPDATED_KEY)
            .valueString(UPDATED_VALUE_STRING)
            .valueNumber(UPDATED_VALUE_NUMBER)
            .valueImage(UPDATED_VALUE_IMAGE)
            .valueImageContentType(UPDATED_VALUE_IMAGE_CONTENT_TYPE)
            .valueBlob(UPDATED_VALUE_BLOB)
            .valueBlobContentType(UPDATED_VALUE_BLOB_CONTENT_TYPE);
        KeyValuPairHolderDTO keyValuPairHolderDTO = keyValuPairHolderMapper.toDto(updatedKeyValuPairHolder);

        restKeyValuPairHolderMockMvc.perform(put("/api/key-valu-pair-holders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyValuPairHolderDTO)))
            .andExpect(status().isOk());

        // Validate the KeyValuPairHolder in the database
        List<KeyValuPairHolder> keyValuPairHolderList = keyValuPairHolderRepository.findAll();
        assertThat(keyValuPairHolderList).hasSize(databaseSizeBeforeUpdate);
        KeyValuPairHolder testKeyValuPairHolder = keyValuPairHolderList.get(keyValuPairHolderList.size() - 1);
        assertThat(testKeyValuPairHolder.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testKeyValuPairHolder.getValueString()).isEqualTo(UPDATED_VALUE_STRING);
        assertThat(testKeyValuPairHolder.getValueNumber()).isEqualTo(UPDATED_VALUE_NUMBER);
        assertThat(testKeyValuPairHolder.getValueImage()).isEqualTo(UPDATED_VALUE_IMAGE);
        assertThat(testKeyValuPairHolder.getValueImageContentType()).isEqualTo(UPDATED_VALUE_IMAGE_CONTENT_TYPE);
        assertThat(testKeyValuPairHolder.getValueBlob()).isEqualTo(UPDATED_VALUE_BLOB);
        assertThat(testKeyValuPairHolder.getValueBlobContentType()).isEqualTo(UPDATED_VALUE_BLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingKeyValuPairHolder() throws Exception {
        int databaseSizeBeforeUpdate = keyValuPairHolderRepository.findAll().size();

        // Create the KeyValuPairHolder
        KeyValuPairHolderDTO keyValuPairHolderDTO = keyValuPairHolderMapper.toDto(keyValuPairHolder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeyValuPairHolderMockMvc.perform(put("/api/key-valu-pair-holders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyValuPairHolderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KeyValuPairHolder in the database
        List<KeyValuPairHolder> keyValuPairHolderList = keyValuPairHolderRepository.findAll();
        assertThat(keyValuPairHolderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteKeyValuPairHolder() throws Exception {
        // Initialize the database
        keyValuPairHolderRepository.saveAndFlush(keyValuPairHolder);

        int databaseSizeBeforeDelete = keyValuPairHolderRepository.findAll().size();

        // Delete the keyValuPairHolder
        restKeyValuPairHolderMockMvc.perform(delete("/api/key-valu-pair-holders/{id}", keyValuPairHolder.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<KeyValuPairHolder> keyValuPairHolderList = keyValuPairHolderRepository.findAll();
        assertThat(keyValuPairHolderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
