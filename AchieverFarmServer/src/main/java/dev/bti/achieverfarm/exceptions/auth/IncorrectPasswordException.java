package dev.bti.achieverfarm.exceptions.auth;

import dev.bti.achieverfarm.common.Constants;
import org.springframework.http.HttpStatus;

public class IncorrectPasswordException extends AuthException {
    public IncorrectPasswordException() {
        super(HttpStatus.UNAUTHORIZED, Constants.Auth._401_1);
    }
}
