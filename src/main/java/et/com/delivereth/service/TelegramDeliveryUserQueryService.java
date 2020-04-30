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

import et.com.delivereth.domain.TelegramDeliveryUser;
import et.com.delivereth.domain.*; // for static metamodels
import et.com.delivereth.repository.TelegramDeliveryUserRepository;
import et.com.delivereth.service.dto.TelegramDeliveryUserCriteria;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.mapper.TelegramDeliveryUserMapper;

/**
 * Service for executing complex queries for {@link TelegramDeliveryUser} entities in the database.
 * The main input is a {@link TelegramDeliveryUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TelegramDeliveryUserDTO} or a {@link Page} of {@link TelegramDeliveryUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TelegramDeliveryUserQueryService extends QueryService<TelegramDeliveryUser> {

    private final Logger log = LoggerFactory.getLogger(TelegramDeliveryUserQueryService.class);

    private final TelegramDeliveryUserRepository telegramDeliveryUserRepository;

    private final TelegramDeliveryUserMapper telegramDeliveryUserMapper;

    public TelegramDeliveryUserQueryService(TelegramDeliveryUserRepository telegramDeliveryUserRepository, TelegramDeliveryUserMapper telegramDeliveryUserMapper) {
        this.telegramDeliveryUserRepository = telegramDeliveryUserRepository;
        this.telegramDeliveryUserMapper = telegramDeliveryUserMapper;
    }

    /**
     * Return a {@link List} of {@link TelegramDeliveryUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TelegramDeliveryUserDTO> findByCriteria(TelegramDeliveryUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TelegramDeliveryUser> specification = createSpecification(criteria);
        return telegramDeliveryUserMapper.toDto(telegramDeliveryUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TelegramDeliveryUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TelegramDeliveryUserDTO> findByCriteria(TelegramDeliveryUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TelegramDeliveryUser> specification = createSpecification(criteria);
        return telegramDeliveryUserRepository.findAll(specification, page)
            .map(telegramDeliveryUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TelegramDeliveryUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TelegramDeliveryUser> specification = createSpecification(criteria);
        return telegramDeliveryUserRepository.count(specification);
    }

    /**
     * Function to convert {@link TelegramDeliveryUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TelegramDeliveryUser> createSpecification(TelegramDeliveryUserCriteria criteria) {
        Specification<TelegramDeliveryUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TelegramDeliveryUser_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), TelegramDeliveryUser_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), TelegramDeliveryUser_.lastName));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), TelegramDeliveryUser_.userName));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), TelegramDeliveryUser_.userId));
            }
            if (criteria.getChatId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChatId(), TelegramDeliveryUser_.chatId));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), TelegramDeliveryUser_.phone));
            }
            if (criteria.getConversationMetaData() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConversationMetaData(), TelegramDeliveryUser_.conversationMetaData));
            }
            if (criteria.getLoadedPage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLoadedPage(), TelegramDeliveryUser_.loadedPage));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), TelegramDeliveryUser_.status));
            }
            if (criteria.getCurrentBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentBalance(), TelegramDeliveryUser_.currentBalance));
            }
            if (criteria.getCurrentLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentLatitude(), TelegramDeliveryUser_.currentLatitude));
            }
            if (criteria.getCurrentLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentLongitude(), TelegramDeliveryUser_.currentLongitude));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderId(),
                    root -> root.join(TelegramDeliveryUser_.orders, JoinType.LEFT).get(Order_.id)));
            }
            if (criteria.getRestorantId() != null) {
                specification = specification.and(buildSpecification(criteria.getRestorantId(),
                    root -> root.join(TelegramDeliveryUser_.restorants, JoinType.LEFT).get(Restorant_.id)));
            }
        }
        return specification;
    }
}
