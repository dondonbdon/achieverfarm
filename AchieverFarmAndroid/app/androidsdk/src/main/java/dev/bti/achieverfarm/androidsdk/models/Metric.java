package dev.bti.achieverfarm.androidsdk.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import dev.bti.achieverfarm.androidsdk.enums.SellingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Metric {
    String id;
    String stocklyticsId;
    Instant timestamp;
    Map<Currency, BigDecimal> amountGenerated;
    Map<SellingType, Double> quantitySold;
}
