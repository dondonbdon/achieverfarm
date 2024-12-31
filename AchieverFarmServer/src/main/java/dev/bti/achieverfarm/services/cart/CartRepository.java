package dev.bti.achieverfarm.services.cart;

import dev.bti.achieverfarm.models.Cart;
import dev.bti.achieverfarm.models.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<List<Cart>> findByUserId(String userId);
}
