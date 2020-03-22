package et.com.delivereth.service.impl;

import et.com.delivereth.service.TelegramUserService;
import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.repository.TelegramUserRepository;
import et.com.delivereth.service.dto.TelegramUserDTO;
import et.com.delivereth.service.mapper.TelegramUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TelegramUser}.
 */
@Service
@Transactional
public class TelegramUserServiceImpl implements TelegramUserService {

    private final Logger log = LoggerFactory.getLogger(TelegramUserServiceImpl.class);

    private final TelegramUserRepository telegramUserRepository;

    private final TelegramUserMapper telegramUserMapper;

    public TelegramUserServiceImpl(TelegramUserRepository telegramUserRepository, TelegramUserMapper telegramUserMapper) {
        this.telegramUserRepository = telegramUserRepository;
        this.telegramUserMapper = telegramUserMapper;
    }

    /**
     * Save a telegramUser.
     *
     * @param telegramUserDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TelegramUserDTO save(TelegramUserDTO telegramUserDTO) {
        log.debug("Request to save TelegramUser : {}", telegramUserDTO);
        TelegramUser telegramUser = telegramUserMapper.toEntity(telegramUserDTO);
        telegramUser = telegramUserRepository.save(telegramUser);
        return telegramUserMapper.toDto(telegramUser);
    }

    /**
     * Get all the telegramUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TelegramUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TelegramUsers");
        return telegramUserRepository.findAll(pageable)
            .map(telegramUserMapper::toDto);
    }

    /**
     * Get one telegramUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TelegramUserDTO> findOne(Long id) {
        log.debug("Request to get TelegramUser : {}", id);
        return telegramUserRepository.findById(id)
            .map(telegramUserMapper::toDto);
    }

    /**
     * Delete the telegramUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TelegramUser : {}", id);
        telegramUserRepository.deleteById(id);
    }
}
