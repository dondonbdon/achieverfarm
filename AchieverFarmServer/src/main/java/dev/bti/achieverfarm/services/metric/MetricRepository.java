package dev.bti.achieverfarm.services.metric;

import dev.bti.achieverfarm.models.Metric;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

public interface MetricRepository extends MongoRepository<Metric, String> {
    List<Metric> findAllByStocklyticsId(String stocklyticsId, Sort sort);
    List<Metric> findAllByStocklyticsIdAndTimestampBetween(String stocklyticsId, Instant from, Instant to, Sort sort);
}
