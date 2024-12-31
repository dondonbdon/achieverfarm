package dev.bti.achieverfarm.services.stocklytics;

import dev.bti.achieverfarm.enums.ChunkUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RequestMapping("/api/v1/admin/stocklytics")
@RestController
public class StocklyticsController {

    @Autowired
    StocklyticsService service;

    @GetMapping("/{stockId}/ranged")
    public ResponseEntity<?> retrieveRange(@PathVariable String stockId, @RequestParam Instant from, @RequestParam Instant to, @RequestParam ChunkUnit chunkSize) {
        try {
            return ResponseEntity.status(200).body(service.retrieve(stockId, from, to, chunkSize));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{stockId}/all")
    public ResponseEntity<?> retrieveAll(@PathVariable String stockId, @RequestParam ChunkUnit chunkSize) {
        try {
            return ResponseEntity.status(200).body(service.retrieve(stockId, chunkSize));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
