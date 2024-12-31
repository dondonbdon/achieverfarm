package dev.bti.achieverfarm.androidsdk.services;

import java.time.Instant;

import dev.bti.achieverfarm.androidsdk.enums.ChunkUnit;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StocklyticsService extends Service {

    @GET("/api/v1/admin/stocklytics/{stockId}/ranged")
    Call<Response<?>> retrieveRange(@Path("stockId") String stockId, @Query("from") Instant from, @Query("to") Instant to, @Query("chunkSize") ChunkUnit chunkSize);

    @GET("/api/v1/admin/stocklytics/{stockId}/all")
    Call<Response<?>> retrieveAll(@Path("stockId") String stockId, @Query("chunkSize") ChunkUnit chunkSize);
}

