package et.com.delivereth.service;

import et.com.delivereth.service.dto.RestorantDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link et.com.delivereth.domain.Restorant}.
 */
public interface RestorantService {

    /**
     * Save a restorant.
     *
     * @param restorantDTO the entity to save.
     * @return the persisted entity.
     */
    RestorantDTO save(RestorantDTO restorantDTO);

    /**
     * Get all the restorants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RestorantDTO> findAll(Pageable pageable);

    /**
     * Get the "id" restorant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RestorantDTO> findOne(Long id);

    /**
     * Delete the "id" restorant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
