package et.com.delivereth.web.rest;

import et.com.delivereth.service.KeyValuPairHolderService;
import et.com.delivereth.web.rest.errors.BadRequestAlertException;
import et.com.delivereth.service.dto.KeyValuPairHolderDTO;
import et.com.delivereth.service.dto.KeyValuPairHolderCriteria;
import et.com.delivereth.service.KeyValuPairHolderQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link et.com.delivereth.domain.KeyValuPairHolder}.
 */
@RestController
@RequestMapping("/api")
public class KeyValuPairHolderResource {

    private final Logger log = LoggerFactory.getLogger(KeyValuPairHolderResource.class);

    private static final String ENTITY_NAME = "keyValuPairHolder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KeyValuPairHolderService keyValuPairHolderService;

    private final KeyValuPairHolderQueryService keyValuPairHolderQueryService;

    public KeyValuPairHolderResource(KeyValuPairHolderService keyValuPairHolderService, KeyValuPairHolderQueryService keyValuPairHolderQueryService) {
        this.keyValuPairHolderService = keyValuPairHolderService;
        this.keyValuPairHolderQueryService = keyValuPairHolderQueryService;
    }

    /**
     * {@code POST  /key-valu-pair-holders} : Create a new keyValuPairHolder.
     *
     * @param keyValuPairHolderDTO the keyValuPairHolderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new keyValuPairHolderDTO, or with status {@code 400 (Bad Request)} if the keyValuPairHolder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/key-valu-pair-holders")
    public ResponseEntity<KeyValuPairHolderDTO> createKeyValuPairHolder(@RequestBody KeyValuPairHolderDTO keyValuPairHolderDTO) throws URISyntaxException {
        log.debug("REST request to save KeyValuPairHolder : {}", keyValuPairHolderDTO);
        if (keyValuPairHolderDTO.getId() != null) {
            throw new BadRequestAlertException("A new keyValuPairHolder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KeyValuPairHolderDTO result = keyValuPairHolderService.save(keyValuPairHolderDTO);
        return ResponseEntity.created(new URI("/api/key-valu-pair-holders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /key-valu-pair-holders} : Updates an existing keyValuPairHolder.
     *
     * @param keyValuPairHolderDTO the keyValuPairHolderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated keyValuPairHolderDTO,
     * or with status {@code 400 (Bad Request)} if the keyValuPairHolderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the keyValuPairHolderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/key-valu-pair-holders")
    public ResponseEntity<KeyValuPairHolderDTO> updateKeyValuPairHolder(@RequestBody KeyValuPairHolderDTO keyValuPairHolderDTO) throws URISyntaxException {
        log.debug("REST request to update KeyValuPairHolder : {}", keyValuPairHolderDTO);
        if (keyValuPairHolderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        KeyValuPairHolderDTO result = keyValuPairHolderService.save(keyValuPairHolderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, keyValuPairHolderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /key-valu-pair-holders} : get all the keyValuPairHolders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of keyValuPairHolders in body.
     */
    @GetMapping("/key-valu-pair-holders")
    public ResponseEntity<List<KeyValuPairHolderDTO>> getAllKeyValuPairHolders(KeyValuPairHolderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get KeyValuPairHolders by criteria: {}", criteria);
        Page<KeyValuPairHolderDTO> page = keyValuPairHolderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /key-valu-pair-holders/count} : count all the keyValuPairHolders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/key-valu-pair-holders/count")
    public ResponseEntity<Long> countKeyValuPairHolders(KeyValuPairHolderCriteria criteria) {
        log.debug("REST request to count KeyValuPairHolders by criteria: {}", criteria);
        return ResponseEntity.ok().body(keyValuPairHolderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /key-valu-pair-holders/:id} : get the "id" keyValuPairHolder.
     *
     * @param id the id of the keyValuPairHolderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the keyValuPairHolderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/key-valu-pair-holders/{id}")
    public ResponseEntity<KeyValuPairHolderDTO> getKeyValuPairHolder(@PathVariable Long id) {
        log.debug("REST request to get KeyValuPairHolder : {}", id);
        Optional<KeyValuPairHolderDTO> keyValuPairHolderDTO = keyValuPairHolderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(keyValuPairHolderDTO);
    }

    /**
     * {@code DELETE  /key-valu-pair-holders/:id} : delete the "id" keyValuPairHolder.
     *
     * @param id the id of the keyValuPairHolderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/key-valu-pair-holders/{id}")
    public ResponseEntity<Void> deleteKeyValuPairHolder(@PathVariable Long id) {
        log.debug("REST request to delete KeyValuPairHolder : {}", id);
        keyValuPairHolderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
