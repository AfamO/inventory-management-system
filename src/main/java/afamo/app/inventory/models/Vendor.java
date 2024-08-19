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
@Table(name = "vendor")

public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private String name;
    private String state = "Lagos";
    private String address;
    private String country = "NG";
    private String region = "VI";
    private boolean itemAvailability=false;
    private int leadTimeInDays=10;
    private int minimumOrderQty = 5;
    private int maximumSuppliableOrder = 0;
    private LocalDateTime createdDate = LocalDateTime.now();

    public Vendor(String name, String region) {
        this.name = name;
        this.region= region;
    }
}