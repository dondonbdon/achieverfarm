package dev.bti.achieverfarm.androidsdk.helpers;

import java.util.List;

import dev.bti.achieverfarm.androidsdk.client.CallHandler;
import dev.bti.achieverfarm.androidsdk.client.ProviderApiClient;
import dev.bti.achieverfarm.androidsdk.models.Order;
import dev.bti.achieverfarm.androidsdk.models.req.Checkout;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import dev.bti.achieverfarm.androidsdk.services.OrderService;

public class OrderHelper {

    private final OrderService service;

    public OrderHelper(String token) {
        this.service = ProviderApiClient.GetInstance(OrderService.class, token);
    }

    public CallHandler<Response<Order>> getOrder(String orderId) {
        return new CallHandler<>(service.getOrder(orderId));
    }

    public CallHandler<Response<List<Order>>> getOrders(String userId) {
        return new CallHandler<>(service.getOrders(userId));
    }

    public CallHandler<Response<List<Order>>> getActiveOrders(String userId) {
        return new CallHandler<>(service.getActiveOrders(userId));
    }

    public CallHandler<Response<List<Order>>> getCompletedOrders(String userId) {
        return new CallHandler<>(service.getCompletedOrders(userId));
    }

    public CallHandler<Response<Order>> checkout(String userId, Checkout checkout) {
        return new CallHandler<>(service.checkout(userId, checkout));
    }
}