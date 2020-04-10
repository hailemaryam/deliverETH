package et.com.delivereth.service;

import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link et.com.delivereth.domain.TelegramRestaurantUser}.
 */
public interface TelegramRestaurantUserService {

    /**
     * Save a telegramRestaurantUser.
     *
     * @param telegramRestaurantUserDTO the entity to save.
     * @return the persisted entity.
     */
    TelegramRestaurantUserDTO save(TelegramRestaurantUserDTO telegramRestaurantUserDTO);

    /**
     * Get all the telegramRestaurantUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TelegramRestaurantUserDTO> findAll(Pageable pageable);

    /**
     * Get all the telegramRestaurantUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<TelegramRestaurantUserDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" telegramRestaurantUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TelegramRestaurantUserDTO> findOne(Long id);

    /**
     * Delete the "id" telegramRestaurantUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
