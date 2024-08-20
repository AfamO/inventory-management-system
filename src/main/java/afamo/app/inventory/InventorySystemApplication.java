package afamo.app.inventory;

import afamo.app.inventory.config.AppProperties;
import afamo.app.inventory.models.Inventory;
import afamo.app.inventory.services.ReorderingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(AppProperties.class)
@PropertySource(value = {"classpath:application.properties"})
@Slf4j
public class InventorySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorySystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ReorderingService reorderingService) {
		return (args) -> {
			// Seed the DB with some data- about 3 inventory data
			Inventory inventory = new Inventory();
			inventory.setStockAtHand(10);
			inventory.setHoldingCost(10);
			inventory.setUncertaintySafetyFactor(3);
			reorderingService.createInventory(inventory);
			inventory = new Inventory();
			inventory.setStockAtHand(20);
			inventory.setHoldingCost(20);
			inventory.setUncertaintySafetyFactor(2);
			reorderingService.createInventory(inventory);
			inventory = new Inventory();
			inventory.setStockAtHand(30);
			inventory.setHoldingCost(30);
			inventory.setUncertaintySafetyFactor(2);
			inventory = reorderingService.createInventory(inventory);
			log.info("Initial  Data Seeding successfully Done");
		};
	}

}
