package dev.bti.achieverfarm.exceptions.inventory;

import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.exceptions.Exception;
import org.springframework.http.HttpStatus;

public class InventoryNotFoundException extends InventoryException {
    public InventoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, Constants.Inventory._404);
    }
}
