package afamo.app.inventory.services.impl;

import afamo.app.inventory.models.Inventory;
import afamo.app.inventory.models.Product;
import afamo.app.inventory.models.WareHouse;
import afamo.app.inventory.services.ReorderingService;

import java.util.List;

public class ReorderingServiceImp implements ReorderingService {

    @Override
    public boolean triggerReOrder(WareHouse wareHouse, Product product) {
        return false;
    }

    @Override
    public void placeOrder(WareHouse wareHouse, Product product) {

    }

    private int calculateReOrderQuantity(Inventory inventory) {
      int reOrderQty = 0;
      if (inventory.getStockAtHand() <= inventory.getMinimumQtyReorderPoint()) {
      }
    }

    private void  calculateReorderPoint(List<Inventory> inventoryList) {
        for (Inventory inventory : inventoryList) {

        }
    }

    private int calculateOptimalReorderQuantity(WareHouse wareHouse, Product product) {

    }
    private void updateStockLevel(WareHouse wareHouse, Product product, int quantity) {

    }

    private void adjustForRegionalDemandVariation(WareHouse wareHouse, Product product) {

    }

    private void invokeBackOrder(WareHouse wareHouse, Product product, int quantity)
}
