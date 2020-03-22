package et.com.delivereth.repository;

import et.com.delivereth.domain.Restorant;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Restorant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestorantRepository extends JpaRepository<Restorant, Long>, JpaSpecificationExecutor<Restorant> {
}
