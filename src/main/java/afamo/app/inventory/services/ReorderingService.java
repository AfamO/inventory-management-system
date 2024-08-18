package afamo.app.inventory.services;

import afamo.app.inventory.models.Product;
import afamo.app.inventory.models.WareHouse;

public interface ReorderingService {

    public boolean triggerReOrder(WareHouse wareHouse, Product product);

    public void  placeOrder(WareHouse wareHouse, Product product);

}
