package dev.bti.achieverfarm.models;

import dev.bti.achieverfarm.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDetails {
    PaymentType paymentType;
    BigDecimal amount;
}
