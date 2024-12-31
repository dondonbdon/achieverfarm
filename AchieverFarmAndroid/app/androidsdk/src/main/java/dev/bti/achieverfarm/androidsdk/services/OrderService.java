package dev.bti.achieverfarm.androidsdk.services;

import java.util.List;

import dev.bti.achieverfarm.androidsdk.models.Order;
import dev.bti.achieverfarm.androidsdk.models.req.Checkout;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService extends Service {

    @GET("user/{userId}/order/{orderId}")
    Call<Response<Order>> getOrder(@Path("orderId") String orderId);

    @GET("user/{userId}/orders")
    Call<Response<List<Order>>> getOrders(@Path("userId") String userId);

    @GET("user/{userId}/orders/active")
    Call<Response<List<Order>>> getActiveOrders(@Path("userId") String userId);

    @GET("user/{userId}/orders/complete")
    Call<Response<List<Order>>> getCompletedOrders(@Path("userId") String userId);

    @POST("user/{userId}/checkout")
    Call<Response<Order>> checkout(@Path("userId") String userId, @Body Checkout checkout);
}

