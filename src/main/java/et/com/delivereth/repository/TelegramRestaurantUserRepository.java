package et.com.delivereth.repository;

import et.com.delivereth.domain.TelegramRestaurantUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the TelegramRestaurantUser entity.
 */
@Repository
public interface TelegramRestaurantUserRepository extends JpaRepository<TelegramRestaurantUser, Long>, JpaSpecificationExecutor<TelegramRestaurantUser> {

    @Query(value = "select distinct telegramRestaurantUser from TelegramRestaurantUser telegramRestaurantUser left join fetch telegramRestaurantUser.restorants",
        countQuery = "select count(distinct telegramRestaurantUser) from TelegramRestaurantUser telegramRestaurantUser")
    Page<TelegramRestaurantUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct telegramRestaurantUser from TelegramRestaurantUser telegramRestaurantUser left join fetch telegramRestaurantUser.restorants")
    List<TelegramRestaurantUser> findAllWithEagerRelationships();

    @Query("select telegramRestaurantUser from TelegramRestaurantUser telegramRestaurantUser left join fetch telegramRestaurantUser.restorants where telegramRestaurantUser.id =:id")
    Optional<TelegramRestaurantUser> findOneWithEagerRelationships(@Param("id") Long id);
}
