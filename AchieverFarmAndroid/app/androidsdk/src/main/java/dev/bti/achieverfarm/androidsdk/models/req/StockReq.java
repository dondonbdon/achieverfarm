package dev.bti.achieverfarm.androidsdk.models.req;

import java.time.Instant;

import dev.bti.achieverfarm.androidsdk.enums.InventoryUpdateType;
import dev.bti.achieverfarm.androidsdk.enums.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
