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

import et.com.delivereth.domain.Restorant;
import et.com.delivereth.domain.*; // for static metamodels
import et.com.delivereth.repository.RestorantRepository;
import et.com.delivereth.service.dto.RestorantCriteria;
import et.com.delivereth.service.dto.RestorantDTO;
import et.com.delivereth.service.mapper.RestorantMapper;

/**
 * Service for executing complex queries for {@link Restorant} entities in the database.
 * The main input is a {@link RestorantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RestorantDTO} or a {@link Page} of {@link RestorantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RestorantQueryService extends QueryService<Restorant> {

    private final Logger log = LoggerFactory.getLogger(RestorantQueryService.class);

    private final RestorantRepository restorantRepository;

    private final RestorantMapper restorantMapper;

    public RestorantQueryService(RestorantRepository restorantRepository, RestorantMapper restorantMapper) {
        this.restorantRepository = restorantRepository;
        this.restorantMapper = restorantMapper;
    }

    /**
     * Return a {@link List} of {@link RestorantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RestorantDTO> findByCriteria(RestorantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Restorant> specification = createSpecification(criteria);
        return restorantMapper.toDto(restorantRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RestorantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RestorantDTO> findByCriteria(RestorantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Restorant> specification = createSpecification(criteria);
        return restorantRepository.findAll(specification, page)
            .map(restorantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RestorantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Restorant> specification = createSpecification(criteria);
        return restorantRepository.count(specification);
    }

    /**
     * Function to convert {@link RestorantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Restorant> createSpecification(RestorantCriteria criteria) {
        Specification<Restorant> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Restorant_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Restorant_.name));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), Restorant_.userName));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Restorant_.latitude));
            }
            if (criteria.getLongtude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongtude(), Restorant_.longtude));
            }
            if (criteria.getFoodId() != null) {
                specification = specification.and(buildSpecification(criteria.getFoodId(),
                    root -> root.join(Restorant_.foods, JoinType.LEFT).get(Food_.id)));
            }
        }
        return specification;
    }
}
