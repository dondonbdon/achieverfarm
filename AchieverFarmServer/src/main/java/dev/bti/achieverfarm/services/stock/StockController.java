package dev.bti.achieverfarm.services.stock;

import dev.bti.achieverfarm.enums.InventoryUpdateType;
import dev.bti.achieverfarm.enums.StockStatus;
import dev.bti.achieverfarm.models.req.StockReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1")
@RestController
public class StockController {
    
    @Autowired
    StockService service;

    @GetMapping("/stock/{stockId}")
    public ResponseEntity<?> getStock(@RequestParam String stockId) {
        try {
            return ResponseEntity.status(200).body(service.getStock(stockId));
        }catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/stock")
    public ResponseEntity<?> getStock(@RequestParam List<String> stockIds) {
        try {
            return ResponseEntity.status(200).body(service.getStock(stockIds));
        }catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/stock/check")
    public ResponseEntity<?> checkStock(@RequestBody Map<String, Integer> itemCounts) {
        try {
            return ResponseEntity.status(200).body(service.checkStock(itemCounts));
        }catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/admin/stock/{stockId}")
    public ResponseEntity<?> updateStock(@PathVariable String stockId, @RequestBody StockReq req) {
        try {
            service.updateStock(stockId, req);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/admin/stock/{stockId}/status")
    public ResponseEntity<?> updateStockStatus(@PathVariable String stockId, StockStatus status) {
        try {
            service.updateStockStatus(stockId, status);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("admin/stock/{stockId}/availUntil")
    public ResponseEntity<?> updateStockAvailableUntil(@PathVariable String stockId, @RequestParam Instant availUntil) {
        try {
            service.updateAvailableUntil(stockId, availUntil);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("admin/stock/{stockId}/type")
    public ResponseEntity<?> updateStockUpdateType(@PathVariable String stockId, @RequestParam InventoryUpdateType updateType, @RequestParam Double quantity) {
        try {
            service.updateStockUpdateType(stockId, updateType, quantity);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
