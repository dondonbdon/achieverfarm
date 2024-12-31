package dev.bti.achieverfarm.services.stocklytics;

import dev.bti.achieverfarm.models.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocklyticsRepository extends MongoRepository<Stock, String> {
}
