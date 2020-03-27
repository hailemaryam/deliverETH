package et.com.delivereth.service.impl;

import et.com.delivereth.service.KeyValuPairHolderService;
import et.com.delivereth.domain.KeyValuPairHolder;
import et.com.delivereth.repository.KeyValuPairHolderRepository;
import et.com.delivereth.service.dto.KeyValuPairHolderDTO;
import et.com.delivereth.service.mapper.KeyValuPairHolderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link KeyValuPairHolder}.
 */
@Service
@Transactional
public class KeyValuPairHolderServiceImpl implements KeyValuPairHolderService {

    private final Logger log = LoggerFactory.getLogger(KeyValuPairHolderServiceImpl.class);

    private final KeyValuPairHolderRepository keyValuPairHolderRepository;

    private final KeyValuPairHolderMapper keyValuPairHolderMapper;

    public KeyValuPairHolderServiceImpl(KeyValuPairHolderRepository keyValuPairHolderRepository, KeyValuPairHolderMapper keyValuPairHolderMapper) {
        this.keyValuPairHolderRepository = keyValuPairHolderRepository;
        this.keyValuPairHolderMapper = keyValuPairHolderMapper;
    }

    /**
     * Save a keyValuPairHolder.
     *
     * @param keyValuPairHolderDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public KeyValuPairHolderDTO save(KeyValuPairHolderDTO keyValuPairHolderDTO) {
        log.debug("Request to save KeyValuPairHolder : {}", keyValuPairHolderDTO);
        KeyValuPairHolder keyValuPairHolder = keyValuPairHolderMapper.toEntity(keyValuPairHolderDTO);
        keyValuPairHolder = keyValuPairHolderRepository.save(keyValuPairHolder);
        return keyValuPairHolderMapper.toDto(keyValuPairHolder);
    }

    /**
     * Get all the keyValuPairHolders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<KeyValuPairHolderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all KeyValuPairHolders");
        return keyValuPairHolderRepository.findAll(pageable)
            .map(keyValuPairHolderMapper::toDto);
    }

    /**
     * Get one keyValuPairHolder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<KeyValuPairHolderDTO> findOne(Long id) {
        log.debug("Request to get KeyValuPairHolder : {}", id);
        return keyValuPairHolderRepository.findById(id)
            .map(keyValuPairHolderMapper::toDto);
    }

    /**
     * Delete the keyValuPairHolder by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete KeyValuPairHolder : {}", id);
        keyValuPairHolderRepository.deleteById(id);
    }
}
