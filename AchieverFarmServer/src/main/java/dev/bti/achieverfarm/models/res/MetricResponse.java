package dev.bti.achieverfarm.models.res;

import dev.bti.achieverfarm.enums.SellingType;
import dev.bti.achieverfarm.models.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MetricResponse {
    String stocklyticsId;
    Instant timestampFrom;
    Instant timestampTo;
    Map<Currency, BigDecimal> amountGenerated;
    Map<SellingType, Double> quantitySold;
}
