package dev.bti.achieverfarm.services.stock;

import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.enums.InventoryUpdateType;
import dev.bti.achieverfarm.enums.StockStatus;
import dev.bti.achieverfarm.exceptions.stock.StockException;
import dev.bti.achieverfarm.models.Stock;
import dev.bti.achieverfarm.models.req.StockReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class StockService {

    @Autowired
    StockRepository repository;

    public Stock getStock(String stockId) throws StockException {
        Stock stock = repository.findById(stockId).orElseThrow(() -> Constants.Throws.StockNotFound);
        Logger.getAnonymousLogger().info("Found: " + stock.toString());
        return stock;
    }

    public List<Stock> getStock(List<String> ids) {
        return repository.findAllById(ids);
    }

    public List<Stock> checkStock(Map<String, Integer> itemCounts) throws StockException {
        List<Stock> notAvailable = new ArrayList<>();

        for (String key : itemCounts.keySet()) {
            Stock stock = getStock(key);

            if ((stock.getQuantityAvailable() < itemCounts.get(key)) ||
                    // (Instant.now().toEpochMilli() > stock.getAvailableUntil().toEpochMilli()) ||
                    (stock.getStatus() != StockStatus.AVAILABLE)) {
                notAvailable.add(stock);
            }
        }

        Logger.getGlobal().log(Level.INFO, notAvailable.toString());
        return notAvailable;
    }

    public void updateStock(String stockId, StockReq req) throws StockException {
        Stock stock = getStock(stockId);
        Stock.update(stock, req);
        repository.save(stock);
    }

    public void updateStockStatus(String stockId, StockStatus status) throws StockException {
        Stock stock = getStock(stockId);
        stock.setStatus(status);
        repository.save(stock);
    }

    public void updateAvailableUntil(String stockId, Instant availUntil) throws StockException {
        Stock stock = getStock(stockId);
        stock.setAvailableUntil(availUntil);
        repository.save(stock);
    }

    public void updateStockUpdateType(String stockId, InventoryUpdateType updateType, Double quantity) throws StockException {
        Stock stock = getStock(stockId);
        stock.setUpdateType(updateType);

        if (updateType == InventoryUpdateType.AUTO) {
            stock.setQuantityAvailable(quantity);
        }

        repository.save(stock);
    }

    public List<Stock> getAllStockByUpdateType(InventoryUpdateType updateType) {
        return repository.findStockByUpdateType(updateType);
    }

}
