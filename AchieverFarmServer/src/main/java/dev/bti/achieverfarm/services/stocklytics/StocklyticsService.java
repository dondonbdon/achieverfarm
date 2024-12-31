package dev.bti.achieverfarm.services.stocklytics;

import dev.bti.achieverfarm.services.metric.MetricRepository;
import dev.bti.achieverfarm.enums.ChunkUnit;
import dev.bti.achieverfarm.enums.SellingType;
import dev.bti.achieverfarm.models.Currency;
import dev.bti.achieverfarm.models.Metric;
import dev.bti.achieverfarm.models.res.MetricResponse;
import dev.bti.achieverfarm.util.ChunkSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StocklyticsService {

    @Autowired
    MetricRepository metricRepository;

    public List<MetricResponse> retrieve(String stocklyticsId, Instant from, Instant to, ChunkUnit unit) {
        Sort sort = Sort.by(Sort.Direction.ASC, "timestamp");
        List<Metric> metrics = metricRepository.findAllByStocklyticsIdAndTimestampBetween(stocklyticsId, from, to, sort);
        List<List<Metric>> metricsList = ChunkSplitter.splitItemsByTimeUnit(metrics, unit.getUnit(), unit.getCount());
        List<MetricResponse> metricResponses = new ArrayList<>();

        for (List<Metric> list : metricsList) {
            metricResponses.add(parse(list));
        }

        return metricResponses;
    }

    public List<MetricResponse> retrieve(String stocklyticsId, ChunkUnit unit) {
        Sort sort = Sort.by(Sort.Direction.ASC, "timestamp");
        List<Metric> metrics = metricRepository.findAllByStocklyticsId(stocklyticsId, sort);
        List<List<Metric>> metricsList = ChunkSplitter.splitItemsByTimeUnit(metrics, unit.getUnit(), unit.getCount());
        List<MetricResponse> metricResponses = new ArrayList<>();

        for (List<Metric> list : metricsList) {
            metricResponses.add(parse(list));
        }

        return metricResponses;
    }

    private MetricResponse parse(List<Metric> metrics) {
        MetricResponse newMetric = new MetricResponse();

        Map<Currency, BigDecimal> amountGenerated = new HashMap<>();
        Map<SellingType, Double> quantitySold = new HashMap<>();

        for (Metric metric : metrics) {
            metric.getAmountGenerated().forEach((key, value) ->
                    amountGenerated.merge(key, value, BigDecimal::add)
            );

            metric.getQuantitySold().forEach((key, value) ->
                    quantitySold.merge(key, value, Double::sum)
            );
        }

        newMetric.setTimestampFrom(metrics.getFirst().getTimestamp());
        newMetric.setTimestampTo(metrics.getLast().getTimestamp());
        newMetric.setAmountGenerated(amountGenerated);
        newMetric.setQuantitySold(quantitySold);

        return newMetric;
    }
}
