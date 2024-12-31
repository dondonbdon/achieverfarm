package dev.bti.achieverfarm.logging.models;

import dev.bti.achieverfarm.logging.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogCommand {
    Boolean enableAllLogs, enableOrderLogs, enableInventoryLogs, enableAuthLogs, enableRealTimeLogs, systemOnlyLogs;
    String start, stop;
    Level level;
    Integer count;

    public LogCommand(Boolean enableAllLogs, Boolean enableAuthLogs, Boolean
            enableInventoryLogs, Boolean enableOrderLogs) {
        this.enableAllLogs = enableAllLogs;
        this.enableAuthLogs = enableAuthLogs;
        this.enableInventoryLogs = enableInventoryLogs;
        this.enableOrderLogs = enableOrderLogs;
    }
}
