package et.com.delivereth.service.impl;

import et.com.delivereth.service.TelegramDeliveryUserService;
import et.com.delivereth.domain.TelegramDeliveryUser;
import et.com.delivereth.repository.TelegramDeliveryUserRepository;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.mapper.TelegramDeliveryUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TelegramDeliveryUser}.
 */
@Service
@Transactional
public class TelegramDeliveryUserServiceImpl implements TelegramDeliveryUserService {

    private final Logger log = LoggerFactory.getLogger(TelegramDeliveryUserServiceImpl.class);

    private final TelegramDeliveryUserRepository telegramDeliveryUserRepository;

    private final TelegramDeliveryUserMapper telegramDeliveryUserMapper;

    public TelegramDeliveryUserServiceImpl(TelegramDeliveryUserRepository telegramDeliveryUserRepository, TelegramDeliveryUserMapper telegramDeliveryUserMapper) {
        this.telegramDeliveryUserRepository = telegramDeliveryUserRepository;
        this.telegramDeliveryUserMapper = telegramDeliveryUserMapper;
    }

    /**
     * Save a telegramDeliveryUser.
     *
     * @param telegramDeliveryUserDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TelegramDeliveryUserDTO save(TelegramDeliveryUserDTO telegramDeliveryUserDTO) {
        log.debug("Request to save TelegramDeliveryUser : {}", telegramDeliveryUserDTO);
        TelegramDeliveryUser telegramDeliveryUser = telegramDeliveryUserMapper.toEntity(telegramDeliveryUserDTO);
        telegramDeliveryUser = telegramDeliveryUserRepository.save(telegramDeliveryUser);
        return telegramDeliveryUserMapper.toDto(telegramDeliveryUser);
    }

    /**
     * Get all the telegramDeliveryUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TelegramDeliveryUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TelegramDeliveryUsers");
        return telegramDeliveryUserRepository.findAll(pageable)
            .map(telegramDeliveryUserMapper::toDto);
    }

    /**
     * Get one telegramDeliveryUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TelegramDeliveryUserDTO> findOne(Long id) {
        log.debug("Request to get TelegramDeliveryUser : {}", id);
        return telegramDeliveryUserRepository.findById(id)
            .map(telegramDeliveryUserMapper::toDto);
    }

    /**
     * Delete the telegramDeliveryUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TelegramDeliveryUser : {}", id);
        telegramDeliveryUserRepository.deleteById(id);
    }
}
