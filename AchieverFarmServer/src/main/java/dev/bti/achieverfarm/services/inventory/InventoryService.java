package dev.bti.achieverfarm.services.inventory;

import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.exceptions.inventory.InventoryException;
import dev.bti.achieverfarm.models.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired InventoryRepository inventoryRepository;

    public Inventory getInventory(String id) throws InventoryException {
        return inventoryRepository.findById(id).orElseThrow(() -> Constants.Throws.InventoryNotFound);
    }

}
