package dev.bti.achieverfarm.androidsdk.models;

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
public class Stock {

    String id;
    String itemId;
    String inventoryId;
    Double quantityAvailable;
    Instant availableUntil;
    StockStatus status;
    InventoryUpdateType updateType;


}
