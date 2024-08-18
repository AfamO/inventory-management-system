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
@Table(name = "warehouse")

public class WareHouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int demandForecast;
    private int regionalDemandVariance;
    private String code;
    private String state;
    private String address;
    private String country;
    private String region;
    private LocalDateTime createdDate = LocalDateTime.now();

    public WareHouse(String name, String region) {
        this.name = name;
        this.region= region;
    }
}