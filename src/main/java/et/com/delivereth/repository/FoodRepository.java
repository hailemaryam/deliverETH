package et.com.delivereth.repository;

import et.com.delivereth.domain.Food;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Food entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodRepository extends JpaRepository<Food, Long>, JpaSpecificationExecutor<Food> {
}
