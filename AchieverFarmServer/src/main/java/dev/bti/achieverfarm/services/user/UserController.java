package dev.bti.achieverfarm.services.user;

import dev.bti.achieverfarm.exceptions.auth.AuthException;
import dev.bti.achieverfarm.models.req.UserReq;
import dev.bti.achieverfarm.models.res.SuccessResponse;
import dev.bti.achieverfarm.models.res.ErrorResponse;
import dev.bti.achieverfarm.models.res.UserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        try {
            UserRes user = service.getUser(userId, UserRes.class);
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("User retrieved successfully")
                    .data(user)
                    .build());
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to retrieve user")
                    .error(e)
                    .build());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserReq userReq) {
        try {
            service.updateUser(userId, userReq);
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("User updated successfully")
                    .build());
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to update user")
                    .error(e)
                    .build());
        }
    }
}
