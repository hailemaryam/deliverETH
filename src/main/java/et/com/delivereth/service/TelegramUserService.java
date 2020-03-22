package et.com.delivereth.service;

import et.com.delivereth.service.dto.TelegramUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link et.com.delivereth.domain.TelegramUser}.
 */
public interface TelegramUserService {

    /**
     * Save a telegramUser.
     *
     * @param telegramUserDTO the entity to save.
     * @return the persisted entity.
     */
    TelegramUserDTO save(TelegramUserDTO telegramUserDTO);

    /**
     * Get all the telegramUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TelegramUserDTO> findAll(Pageable pageable);

    /**
     * Get the "id" telegramUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TelegramUserDTO> findOne(Long id);

    /**
     * Delete the "id" telegramUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
