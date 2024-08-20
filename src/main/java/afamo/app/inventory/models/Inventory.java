package afamo.app.inventory.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
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
    private int averageDemandPerDay = 10;
    /**
     * NB: Normally the uncertainty safety stock formula should be :
     *     // Safety Stock(SS) =Z× σD × LT
     *     Where Z represents the expected service level such as 99.999%
     *     σD is the standard deviation for the demand
     *     LT is obviously the lead time in days.
     */
    private int uncertaintySafetyFactor = 1; // Assuming that it was calculated to be 1 using to the above formula.
    private int holdingCost = 1;
    private LocalDateTime createdDate = LocalDateTime.now();

}