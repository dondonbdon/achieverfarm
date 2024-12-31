package dev.bti.achieverfarm.exceptions.auth;

import dev.bti.achieverfarm.common.Constants;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException() {
        super(HttpStatus.UNAUTHORIZED, Constants.Auth._401);
    }
}
