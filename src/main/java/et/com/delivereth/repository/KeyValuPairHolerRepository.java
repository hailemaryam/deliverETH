package et.com.delivereth.repository;

import et.com.delivereth.domain.KeyValuPairHoler;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the KeyValuPairHoler entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KeyValuPairHolerRepository extends JpaRepository<KeyValuPairHoler, Long>, JpaSpecificationExecutor<KeyValuPairHoler> {
}
