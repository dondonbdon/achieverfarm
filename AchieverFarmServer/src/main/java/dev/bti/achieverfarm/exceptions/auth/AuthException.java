package dev.bti.achieverfarm.exceptions.auth;

import dev.bti.achieverfarm.exceptions.Exception;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends Exception {
    public AuthException(HttpStatus status, String message) {
        super(status, message);
    }
}
