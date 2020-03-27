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

import et.com.delivereth.domain.KeyValuPairHoler;
import et.com.delivereth.domain.*; // for static metamodels
import et.com.delivereth.repository.KeyValuPairHolerRepository;
import et.com.delivereth.service.dto.KeyValuPairHolerCriteria;
import et.com.delivereth.service.dto.KeyValuPairHolerDTO;
import et.com.delivereth.service.mapper.KeyValuPairHolerMapper;

/**
 * Service for executing complex queries for {@link KeyValuPairHoler} entities in the database.
 * The main input is a {@link KeyValuPairHolerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link KeyValuPairHolerDTO} or a {@link Page} of {@link KeyValuPairHolerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KeyValuPairHolerQueryService extends QueryService<KeyValuPairHoler> {

    private final Logger log = LoggerFactory.getLogger(KeyValuPairHolerQueryService.class);

    private final KeyValuPairHolerRepository keyValuPairHolerRepository;

    private final KeyValuPairHolerMapper keyValuPairHolerMapper;

    public KeyValuPairHolerQueryService(KeyValuPairHolerRepository keyValuPairHolerRepository, KeyValuPairHolerMapper keyValuPairHolerMapper) {
        this.keyValuPairHolerRepository = keyValuPairHolerRepository;
        this.keyValuPairHolerMapper = keyValuPairHolerMapper;
    }

    /**
     * Return a {@link List} of {@link KeyValuPairHolerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<KeyValuPairHolerDTO> findByCriteria(KeyValuPairHolerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<KeyValuPairHoler> specification = createSpecification(criteria);
        return keyValuPairHolerMapper.toDto(keyValuPairHolerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link KeyValuPairHolerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<KeyValuPairHolerDTO> findByCriteria(KeyValuPairHolerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<KeyValuPairHoler> specification = createSpecification(criteria);
        return keyValuPairHolerRepository.findAll(specification, page)
            .map(keyValuPairHolerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(KeyValuPairHolerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<KeyValuPairHoler> specification = createSpecification(criteria);
        return keyValuPairHolerRepository.count(specification);
    }

    /**
     * Function to convert {@link KeyValuPairHolerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<KeyValuPairHoler> createSpecification(KeyValuPairHolerCriteria criteria) {
        Specification<KeyValuPairHoler> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), KeyValuPairHoler_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), KeyValuPairHoler_.key));
            }
            if (criteria.getValueString() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValueString(), KeyValuPairHoler_.valueString));
            }
            if (criteria.getValueNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValueNumber(), KeyValuPairHoler_.valueNumber));
            }
        }
        return specification;
    }
}
