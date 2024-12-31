package dev.bti.achieverfarm.exceptions.inventory;

import dev.bti.achieverfarm.exceptions.Exception;
import org.springframework.http.HttpStatus;

public class InventoryException extends Exception {
    public InventoryException(HttpStatus status, String message) {
        super(status, message);
    }
}
