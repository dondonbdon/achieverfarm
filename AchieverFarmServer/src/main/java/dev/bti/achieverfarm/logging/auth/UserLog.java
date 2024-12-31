package dev.bti.achieverfarm.logging.auth;


import dev.bti.achieverfarm.logging.enums.Level;
import dev.bti.achieverfarm.logging.enums.LogType;
import dev.bti.achieverfarm.logging.models.Log;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UserLog extends Log {
    @Id
    String id;
    Type authLogType;
    String name;

    public UserLog(LogType type, String id, String commanderName, Instant timestamp, Type authLogType, String name, String note, Level level) {
        super(type, commanderName, timestamp, note, level);
        this.id = id;
        this.authLogType = authLogType;
        this.name = name;
    }
}
