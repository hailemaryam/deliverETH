package et.com.delivereth.web.rest;

import et.com.delivereth.service.KeyValuPairHolerService;
import et.com.delivereth.web.rest.errors.BadRequestAlertException;
import et.com.delivereth.service.dto.KeyValuPairHolerDTO;
import et.com.delivereth.service.dto.KeyValuPairHolerCriteria;
import et.com.delivereth.service.KeyValuPairHolerQueryService;

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
 * REST controller for managing {@link et.com.delivereth.domain.KeyValuPairHoler}.
 */
@RestController
@RequestMapping("/api")
public class KeyValuPairHolerResource {

    private final Logger log = LoggerFactory.getLogger(KeyValuPairHolerResource.class);

    private static final String ENTITY_NAME = "keyValuPairHoler";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KeyValuPairHolerService keyValuPairHolerService;

    private final KeyValuPairHolerQueryService keyValuPairHolerQueryService;

    public KeyValuPairHolerResource(KeyValuPairHolerService keyValuPairHolerService, KeyValuPairHolerQueryService keyValuPairHolerQueryService) {
        this.keyValuPairHolerService = keyValuPairHolerService;
        this.keyValuPairHolerQueryService = keyValuPairHolerQueryService;
    }

    /**
     * {@code POST  /key-valu-pair-holers} : Create a new keyValuPairHoler.
     *
     * @param keyValuPairHolerDTO the keyValuPairHolerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new keyValuPairHolerDTO, or with status {@code 400 (Bad Request)} if the keyValuPairHoler has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/key-valu-pair-holers")
    public ResponseEntity<KeyValuPairHolerDTO> createKeyValuPairHoler(@RequestBody KeyValuPairHolerDTO keyValuPairHolerDTO) throws URISyntaxException {
        log.debug("REST request to save KeyValuPairHoler : {}", keyValuPairHolerDTO);
        if (keyValuPairHolerDTO.getId() != null) {
            throw new BadRequestAlertException("A new keyValuPairHoler cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KeyValuPairHolerDTO result = keyValuPairHolerService.save(keyValuPairHolerDTO);
        return ResponseEntity.created(new URI("/api/key-valu-pair-holers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /key-valu-pair-holers} : Updates an existing keyValuPairHoler.
     *
     * @param keyValuPairHolerDTO the keyValuPairHolerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated keyValuPairHolerDTO,
     * or with status {@code 400 (Bad Request)} if the keyValuPairHolerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the keyValuPairHolerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/key-valu-pair-holers")
    public ResponseEntity<KeyValuPairHolerDTO> updateKeyValuPairHoler(@RequestBody KeyValuPairHolerDTO keyValuPairHolerDTO) throws URISyntaxException {
        log.debug("REST request to update KeyValuPairHoler : {}", keyValuPairHolerDTO);
        if (keyValuPairHolerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        KeyValuPairHolerDTO result = keyValuPairHolerService.save(keyValuPairHolerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, keyValuPairHolerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /key-valu-pair-holers} : get all the keyValuPairHolers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of keyValuPairHolers in body.
     */
    @GetMapping("/key-valu-pair-holers")
    public ResponseEntity<List<KeyValuPairHolerDTO>> getAllKeyValuPairHolers(KeyValuPairHolerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get KeyValuPairHolers by criteria: {}", criteria);
        Page<KeyValuPairHolerDTO> page = keyValuPairHolerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /key-valu-pair-holers/count} : count all the keyValuPairHolers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/key-valu-pair-holers/count")
    public ResponseEntity<Long> countKeyValuPairHolers(KeyValuPairHolerCriteria criteria) {
        log.debug("REST request to count KeyValuPairHolers by criteria: {}", criteria);
        return ResponseEntity.ok().body(keyValuPairHolerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /key-valu-pair-holers/:id} : get the "id" keyValuPairHoler.
     *
     * @param id the id of the keyValuPairHolerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the keyValuPairHolerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/key-valu-pair-holers/{id}")
    public ResponseEntity<KeyValuPairHolerDTO> getKeyValuPairHoler(@PathVariable Long id) {
        log.debug("REST request to get KeyValuPairHoler : {}", id);
        Optional<KeyValuPairHolerDTO> keyValuPairHolerDTO = keyValuPairHolerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(keyValuPairHolerDTO);
    }

    /**
     * {@code DELETE  /key-valu-pair-holers/:id} : delete the "id" keyValuPairHoler.
     *
     * @param id the id of the keyValuPairHolerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/key-valu-pair-holers/{id}")
    public ResponseEntity<Void> deleteKeyValuPairHoler(@PathVariable Long id) {
        log.debug("REST request to delete KeyValuPairHoler : {}", id);
        keyValuPairHolerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
