package et.com.delivereth.service.impl;

import et.com.delivereth.domain.Restorant;
import et.com.delivereth.repository.CustomRestaurantRepository;
import et.com.delivereth.service.CustomRestorantService;
import et.com.delivereth.service.dto.RestorantDTO;
import et.com.delivereth.service.mapper.RestorantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Restorant}.
 */
@Service
@Transactional
public class CustomRestorantServiceImpl implements CustomRestorantService {

    private final Logger log = LoggerFactory.getLogger(RestorantServiceImpl.class);

    private final CustomRestaurantRepository customRestaurantRepository;

    private final RestorantMapper restorantMapper;

    public CustomRestorantServiceImpl(CustomRestaurantRepository customRestaurantRepository, RestorantMapper restorantMapper) {
        this.customRestaurantRepository = customRestaurantRepository;
        this.restorantMapper = restorantMapper;
    }

    /**
     * Get all the restorants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RestorantDTO> findAllSorByProximity(Float latitude, Float longitue, Float distance, Pageable pageable) {
        log.debug("Request to get all Restorants");
        return customRestaurantRepository.findEntitiesByLocation(latitude, longitue, distance ,pageable)
            .map(restorantMapper::toDto);
    }
}
