package et.com.delivereth.service;

import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link et.com.delivereth.domain.TelegramDeliveryUser}.
 */
public interface TelegramDeliveryUserService {

    /**
     * Save a telegramDeliveryUser.
     *
     * @param telegramDeliveryUserDTO the entity to save.
     * @return the persisted entity.
     */
    TelegramDeliveryUserDTO save(TelegramDeliveryUserDTO telegramDeliveryUserDTO);

    /**
     * Get all the telegramDeliveryUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TelegramDeliveryUserDTO> findAll(Pageable pageable);

    /**
     * Get the "id" telegramDeliveryUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TelegramDeliveryUserDTO> findOne(Long id);

    /**
     * Delete the "id" telegramDeliveryUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
