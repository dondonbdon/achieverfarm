package dev.bti.achieverfarm.models;

import dev.bti.achieverfarm.enums.SellingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Document(collection = "metrics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Metric {
    @Id
    String id;
    String stocklyticsId;
    Instant timestamp;
    Map<Currency, BigDecimal> amountGenerated;
    Map<SellingType, Double> quantitySold;
}
