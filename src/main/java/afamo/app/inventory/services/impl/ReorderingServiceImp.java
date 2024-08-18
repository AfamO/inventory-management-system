package afamo.app.inventory.services.impl;

import afamo.app.inventory.models.Inventory;
import afamo.app.inventory.models.Product;
import afamo.app.inventory.models.Supply;
import afamo.app.inventory.models.Vendor;
import afamo.app.inventory.models.WareHouse;
import afamo.app.inventory.repository.InventoryRepository;
import afamo.app.inventory.repository.ProductRepository;
import afamo.app.inventory.repository.VendorRepository;
import afamo.app.inventory.repository.WareHouseRepository;
import afamo.app.inventory.services.ReorderingService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ReorderingServiceImp implements ReorderingService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    private final VendorRepository vendorRepository;

    private final WareHouseRepository wareHouseRepository;

    public ReorderingServiceImp(InventoryRepository inventoryRepository, ProductRepository productRepository, VendorRepository vendorRepository, WareHouseRepository wareHouseRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
        this.wareHouseRepository = wareHouseRepository;
    }


    @Override
    public void triggerReOrder(Inventory inventory) {
        int optimalQty = this.calculateOptimalReorderQuantity(inventory);
        if (optimalQty > 0) {
            placeOrder(inventory, optimalQty);
        }
    }

    @Override
    public void placeOrder(Inventory inventory, int quantity) {
        Product product = productRepository.findById(inventory.getProductId()).
                orElseThrow();
        List<Vendor> vendorList = vendorRepository.findByProductId(product.getId());
        //Does the desired quantity meet the barest minimum requirement of any supplier?
        Vendor availableVendor = vendorList.stream().filter(vendor -> vendor.getMinimumOrderQty() >= quantity).findAny().get();
        if (availableVendor !=null) {
            /**
             // SubmitOrder...Typically there should be a kind of web service here, but let's just demo and assume it
             */
            log.info("Order Successfully Submitted");
            Supply supply = new Supply();
            supply.setOrderedQty(quantity);
            supply.setInventoryId(inventory.getId());

            log.info("Order Successfully Acquired and Purchased::{}",supply);
            //update stock level
            this.updateStockLevel(inventory, supply.getOrderedQty());
        } else { // since the demand is less than what(the minimum threshold) the vendor plans to supply
            // invoke backorder then
            this.invokeBackOrder(inventory, quantity);
        }



    }

    public void monitorStockLevel (List<Inventory> inventoryList) {
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

    private int calculateOptimalReorderQuantity(Inventory inventory) {
        Product product = productRepository.findById(inventory.getProductId()).
                orElseThrow();
        WareHouse wareHouse = wareHouseRepository.getReferenceById(inventory.getWarehouseId());
        int optimalQtyToOrder = 0;
        if (wareHouse!= null){
            List<Vendor> vendorList = vendorRepository.findByProductId(product.getId());
            double avgLeadTime = 0D;
            if(!vendorList.isEmpty()) {
                avgLeadTime = vendorList.stream().map((vendor) -> vendor.getLeadTimeInDays()).count()/vendorList.size();
            }

            double leadTimeRegionalDemandSupplierConstraints =
                    (avgLeadTime * wareHouse.getRegionalDemandForecast() * wareHouse.getRegionalDemandVariance())/inventory.getHoldingCost();
            optimalQtyToOrder = (int)Double.max(inventory.getMaximumQty(), leadTimeRegionalDemandSupplierConstraints);

        }
        return optimalQtyToOrder;

    }
    private void updateStockLevel(Inventory inventory, int quantity) {
        inventory.setStockAtHand(inventory.getStockAtHand() + quantity);
        inventoryRepository.save(inventory);
    }

    private void invokeBackOrder(Inventory inventory, int quantity) {

    }
}
