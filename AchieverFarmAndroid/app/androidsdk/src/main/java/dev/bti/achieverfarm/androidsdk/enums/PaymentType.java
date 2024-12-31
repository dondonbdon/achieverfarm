package dev.bti.achieverfarm.androidsdk.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PaymentType {
    COD("Cash"), CARD("Cart"), ECOCASH("EcoCash");
    String paymentType;

    public static List<String> valuesString() {
        List<String> values = new ArrayList<>();

        for (PaymentType type : values()) {
            values.add(type.getPaymentType());
        }

        return values;
    }
}
