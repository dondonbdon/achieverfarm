package dev.bti.achieverfarm.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    String id;
    String userId;
    Integer quantity;
    Map<String, Integer> itemCounts;
    String name;
    Instant createdTimestamp, lastUpdatedTimestamp;
    BigDecimal total;
}
