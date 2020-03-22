package et.com.delivereth.service.impl;

import et.com.delivereth.service.FoodService;
import et.com.delivereth.domain.Food;
import et.com.delivereth.repository.FoodRepository;
import et.com.delivereth.service.dto.FoodDTO;
import et.com.delivereth.service.mapper.FoodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Food}.
 */
@Service
@Transactional
public class FoodServiceImpl implements FoodService {

    private final Logger log = LoggerFactory.getLogger(FoodServiceImpl.class);

    private final FoodRepository foodRepository;

    private final FoodMapper foodMapper;

    public FoodServiceImpl(FoodRepository foodRepository, FoodMapper foodMapper) {
        this.foodRepository = foodRepository;
        this.foodMapper = foodMapper;
    }

    /**
     * Save a food.
     *
     * @param foodDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FoodDTO save(FoodDTO foodDTO) {
        log.debug("Request to save Food : {}", foodDTO);
        Food food = foodMapper.toEntity(foodDTO);
        food = foodRepository.save(food);
        return foodMapper.toDto(food);
    }

    /**
     * Get all the foods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FoodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Foods");
        return foodRepository.findAll(pageable)
            .map(foodMapper::toDto);
    }

    /**
     * Get one food by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FoodDTO> findOne(Long id) {
        log.debug("Request to get Food : {}", id);
        return foodRepository.findById(id)
            .map(foodMapper::toDto);
    }

    /**
     * Delete the food by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Food : {}", id);
        foodRepository.deleteById(id);
    }
}
