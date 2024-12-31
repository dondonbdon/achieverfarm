package dev.bti.achieverfarm.models;

import dev.bti.achieverfarm.enums.SellingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PricingInfo {
    BigDecimal usdPrice;
    List<Currency> conversionRates;
    Double max;
    SellingType sellingType;
}
