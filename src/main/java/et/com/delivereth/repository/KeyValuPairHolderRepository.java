package et.com.delivereth.repository;

import et.com.delivereth.domain.KeyValuPairHolder;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the KeyValuPairHolder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KeyValuPairHolderRepository extends JpaRepository<KeyValuPairHolder, Long>, JpaSpecificationExecutor<KeyValuPairHolder> {
}
