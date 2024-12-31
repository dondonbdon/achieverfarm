package dev.bti.achieverfarm.models;

import dev.bti.achieverfarm.common.Pair;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class Invoice {
    String id;
    Instant timestamp;
    String userId;
    String orderId;
    Pair<Currency, BigDecimal> amount;
    Map<String, Integer> items;

}
