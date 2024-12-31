package dev.bti.achieverfarm.util;

import dev.bti.achieverfarm.common.Common;
import dev.bti.achieverfarm.logging.auth.Type;
import dev.bti.achieverfarm.logging.auth.UserLog;
import dev.bti.achieverfarm.logging.enums.Level;
import dev.bti.achieverfarm.logging.enums.LogType;
import dev.bti.achieverfarm.logging.inventory.InventoryLog;
import dev.bti.achieverfarm.logging.inventory.Operation;
import dev.bti.achieverfarm.logging.inventory.UpdateType;
import dev.bti.achieverfarm.logging.models.BasicLog;
import dev.bti.achieverfarm.logging.models.OrderLog;
import dev.bti.achieverfarm.models.Inventory;
import dev.bti.achieverfarm.models.Item;
import dev.bti.achieverfarm.models.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

public class LogFactory {

    public static <T> T get(Class<T> tClass, Payload payload) {
        if (tClass == BasicLog.class) {
            return tClass.cast(basicLogMaker((BasicPayload) payload));
        } else if (tClass == UserLog.class) {
            return tClass.cast(userLogMaker((UserPayload) payload));
        } else if (tClass == InventoryLog.class) {
            return tClass.cast(inventoryLogMaker((InventoryPayload) payload));
        } else if (tClass == OrderLog.class) {
            return tClass.cast(orderLogMaker((OrderPayload) payload));
        }

        return null;
    }

    static BasicLog basicLogMaker(BasicPayload payload) {
        BasicLog.BasicLogBuilder<?, ?> builder = BasicLog.builder();
        builder.id(Common.generateRandomId());
        builder.type(LogType.BSC);
        builder.timestamp(Instant.now());
        builder.note(payload.getNote());
        return builder.build();
    }

    static UserLog userLogMaker(UserPayload payload) {
        UserLog.UserLogBuilder<?, ?> builder = UserLog.builder();
        builder.id(Common.generateRandomId());
        builder.commanderName("SYS");
        builder.type(LogType.USR);
        builder.timestamp(Instant.now());
        builder.name(payload.getEmail());
        builder.note(payload.getNote());
        builder.level(payload.getLevel());
        builder.authLogType(payload.type);
        return builder.build();
    }

    static InventoryLog inventoryLogMaker(InventoryPayload payload) {
        InventoryLog.InventoryLogBuilder<?, ?> builder = InventoryLog.builder();

        builder.id(Common.generateRandomId());
        builder.commanderName("SYS");
        builder.type(LogType.INV);
        builder.timestamp(Instant.now());
        builder.updateType(payload.getType());
        builder.itemId(payload.getStock().getId());
        builder.itemName(payload.getItem().getItemInformation().getName());
        builder.operation(payload.getOperation());
        builder.quantity(payload.getInventory().getToday().getTotal());
        builder.sellingType(payload.getItem().getPricingInfo().getSellingType());
        builder.note(payload.getNote());
        builder.level(payload.getLevel());

        return builder.build();
    }

    static OrderLog orderLogMaker(OrderPayload payload) {
        OrderLog.OrderLogBuilder<?, ?> builder = OrderLog.builder();
        builder.id(Common.generateRandomId());
        builder.commanderName("SYS");
        builder.type(LogType.ODR);
        builder.timestamp(Instant.now());
        builder.note(payload.getNote());
        builder.level(payload.getLevel());
        return builder.build();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class Payload {
        String note;
        Level level;
    }

    @Getter
    @Setter
    @SuperBuilder
    public static class BasicPayload extends Payload {
        public BasicPayload(String note, Level level) {
            super(note, level);
        }
    }

    @Getter
    @Setter
    @SuperBuilder
    public static class UserPayload extends Payload {
        String email;
        Type type;

        public UserPayload(String note, Level level, String email, Type type) {
            super(note, level);
            this.email = email;
            this.type = type;
        }
    }

    @Getter
    @Setter
    @SuperBuilder
    public static class InventoryPayload extends Payload {
        Item item;
        Operation operation;
        UpdateType type;
        Stock stock;
        Inventory inventory;

        public InventoryPayload(String note, Level level, Item item, Operation operation, UpdateType type, Stock stock, Inventory inventory) {
            super(note, level);
            this.item = item;
            this.operation = operation;
            this.type = type;
            this.stock = stock;
            this.inventory = inventory;
        }
    }

    @Getter
    @Setter
    @SuperBuilder
    public static class OrderPayload extends Payload {

    }
}
