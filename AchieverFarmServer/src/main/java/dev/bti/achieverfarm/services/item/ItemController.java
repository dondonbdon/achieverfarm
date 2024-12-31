package dev.bti.achieverfarm.services.item;

import dev.bti.achieverfarm.enums.ItemType;
import dev.bti.achieverfarm.exceptions.Exception;
import dev.bti.achieverfarm.models.ItemOptions;
import dev.bti.achieverfarm.models.PricingInfo;
import dev.bti.achieverfarm.models.req.ItemReq;
import dev.bti.achieverfarm.models.res.Response;
import dev.bti.achieverfarm.models.res.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1")
@RestController
public class ItemController {

    @Autowired
    ItemService service;

    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable String itemId) {
        try {
            return ResponseEntity.ok().body(SuccessResponse.builder()
                    .success(true)
                    .message("Item retrieved successfully")
                    .data(service.getItem(itemId))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/items")
    public ResponseEntity<?> getItemsWithOptions(@RequestParam(required = false) String lastId,
                                                 @RequestParam(required = false) String searchTerm,
                                                 @RequestParam(required = false) ItemType type,
                                                 @RequestParam(required = false) int count) {
        return ResponseEntity.ok().body(SuccessResponse.builder()
                .success(true)
                .message("Items retrieved successfully")
                .data(service.getItemsWithOptions(new ItemOptions(lastId, searchTerm, type, count)))
                .build());
    }

    @GetMapping("/items/userOrderedBefore")
    public ResponseEntity<?> getItemsOrderedBefore() {
        return ResponseEntity.ok().body(SuccessResponse.builder()
                .success(true)
                .message("Items previously ordered by user retrieved successfully")
                .data(service.getItemsOrderedBefore())
                .build());
    }

    @GetMapping("/items/recommendations")
    public ResponseEntity<?> getItemsRecommendations() {
        return ResponseEntity.ok().body(SuccessResponse.builder()
                .success(true)
                .message("Recommended items retrieved successfully")
                .data(service.getItemsRecommendations())
                .build());
    }

    // ADMIN

    @PostMapping("/admin/item")
    public ResponseEntity<?> addItem(@RequestBody ItemReq item) {
        service.addItem(item);
        return ResponseEntity.ok().body(Response.builder()
                .success(true)
                .message("Item added successfully")
                .build());

    }

    @PostMapping("/admin/items")
    public ResponseEntity<?> addItems(@RequestBody List<ItemReq> items) {
        service.addItems(items);
        return ResponseEntity.ok().body(Response.builder()
                .success(true)
                .message("Items added successfully")
                .build());

    }

    @DeleteMapping("/admin/item/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable String itemId) {
        try {
            service.deleteItem(itemId);
            return ResponseEntity.ok().body(Response.builder()
                    .success(true)
                    .message("Item deleted successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @DeleteMapping("/admin/items")
    public ResponseEntity<?> deleteItems(@RequestBody List<String> itemIds) {
        try {
            service.deleteItems(itemIds);
            return ResponseEntity.ok().body(Response.builder()
                    .success(true)
                    .message("Items deleted successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PutMapping("/admin/item/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable String itemId, @RequestBody ItemReq itemToUpdate) {
        try {
            service.updateItem(itemId, itemToUpdate);
            return ResponseEntity.ok().body(Response.builder()
                    .success(true)
                    .message("Item updated successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PutMapping("/admin/items")
    public ResponseEntity<?> updateItems(@RequestBody Map<String, ItemReq> itemsToUpdate) {
        try {
            service.updateItems(itemsToUpdate);
            return ResponseEntity.ok().body(Response.builder()
                    .success(true)
                    .message("Items updated successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PutMapping("/admin/item/{itemId}/update-pricing")
    public ResponseEntity<?> updatePricingInfo(@PathVariable String itemId, @RequestBody PricingInfo pricingInfo) {
        try {
            service.updatePricingInfo(itemId, pricingInfo);
            return ResponseEntity.ok().body(Response.builder()
                    .success(true)
                    .message("Pricing info updated successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(e.getStatus()).body(Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
}
