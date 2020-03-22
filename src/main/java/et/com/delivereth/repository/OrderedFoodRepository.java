package et.com.delivereth.repository;

import et.com.delivereth.domain.OrderedFood;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the OrderedFood entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderedFoodRepository extends JpaRepository<OrderedFood, Long>, JpaSpecificationExecutor<OrderedFood> {
}
