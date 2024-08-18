package afamo.app.inventory.repository;

import afamo.app.inventory.models.Inventory;
import afamo.app.inventory.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
