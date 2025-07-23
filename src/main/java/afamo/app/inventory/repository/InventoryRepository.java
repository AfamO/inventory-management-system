package afamo.app.inventory.repository;

import afamo.app.inventory.models.Inventory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Cacheable("inventory")
    @Query(value = "SELECT * FROM Inventory iv WHERE iv.warehouseId=:warehouseId", nativeQuery = true, countQuery = "SELECT count(*) WHERE warehouseId=?1")
    Page<List<Inventory>> selectInventoryByWareHouse(Long warehouseId, Pageable pageable);

    @Cacheable("products")
    Page<List<Inventory>> findByProductId(Long productId,Pageable pageable);

}
