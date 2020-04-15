package et.com.delivereth.repository;

import et.com.delivereth.domain.TelegramDeliveryUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the TelegramDeliveryUser entity.
 */
@Repository
public interface TelegramDeliveryUserRepository extends JpaRepository<TelegramDeliveryUser, Long>, JpaSpecificationExecutor<TelegramDeliveryUser> {

    @Query(value = "select distinct telegramDeliveryUser from TelegramDeliveryUser telegramDeliveryUser left join fetch telegramDeliveryUser.restorants",
        countQuery = "select count(distinct telegramDeliveryUser) from TelegramDeliveryUser telegramDeliveryUser")
    Page<TelegramDeliveryUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct telegramDeliveryUser from TelegramDeliveryUser telegramDeliveryUser left join fetch telegramDeliveryUser.restorants")
    List<TelegramDeliveryUser> findAllWithEagerRelationships();

    @Query("select telegramDeliveryUser from TelegramDeliveryUser telegramDeliveryUser left join fetch telegramDeliveryUser.restorants where telegramDeliveryUser.id =:id")
    Optional<TelegramDeliveryUser> findOneWithEagerRelationships(@Param("id") Long id);
}
