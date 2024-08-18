package afamo.app.inventory.repository;

import afamo.app.inventory.models.Product;
import afamo.app.inventory.models.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {
}
