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

import et.com.delivereth.domain.OrderedFood;
import et.com.delivereth.domain.*; // for static metamodels
import et.com.delivereth.repository.OrderedFoodRepository;
import et.com.delivereth.service.dto.OrderedFoodCriteria;
import et.com.delivereth.service.dto.OrderedFoodDTO;
import et.com.delivereth.service.mapper.OrderedFoodMapper;

/**
 * Service for executing complex queries for {@link OrderedFood} entities in the database.
 * The main input is a {@link OrderedFoodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderedFoodDTO} or a {@link Page} of {@link OrderedFoodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderedFoodQueryService extends QueryService<OrderedFood> {

    private final Logger log = LoggerFactory.getLogger(OrderedFoodQueryService.class);

    private final OrderedFoodRepository orderedFoodRepository;

    private final OrderedFoodMapper orderedFoodMapper;

    public OrderedFoodQueryService(OrderedFoodRepository orderedFoodRepository, OrderedFoodMapper orderedFoodMapper) {
        this.orderedFoodRepository = orderedFoodRepository;
        this.orderedFoodMapper = orderedFoodMapper;
    }

    /**
     * Return a {@link List} of {@link OrderedFoodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderedFoodDTO> findByCriteria(OrderedFoodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderedFood> specification = createSpecification(criteria);
        return orderedFoodMapper.toDto(orderedFoodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrderedFoodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderedFoodDTO> findByCriteria(OrderedFoodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderedFood> specification = createSpecification(criteria);
        return orderedFoodRepository.findAll(specification, page)
            .map(orderedFoodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderedFoodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderedFood> specification = createSpecification(criteria);
        return orderedFoodRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderedFoodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderedFood> createSpecification(OrderedFoodCriteria criteria) {
        Specification<OrderedFood> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderedFood_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), OrderedFood_.quantity));
            }
            if (criteria.getFoodId() != null) {
                specification = specification.and(buildSpecification(criteria.getFoodId(),
                    root -> root.join(OrderedFood_.food, JoinType.LEFT).get(Food_.id)));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderId(),
                    root -> root.join(OrderedFood_.order, JoinType.LEFT).get(Order_.id)));
            }
        }
        return specification;
    }
}
