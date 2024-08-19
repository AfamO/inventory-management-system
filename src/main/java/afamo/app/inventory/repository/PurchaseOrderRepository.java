package afamo.app.inventory.repository;

import afamo.app.inventory.enums.PurchaseOrderStatus;
import afamo.app.inventory.models.PurchaseOrder;
import afamo.app.inventory.models.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    public Page<List<PurchaseOrder>> findByProductId(Long productId, Pageable pageable);
    public Page<List<PurchaseOrder>> findByStatus(PurchaseOrderStatus purchaseOrderStatus, Pageable pageable);
}
