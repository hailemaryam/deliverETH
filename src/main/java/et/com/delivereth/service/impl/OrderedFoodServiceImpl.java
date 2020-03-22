package et.com.delivereth.service.impl;

import et.com.delivereth.service.OrderedFoodService;
import et.com.delivereth.domain.OrderedFood;
import et.com.delivereth.repository.OrderedFoodRepository;
import et.com.delivereth.service.dto.OrderedFoodDTO;
import et.com.delivereth.service.mapper.OrderedFoodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link OrderedFood}.
 */
@Service
@Transactional
public class OrderedFoodServiceImpl implements OrderedFoodService {

    private final Logger log = LoggerFactory.getLogger(OrderedFoodServiceImpl.class);

    private final OrderedFoodRepository orderedFoodRepository;

    private final OrderedFoodMapper orderedFoodMapper;

    public OrderedFoodServiceImpl(OrderedFoodRepository orderedFoodRepository, OrderedFoodMapper orderedFoodMapper) {
        this.orderedFoodRepository = orderedFoodRepository;
        this.orderedFoodMapper = orderedFoodMapper;
    }

    /**
     * Save a orderedFood.
     *
     * @param orderedFoodDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public OrderedFoodDTO save(OrderedFoodDTO orderedFoodDTO) {
        log.debug("Request to save OrderedFood : {}", orderedFoodDTO);
        OrderedFood orderedFood = orderedFoodMapper.toEntity(orderedFoodDTO);
        orderedFood = orderedFoodRepository.save(orderedFood);
        return orderedFoodMapper.toDto(orderedFood);
    }

    /**
     * Get all the orderedFoods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderedFoodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderedFoods");
        return orderedFoodRepository.findAll(pageable)
            .map(orderedFoodMapper::toDto);
    }

    /**
     * Get one orderedFood by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderedFoodDTO> findOne(Long id) {
        log.debug("Request to get OrderedFood : {}", id);
        return orderedFoodRepository.findById(id)
            .map(orderedFoodMapper::toDto);
    }

    /**
     * Delete the orderedFood by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderedFood : {}", id);
        orderedFoodRepository.deleteById(id);
    }
}
