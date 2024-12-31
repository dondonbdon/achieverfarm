package dev.bti.achieverfarm.androidsdk.models.res;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import dev.bti.achieverfarm.androidsdk.enums.SellingType;
import dev.bti.achieverfarm.androidsdk.models.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
