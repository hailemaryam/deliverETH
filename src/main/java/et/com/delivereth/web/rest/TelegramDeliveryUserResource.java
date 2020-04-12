package et.com.delivereth.web.rest;

import et.com.delivereth.service.TelegramDeliveryUserService;
import et.com.delivereth.web.rest.errors.BadRequestAlertException;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.dto.TelegramDeliveryUserCriteria;
import et.com.delivereth.service.TelegramDeliveryUserQueryService;

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
 * REST controller for managing {@link et.com.delivereth.domain.TelegramDeliveryUser}.
 */
@RestController
@RequestMapping("/api")
public class TelegramDeliveryUserResource {

    private final Logger log = LoggerFactory.getLogger(TelegramDeliveryUserResource.class);

    private static final String ENTITY_NAME = "telegramDeliveryUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TelegramDeliveryUserService telegramDeliveryUserService;

    private final TelegramDeliveryUserQueryService telegramDeliveryUserQueryService;

    public TelegramDeliveryUserResource(TelegramDeliveryUserService telegramDeliveryUserService, TelegramDeliveryUserQueryService telegramDeliveryUserQueryService) {
        this.telegramDeliveryUserService = telegramDeliveryUserService;
        this.telegramDeliveryUserQueryService = telegramDeliveryUserQueryService;
    }

    /**
     * {@code POST  /telegram-delivery-users} : Create a new telegramDeliveryUser.
     *
     * @param telegramDeliveryUserDTO the telegramDeliveryUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new telegramDeliveryUserDTO, or with status {@code 400 (Bad Request)} if the telegramDeliveryUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/telegram-delivery-users")
    public ResponseEntity<TelegramDeliveryUserDTO> createTelegramDeliveryUser(@Valid @RequestBody TelegramDeliveryUserDTO telegramDeliveryUserDTO) throws URISyntaxException {
        log.debug("REST request to save TelegramDeliveryUser : {}", telegramDeliveryUserDTO);
        if (telegramDeliveryUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new telegramDeliveryUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TelegramDeliveryUserDTO result = telegramDeliveryUserService.save(telegramDeliveryUserDTO);
        return ResponseEntity.created(new URI("/api/telegram-delivery-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /telegram-delivery-users} : Updates an existing telegramDeliveryUser.
     *
     * @param telegramDeliveryUserDTO the telegramDeliveryUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated telegramDeliveryUserDTO,
     * or with status {@code 400 (Bad Request)} if the telegramDeliveryUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the telegramDeliveryUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/telegram-delivery-users")
    public ResponseEntity<TelegramDeliveryUserDTO> updateTelegramDeliveryUser(@Valid @RequestBody TelegramDeliveryUserDTO telegramDeliveryUserDTO) throws URISyntaxException {
        log.debug("REST request to update TelegramDeliveryUser : {}", telegramDeliveryUserDTO);
        if (telegramDeliveryUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TelegramDeliveryUserDTO result = telegramDeliveryUserService.save(telegramDeliveryUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, telegramDeliveryUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /telegram-delivery-users} : get all the telegramDeliveryUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of telegramDeliveryUsers in body.
     */
    @GetMapping("/telegram-delivery-users")
    public ResponseEntity<List<TelegramDeliveryUserDTO>> getAllTelegramDeliveryUsers(TelegramDeliveryUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TelegramDeliveryUsers by criteria: {}", criteria);
        Page<TelegramDeliveryUserDTO> page = telegramDeliveryUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /telegram-delivery-users/count} : count all the telegramDeliveryUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/telegram-delivery-users/count")
    public ResponseEntity<Long> countTelegramDeliveryUsers(TelegramDeliveryUserCriteria criteria) {
        log.debug("REST request to count TelegramDeliveryUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(telegramDeliveryUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /telegram-delivery-users/:id} : get the "id" telegramDeliveryUser.
     *
     * @param id the id of the telegramDeliveryUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the telegramDeliveryUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/telegram-delivery-users/{id}")
    public ResponseEntity<TelegramDeliveryUserDTO> getTelegramDeliveryUser(@PathVariable Long id) {
        log.debug("REST request to get TelegramDeliveryUser : {}", id);
        Optional<TelegramDeliveryUserDTO> telegramDeliveryUserDTO = telegramDeliveryUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(telegramDeliveryUserDTO);
    }

    /**
     * {@code DELETE  /telegram-delivery-users/:id} : delete the "id" telegramDeliveryUser.
     *
     * @param id the id of the telegramDeliveryUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/telegram-delivery-users/{id}")
    public ResponseEntity<Void> deleteTelegramDeliveryUser(@PathVariable Long id) {
        log.debug("REST request to delete TelegramDeliveryUser : {}", id);
        telegramDeliveryUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
