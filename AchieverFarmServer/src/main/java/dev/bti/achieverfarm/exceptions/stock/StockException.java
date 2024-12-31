package dev.bti.achieverfarm.exceptions.stock;

import dev.bti.achieverfarm.exceptions.Exception;
import org.springframework.http.HttpStatus;

public class StockException extends Exception {
    public StockException(HttpStatus status, String message) {
        super(status, message);
    }
}
