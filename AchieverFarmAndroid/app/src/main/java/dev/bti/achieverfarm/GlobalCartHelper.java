package dev.bti.achieverfarm;

import java.util.Map;

import dev.bti.achieverfarm.androidsdk.models.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GlobalCartHelper {
    private Cart globalCart;
    private Map<String, Cart> savedCarts;

    public Cart getCart() {
        String id = Application.getCurrCart();

        if (id.isEmpty()) {
            return globalCart;
        } else {
            return savedCarts.get(id);
        }
    }
}
