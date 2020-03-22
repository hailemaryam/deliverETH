package et.com.delivereth.web.rest;

import et.com.delivereth.service.OrderedFoodService;
import et.com.delivereth.web.rest.errors.BadRequestAlertException;
import et.com.delivereth.service.dto.OrderedFoodDTO;
import et.com.delivereth.service.dto.OrderedFoodCriteria;
import et.com.delivereth.service.OrderedFoodQueryService;

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
 * REST controller for managing {@link et.com.delivereth.domain.OrderedFood}.
 */
@RestController
@RequestMapping("/api")
public class OrderedFoodResource {

    private final Logger log = LoggerFactory.getLogger(OrderedFoodResource.class);

    private static final String ENTITY_NAME = "orderedFood";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderedFoodService orderedFoodService;

    private final OrderedFoodQueryService orderedFoodQueryService;

    public OrderedFoodResource(OrderedFoodService orderedFoodService, OrderedFoodQueryService orderedFoodQueryService) {
        this.orderedFoodService = orderedFoodService;
        this.orderedFoodQueryService = orderedFoodQueryService;
    }

    /**
     * {@code POST  /ordered-foods} : Create a new orderedFood.
     *
     * @param orderedFoodDTO the orderedFoodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderedFoodDTO, or with status {@code 400 (Bad Request)} if the orderedFood has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordered-foods")
    public ResponseEntity<OrderedFoodDTO> createOrderedFood(@RequestBody OrderedFoodDTO orderedFoodDTO) throws URISyntaxException {
        log.debug("REST request to save OrderedFood : {}", orderedFoodDTO);
        if (orderedFoodDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderedFood cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderedFoodDTO result = orderedFoodService.save(orderedFoodDTO);
        return ResponseEntity.created(new URI("/api/ordered-foods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordered-foods} : Updates an existing orderedFood.
     *
     * @param orderedFoodDTO the orderedFoodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderedFoodDTO,
     * or with status {@code 400 (Bad Request)} if the orderedFoodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderedFoodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordered-foods")
    public ResponseEntity<OrderedFoodDTO> updateOrderedFood(@RequestBody OrderedFoodDTO orderedFoodDTO) throws URISyntaxException {
        log.debug("REST request to update OrderedFood : {}", orderedFoodDTO);
        if (orderedFoodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderedFoodDTO result = orderedFoodService.save(orderedFoodDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orderedFoodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ordered-foods} : get all the orderedFoods.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderedFoods in body.
     */
    @GetMapping("/ordered-foods")
    public ResponseEntity<List<OrderedFoodDTO>> getAllOrderedFoods(OrderedFoodCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrderedFoods by criteria: {}", criteria);
        Page<OrderedFoodDTO> page = orderedFoodQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ordered-foods/count} : count all the orderedFoods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ordered-foods/count")
    public ResponseEntity<Long> countOrderedFoods(OrderedFoodCriteria criteria) {
        log.debug("REST request to count OrderedFoods by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderedFoodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ordered-foods/:id} : get the "id" orderedFood.
     *
     * @param id the id of the orderedFoodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderedFoodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordered-foods/{id}")
    public ResponseEntity<OrderedFoodDTO> getOrderedFood(@PathVariable Long id) {
        log.debug("REST request to get OrderedFood : {}", id);
        Optional<OrderedFoodDTO> orderedFoodDTO = orderedFoodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderedFoodDTO);
    }

    /**
     * {@code DELETE  /ordered-foods/:id} : delete the "id" orderedFood.
     *
     * @param id the id of the orderedFoodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordered-foods/{id}")
    public ResponseEntity<Void> deleteOrderedFood(@PathVariable Long id) {
        log.debug("REST request to delete OrderedFood : {}", id);
        orderedFoodService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
