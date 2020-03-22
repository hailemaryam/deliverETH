package et.com.delivereth.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import et.com.delivereth.domain.Food;
import et.com.delivereth.domain.*; // for static metamodels
import et.com.delivereth.repository.FoodRepository;
import et.com.delivereth.service.dto.FoodCriteria;
import et.com.delivereth.service.dto.FoodDTO;
import et.com.delivereth.service.mapper.FoodMapper;

/**
 * Service for executing complex queries for {@link Food} entities in the database.
 * The main input is a {@link FoodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FoodDTO} or a {@link Page} of {@link FoodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FoodQueryService extends QueryService<Food> {

    private final Logger log = LoggerFactory.getLogger(FoodQueryService.class);

    private final FoodRepository foodRepository;

    private final FoodMapper foodMapper;

    public FoodQueryService(FoodRepository foodRepository, FoodMapper foodMapper) {
        this.foodRepository = foodRepository;
        this.foodMapper = foodMapper;
    }

    /**
     * Return a {@link List} of {@link FoodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FoodDTO> findByCriteria(FoodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Food> specification = createSpecification(criteria);
        return foodMapper.toDto(foodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FoodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FoodDTO> findByCriteria(FoodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Food> specification = createSpecification(criteria);
        return foodRepository.findAll(specification, page)
            .map(foodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FoodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Food> specification = createSpecification(criteria);
        return foodRepository.count(specification);
    }

    /**
     * Function to convert {@link FoodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Food> createSpecification(FoodCriteria criteria) {
        Specification<Food> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Food_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Food_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Food_.price));
            }
            if (criteria.getOrderedFoodId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderedFoodId(),
                    root -> root.join(Food_.orderedFoods, JoinType.LEFT).get(OrderedFood_.id)));
            }
            if (criteria.getRestorantId() != null) {
                specification = specification.and(buildSpecification(criteria.getRestorantId(),
                    root -> root.join(Food_.restorant, JoinType.LEFT).get(Restorant_.id)));
            }
        }
        return specification;
    }
}
