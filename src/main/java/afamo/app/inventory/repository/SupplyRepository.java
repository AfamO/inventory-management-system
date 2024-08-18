package afamo.app.inventory.repository;

import afamo.app.inventory.models.Inventory;
import afamo.app.inventory.models.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {
}
