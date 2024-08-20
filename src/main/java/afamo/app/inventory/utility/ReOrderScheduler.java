package afamo.app.inventory.utility;

import afamo.app.inventory.config.AppProperties;
import afamo.app.inventory.models.Inventory;
import afamo.app.inventory.services.ReorderingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ReOrderScheduler {

    private final ReorderingService reorderingService;


    private final AppProperties appProperties;
    private static final long monitorRate = 15000;
    public ReOrderScheduler(ReorderingService reorderingService, AppProperties appProperties) {
        this.reorderingService = reorderingService;
        this.appProperties = appProperties;
        log.info("ReOrder Monitoring Rate===>{}",appProperties.getReOrderMonitorRate());
        log.info("ReOrder Monitoring Interval===>{}",appProperties.getReOrderMonitorInterval());
    }

    //Every 15 seconds
    @Scheduled(fixedRate = monitorRate)
    public void scheduleReOrderMonitoring() throws BadRequestException {
        //Assume page size is 5, page is 0
        int page = 0; int size =5;
        // retrieve pageable stock
        List<Inventory> inventoryList = reorderingService.getPageableInventories(PageRequest.of(page,size)).getContent();
        for (Inventory inventory : inventoryList) {
            reorderingService.calculateOptimalReorderQuantity(inventory);
        }
        reorderingService.monitorStockLevel(inventoryList);
        log.info("Stock Level Monitoring started.... ==>{}",inventoryList);
    }

}
