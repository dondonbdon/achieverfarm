package dev.bti.achieverfarm.androidsdk.services;

import java.util.List;
import java.util.Map;

import dev.bti.achieverfarm.androidsdk.enums.ItemType;
import dev.bti.achieverfarm.androidsdk.models.Item;
import dev.bti.achieverfarm.androidsdk.models.PricingInfo;
import dev.bti.achieverfarm.androidsdk.models.req.ItemReq;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ItemService extends Service {

    @GET("item/{itemId}")
    Call<Response<Item>> getItem(@Path("itemId") String itemId);

    @GET("items")
    Call<Response<List<Item>>> getItemsWithOptions(@Query("lastId") String lastId,
                                                   @Query("count") int count,
                                                   @Query("type") ItemType type,
                                                   @Query("searchTerm") String searchTerm);

    @GET("items/userOrderedBefore")
    Call<Response<List<Item>>> getItemsOrderedBefore();

    @GET("items/recommendations")
    Call<Response<List<Item>>> getItemsRecommendations();

    @POST("admin/item")
    Call<Response<Void>> addItem(@Body ItemReq item);

    @POST("admin/items")
    Call<Response<Void>> addItems(@Body List<ItemReq> items);

    @DELETE("admin/item/{itemId}")
    Call<Response<Void>> deleteItem(@Path("itemId") String itemId);

    @DELETE("admin/items")
    Call<Response<Void>> deleteItems(@Body List<String> itemIds);

    @PUT("admin/item/{itemId}")
    Call<Response<Void>> updateItem(@Path("itemId") String itemId, @Body ItemReq itemToUpdate);

    @PUT("admin/items")
    Call<Response<Void>> updateItems(@Body Map<String, ItemReq> itemsToUpdate);

    @PUT("admin/item/{itemId}/update-pricing")
    Call<Response<Void>> updatePricingInfo(@Path("itemId") String itemId, @Body PricingInfo pricingInfo);


}

