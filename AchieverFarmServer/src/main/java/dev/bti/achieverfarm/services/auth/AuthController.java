package dev.bti.achieverfarm.services.auth;

import dev.bti.achieverfarm.exceptions.auth.AuthException;
import dev.bti.achieverfarm.models.req.LoginReq;
import dev.bti.achieverfarm.models.req.UserReq;
import dev.bti.achieverfarm.models.res.ErrorResponse;
import dev.bti.achieverfarm.models.res.Response;
import dev.bti.achieverfarm.models.res.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserReq userReq) {
        Response response = SuccessResponse.builder()
                .success(true)
                .message("User created successfully")
                .data(service.createUser(userReq))
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginReq loginReq) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("User logged in successfully")
                    .data(service.loginUser(loginReq))
                    .build());
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("User login failed")
                    .error(e)
                    .build());
        }
    }

    @PostMapping("/invalidate")
    public ResponseEntity<?> invalidateUserToken(@RequestHeader("Authorization") String token) {
        service.invalidateToken(token);

        return ResponseEntity.ok().body(Response.builder()
                .success(true)
                .message("Invalidated token appropriately.")
                .build());
    }

}
