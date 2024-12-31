package dev.bti.achieverfarm.androidsdk.models.req;

import java.util.Map;

import dev.bti.achieverfarm.androidsdk.models.PaymentDetails;
import dev.bti.achieverfarm.androidsdk.models.PickupDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Checkout {
    PaymentDetails paymentDetails;
    PickupDetails pickupDetails;
    Map<String, Integer> items;
    Integer quantity;
}
