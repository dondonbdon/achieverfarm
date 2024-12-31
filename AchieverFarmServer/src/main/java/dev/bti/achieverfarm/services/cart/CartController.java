package dev.bti.achieverfarm.services.cart;

import dev.bti.achieverfarm.exceptions.Exception;
import dev.bti.achieverfarm.models.res.ErrorResponse;
import dev.bti.achieverfarm.models.res.Response;
import dev.bti.achieverfarm.models.res.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/{userId}")
public class CartController {

    @Autowired
    CartService service;

    @GetMapping({"/cart/{cartId}", "/cart"})
    public ResponseEntity<?> getCart(@PathVariable String userId,
                                     @PathVariable(value = "cartId", required = false) String cartId) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Cart retrieved successfully")
                    .data(service.getCart(userId, cartId))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to retrieve cart")
                    .error(e)
                    .build());
        }
    }

    @GetMapping("/carts")
    public ResponseEntity<?> getCarts(@PathVariable String userId) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Carts retrieved successfully")
                    .data(service.getCarts(userId))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to retrieve carts")
                    .error(e)
                    .build());
        }
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<?> deleteCart(@PathVariable String userId, @PathVariable String cartId) {
        service.deleteCart(userId, cartId);
        return ResponseEntity.ok().body(Response.builder()
                .success(true)
                .message("Cart deleted successfully")
                .build());
    }

    @PostMapping("/cart")
    public ResponseEntity<?> saveCart(@PathVariable String userId, @RequestParam String cartName) {
        try {
            service.saveCart(userId, cartName);
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Cart saved successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to save cart")
                    .error(e)
                    .build());
        }
    }

    @PutMapping("/cart/{cartId}/updateName")
    public ResponseEntity<?> updateCartName(@PathVariable String userId, @PathVariable String cartId, @RequestParam String cartName) {
        try {
            service.updateCartName(userId, cartId, cartName);
            return ResponseEntity.ok().body(Response.builder()
                    .success(true)
                    .message("Cart name updated successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to update cart name")
                    .error(e)
                    .build());
        }
    }

    @DeleteMapping("/cart")
    public ResponseEntity<?> clearCart(@PathVariable String userId) {
        service.deleteCart(userId, null);
        return ResponseEntity.ok().body(Response.builder()
                .success(true)
                .message("Cart cleared successfully")
                .build());
    }

    @PutMapping({"/cart/{cartId}/item/{itemId}/inc", "/cart/item/{itemId}/inc"})
    public ResponseEntity<?> incrementCartItem(@PathVariable("userId") String userId,
                                               @PathVariable(value = "cartId", required = false) String cartId,
                                               @PathVariable("itemId") String itemId,
                                               @RequestParam Integer count) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Cart item incremented successfully")
                    .data(service.incrementCartItem(userId, cartId, itemId, count))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to increment cart item")
                    .error(e)
                    .build());
        }
    }

    @PutMapping({"/cart/{cartId}/item/{itemId}/dec", "/cart/item/{itemId}/dec"})
    public ResponseEntity<?> decrementCartItem(@PathVariable("userId") String userId,
                                               @PathVariable(value = "cartId", required = false) String cartId,
                                               @PathVariable("itemId") String itemId,
                                               @RequestParam Integer count) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Cart item decremented successfully")
                    .data(service.decrementCartItem(userId, cartId, itemId, count))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to decrement cart item")
                    .error(e)
                    .build());
        }
    }

    @DeleteMapping({"/cart/{cartId}/item/{itemId}", "/cart/item/{itemId}"})
    public ResponseEntity<?> deleteCartItem(@PathVariable("userId") String userId,
                                            @PathVariable(value = "cartId", required = false) String cartId,
                                            @PathVariable("itemId") String itemId) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Cart item deleted successfully")
                    .data(service.deleteCartItem(userId, cartId, itemId))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to delete cart item")
                    .error(e)
                    .build());
        }
    }
}
