package dev.bti.achieverfarm.exceptions.cart;

import dev.bti.achieverfarm.exceptions.Exception;
import org.springframework.http.HttpStatus;

public class CartException extends Exception {

    public CartException(HttpStatus status, String message) {
        super(status, message);
    }
}
