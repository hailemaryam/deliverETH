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

import et.com.delivereth.domain.KeyValuPairHolder;
import et.com.delivereth.domain.*; // for static metamodels
import et.com.delivereth.repository.KeyValuPairHolderRepository;
import et.com.delivereth.service.dto.KeyValuPairHolderCriteria;
import et.com.delivereth.service.dto.KeyValuPairHolderDTO;
import et.com.delivereth.service.mapper.KeyValuPairHolderMapper;

/**
 * Service for executing complex queries for {@link KeyValuPairHolder} entities in the database.
 * The main input is a {@link KeyValuPairHolderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link KeyValuPairHolderDTO} or a {@link Page} of {@link KeyValuPairHolderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KeyValuPairHolderQueryService extends QueryService<KeyValuPairHolder> {

    private final Logger log = LoggerFactory.getLogger(KeyValuPairHolderQueryService.class);

    private final KeyValuPairHolderRepository keyValuPairHolderRepository;

    private final KeyValuPairHolderMapper keyValuPairHolderMapper;

    public KeyValuPairHolderQueryService(KeyValuPairHolderRepository keyValuPairHolderRepository, KeyValuPairHolderMapper keyValuPairHolderMapper) {
        this.keyValuPairHolderRepository = keyValuPairHolderRepository;
        this.keyValuPairHolderMapper = keyValuPairHolderMapper;
    }

    /**
     * Return a {@link List} of {@link KeyValuPairHolderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<KeyValuPairHolderDTO> findByCriteria(KeyValuPairHolderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<KeyValuPairHolder> specification = createSpecification(criteria);
        return keyValuPairHolderMapper.toDto(keyValuPairHolderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link KeyValuPairHolderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<KeyValuPairHolderDTO> findByCriteria(KeyValuPairHolderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<KeyValuPairHolder> specification = createSpecification(criteria);
        return keyValuPairHolderRepository.findAll(specification, page)
            .map(keyValuPairHolderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(KeyValuPairHolderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<KeyValuPairHolder> specification = createSpecification(criteria);
        return keyValuPairHolderRepository.count(specification);
    }

    /**
     * Function to convert {@link KeyValuPairHolderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<KeyValuPairHolder> createSpecification(KeyValuPairHolderCriteria criteria) {
        Specification<KeyValuPairHolder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), KeyValuPairHolder_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), KeyValuPairHolder_.key));
            }
            if (criteria.getValueString() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValueString(), KeyValuPairHolder_.valueString));
            }
            if (criteria.getValueNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValueNumber(), KeyValuPairHolder_.valueNumber));
            }
        }
        return specification;
    }
}
