package dev.bti.achieverfarm.androidsdk.helpers;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import dev.bti.achieverfarm.androidsdk.client.CallHandler;
import dev.bti.achieverfarm.androidsdk.client.ProviderApiClient;
import dev.bti.achieverfarm.androidsdk.enums.InventoryUpdateType;
import dev.bti.achieverfarm.androidsdk.enums.StockStatus;
import dev.bti.achieverfarm.androidsdk.models.req.StockReq;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import dev.bti.achieverfarm.androidsdk.services.StockService;

public class StockHelper {

    private final StockService service;

    public StockHelper(String token) {
        this.service = ProviderApiClient.GetInstance(StockService.class, token);
    }

    public CallHandler<Response<?>> getStock(String stockId) {
        return new CallHandler<>(service.getStock(stockId));
    }

    public CallHandler<Response<?>> getStocks(List<String> stockIds) {
        return new CallHandler<>(service.getStocks(stockIds));
    }

    public CallHandler<Response<?>> checkStock(Map<String, Integer> itemCounts) {
        return new CallHandler<>(service.checkStock(itemCounts));
    }

    public CallHandler<Response<?>> updateStock(String stockId, StockReq req) {
        return new CallHandler<>(service.updateStock(stockId, req));
    }

    public CallHandler<Response<?>> updateStockStatus(String stockId, StockStatus status) {
        return new CallHandler<>(service.updateStockStatus(stockId, status));
    }

    public CallHandler<Response<?>> updateStockAvailableUntil(String stockId, Instant availUntil) {
        return new CallHandler<>(service.updateStockAvailableUntil(stockId, availUntil));
    }

    public CallHandler<Response<?>> updateStockUpdateType(String stockId, InventoryUpdateType updateType, Double quantity) {
        return new CallHandler<>(service.updateStockUpdateType(stockId, updateType, quantity));
    }
}