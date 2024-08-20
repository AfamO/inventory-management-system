package afamo.app.inventory.models;

import afamo.app.inventory.enums.PurchaseOrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchase_order")

public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long inventoryId;
    private Long vendorId;
    private Long productId;
    private int quantity;
    private int orderDeliveryDuration;
    private BigDecimal amount;
    private PurchaseOrderStatus status = PurchaseOrderStatus.FULFILLED;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime deliveryDate;

}