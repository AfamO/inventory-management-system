package afamo.app.inventory.services;

import afamo.app.inventory.models.Inventory;
import afamo.app.inventory.models.Product;
import afamo.app.inventory.models.WareHouse;

public interface ReorderingService {

    public void triggerReOrder(Inventory inventory);

    public void  placeOrder(Inventory inventory, int qty);

}
