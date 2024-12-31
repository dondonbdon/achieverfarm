package dev.bti.achieverfarm.services.stock;

import dev.bti.achieverfarm.enums.InventoryUpdateType;
import dev.bti.achieverfarm.models.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends MongoRepository<Stock, String> {
    List<Stock> findStockByUpdateType(InventoryUpdateType updateType);
}
