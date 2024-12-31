package dev.bti.achieverfarm.services.inventory;

import dev.bti.achieverfarm.models.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {
}
