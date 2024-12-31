package dev.bti.achieverfarm.exceptions.stock;

import dev.bti.achieverfarm.common.Constants;
import org.springframework.http.HttpStatus;

public class StockNotFoundException extends StockException {
    public StockNotFoundException() {
        super(HttpStatus.NOT_FOUND, Constants.Stock._404);
    }
}
