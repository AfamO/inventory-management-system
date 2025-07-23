package afamo.app.inventory.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import javax.crypto.KeyGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order")
@NamedStoredProcedureQuery(
        name = "findTotalOrderssByQuantity",
        procedureName = "GET_TOTAL_Orders_BY_Quantity",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "qty_in", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "count_out", type = Integer.class)
        }
)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long productId;
    private BigDecimal amount;
    private long quantity;
    private LocalDateTime createdDate = LocalDateTime.now();

    /**
    @Bean("customKeyGenerator")
    public KeyGenerator customKeyGenerator() {
        return (target, method, params) ->{
            return  user.getId() + "-" + createdDate.toString();
        };
    }
    */
}