package dev.bti.achieverfarm.logging.models;


import dev.bti.achieverfarm.logging.enums.LogType;
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
public class BasicLog extends Log {
    @Id
    String id;

    public BasicLog(LogType type, String id, String note, Instant timestamp) {
        super(type, null, timestamp, note, null);
        this.id = id;
    }
}
