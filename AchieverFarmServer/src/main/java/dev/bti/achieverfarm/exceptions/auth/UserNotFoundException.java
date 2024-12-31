package dev.bti.achieverfarm.exceptions.auth;

import dev.bti.achieverfarm.common.Constants;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, Constants.Auth._404);
    }
}