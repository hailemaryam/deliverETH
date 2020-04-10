package et.com.delivereth.service.impl;

import et.com.delivereth.service.TelegramRestaurantUserService;
import et.com.delivereth.domain.TelegramRestaurantUser;
import et.com.delivereth.repository.TelegramRestaurantUserRepository;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import et.com.delivereth.service.mapper.TelegramRestaurantUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TelegramRestaurantUser}.
 */
@Service
@Transactional
public class TelegramRestaurantUserServiceImpl implements TelegramRestaurantUserService {

    private final Logger log = LoggerFactory.getLogger(TelegramRestaurantUserServiceImpl.class);

    private final TelegramRestaurantUserRepository telegramRestaurantUserRepository;

    private final TelegramRestaurantUserMapper telegramRestaurantUserMapper;

    public TelegramRestaurantUserServiceImpl(TelegramRestaurantUserRepository telegramRestaurantUserRepository, TelegramRestaurantUserMapper telegramRestaurantUserMapper) {
        this.telegramRestaurantUserRepository = telegramRestaurantUserRepository;
        this.telegramRestaurantUserMapper = telegramRestaurantUserMapper;
    }

    /**
     * Save a telegramRestaurantUser.
     *
     * @param telegramRestaurantUserDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TelegramRestaurantUserDTO save(TelegramRestaurantUserDTO telegramRestaurantUserDTO) {
        log.debug("Request to save TelegramRestaurantUser : {}", telegramRestaurantUserDTO);
        TelegramRestaurantUser telegramRestaurantUser = telegramRestaurantUserMapper.toEntity(telegramRestaurantUserDTO);
        telegramRestaurantUser = telegramRestaurantUserRepository.save(telegramRestaurantUser);
        return telegramRestaurantUserMapper.toDto(telegramRestaurantUser);
    }

    /**
     * Get all the telegramRestaurantUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TelegramRestaurantUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TelegramRestaurantUsers");
        return telegramRestaurantUserRepository.findAll(pageable)
            .map(telegramRestaurantUserMapper::toDto);
    }

    /**
     * Get all the telegramRestaurantUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TelegramRestaurantUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return telegramRestaurantUserRepository.findAllWithEagerRelationships(pageable).map(telegramRestaurantUserMapper::toDto);
    }

    /**
     * Get one telegramRestaurantUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TelegramRestaurantUserDTO> findOne(Long id) {
        log.debug("Request to get TelegramRestaurantUser : {}", id);
        return telegramRestaurantUserRepository.findOneWithEagerRelationships(id)
            .map(telegramRestaurantUserMapper::toDto);
    }

    /**
     * Delete the telegramRestaurantUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TelegramRestaurantUser : {}", id);
        telegramRestaurantUserRepository.deleteById(id);
    }
}
