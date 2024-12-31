package dev.bti.achieverfarm.services.terminal;

import dev.bti.achieverfarm.logging.models.LogCommand;
import dev.bti.achieverfarm.logging.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/terminal")
public class TerminalController {

    @Autowired
    private LogService logService;


    @PostMapping("/execute")
    public ResponseEntity<?> executeCommand(@RequestParam String command, @RequestBody LogCommand options) {
        try {
            ResponseEntity<?> response = switch (command.toLowerCase().trim()) {
                case "health" -> ResponseEntity.ok(checkServerHealth());
                case "logs" -> ResponseEntity.ok(logService.getAll(options));
                default -> ResponseEntity.badRequest().body("Unknown command: " + command);
            };
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error executing command: " + e.getMessage());
        }
    }

    private String checkServerHealth() {
        return "Server is healthy!";
    }

    @GetMapping("/logs/stream")
    public SseEmitter rtLogs(@RequestParam Boolean enableAllLogs, @RequestParam Boolean enableAuthLogs, @RequestParam Boolean enableOrderLogs, @RequestParam Boolean enableInventoryLogs) {
        return logService.addEmitter(new LogCommand(enableAllLogs, enableAuthLogs, enableInventoryLogs, enableOrderLogs));
    }


}

