package et.com.delivereth.service.impl;

import et.com.delivereth.service.KeyValuPairHolerService;
import et.com.delivereth.domain.KeyValuPairHoler;
import et.com.delivereth.repository.KeyValuPairHolerRepository;
import et.com.delivereth.service.dto.KeyValuPairHolerDTO;
import et.com.delivereth.service.mapper.KeyValuPairHolerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link KeyValuPairHoler}.
 */
@Service
@Transactional
public class KeyValuPairHolerServiceImpl implements KeyValuPairHolerService {

    private final Logger log = LoggerFactory.getLogger(KeyValuPairHolerServiceImpl.class);

    private final KeyValuPairHolerRepository keyValuPairHolerRepository;

    private final KeyValuPairHolerMapper keyValuPairHolerMapper;

    public KeyValuPairHolerServiceImpl(KeyValuPairHolerRepository keyValuPairHolerRepository, KeyValuPairHolerMapper keyValuPairHolerMapper) {
        this.keyValuPairHolerRepository = keyValuPairHolerRepository;
        this.keyValuPairHolerMapper = keyValuPairHolerMapper;
    }

    /**
     * Save a keyValuPairHoler.
     *
     * @param keyValuPairHolerDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public KeyValuPairHolerDTO save(KeyValuPairHolerDTO keyValuPairHolerDTO) {
        log.debug("Request to save KeyValuPairHoler : {}", keyValuPairHolerDTO);
        KeyValuPairHoler keyValuPairHoler = keyValuPairHolerMapper.toEntity(keyValuPairHolerDTO);
        keyValuPairHoler = keyValuPairHolerRepository.save(keyValuPairHoler);
        return keyValuPairHolerMapper.toDto(keyValuPairHoler);
    }

    /**
     * Get all the keyValuPairHolers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<KeyValuPairHolerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all KeyValuPairHolers");
        return keyValuPairHolerRepository.findAll(pageable)
            .map(keyValuPairHolerMapper::toDto);
    }

    /**
     * Get one keyValuPairHoler by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<KeyValuPairHolerDTO> findOne(Long id) {
        log.debug("Request to get KeyValuPairHoler : {}", id);
        return keyValuPairHolerRepository.findById(id)
            .map(keyValuPairHolerMapper::toDto);
    }

    /**
     * Delete the keyValuPairHoler by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete KeyValuPairHoler : {}", id);
        keyValuPairHolerRepository.deleteById(id);
    }
}
