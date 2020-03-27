package et.com.delivereth.repository;

import et.com.delivereth.domain.Order;
import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.domain.enumeration.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface CustomOrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByOrderStatusAndTelegramUser(OrderStatus orderStatus, TelegramUser telegramUser);
}
