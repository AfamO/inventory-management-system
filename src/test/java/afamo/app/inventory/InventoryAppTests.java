package afamo.app.inventory;

import afamo.app.inventory.models.Inventory;
import afamo.app.inventory.services.ReorderingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class InventoryAppTests {

    @Autowired
    private  TestRestTemplate restTemplate;

    @Autowired
    private ReorderingService reorderingService;

    private static HttpHeaders headers;

    //Assuming the webservice exists here
    private  static String WEBSERVICEBASEURL = "http://localhost:";

    @Test
    void contextLoads() {
    }

    @BeforeAll
    public static void setUpRequestParams() {

        // set up intial inventory for test
        // we need to create a new headers instance
        headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        int port = 8081;
        WEBSERVICEBASEURL = "http://localhost:"+ port + "/ims/app/welcome";


    }

    @Test
    @BeforeEach
    public void testCreateSingleInventory()  {
        Inventory inventory = new Inventory();
        inventory.setStockAtHand(10);
        inventory.setHoldingCost(10);
        inventory.setUncertaintySafetyFactor(3);
        inventory = reorderingService.createInventory(inventory);
        log.info("Inventory {} successfully created", inventory);
        Assertions.assertNotNull(inventory.getId());
    }
    @Test
    public void testCalculateOptimalReorderQuantity() throws BadRequestException {
        // first set up inventory
        Inventory inventoryFind = reorderingService.getSingleInventory(1L);
        Integer optimalReOrderQty = reorderingService.calculateOptimalReorderQuantity(inventoryFind);
        Assertions.assertNotNull(optimalReOrderQty);
    }


    @Test
    public void testTriggerAndPlaceOrder()  {
        Inventory inventory = new Inventory();
        inventory.setStockAtHand(10);
        inventory.setHoldingCost(10);
        inventory.setUncertaintySafetyFactor(3);
        inventory = reorderingService.createInventory(inventory);
        log.info("Inventory {} successfully created", inventory);

        inventory = reorderingService.getSingleInventory(inventory.getId());
        int initialStockAtHand = inventory.getStockAtHand();
        int newOrderedQty = 10;
        reorderingService.placeOrder(inventory,10);
        inventory = reorderingService.getSingleInventory(inventory.getId());

        Assertions.assertTrue(inventory.getStockAtHand()  > initialStockAtHand);
    }

}
