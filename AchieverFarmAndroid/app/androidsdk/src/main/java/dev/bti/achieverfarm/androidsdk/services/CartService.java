package dev.bti.achieverfarm.androidsdk.services;

import java.util.List;

import dev.bti.achieverfarm.androidsdk.models.Cart;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartService extends Service {

    @GET("user/{userId}/cart/{cartId}")
    Call<Response<Cart>> getCart(@Path("userId") String userId, @Path("cartId") String cartId);

    @GET("user/{userId}/cart")
    Call<Response<Cart>> getCart(@Path("userId") String userId);

    @GET("user/{userId}/carts")
    Call<Response<List<Cart>>> getCarts(@Path("userId") String userId);

    @DELETE("user/{userId}/cart/{cartId}")
    Call<Response<Void>> deleteCart(@Path("userId") String userId, @Path("cartId") String cartId);

    @POST("user/{userId}/cart")
    Call<Response<Void>> saveCart(@Path("userId") String userId, @Query("cartName") String cartName);

    @DELETE("user/{userId}/cart")
    Call<Response<Void>> clearCart(@Path("userId") String userId);

    @PUT("user/{userId}/cart/{cartId}/updateName")
    Call<Response<Void>> updateCartName(@Path("userId") String userId, @Path("cartId") String cartId,
                                        @Query("cartName") String cartName);

    @PUT("user/{userId}/cart/{cartId}/item/{itemId}/inc")
    Call<Response<Cart>> incrementCartItem(@Path("userId") String userId, @Path("cartId") String cartId,
                                           @Path("itemId") String itemId, @Query("count") Integer count);

    @PUT("user/{userId}/cart/item/{itemId}/inc")
    Call<Response<Cart>> incrementCartItem(@Path("userId") String userId, @Path("itemId") String itemId,
                                           @Query("count") Integer count);

    @PUT("user/{userId}/cart/{cartId}/item/{itemId}/dec")
    Call<Response<Cart>> decrementCartItem(@Path("userId") String userId, @Path("cartId") String cartId,
                                           @Path("itemId") String itemId, @Query("count") Integer count);

    @PUT("user/{userId}/cart/item/{itemId}/dec")
    Call<Response<Cart>> decrementCartItem(@Path("userId") String userId, @Path("itemId") String itemId,
                                           @Query("count") Integer count);

    @DELETE("user/{userId}/cart/{cartId}/item/{itemId}")
    Call<Response<Cart>> deleteCartItem(@Path("userId") String userId, @Path("cartId") String cartId,
                                        @Path("itemId") String itemId);

    @DELETE("user/{userId}/cart/item/{itemId}")
    Call<Response<Cart>> deleteCartItem(@Path("userId") String userId, @Path("itemId") String itemId);


}

