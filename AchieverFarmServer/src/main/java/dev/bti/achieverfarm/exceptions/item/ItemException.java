package dev.bti.achieverfarm.exceptions.item;

import dev.bti.achieverfarm.exceptions.Exception;
import org.springframework.http.HttpStatus;

public class ItemException extends Exception {

    public ItemException(HttpStatus status, String message) {
        super(status, message);
    }
}
