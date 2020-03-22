package et.com.delivereth.web.rest;

import et.com.delivereth.service.TelegramUserService;
import et.com.delivereth.web.rest.errors.BadRequestAlertException;
import et.com.delivereth.service.dto.TelegramUserDTO;
import et.com.delivereth.service.dto.TelegramUserCriteria;
import et.com.delivereth.service.TelegramUserQueryService;

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
 * REST controller for managing {@link et.com.delivereth.domain.TelegramUser}.
 */
@RestController
@RequestMapping("/api")
public class TelegramUserResource {

    private final Logger log = LoggerFactory.getLogger(TelegramUserResource.class);

    private static final String ENTITY_NAME = "telegramUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TelegramUserService telegramUserService;

    private final TelegramUserQueryService telegramUserQueryService;

    public TelegramUserResource(TelegramUserService telegramUserService, TelegramUserQueryService telegramUserQueryService) {
        this.telegramUserService = telegramUserService;
        this.telegramUserQueryService = telegramUserQueryService;
    }

    /**
     * {@code POST  /telegram-users} : Create a new telegramUser.
     *
     * @param telegramUserDTO the telegramUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new telegramUserDTO, or with status {@code 400 (Bad Request)} if the telegramUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/telegram-users")
    public ResponseEntity<TelegramUserDTO> createTelegramUser(@Valid @RequestBody TelegramUserDTO telegramUserDTO) throws URISyntaxException {
        log.debug("REST request to save TelegramUser : {}", telegramUserDTO);
        if (telegramUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new telegramUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TelegramUserDTO result = telegramUserService.save(telegramUserDTO);
        return ResponseEntity.created(new URI("/api/telegram-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /telegram-users} : Updates an existing telegramUser.
     *
     * @param telegramUserDTO the telegramUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated telegramUserDTO,
     * or with status {@code 400 (Bad Request)} if the telegramUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the telegramUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/telegram-users")
    public ResponseEntity<TelegramUserDTO> updateTelegramUser(@Valid @RequestBody TelegramUserDTO telegramUserDTO) throws URISyntaxException {
        log.debug("REST request to update TelegramUser : {}", telegramUserDTO);
        if (telegramUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TelegramUserDTO result = telegramUserService.save(telegramUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, telegramUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /telegram-users} : get all the telegramUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of telegramUsers in body.
     */
    @GetMapping("/telegram-users")
    public ResponseEntity<List<TelegramUserDTO>> getAllTelegramUsers(TelegramUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TelegramUsers by criteria: {}", criteria);
        Page<TelegramUserDTO> page = telegramUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /telegram-users/count} : count all the telegramUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/telegram-users/count")
    public ResponseEntity<Long> countTelegramUsers(TelegramUserCriteria criteria) {
        log.debug("REST request to count TelegramUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(telegramUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /telegram-users/:id} : get the "id" telegramUser.
     *
     * @param id the id of the telegramUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the telegramUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/telegram-users/{id}")
    public ResponseEntity<TelegramUserDTO> getTelegramUser(@PathVariable Long id) {
        log.debug("REST request to get TelegramUser : {}", id);
        Optional<TelegramUserDTO> telegramUserDTO = telegramUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(telegramUserDTO);
    }

    /**
     * {@code DELETE  /telegram-users/:id} : delete the "id" telegramUser.
     *
     * @param id the id of the telegramUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/telegram-users/{id}")
    public ResponseEntity<Void> deleteTelegramUser(@PathVariable Long id) {
        log.debug("REST request to delete TelegramUser : {}", id);
        telegramUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
