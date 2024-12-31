package dev.bti.achieverfarm.logging.models;


import dev.bti.achieverfarm.logging.enums.Level;
import dev.bti.achieverfarm.logging.enums.LogType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderLog extends Log {
    @Id
    String id;

    public OrderLog(LogType type, String id, String commanderName, Instant timestamp, String note, Level level) {
        super(type, commanderName, timestamp, note, level);
        this.id = id;
    }
}
