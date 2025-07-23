package afamo.app.inventory.repository;

import afamo.app.inventory.models.Order;
import afamo.app.inventory.models.User;
import afamo.app.inventory.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findByProductId(Long orderId);

    public List<Order> findByIdAndCreatedDate(Long userId, LocalDateTime localDateTime);
}
