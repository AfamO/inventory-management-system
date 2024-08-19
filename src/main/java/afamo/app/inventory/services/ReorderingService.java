package afamo.app.inventory.services;

import afamo.app.inventory.models.Inventory;
import afamo.app.inventory.models.Product;
import afamo.app.inventory.models.WareHouse;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ReorderingService {

    public void triggerReOrder(Inventory inventory) throws BadRequestException;

    public Inventory getSingleInventory(Long id);

    public Integer calculateOptimalReorderQuantity(Inventory inventory) throws BadRequestException;

    public WareHouse createWareHouse(WareHouse wareHouse);

    public Inventory createInventory(Inventory inventory);
    public Page<List<Inventory>> getInventories(Long productId,PageRequest pageRequest);
    public void  placeOrder(Inventory inventory, int qty);

}
