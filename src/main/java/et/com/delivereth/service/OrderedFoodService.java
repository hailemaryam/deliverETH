package et.com.delivereth.service;

import et.com.delivereth.service.dto.OrderedFoodDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link et.com.delivereth.domain.OrderedFood}.
 */
public interface OrderedFoodService {

    /**
     * Save a orderedFood.
     *
     * @param orderedFoodDTO the entity to save.
     * @return the persisted entity.
     */
    OrderedFoodDTO save(OrderedFoodDTO orderedFoodDTO);

    /**
     * Get all the orderedFoods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderedFoodDTO> findAll(Pageable pageable);

    /**
     * Get the "id" orderedFood.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderedFoodDTO> findOne(Long id);

    /**
     * Delete the "id" orderedFood.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
