package et.com.delivereth.repository;

import et.com.delivereth.domain.TelegramUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TelegramUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long>, JpaSpecificationExecutor<TelegramUser> {
}
