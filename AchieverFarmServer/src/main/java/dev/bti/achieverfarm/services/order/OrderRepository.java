package dev.bti.achieverfarm.services.order;

import dev.bti.achieverfarm.enums.OrderStatus;
import dev.bti.achieverfarm.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

//    Optional<List<Order>> findByUserId(String userId);
//
//    Optional<List<Order>> findByUserIdAndStatus(String userId, OrderStatus status);
//
//    Optional<List<Order>> findByUserIdAndStatusIn(String userId, OrderStatus[] active);

    @Query("{ 'userId': ?0 }")
    Optional<List<Order>> findByUserId(String userId);

    @Query("{ 'userId': ?0, 'status': ?1 }")
    Optional<List<Order>> findByUserIdAndStatus(String userId, OrderStatus status);

    @Query("{ 'userId': ?0, 'status': { $in: ?1 } }")
    Optional<List<Order>> findByUserIdAndStatusIn(String userId, OrderStatus[] active);

}
