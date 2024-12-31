package dev.bti.achieverfarm.logging.models;

import dev.bti.achieverfarm.logging.enums.Level;
import dev.bti.achieverfarm.logging.enums.LogType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Log {
    LogType type;
    String commanderName;
    Instant timestamp;
    String note;
    Level level;

    public boolean matches(LogCommand criteria) {
        if (criteria.getEnableAllLogs()) return true;
        if (criteria.getEnableAuthLogs() && getType() == LogType.USR) return true;
        if (criteria.getEnableInventoryLogs() && getType() == LogType.INV) return true;
        if (criteria.getEnableOrderLogs() && getType() == LogType.ODR) return true;
        return criteria.getSystemOnlyLogs() && commanderName.equals("SYS");
    }
}
