package dev.bti.achieverfarm.models;

import dev.bti.achieverfarm.common.Common;
import dev.bti.achieverfarm.enums.StockStatus;
import dev.bti.achieverfarm.enums.InventoryUpdateType;
import dev.bti.achieverfarm.models.req.StockReq;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "stock")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Stock {

    @Id
    String id;
    Double quantityAvailable;
    Instant availableUntil;
    StockStatus status;
    InventoryUpdateType updateType;

    public Stock(StockReq stockReq, String itemId) {
        this.id = itemId;
        this.quantityAvailable = stockReq.getQuantityAvailable();
        this.availableUntil = stockReq.getAvailableUntil();
        this.status = stockReq.getStatus();
        this.updateType = stockReq.getUpdateType();
    }

    public static void update(Stock stock, StockReq stockReq) {
        stock.setQuantityAvailable(stockReq.getQuantityAvailable());
        stock.setAvailableUntil(stockReq.getAvailableUntil());
        stock.setStatus(stockReq.getStatus());
        stock.setUpdateType(stockReq.getUpdateType());
    }
}
