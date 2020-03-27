package et.com.delivereth.service;

import et.com.delivereth.service.dto.KeyValuPairHolderDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link et.com.delivereth.domain.KeyValuPairHolder}.
 */
public interface KeyValuPairHolderService {

    /**
     * Save a keyValuPairHolder.
     *
     * @param keyValuPairHolderDTO the entity to save.
     * @return the persisted entity.
     */
    KeyValuPairHolderDTO save(KeyValuPairHolderDTO keyValuPairHolderDTO);

    /**
     * Get all the keyValuPairHolders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<KeyValuPairHolderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" keyValuPairHolder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KeyValuPairHolderDTO> findOne(Long id);

    /**
     * Delete the "id" keyValuPairHolder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
