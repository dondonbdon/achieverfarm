package dev.bti.achieverfarm.services.order;

import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.enums.OrderStatus;
import dev.bti.achieverfarm.exceptions.order.OrderException;
import dev.bti.achieverfarm.models.Order;
import dev.bti.achieverfarm.models.req.Checkout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;

    public Order getOrder(String orderId) throws OrderException {
        return repository.findById(orderId).orElseThrow(() -> Constants.Throws.OrderNotFound);
    }

    public List<Order> getOrders(String userId) throws OrderException {
        return repository.findByUserId(userId).orElseThrow(() -> Constants.Throws.OrdersNotFound);
    }

    public List<Order> getActiveOrders(String userId) throws OrderException {
        OrderStatus[] active = {OrderStatus.READY, OrderStatus.PREPARING};
        return repository.findByUserIdAndStatusIn(userId, active).orElseThrow(() -> Constants.Throws.ActiveOrdersNotFound);
    }

    public List<Order> getCompletedOrders(String userId) throws OrderException {
        return repository.findByUserIdAndStatus(userId, OrderStatus.COMPLETE).orElseThrow(() -> Constants.Throws.CompletedOrderNotFound);
    }

    public Order checkout(String userId, Checkout checkout) {
        Order order = Order.parse(checkout);
        order.setUserId(userId);
        return repository.save(order);
    }


}
