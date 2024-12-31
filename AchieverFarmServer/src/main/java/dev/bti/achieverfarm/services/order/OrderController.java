package dev.bti.achieverfarm.services.order;

import dev.bti.achieverfarm.exceptions.Exception;
import dev.bti.achieverfarm.models.Stock;
import dev.bti.achieverfarm.models.req.Checkout;
import dev.bti.achieverfarm.models.res.CheckoutFail;
import dev.bti.achieverfarm.models.res.ErrorResponse;
import dev.bti.achieverfarm.models.res.SuccessResponse;
import dev.bti.achieverfarm.services.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/user/{userId}")
@RestController
public class OrderController {

    @Autowired
    OrderService service;

    @Autowired
    StockService stockService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Order retrieved successfully")
                    .data(service.getOrder(orderId))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to retrieve order")
                    .error(e)
                    .build());
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@PathVariable String userId) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Orders retrieved successfully")
                    .data(service.getOrders(userId))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to retrieve orders")
                    .error(e)
                    .build());
        }
    }

    @GetMapping("/orders/active")
    public ResponseEntity<?> getActiveOrders(@PathVariable String userId) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Active orders retrieved successfully")
                    .data(service.getActiveOrders(userId))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to retrieve active orders")
                    .error(e)
                    .build());
        }
    }

    @GetMapping("/orders/complete")
    public ResponseEntity<?> getCompleteOrders(@PathVariable String userId) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Completed orders retrieved successfully")
                    .data(service.getCompletedOrders(userId))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Failed to retrieve completed orders")
                    .error(e)
                    .build());
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@PathVariable String userId, @RequestBody Checkout checkout) {
        try {
            List<Stock> notAvailable = stockService.checkStock(checkout.getItems());

            if (notAvailable.isEmpty()) {
                return ResponseEntity.ok().body(SuccessResponse.builder()
                        .success(true)
                        .message("Checkout completed successfully")
                        .data(service.checkout(userId, checkout))
                        .build());
            } else {
                return ResponseEntity.ok().body(SuccessResponse.builder()
                        .success(false)
                        .message("Some items are not available in stock")
                        .data(new CheckoutFail("Items not available in stock", notAvailable))
                        .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                    .success(false)
                    .message("Checkout failed due to stock issues or other errors")
                    .error(e)
                    .build());
        }
    }
}
