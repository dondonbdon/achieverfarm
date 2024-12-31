package dev.bti.achieverfarm.exceptions.order;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends OrderException {
    public OrderNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
