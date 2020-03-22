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

import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.domain.*; // for static metamodels
import et.com.delivereth.repository.TelegramUserRepository;
import et.com.delivereth.service.dto.TelegramUserCriteria;
import et.com.delivereth.service.dto.TelegramUserDTO;
import et.com.delivereth.service.mapper.TelegramUserMapper;

/**
 * Service for executing complex queries for {@link TelegramUser} entities in the database.
 * The main input is a {@link TelegramUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TelegramUserDTO} or a {@link Page} of {@link TelegramUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TelegramUserQueryService extends QueryService<TelegramUser> {

    private final Logger log = LoggerFactory.getLogger(TelegramUserQueryService.class);

    private final TelegramUserRepository telegramUserRepository;

    private final TelegramUserMapper telegramUserMapper;

    public TelegramUserQueryService(TelegramUserRepository telegramUserRepository, TelegramUserMapper telegramUserMapper) {
        this.telegramUserRepository = telegramUserRepository;
        this.telegramUserMapper = telegramUserMapper;
    }

    /**
     * Return a {@link List} of {@link TelegramUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TelegramUserDTO> findByCriteria(TelegramUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TelegramUser> specification = createSpecification(criteria);
        return telegramUserMapper.toDto(telegramUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TelegramUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TelegramUserDTO> findByCriteria(TelegramUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TelegramUser> specification = createSpecification(criteria);
        return telegramUserRepository.findAll(specification, page)
            .map(telegramUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TelegramUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TelegramUser> specification = createSpecification(criteria);
        return telegramUserRepository.count(specification);
    }

    /**
     * Function to convert {@link TelegramUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TelegramUser> createSpecification(TelegramUserCriteria criteria) {
        Specification<TelegramUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TelegramUser_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), TelegramUser_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), TelegramUser_.lastName));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), TelegramUser_.userName));
            }
            if (criteria.getChatId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChatId(), TelegramUser_.chatId));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), TelegramUser_.phone));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderId(),
                    root -> root.join(TelegramUser_.orders, JoinType.LEFT).get(Order_.id)));
            }
        }
        return specification;
    }
}
