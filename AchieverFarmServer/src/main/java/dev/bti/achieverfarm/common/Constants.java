package dev.bti.achieverfarm.common;

import dev.bti.achieverfarm.exceptions.auth.AuthException;
import dev.bti.achieverfarm.exceptions.auth.IncorrectPasswordException;
import dev.bti.achieverfarm.exceptions.auth.MaxFailedAttemptsException;
import dev.bti.achieverfarm.exceptions.auth.UserNotFoundException;
import dev.bti.achieverfarm.exceptions.cart.CartException;
import dev.bti.achieverfarm.exceptions.cart.CartNotFoundException;
import dev.bti.achieverfarm.exceptions.inventory.InventoryException;
import dev.bti.achieverfarm.exceptions.inventory.InventoryNotFoundException;
import dev.bti.achieverfarm.exceptions.item.ItemException;
import dev.bti.achieverfarm.exceptions.item.ItemNotFoundException;
import dev.bti.achieverfarm.exceptions.order.OrderException;
import dev.bti.achieverfarm.exceptions.order.OrderNotFoundException;
import dev.bti.achieverfarm.exceptions.stock.StockException;
import dev.bti.achieverfarm.exceptions.stock.StockNotFoundException;

public class Constants {


    public static class Auth {
        public static final String _401 = "Provided token is invalid.";
        public static final String _401_1 = "Provided password was incorrect.";
        public static final String _403 = "Max password failed attempts.";
        public static final String _404 = "User not found.";

        public static final Integer MAX_ATTEMPTS = 5;
    }

    public static class Cart {
        public static final String _404 = "Cart not found.";
    }

    public static class Order {
        public static final String _404 = "Order not found.";
        public static final String _404_1 = "Orders not found.";
        public static final String _404_2 = "Active orders not found.";
        public static final String _404_3 = "Completed orders not found.";
    }

    public static class Item {
        public static final String _404 = "Item not found.";
    }

    public static class Stock {
        public static final String _404 = "Stock not found.";
    }

    public static class Inventory {
        public static final String _404 = "Inventory not found.";
    }

    public static class Throws {
        public static OrderException OrderNotFound = new OrderNotFoundException(Order._404);
        public static OrderException OrdersNotFound = new OrderNotFoundException(Order._404_1);
        public static OrderException ActiveOrdersNotFound = new OrderNotFoundException(Order._404_2);
        public static OrderException CompletedOrderNotFound = new OrderNotFoundException(Order._404_3);

        public static AuthException UserNotFound = new UserNotFoundException();
        public static AuthException IncorrectPassword = new IncorrectPasswordException();
        public static AuthException MaxFailedAttempts = new MaxFailedAttemptsException();

        public static CartException CartNotFoundException = new CartNotFoundException();

        public static ItemException ItemNotFound = new ItemNotFoundException();

        public static StockException StockNotFound = new StockNotFoundException();

        public static InventoryException InventoryNotFound = new InventoryNotFoundException();
    }
}
