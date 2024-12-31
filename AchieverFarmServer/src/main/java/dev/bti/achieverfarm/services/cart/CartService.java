package dev.bti.achieverfarm.services.cart;

import dev.bti.achieverfarm.common.Common;
import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.exceptions.Exception;
import dev.bti.achieverfarm.exceptions.cart.CartException;
import dev.bti.achieverfarm.models.Cart;
import dev.bti.achieverfarm.models.Item;
import dev.bti.achieverfarm.services.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    CartRepository repository;

    @Autowired
    ItemService itemService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Cart getCart(String userId, String cartId) throws Exception {
        if (cartId == null) {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(userId);

            Cart.CartBuilder cart = Cart.builder();
            cart.id(userId);
            cart.userId(userId);
            Map<String, Integer> convertedMap = new HashMap<>();
            int quantity = 0;
            BigDecimal total = BigDecimal.ZERO;
            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                String key = (String) entry.getKey();
                Item item = itemService.getItem(key);
                int value = Integer.parseInt(entry.getValue().toString());

                convertedMap.put(key, value);
                quantity += value;
                total = total.add(BigDecimal.valueOf(value).multiply((item.getPricingInfo().getUsdPrice())));
            }

            cart.itemCounts(convertedMap);
            cart.quantity(quantity);
            cart.total(total);

            return cart.build();
        } else {
            return repository.findById(cartId).orElseThrow(() -> Constants.Throws.CartNotFoundException);
        }
    }

    public List<Cart> getCarts(String userId) throws CartException {
        return repository.findByUserId(userId).orElseThrow(() -> Constants.Throws.CartNotFoundException);
    }

    public void deleteCart(String userId, String cartId) {
        if (cartId == null) {
            removeCartRedis(userId);
        } else {
            repository.deleteById(cartId);
        }
    }

    public void saveCart(String userId, String cartName) throws Exception {
        Cart cart = getCart(userId, null);
        cart.setId(Common.generateRandomId());
        cart.setName(cartName);
        cart.setCreatedTimestamp(Instant.now());
        cart.setLastUpdatedTimestamp(Instant.now());
        repository.save(cart);

        deleteCart(userId, null);
    }

    public void updateCartName(String userId, String cartId, String cartName) throws Exception {
        Cart cart = getCart(userId, cartId);
        cart.setName(cartName);
        cart.setLastUpdatedTimestamp(Instant.now());
        repository.save(cart);
    }

    public Cart incrementCartItem(String userId, String cartId, String itemId, int count) throws Exception {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive.");
        }

        Cart cart = getCart(userId, cartId);

        int currentCount = cart.getItemCounts().getOrDefault(itemId, 0);
        cart.getItemCounts().put(itemId, currentCount + count);
        cart.setQuantity(cart.getQuantity() + count);

        cart.setTotal(getTotal(cart));

        if (cartId == null) {
            incrementItemRedis(userId, itemId, count);
        } else {
            cart = repository.save(cart);
        }

        return cart;
    }

    public Cart decrementCartItem(String userId, String cartId, String itemId, int count) throws Exception {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive.");
        }

        Cart cart = getCart(userId, cartId);

        int currentCount = cart.getItemCounts().getOrDefault(itemId, 0);
        if (currentCount == 0) {
            throw new IllegalArgumentException("Item does not exist in the cart.");
        }

        if (count >= currentCount) {
            return deleteCartItem(userId, cartId, itemId);
        } else {
            cart.getItemCounts().put(itemId, currentCount - count);
            cart.setQuantity(cart.getQuantity() - count);
        }

        cart.setTotal(getTotal(cart));

        if (cartId == null) {
            incrementItemRedis(userId, itemId, -count);
        } else {
            cart = repository.save(cart);
        }

        return cart;
    }

    public Cart deleteCartItem(String userId, String cartId, String itemId) throws Exception {
        Cart cart = getCart(userId, cartId);
        cart.setQuantity(cart.getQuantity() - cart.getItemCounts().getOrDefault(itemId, 0));
        cart.getItemCounts().remove(itemId);
        cart.setTotal(getTotal(cart));

        if (cartId == null) {
            removeItemRedis(userId, itemId);
        } else {
            repository.deleteById(cartId);
        }

        return cart;
    }

    // Redis

    private void updateCartRedis(String key, Cart cart) {
        redisTemplate.opsForHash().putAll(key, cart.getItemCounts());
    }

    private void incrementItemRedis(String key, String itemId, int delta) {
        redisTemplate.opsForHash().increment(key, itemId, delta);
    }

    private void removeItemRedis(String key, String itemId) {
        redisTemplate.opsForHash().delete(key, itemId);
    }

    private void removeCartRedis(String key) {
        redisTemplate.delete(key);
    }

    // Helper

    public BigDecimal getTotal(Cart cart) throws Exception {

        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entry : cart.getItemCounts().entrySet()) {
            String itemId = entry.getKey();
            int quantity = entry.getValue();

            Item item = itemService.getItem(itemId);

            if (item != null && item.getPricingInfo() != null) {
                BigDecimal price = item.getPricingInfo().getUsdPrice();
                total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
            }
        }

        return total;
    }
}
