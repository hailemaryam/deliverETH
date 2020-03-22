package et.com.delivereth.service;

import et.com.delivereth.service.dto.FoodDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link et.com.delivereth.domain.Food}.
 */
public interface FoodService {

    /**
     * Save a food.
     *
     * @param foodDTO the entity to save.
     * @return the persisted entity.
     */
    FoodDTO save(FoodDTO foodDTO);

    /**
     * Get all the foods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FoodDTO> findAll(Pageable pageable);

    /**
     * Get the "id" food.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FoodDTO> findOne(Long id);

    /**
     * Delete the "id" food.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
