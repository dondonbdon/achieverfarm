package dev.bti.achieverfarm.models.req;

import dev.bti.achieverfarm.enums.StockStatus;
import dev.bti.achieverfarm.enums.InventoryUpdateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockReq {
    Double quantityAvailable;
    Instant availableUntil;
    StockStatus status;
    InventoryUpdateType updateType;
}
