package afamo.app.inventory.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory")

public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long warehouseId;
    private Long productId;
    private int stockAtHand;
    private int minimumQtyReorderPoint;
    private int maximumQty;
    private int forecastedDemand;
    private int uncertaintySafetyFactor;
    private int holdingCost;
    private LocalDateTime createdDate = LocalDateTime.now();

}