package afamo.app.inventory.services.impl;

import afamo.app.inventory.enums.PurchaseOrderStatus;
import afamo.app.inventory.models.Inventory;
import afamo.app.inventory.models.Product;
import afamo.app.inventory.models.PurchaseOrder;
import afamo.app.inventory.models.Supply;
import afamo.app.inventory.models.Vendor;
import afamo.app.inventory.models.WareHouse;
import afamo.app.inventory.repository.InventoryRepository;
import afamo.app.inventory.repository.ProductRepository;
import afamo.app.inventory.repository.PurchaseOrderRepository;
import afamo.app.inventory.repository.VendorRepository;
import afamo.app.inventory.repository.WareHouseRepository;
import afamo.app.inventory.services.ReorderingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class ReorderingServiceImp implements ReorderingService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    private final VendorRepository vendorRepository;

    private final WareHouseRepository wareHouseRepository;

    private final PurchaseOrderRepository purchaseOrderRepository;
    public ReorderingServiceImp(InventoryRepository inventoryRepository, ProductRepository productRepository, VendorRepository vendorRepository, WareHouseRepository wareHouseRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
        this.wareHouseRepository = wareHouseRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }


    @Override
    @Cacheable
    @Transactional
    public void triggerReOrder(Inventory inventory) throws BadRequestException {
        int optimalQty = this.calculateOptimalReorderQuantity(inventory);
        log.info("OptimalQty=={}",optimalQty);
        if (optimalQty > 0) {
            placeOrder(inventory, optimalQty);
        }
    }

    @Override
    @Cacheable
    public Inventory getSingleInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(()->new NoSuchElementException("OOps no inventory with this Id found"));
        return inventory;
    }

    @Cacheable
    @Transactional
    private Product createProduct(Product product) {
        // First check if the product already exists
        // Usually, other parameters  such as product/item unique bar code would be used to query the DB to find out if the product already exists.
        if (product.getId() == null) {
            product = productRepository.save(product);
            //Create Vendor for the product
            Vendor vendor = new Vendor("Laptop Supplier", "VI");
            vendor.setProductId(product.getId());
            vendorRepository.save(vendor);
        }
        log.info("Inventory already exists, no need of creating it again");
        return product;
    }

    @Override
    public WareHouse createWareHouse(WareHouse wareHouse) {
        // First check if the wareHouse already exists
        // Usually, other parameters  such as wareHouse unique code would be used to query the DB to find out if the wareHouse already exists.
        //
        if (wareHouse.getId() == null) {
            wareHouse = wareHouseRepository.save(wareHouse);
        }
        log.info("Inventory already exists, no need of creating it again");
        return wareHouse;
    }
    @Override
    @Cacheable
    public Inventory createInventory(Inventory inventory) {
        // First check if the inventory already exists
        // Usually, other parameters  such as wareHouse code would be used to query the DB to find out if the inventory already exists.
        //
        if (inventory.getId() == null) {
            WareHouse wareHouse = this.createWareHouse(new WareHouse()); // normally there should be many parameters
            Product product = this.createProduct(new Product()); // normally there should be many parameters
            inventory.setWarehouseId(wareHouse.getId());
            inventory.setProductId(product.getId());
            inventory = inventoryRepository.save(inventory);
        }
        log.info("Inventory already exists, no need of creating it again");
        return inventory;
    }

    @Override
    public Page<List<Inventory>> getInventories(Long wareHouseId, PageRequest pageRequest) {
        return inventoryRepository.selectInventoryByWareHouse(wareHouseId, pageRequest);
    }

    @Override
    @Cacheable
    public void placeOrder(Inventory inventory, int quantity) {
        Product product = productRepository.findById(inventory.getProductId()).
                orElseThrow();
        List<Vendor> vendorList = vendorRepository.findByProductId(product.getId());
        //Does the desired quantity meet the barest minimum requirement of any supplier?
        Optional<Vendor> availableVendor = vendorList.stream().filter(vendor -> quantity >= vendor.getMinimumOrderQty()).findAny();
        log.info("Found Vendor {}", availableVendor);
        if (!availableVendor.isEmpty()) {
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setQuantity(quantity);
            purchaseOrder.setStatus(PurchaseOrderStatus.INITIATED);
            purchaseOrder.setProductId(product.getId());
            purchaseOrder.setInventoryId(inventory.getId());
            purchaseOrder.setDeliveryDate(LocalDateTime.now().plusDays(availableVendor.get().getLeadTimeInDays()));
            purchaseOrder = purchaseOrderRepository.save(purchaseOrder);

            /**
             // SubmitOrder...Typically there should be a kind of web service here an email should be sent to the vendor, but let's just demo and assume it
             */
            log.info("Order's RFQ Successfully Submitted");
            Supply supply = new Supply();
            supply.setOrderedQty(quantity);
            supply.setInventoryId(inventory.getId());
            supply.setVendorId(availableVendor.get().getId());

            log.info("Order Successfully Acquired and Purchased::{}",supply);
            //update stock level
            this.updateStockLevel(inventory, supply.getOrderedQty());
        } else { // since the demand is less than what(the minimum threshold) the vendor plans to supply
            // invoke backorder then
            this.invokeBackOrder(inventory, quantity);
        }
    }

    @Override
    public Page<Inventory> getPageableInventories(PageRequest pageRequest) throws BadRequestException {
        if (pageRequest == null)
            throw new BadRequestException("You must provide a page parameter");
        return inventoryRepository.findAll(pageRequest);
    }

    public void monitorStockLevel (List<Inventory> inventoryList) throws BadRequestException {
        for (Inventory inventory : inventoryList) {
            if (inventory.getStockAtHand() <= inventory.getMinimumQtyReorderPoint()) {
                triggerReOrder(inventory);
            }
        }
    }

    private void  calculateReorderPoints(List<Inventory> inventoryList) {
        for (Inventory inventory : inventoryList) {
        Product product = productRepository.findById(inventory.getProductId()).
                orElseThrow();
        List<Vendor> vendorList = vendorRepository.findByProductId(product.getId());

        for (Vendor vendor : vendorList) {
            int reorderPointQty = (vendor.getLeadTimeInDays() * inventory.getForecastedDemand()) + inventory.getUncertaintySafetyFactor();
            inventory.setMinimumQtyReorderPoint(reorderPointQty);
            inventoryRepository.save(inventory); //save the inventory;
        }
        }
    }

    @Transactional
    public Integer calculateOptimalReorderQuantity(Inventory inventory) throws BadRequestException {
        Product product = productRepository.findById(inventory.getProductId()).
                orElseThrow();
        WareHouse wareHouse = wareHouseRepository.getReferenceById(inventory.getWarehouseId());
        Integer optimalQtyToOrder = 0;
        if (wareHouse!= null){
            log.info("The retrieved WareHouse is :: {}",wareHouse);
            List<Vendor> vendorList = vendorRepository.findByProductId(product.getId());
            double avgLeadTime = 0D;
            if(!vendorList.isEmpty()) {
                avgLeadTime = vendorList.stream().map((vendor) -> vendor.getLeadTimeInDays()).count()/vendorList.size();
            }

            if (inventory.getHoldingCost() == 0){
                throw new BadRequestException("The holding cost cannot be zero(0)");
            }
            double leadTimeRegionalDemandSupplierConstraints =
                    (avgLeadTime * wareHouse.getRegionalDemandForecast() * wareHouse.getRegionalDemandVariance())/inventory.getHoldingCost();
            optimalQtyToOrder = (int)Double.max(inventory.getMaximumQty(), leadTimeRegionalDemandSupplierConstraints);

        }
        return optimalQtyToOrder;

    }
    @Transactional
    private void updateStockLevel(Inventory inventory, int quantity) {
        inventory.setStockAtHand(inventory.getStockAtHand() + quantity);
        inventoryRepository.save(inventory);
    }

    public void adjustReOrderPointForRegionalDemandVariance(Inventory inventory) {
        WareHouse wareHouse = wareHouseRepository.getReferenceById(inventory.getWarehouseId());
        if (wareHouse != null) {
            //adjust demandForecast to reflect regional demand factor
            inventory.setForecastedDemand((int) (Math.floor(inventory.getForecastedDemand()) * wareHouse.getRegionalDemandVariance()));
            inventory.setUncertaintySafetyFactor(2); //recalculate safety stock, let's assume it is 2
            calculateReorderPoints(Arrays.asList(inventory)); //finally re-calculate re-order points based on the above regional adjustments;
        }
    }

    private void invokeBackOrder(Inventory inventory, int quantity) {
      log.info("Invoking Back Order....");
      //Yet to be implemented
    }
}
