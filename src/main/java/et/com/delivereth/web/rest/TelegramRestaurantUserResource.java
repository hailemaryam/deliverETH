package et.com.delivereth.web.rest;

import et.com.delivereth.service.TelegramRestaurantUserService;
import et.com.delivereth.web.rest.errors.BadRequestAlertException;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserCriteria;
import et.com.delivereth.service.TelegramRestaurantUserQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link et.com.delivereth.domain.TelegramRestaurantUser}.
 */
@RestController
@RequestMapping("/api")
public class TelegramRestaurantUserResource {

    private final Logger log = LoggerFactory.getLogger(TelegramRestaurantUserResource.class);

    private static final String ENTITY_NAME = "telegramRestaurantUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TelegramRestaurantUserService telegramRestaurantUserService;

    private final TelegramRestaurantUserQueryService telegramRestaurantUserQueryService;

    public TelegramRestaurantUserResource(TelegramRestaurantUserService telegramRestaurantUserService, TelegramRestaurantUserQueryService telegramRestaurantUserQueryService) {
        this.telegramRestaurantUserService = telegramRestaurantUserService;
        this.telegramRestaurantUserQueryService = telegramRestaurantUserQueryService;
    }

    /**
     * {@code POST  /telegram-restaurant-users} : Create a new telegramRestaurantUser.
     *
     * @param telegramRestaurantUserDTO the telegramRestaurantUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new telegramRestaurantUserDTO, or with status {@code 400 (Bad Request)} if the telegramRestaurantUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/telegram-restaurant-users")
    public ResponseEntity<TelegramRestaurantUserDTO> createTelegramRestaurantUser(@Valid @RequestBody TelegramRestaurantUserDTO telegramRestaurantUserDTO) throws URISyntaxException {
        log.debug("REST request to save TelegramRestaurantUser : {}", telegramRestaurantUserDTO);
        if (telegramRestaurantUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new telegramRestaurantUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TelegramRestaurantUserDTO result = telegramRestaurantUserService.save(telegramRestaurantUserDTO);
        return ResponseEntity.created(new URI("/api/telegram-restaurant-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /telegram-restaurant-users} : Updates an existing telegramRestaurantUser.
     *
     * @param telegramRestaurantUserDTO the telegramRestaurantUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated telegramRestaurantUserDTO,
     * or with status {@code 400 (Bad Request)} if the telegramRestaurantUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the telegramRestaurantUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/telegram-restaurant-users")
    public ResponseEntity<TelegramRestaurantUserDTO> updateTelegramRestaurantUser(@Valid @RequestBody TelegramRestaurantUserDTO telegramRestaurantUserDTO) throws URISyntaxException {
        log.debug("REST request to update TelegramRestaurantUser : {}", telegramRestaurantUserDTO);
        if (telegramRestaurantUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TelegramRestaurantUserDTO result = telegramRestaurantUserService.save(telegramRestaurantUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, telegramRestaurantUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /telegram-restaurant-users} : get all the telegramRestaurantUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of telegramRestaurantUsers in body.
     */
    @GetMapping("/telegram-restaurant-users")
    public ResponseEntity<List<TelegramRestaurantUserDTO>> getAllTelegramRestaurantUsers(TelegramRestaurantUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TelegramRestaurantUsers by criteria: {}", criteria);
        Page<TelegramRestaurantUserDTO> page = telegramRestaurantUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /telegram-restaurant-users/count} : count all the telegramRestaurantUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/telegram-restaurant-users/count")
    public ResponseEntity<Long> countTelegramRestaurantUsers(TelegramRestaurantUserCriteria criteria) {
        log.debug("REST request to count TelegramRestaurantUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(telegramRestaurantUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /telegram-restaurant-users/:id} : get the "id" telegramRestaurantUser.
     *
     * @param id the id of the telegramRestaurantUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the telegramRestaurantUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/telegram-restaurant-users/{id}")
    public ResponseEntity<TelegramRestaurantUserDTO> getTelegramRestaurantUser(@PathVariable Long id) {
        log.debug("REST request to get TelegramRestaurantUser : {}", id);
        Optional<TelegramRestaurantUserDTO> telegramRestaurantUserDTO = telegramRestaurantUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(telegramRestaurantUserDTO);
    }

    /**
     * {@code DELETE  /telegram-restaurant-users/:id} : delete the "id" telegramRestaurantUser.
     *
     * @param id the id of the telegramRestaurantUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/telegram-restaurant-users/{id}")
    public ResponseEntity<Void> deleteTelegramRestaurantUser(@PathVariable Long id) {
        log.debug("REST request to delete TelegramRestaurantUser : {}", id);
        telegramRestaurantUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
