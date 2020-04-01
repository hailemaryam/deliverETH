package et.com.delivereth.repository;

import et.com.delivereth.domain.Order;
import et.com.delivereth.domain.OrderedFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the OrderedFood entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomOrderedFoodRepository extends JpaRepository<OrderedFood, Long>, JpaSpecificationExecutor<OrderedFood> {
    List<OrderedFood> findAllByOrder(Order order);
}
