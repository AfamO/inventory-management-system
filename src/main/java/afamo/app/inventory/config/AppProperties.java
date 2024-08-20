package afamo.app.inventory.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties("reorder")
public class AppProperties {

    @Value("${reorder.monitor_rate}")
    private Long reOrderMonitorRate;

    @Value("${reorder.monitor_interval}")
    private Long reOrderMonitorInterval;


}
