package dev.bti.achieverfarm.scheduled;

import dev.bti.achieverfarm.common.Common;
import dev.bti.achieverfarm.enums.InventoryUpdateType;
import dev.bti.achieverfarm.enums.StockStatus;
import dev.bti.achieverfarm.exceptions.inventory.InventoryException;
import dev.bti.achieverfarm.logging.enums.Level;
import dev.bti.achieverfarm.logging.inventory.Operation;
import dev.bti.achieverfarm.logging.inventory.UpdateType;
import dev.bti.achieverfarm.logging.service.LogService;
import dev.bti.achieverfarm.models.Availability;
import dev.bti.achieverfarm.models.Inventory;
import dev.bti.achieverfarm.models.Stock;
import dev.bti.achieverfarm.services.inventory.InventoryRepository;
import dev.bti.achieverfarm.services.inventory.InventoryService;
import dev.bti.achieverfarm.services.item.ItemService;
import dev.bti.achieverfarm.services.stock.StockService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class InventoryScheduleService {

    @Autowired
    StockService stockService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    LogService logService;

    @Autowired
    ItemService itemService;

    @Scheduled(cron = "0 0 5 * * ?", zone = "Africa/Harare")
    public void autoResetInventorySchedule() {
        Common.Log.basic(logService, "----- start auto inventory updates -----");
        Common.Log.basic(logService, "----- start manual inventory updates-----");

        for (Stock stock : stockService.getAllStockByUpdateType(InventoryUpdateType.AUTO)) {
            Inventory inventory;

            try {
                inventory = inventoryService.getInventory(stock.getId());
                autoResetInventory(stock, inventory);
            } catch (InventoryException ignored) {
            }

            Logger.getGlobal().info(stock.toString());
        }

        for (Stock stock : stockService.getAllStockByUpdateType(InventoryUpdateType.MANUAL)) {
            Inventory inventory;
            try {
                inventory = inventoryService.getInventory(stock.getId());
                attemptManualReset(stock, inventory);
            } catch (InventoryException ignored) {
            }

        }

        Common.Log.basic(logService, "----- end auto inventory updates -----");
        Common.Log.basic(logService, "----- end manual inventory updates -----");
    }

    public void autoResetInventory(Stock stock, Inventory inventory) {
        Availability availability = new Availability();
        availability.setTotal(stock.getQuantityAvailable());
        availability.setSold(0D);
        availability.setAvailable(stock.getQuantityAvailable());

        inventory.setToday(availability);
        inventory.setLastUpdated(System.currentTimeMillis());
        inventoryRepository.save(inventory);

        Common.Log.inventory(logService, itemService, stock, inventory, UpdateType.AUTO,
                Level.I, Operation.ADD, null);
    }


    public void attemptManualReset(@NonNull Stock stock, Inventory inventory) {
        if (inventory.getFutureAvailability().isEmpty()) {

            try {
                stockService.updateStockStatus(stock.getId(), StockStatus.OUT_OF_STOCK);
            } catch (Exception ignored) {
            }

            String note = "No inventory left! Product is off the store!";
            Common.Log.inventory(logService, itemService, stock, inventory, UpdateType.MANU,
                    Level.E, Operation.NON, note);

        } else if (inventory.getFutureAvailability().size() == 1) {
            inventory.setToday(inventory.getFutureAvailability().remove());
            inventory.setLastUpdated(System.currentTimeMillis());
            inventoryRepository.save(inventory);

            String note = "No inventory for tomorrow. Add more inventory.";
            Common.Log.inventory(logService, itemService, stock, inventory, UpdateType.MANU,
                    Level.I, Operation.ADD, note);
        } else {
            inventory.setToday(inventory.getFutureAvailability().remove());
            inventory.setLastUpdated(System.currentTimeMillis());
            inventoryRepository.save(inventory);

            String note = String.format("Can auto update for %d more days.", inventory.getFutureAvailability().size());
            Common.Log.inventory(logService, itemService, stock, inventory, UpdateType.MANU,
                    Level.I, Operation.ADD, note);

        }
    }


}
