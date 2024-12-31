package dev.bti.achieverfarm.models;

import java.time.Instant;
import java.util.Map;

import dev.bti.achieverfarm.common.Common;
import dev.bti.achieverfarm.enums.OrderStatus;
import dev.bti.achieverfarm.models.req.Checkout;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    String id;
    String userId;
    PaymentDetails paymentDetails;
    PickupDetails pickupDetails;
    Map<String, Integer> items;
    Instant timestamp;
    OrderStatus status;
    Integer quantity;

    public static Order parse(Checkout checkout) {
        Order order = new Order();

        order.id = Common.generateRandomId();
        order.items = checkout.getItems();
        order.paymentDetails = checkout.getPaymentDetails();
        order.pickupDetails = checkout.getPickupDetails();
        order.status = OrderStatus.PREPARING;
        order.timestamp = Instant.now();
        order.quantity = checkout.getQuantity();

        return order;
    }
}
