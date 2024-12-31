package dev.bti.achieverfarm.exceptions.auth;

import dev.bti.achieverfarm.common.Constants;
import org.springframework.http.HttpStatus;

public class MaxFailedAttemptsException extends AuthException {
    public MaxFailedAttemptsException() {
        super(HttpStatus.UNAUTHORIZED, Constants.Auth._403);
    }
}
