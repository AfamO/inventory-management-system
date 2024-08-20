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
@Table(name = "product")
@NamedStoredProcedureQuery(
        name = "findTotalProductsByPrice",
        procedureName = "GET_TOTAL_PRODUCTS_BY_PRICE",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "price_in", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "count_out", type = Integer.class)
        }
)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String barCode;
    private double price;
    private LocalDateTime createdDate = LocalDateTime.now();

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
}