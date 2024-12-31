package dev.bti.achieverfarm.models.req;

import dev.bti.achieverfarm.models.PaymentDetails;
import dev.bti.achieverfarm.models.PickupDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Checkout {
    PaymentDetails paymentDetails;
    PickupDetails pickupDetails;
    Map<String, Integer> items;
    Integer quantity;
}
