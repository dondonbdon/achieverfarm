package dev.bti.achieverfarm.androidsdk.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import dev.bti.achieverfarm.androidsdk.common.Pair;

public class Invoice {
    String id;
    Instant timestamp;
    String userId;
    String orderId;
    Pair<Currency, BigDecimal> amount;
    Map<String, Integer> items;

}
