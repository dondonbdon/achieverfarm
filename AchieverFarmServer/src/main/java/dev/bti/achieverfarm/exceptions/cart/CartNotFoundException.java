package dev.bti.achieverfarm.exceptions.cart;

import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.exceptions.auth.AuthException;
import org.springframework.http.HttpStatus;

public class CartNotFoundException extends CartException {
    public CartNotFoundException() {
        super(HttpStatus.NOT_FOUND, Constants.Cart._404);
    }
}
