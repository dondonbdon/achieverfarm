package dev.bti.achieverfarm.androidsdk.helpers;

import java.time.Instant;

import dev.bti.achieverfarm.androidsdk.client.CallHandler;
import dev.bti.achieverfarm.androidsdk.client.ProviderApiClient;
import dev.bti.achieverfarm.androidsdk.enums.ChunkUnit;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import dev.bti.achieverfarm.androidsdk.services.StocklyticsService;

public class StocklyticsHelper {

    private final StocklyticsService service;

    public StocklyticsHelper(String token) {
        this.service = ProviderApiClient.GetInstance(StocklyticsService.class, token);
    }

    public CallHandler<Response<?>> retrieveRange(String stockId, Instant from, Instant to, ChunkUnit chunkSize) {
        return new CallHandler<>(service.retrieveRange(stockId, from, to, chunkSize));
    }

    public CallHandler<Response<?>> retrieveAll(String stockId, ChunkUnit chunkSize) {
        return new CallHandler<>(service.retrieveAll(stockId, chunkSize));
    }

}