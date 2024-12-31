package dev.bti.achieverfarm.androidsdk.helpers;

import java.util.List;
import java.util.Map;

import dev.bti.achieverfarm.androidsdk.client.CallHandler;
import dev.bti.achieverfarm.androidsdk.client.ProviderApiClient;
import dev.bti.achieverfarm.androidsdk.models.Item;
import dev.bti.achieverfarm.androidsdk.models.ItemOptions;
import dev.bti.achieverfarm.androidsdk.models.PricingInfo;
import dev.bti.achieverfarm.androidsdk.models.req.ItemReq;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import dev.bti.achieverfarm.androidsdk.services.ItemService;

public class ItemHelper {

    private final ItemService service;

    public ItemHelper(String token) {
        this.service = ProviderApiClient.GetInstance(ItemService.class, token);
    }

    public CallHandler<Response<Item>> getItem(String itemId) {
        return new CallHandler<>(service.getItem(itemId));
    }

    public CallHandler<Response<List<Item>>> getItemsWithOptions(ItemOptions options) {
        return new CallHandler<>(service.getItemsWithOptions(options.getLastId(), options.getCount(), options.getType(), options.getSearchTerm()));
    }

    public CallHandler<Response<List<Item>>> getItemsOrderedBefore() {
        return new CallHandler<>(service.getItemsOrderedBefore());
    }

    public CallHandler<Response<List<Item>>> getItemsRecommendations() {
        return new CallHandler<>(service.getItemsRecommendations());
    }

    public CallHandler<Response<Void>> addItem(ItemReq item) {
        return new CallHandler<>(service.addItem(item));
    }

    public CallHandler<Response<Void>> addItems(List<ItemReq> items) {
        return new CallHandler<>(service.addItems(items));
    }

    public CallHandler<Response<Void>> deleteItem(String itemId) {
        return new CallHandler<>(service.deleteItem(itemId));
    }

    public CallHandler<Response<Void>> deleteItems(List<String> itemIds) {
        return new CallHandler<>(service.deleteItems(itemIds));
    }

    public CallHandler<Response<Void>> updateItem(String itemId, ItemReq updatedItem) {
        return new CallHandler<>(service.updateItem(itemId, updatedItem));
    }

    public CallHandler<Response<Void>> updateItems(Map<String, ItemReq> itemsToUpdate) {
        return new CallHandler<>(service.updateItems(itemsToUpdate));
    }

    public CallHandler<Response<Void>> updatePricingInfo(String itemId, PricingInfo pricingInfo) {
        return new CallHandler<>(service.updatePricingInfo(itemId, pricingInfo));
    }
}