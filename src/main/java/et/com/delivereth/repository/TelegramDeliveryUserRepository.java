package et.com.delivereth.repository;

import et.com.delivereth.domain.TelegramDeliveryUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TelegramDeliveryUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelegramDeliveryUserRepository extends JpaRepository<TelegramDeliveryUser, Long>, JpaSpecificationExecutor<TelegramDeliveryUser> {
}
