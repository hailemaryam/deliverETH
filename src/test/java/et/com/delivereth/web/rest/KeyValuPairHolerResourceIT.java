package et.com.delivereth.web.rest;

import et.com.delivereth.DeliverEthApp;
import et.com.delivereth.domain.KeyValuPairHoler;
import et.com.delivereth.repository.KeyValuPairHolerRepository;
import et.com.delivereth.service.KeyValuPairHolerService;
import et.com.delivereth.service.dto.KeyValuPairHolerDTO;
import et.com.delivereth.service.mapper.KeyValuPairHolerMapper;
import et.com.delivereth.service.dto.KeyValuPairHolerCriteria;
import et.com.delivereth.service.KeyValuPairHolerQueryService;

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
 * Integration tests for the {@link KeyValuPairHolerResource} REST controller.
 */
@SpringBootTest(classes = DeliverEthApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class KeyValuPairHolerResourceIT {

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
    private KeyValuPairHolerRepository keyValuPairHolerRepository;

    @Autowired
    private KeyValuPairHolerMapper keyValuPairHolerMapper;

    @Autowired
    private KeyValuPairHolerService keyValuPairHolerService;

    @Autowired
    private KeyValuPairHolerQueryService keyValuPairHolerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKeyValuPairHolerMockMvc;

    private KeyValuPairHoler keyValuPairHoler;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeyValuPairHoler createEntity(EntityManager em) {
        KeyValuPairHoler keyValuPairHoler = new KeyValuPairHoler()
            .key(DEFAULT_KEY)
            .valueString(DEFAULT_VALUE_STRING)
            .valueNumber(DEFAULT_VALUE_NUMBER)
            .valueImage(DEFAULT_VALUE_IMAGE)
            .valueImageContentType(DEFAULT_VALUE_IMAGE_CONTENT_TYPE)
            .valueBlob(DEFAULT_VALUE_BLOB)
            .valueBlobContentType(DEFAULT_VALUE_BLOB_CONTENT_TYPE);
        return keyValuPairHoler;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeyValuPairHoler createUpdatedEntity(EntityManager em) {
        KeyValuPairHoler keyValuPairHoler = new KeyValuPairHoler()
            .key(UPDATED_KEY)
            .valueString(UPDATED_VALUE_STRING)
            .valueNumber(UPDATED_VALUE_NUMBER)
            .valueImage(UPDATED_VALUE_IMAGE)
            .valueImageContentType(UPDATED_VALUE_IMAGE_CONTENT_TYPE)
            .valueBlob(UPDATED_VALUE_BLOB)
            .valueBlobContentType(UPDATED_VALUE_BLOB_CONTENT_TYPE);
        return keyValuPairHoler;
    }

    @BeforeEach
    public void initTest() {
        keyValuPairHoler = createEntity(em);
    }

    @Test
    @Transactional
    public void createKeyValuPairHoler() throws Exception {
        int databaseSizeBeforeCreate = keyValuPairHolerRepository.findAll().size();

        // Create the KeyValuPairHoler
        KeyValuPairHolerDTO keyValuPairHolerDTO = keyValuPairHolerMapper.toDto(keyValuPairHoler);
        restKeyValuPairHolerMockMvc.perform(post("/api/key-valu-pair-holers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyValuPairHolerDTO)))
            .andExpect(status().isCreated());

        // Validate the KeyValuPairHoler in the database
        List<KeyValuPairHoler> keyValuPairHolerList = keyValuPairHolerRepository.findAll();
        assertThat(keyValuPairHolerList).hasSize(databaseSizeBeforeCreate + 1);
        KeyValuPairHoler testKeyValuPairHoler = keyValuPairHolerList.get(keyValuPairHolerList.size() - 1);
        assertThat(testKeyValuPairHoler.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testKeyValuPairHoler.getValueString()).isEqualTo(DEFAULT_VALUE_STRING);
        assertThat(testKeyValuPairHoler.getValueNumber()).isEqualTo(DEFAULT_VALUE_NUMBER);
        assertThat(testKeyValuPairHoler.getValueImage()).isEqualTo(DEFAULT_VALUE_IMAGE);
        assertThat(testKeyValuPairHoler.getValueImageContentType()).isEqualTo(DEFAULT_VALUE_IMAGE_CONTENT_TYPE);
        assertThat(testKeyValuPairHoler.getValueBlob()).isEqualTo(DEFAULT_VALUE_BLOB);
        assertThat(testKeyValuPairHoler.getValueBlobContentType()).isEqualTo(DEFAULT_VALUE_BLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createKeyValuPairHolerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = keyValuPairHolerRepository.findAll().size();

        // Create the KeyValuPairHoler with an existing ID
        keyValuPairHoler.setId(1L);
        KeyValuPairHolerDTO keyValuPairHolerDTO = keyValuPairHolerMapper.toDto(keyValuPairHoler);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeyValuPairHolerMockMvc.perform(post("/api/key-valu-pair-holers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyValuPairHolerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KeyValuPairHoler in the database
        List<KeyValuPairHoler> keyValuPairHolerList = keyValuPairHolerRepository.findAll();
        assertThat(keyValuPairHolerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllKeyValuPairHolers() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList
        restKeyValuPairHolerMockMvc.perform(get("/api/key-valu-pair-holers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyValuPairHoler.getId().intValue())))
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
    public void getKeyValuPairHoler() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get the keyValuPairHoler
        restKeyValuPairHolerMockMvc.perform(get("/api/key-valu-pair-holers/{id}", keyValuPairHoler.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(keyValuPairHoler.getId().intValue()))
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
    public void getKeyValuPairHolersByIdFiltering() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        Long id = keyValuPairHoler.getId();

        defaultKeyValuPairHolerShouldBeFound("id.equals=" + id);
        defaultKeyValuPairHolerShouldNotBeFound("id.notEquals=" + id);

        defaultKeyValuPairHolerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultKeyValuPairHolerShouldNotBeFound("id.greaterThan=" + id);

        defaultKeyValuPairHolerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultKeyValuPairHolerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllKeyValuPairHolersByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where key equals to DEFAULT_KEY
        defaultKeyValuPairHolerShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the keyValuPairHolerList where key equals to UPDATED_KEY
        defaultKeyValuPairHolerShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where key not equals to DEFAULT_KEY
        defaultKeyValuPairHolerShouldNotBeFound("key.notEquals=" + DEFAULT_KEY);

        // Get all the keyValuPairHolerList where key not equals to UPDATED_KEY
        defaultKeyValuPairHolerShouldBeFound("key.notEquals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where key in DEFAULT_KEY or UPDATED_KEY
        defaultKeyValuPairHolerShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the keyValuPairHolerList where key equals to UPDATED_KEY
        defaultKeyValuPairHolerShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where key is not null
        defaultKeyValuPairHolerShouldBeFound("key.specified=true");

        // Get all the keyValuPairHolerList where key is null
        defaultKeyValuPairHolerShouldNotBeFound("key.specified=false");
    }
                @Test
    @Transactional
    public void getAllKeyValuPairHolersByKeyContainsSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where key contains DEFAULT_KEY
        defaultKeyValuPairHolerShouldBeFound("key.contains=" + DEFAULT_KEY);

        // Get all the keyValuPairHolerList where key contains UPDATED_KEY
        defaultKeyValuPairHolerShouldNotBeFound("key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where key does not contain DEFAULT_KEY
        defaultKeyValuPairHolerShouldNotBeFound("key.doesNotContain=" + DEFAULT_KEY);

        // Get all the keyValuPairHolerList where key does not contain UPDATED_KEY
        defaultKeyValuPairHolerShouldBeFound("key.doesNotContain=" + UPDATED_KEY);
    }


    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueStringIsEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueString equals to DEFAULT_VALUE_STRING
        defaultKeyValuPairHolerShouldBeFound("valueString.equals=" + DEFAULT_VALUE_STRING);

        // Get all the keyValuPairHolerList where valueString equals to UPDATED_VALUE_STRING
        defaultKeyValuPairHolerShouldNotBeFound("valueString.equals=" + UPDATED_VALUE_STRING);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueStringIsNotEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueString not equals to DEFAULT_VALUE_STRING
        defaultKeyValuPairHolerShouldNotBeFound("valueString.notEquals=" + DEFAULT_VALUE_STRING);

        // Get all the keyValuPairHolerList where valueString not equals to UPDATED_VALUE_STRING
        defaultKeyValuPairHolerShouldBeFound("valueString.notEquals=" + UPDATED_VALUE_STRING);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueStringIsInShouldWork() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueString in DEFAULT_VALUE_STRING or UPDATED_VALUE_STRING
        defaultKeyValuPairHolerShouldBeFound("valueString.in=" + DEFAULT_VALUE_STRING + "," + UPDATED_VALUE_STRING);

        // Get all the keyValuPairHolerList where valueString equals to UPDATED_VALUE_STRING
        defaultKeyValuPairHolerShouldNotBeFound("valueString.in=" + UPDATED_VALUE_STRING);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueStringIsNullOrNotNull() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueString is not null
        defaultKeyValuPairHolerShouldBeFound("valueString.specified=true");

        // Get all the keyValuPairHolerList where valueString is null
        defaultKeyValuPairHolerShouldNotBeFound("valueString.specified=false");
    }
                @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueStringContainsSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueString contains DEFAULT_VALUE_STRING
        defaultKeyValuPairHolerShouldBeFound("valueString.contains=" + DEFAULT_VALUE_STRING);

        // Get all the keyValuPairHolerList where valueString contains UPDATED_VALUE_STRING
        defaultKeyValuPairHolerShouldNotBeFound("valueString.contains=" + UPDATED_VALUE_STRING);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueStringNotContainsSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueString does not contain DEFAULT_VALUE_STRING
        defaultKeyValuPairHolerShouldNotBeFound("valueString.doesNotContain=" + DEFAULT_VALUE_STRING);

        // Get all the keyValuPairHolerList where valueString does not contain UPDATED_VALUE_STRING
        defaultKeyValuPairHolerShouldBeFound("valueString.doesNotContain=" + UPDATED_VALUE_STRING);
    }


    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueNumber equals to DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolerShouldBeFound("valueNumber.equals=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolerList where valueNumber equals to UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolerShouldNotBeFound("valueNumber.equals=" + UPDATED_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueNumber not equals to DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolerShouldNotBeFound("valueNumber.notEquals=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolerList where valueNumber not equals to UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolerShouldBeFound("valueNumber.notEquals=" + UPDATED_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueNumberIsInShouldWork() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueNumber in DEFAULT_VALUE_NUMBER or UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolerShouldBeFound("valueNumber.in=" + DEFAULT_VALUE_NUMBER + "," + UPDATED_VALUE_NUMBER);

        // Get all the keyValuPairHolerList where valueNumber equals to UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolerShouldNotBeFound("valueNumber.in=" + UPDATED_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueNumber is not null
        defaultKeyValuPairHolerShouldBeFound("valueNumber.specified=true");

        // Get all the keyValuPairHolerList where valueNumber is null
        defaultKeyValuPairHolerShouldNotBeFound("valueNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueNumber is greater than or equal to DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolerShouldBeFound("valueNumber.greaterThanOrEqual=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolerList where valueNumber is greater than or equal to UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolerShouldNotBeFound("valueNumber.greaterThanOrEqual=" + UPDATED_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueNumber is less than or equal to DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolerShouldBeFound("valueNumber.lessThanOrEqual=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolerList where valueNumber is less than or equal to SMALLER_VALUE_NUMBER
        defaultKeyValuPairHolerShouldNotBeFound("valueNumber.lessThanOrEqual=" + SMALLER_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueNumber is less than DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolerShouldNotBeFound("valueNumber.lessThan=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolerList where valueNumber is less than UPDATED_VALUE_NUMBER
        defaultKeyValuPairHolerShouldBeFound("valueNumber.lessThan=" + UPDATED_VALUE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllKeyValuPairHolersByValueNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        // Get all the keyValuPairHolerList where valueNumber is greater than DEFAULT_VALUE_NUMBER
        defaultKeyValuPairHolerShouldNotBeFound("valueNumber.greaterThan=" + DEFAULT_VALUE_NUMBER);

        // Get all the keyValuPairHolerList where valueNumber is greater than SMALLER_VALUE_NUMBER
        defaultKeyValuPairHolerShouldBeFound("valueNumber.greaterThan=" + SMALLER_VALUE_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultKeyValuPairHolerShouldBeFound(String filter) throws Exception {
        restKeyValuPairHolerMockMvc.perform(get("/api/key-valu-pair-holers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyValuPairHoler.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueString").value(hasItem(DEFAULT_VALUE_STRING)))
            .andExpect(jsonPath("$.[*].valueNumber").value(hasItem(DEFAULT_VALUE_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].valueImageContentType").value(hasItem(DEFAULT_VALUE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].valueImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_VALUE_IMAGE))))
            .andExpect(jsonPath("$.[*].valueBlobContentType").value(hasItem(DEFAULT_VALUE_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].valueBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_VALUE_BLOB))));

        // Check, that the count call also returns 1
        restKeyValuPairHolerMockMvc.perform(get("/api/key-valu-pair-holers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultKeyValuPairHolerShouldNotBeFound(String filter) throws Exception {
        restKeyValuPairHolerMockMvc.perform(get("/api/key-valu-pair-holers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restKeyValuPairHolerMockMvc.perform(get("/api/key-valu-pair-holers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingKeyValuPairHoler() throws Exception {
        // Get the keyValuPairHoler
        restKeyValuPairHolerMockMvc.perform(get("/api/key-valu-pair-holers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKeyValuPairHoler() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        int databaseSizeBeforeUpdate = keyValuPairHolerRepository.findAll().size();

        // Update the keyValuPairHoler
        KeyValuPairHoler updatedKeyValuPairHoler = keyValuPairHolerRepository.findById(keyValuPairHoler.getId()).get();
        // Disconnect from session so that the updates on updatedKeyValuPairHoler are not directly saved in db
        em.detach(updatedKeyValuPairHoler);
        updatedKeyValuPairHoler
            .key(UPDATED_KEY)
            .valueString(UPDATED_VALUE_STRING)
            .valueNumber(UPDATED_VALUE_NUMBER)
            .valueImage(UPDATED_VALUE_IMAGE)
            .valueImageContentType(UPDATED_VALUE_IMAGE_CONTENT_TYPE)
            .valueBlob(UPDATED_VALUE_BLOB)
            .valueBlobContentType(UPDATED_VALUE_BLOB_CONTENT_TYPE);
        KeyValuPairHolerDTO keyValuPairHolerDTO = keyValuPairHolerMapper.toDto(updatedKeyValuPairHoler);

        restKeyValuPairHolerMockMvc.perform(put("/api/key-valu-pair-holers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyValuPairHolerDTO)))
            .andExpect(status().isOk());

        // Validate the KeyValuPairHoler in the database
        List<KeyValuPairHoler> keyValuPairHolerList = keyValuPairHolerRepository.findAll();
        assertThat(keyValuPairHolerList).hasSize(databaseSizeBeforeUpdate);
        KeyValuPairHoler testKeyValuPairHoler = keyValuPairHolerList.get(keyValuPairHolerList.size() - 1);
        assertThat(testKeyValuPairHoler.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testKeyValuPairHoler.getValueString()).isEqualTo(UPDATED_VALUE_STRING);
        assertThat(testKeyValuPairHoler.getValueNumber()).isEqualTo(UPDATED_VALUE_NUMBER);
        assertThat(testKeyValuPairHoler.getValueImage()).isEqualTo(UPDATED_VALUE_IMAGE);
        assertThat(testKeyValuPairHoler.getValueImageContentType()).isEqualTo(UPDATED_VALUE_IMAGE_CONTENT_TYPE);
        assertThat(testKeyValuPairHoler.getValueBlob()).isEqualTo(UPDATED_VALUE_BLOB);
        assertThat(testKeyValuPairHoler.getValueBlobContentType()).isEqualTo(UPDATED_VALUE_BLOB_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingKeyValuPairHoler() throws Exception {
        int databaseSizeBeforeUpdate = keyValuPairHolerRepository.findAll().size();

        // Create the KeyValuPairHoler
        KeyValuPairHolerDTO keyValuPairHolerDTO = keyValuPairHolerMapper.toDto(keyValuPairHoler);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeyValuPairHolerMockMvc.perform(put("/api/key-valu-pair-holers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyValuPairHolerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KeyValuPairHoler in the database
        List<KeyValuPairHoler> keyValuPairHolerList = keyValuPairHolerRepository.findAll();
        assertThat(keyValuPairHolerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteKeyValuPairHoler() throws Exception {
        // Initialize the database
        keyValuPairHolerRepository.saveAndFlush(keyValuPairHoler);

        int databaseSizeBeforeDelete = keyValuPairHolerRepository.findAll().size();

        // Delete the keyValuPairHoler
        restKeyValuPairHolerMockMvc.perform(delete("/api/key-valu-pair-holers/{id}", keyValuPairHoler.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<KeyValuPairHoler> keyValuPairHolerList = keyValuPairHolerRepository.findAll();
        assertThat(keyValuPairHolerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
