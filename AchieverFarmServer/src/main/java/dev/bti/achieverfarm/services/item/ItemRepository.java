package dev.bti.achieverfarm.services.item;

import dev.bti.achieverfarm.models.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
}
