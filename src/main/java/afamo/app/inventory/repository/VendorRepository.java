package afamo.app.inventory.repository;

import afamo.app.inventory.models.Product;
import afamo.app.inventory.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    public List<Vendor> findByProductId(Long productId);
}
