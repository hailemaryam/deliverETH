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

import et.com.delivereth.domain.TelegramRestaurantUser;
import et.com.delivereth.domain.*; // for static metamodels
import et.com.delivereth.repository.TelegramRestaurantUserRepository;
import et.com.delivereth.service.dto.TelegramRestaurantUserCriteria;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import et.com.delivereth.service.mapper.TelegramRestaurantUserMapper;

/**
 * Service for executing complex queries for {@link TelegramRestaurantUser} entities in the database.
 * The main input is a {@link TelegramRestaurantUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TelegramRestaurantUserDTO} or a {@link Page} of {@link TelegramRestaurantUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TelegramRestaurantUserQueryService extends QueryService<TelegramRestaurantUser> {

    private final Logger log = LoggerFactory.getLogger(TelegramRestaurantUserQueryService.class);

    private final TelegramRestaurantUserRepository telegramRestaurantUserRepository;

    private final TelegramRestaurantUserMapper telegramRestaurantUserMapper;

    public TelegramRestaurantUserQueryService(TelegramRestaurantUserRepository telegramRestaurantUserRepository, TelegramRestaurantUserMapper telegramRestaurantUserMapper) {
        this.telegramRestaurantUserRepository = telegramRestaurantUserRepository;
        this.telegramRestaurantUserMapper = telegramRestaurantUserMapper;
    }

    /**
     * Return a {@link List} of {@link TelegramRestaurantUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TelegramRestaurantUserDTO> findByCriteria(TelegramRestaurantUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TelegramRestaurantUser> specification = createSpecification(criteria);
        return telegramRestaurantUserMapper.toDto(telegramRestaurantUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TelegramRestaurantUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TelegramRestaurantUserDTO> findByCriteria(TelegramRestaurantUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TelegramRestaurantUser> specification = createSpecification(criteria);
        return telegramRestaurantUserRepository.findAll(specification, page)
            .map(telegramRestaurantUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TelegramRestaurantUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TelegramRestaurantUser> specification = createSpecification(criteria);
        return telegramRestaurantUserRepository.count(specification);
    }

    /**
     * Function to convert {@link TelegramRestaurantUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TelegramRestaurantUser> createSpecification(TelegramRestaurantUserCriteria criteria) {
        Specification<TelegramRestaurantUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TelegramRestaurantUser_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), TelegramRestaurantUser_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), TelegramRestaurantUser_.lastName));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), TelegramRestaurantUser_.userName));
            }
            if (criteria.getChatId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChatId(), TelegramRestaurantUser_.chatId));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), TelegramRestaurantUser_.phone));
            }
            if (criteria.getConversationMetaData() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConversationMetaData(), TelegramRestaurantUser_.conversationMetaData));
            }
            if (criteria.getLoadedPage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLoadedPage(), TelegramRestaurantUser_.loadedPage));
            }
            if (criteria.getRestorantId() != null) {
                specification = specification.and(buildSpecification(criteria.getRestorantId(),
                    root -> root.join(TelegramRestaurantUser_.restorants, JoinType.LEFT).get(Restorant_.id)));
            }
        }
        return specification;
    }
}
