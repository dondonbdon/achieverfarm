package dev.bti.achieverfarm.androidsdk.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import dev.bti.achieverfarm.androidsdk.enums.InventoryUpdateType;
import dev.bti.achieverfarm.androidsdk.enums.StockStatus;
import dev.bti.achieverfarm.androidsdk.models.req.StockReq;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StockService extends Service {

    @GET("/stock/{stockId}")
    Call<Response<?>> getStock(@Path("stockId") String stockId);

    @GET("/stock")
    Call<Response<?>> getStocks(@Query("stockIds") List<String> stockIds);

    @POST("/stock/check")
    Call<Response<?>> checkStock(@Body Map<String, Integer> itemCounts);

    @PUT("admin/stock/{stockId}")
    Call<Response<?>> updateStock(@Path("stockId") String stockId, @Body StockReq req);

    @PUT("admin/stock/{stockId}/status")
    Call<Response<?>> updateStockStatus(@Path("stockId") String stockId, @Body StockStatus status);

    @PUT("admin/stock/{stockId}/availUntil")
    Call<Response<?>> updateStockAvailableUntil(@Path("stockId") String stockId, @Query("availUntil") Instant availUntil);

    @PUT("admin/stock/{stockId}/type")
    Call<Response<?>> updateStockUpdateType(@Path("stockId") String stockId, @Query("updateType") InventoryUpdateType updateType, @Query("quantity") Double quantity);
}

