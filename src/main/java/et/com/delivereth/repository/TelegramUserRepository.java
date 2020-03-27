package et.com.delivereth.repository;

import et.com.delivereth.domain.TelegramUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the TelegramUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long>, JpaSpecificationExecutor<TelegramUser> {
    Optional<TelegramUser> findTelegramUserByUserNameEquals(String userName);
}
