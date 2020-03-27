package et.com.delivereth.service;

import et.com.delivereth.service.dto.KeyValuPairHolerDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link et.com.delivereth.domain.KeyValuPairHoler}.
 */
public interface KeyValuPairHolerService {

    /**
     * Save a keyValuPairHoler.
     *
     * @param keyValuPairHolerDTO the entity to save.
     * @return the persisted entity.
     */
    KeyValuPairHolerDTO save(KeyValuPairHolerDTO keyValuPairHolerDTO);

    /**
     * Get all the keyValuPairHolers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<KeyValuPairHolerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" keyValuPairHoler.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KeyValuPairHolerDTO> findOne(Long id);

    /**
     * Delete the "id" keyValuPairHoler.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
