package et.com.delivereth.service.impl;

import et.com.delivereth.service.RestorantService;
import et.com.delivereth.domain.Restorant;
import et.com.delivereth.repository.RestorantRepository;
import et.com.delivereth.service.dto.RestorantDTO;
import et.com.delivereth.service.mapper.RestorantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Restorant}.
 */
@Service
@Transactional
public class RestorantServiceImpl implements RestorantService {

    private final Logger log = LoggerFactory.getLogger(RestorantServiceImpl.class);

    private final RestorantRepository restorantRepository;

    private final RestorantMapper restorantMapper;

    public RestorantServiceImpl(RestorantRepository restorantRepository, RestorantMapper restorantMapper) {
        this.restorantRepository = restorantRepository;
        this.restorantMapper = restorantMapper;
    }

    /**
     * Save a restorant.
     *
     * @param restorantDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RestorantDTO save(RestorantDTO restorantDTO) {
        log.debug("Request to save Restorant : {}", restorantDTO);
        Restorant restorant = restorantMapper.toEntity(restorantDTO);
        restorant = restorantRepository.save(restorant);
        return restorantMapper.toDto(restorant);
    }

    /**
     * Get all the restorants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RestorantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Restorants");
        return restorantRepository.findAll(pageable)
            .map(restorantMapper::toDto);
    }

    /**
     * Get one restorant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RestorantDTO> findOne(Long id) {
        log.debug("Request to get Restorant : {}", id);
        return restorantRepository.findById(id)
            .map(restorantMapper::toDto);
    }

    /**
     * Delete the restorant by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Restorant : {}", id);
        restorantRepository.deleteById(id);
    }
}
