package dev.bti.achieverfarm.androidsdk.helpers;

import java.util.List;

import dev.bti.achieverfarm.androidsdk.client.CallHandler;
import dev.bti.achieverfarm.androidsdk.client.ProviderApiClient;
import dev.bti.achieverfarm.androidsdk.models.Cart;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import dev.bti.achieverfarm.androidsdk.services.CartService;

public class CartHelper {

    private final CartService service;

    public CartHelper(String token) {
        this.service = ProviderApiClient.GetInstance(CartService.class, token);
    }

    public CallHandler<Response<Cart>> getCart(String userId, String cartId) {
        return new CallHandler<>(service.getCart(userId, cartId));
    }

    public CallHandler<Response<Cart>> getCart(String userId) {
        return new CallHandler<>(service.getCart(userId));
    }

    public CallHandler<Response<List<Cart>>> getCarts(String userId) {
        return new CallHandler<>(service.getCarts(userId));
    }

    public CallHandler<Response<Void>> deleteCart(String userId, String cartId) {
        return new CallHandler<>(service.deleteCart(userId, cartId));
    }

    public CallHandler<Response<Void>> saveCart(String userId, String cartName) {
        return new CallHandler<>(service.saveCart(userId, cartName));
    }

    public CallHandler<Response<Void>> clearCart(String userId) {
        return new CallHandler<>(service.clearCart(userId));
    }

    public CallHandler<Response<Void>> updateCartName(String userId, String cartId, String cartName) {
        return new CallHandler<>(service.updateCartName(userId, cartId, cartName));
    }

    public CallHandler<Response<Cart>> incrementCartItem(String userId, String itemId, Integer count) {
        return new CallHandler<>(service.incrementCartItem(userId, itemId, count));
    }

    public CallHandler<Response<Cart>> incrementCartItem(String userId, String cartId, String itemId, Integer count) {
        return new CallHandler<>(service.incrementCartItem(userId, cartId, itemId, count));
    }

    public CallHandler<Response<Cart>> decrementCartItem(String userId, String itemId, Integer count) {
        return new CallHandler<>(service.decrementCartItem(userId, itemId, count));
    }

    public CallHandler<Response<Cart>> decrementCartItem(String userId, String cartId, String itemId, Integer count) {
        return new CallHandler<>(service.decrementCartItem(userId, cartId, itemId, count));
    }

    public CallHandler<Response<Cart>> deleteCartItem(String userId, String itemId) {
        return new CallHandler<>(service.deleteCartItem(userId, itemId));
    }

    public CallHandler<Response<Cart>> deleteCartItem(String userId, String cartId, String itemId) {
        return new CallHandler<>(service.deleteCartItem(userId, cartId, itemId));
    }
}