package dev.bti.achieverfarm.logging.inventory;


import dev.bti.achieverfarm.enums.SellingType;
import dev.bti.achieverfarm.logging.enums.Level;
import dev.bti.achieverfarm.logging.enums.LogType;
import dev.bti.achieverfarm.logging.models.Log;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
@Setter
public class InventoryLog extends Log {
    @Id
    String id;
    UpdateType updateType;
    String itemId;
    String itemName;
    Operation operation;
    Double quantity;
    SellingType sellingType;

    public InventoryLog(LogType type, String id, String commanderName, Instant timestamp, UpdateType updateType, String itemId, String itemName, Operation operation, Double quantity, SellingType sellingType, String note, Level level) {
        super(type, commanderName, timestamp, note, level);
        this.id = id;
        this.updateType = updateType;
        this.itemId = itemId;
        this.itemName = itemName;
        this.operation = operation;
        this.quantity = quantity;
        this.sellingType = sellingType;
    }
}
