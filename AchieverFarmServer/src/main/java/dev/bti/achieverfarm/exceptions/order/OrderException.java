package dev.bti.achieverfarm.exceptions.order;

import dev.bti.achieverfarm.exceptions.Exception;
import org.springframework.http.HttpStatus;

public class OrderException extends Exception {
    public OrderException(HttpStatus status, String message) {
        super(status, message);
    }
}
