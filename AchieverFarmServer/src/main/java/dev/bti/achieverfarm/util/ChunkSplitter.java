package dev.bti.achieverfarm.util;

import dev.bti.achieverfarm.models.Metric;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChunkSplitter {

    public static List<List<Metric>> splitItemsByTimeUnit(List<Metric> items, ChronoUnit unit, int interval) {
        items.sort(Comparator.comparing(Metric::getTimestamp));

        List<List<Metric>> result = new ArrayList<>();
        List<Metric> currentChunk = new ArrayList<>();

        if (items.isEmpty()) return result;

        Instant chunkStart = items.getFirst().getTimestamp();
        Instant chunkEnd = chunkStart.plus(interval, unit);

        for (Metric item : items) {
            if (item.getTimestamp().isBefore(chunkEnd)) {

                currentChunk.add(item);
            } else {

                result.add(new ArrayList<>(currentChunk));
                currentChunk.clear();


                chunkStart = chunkStart.plus(interval, unit);
                chunkEnd = chunkStart.plus(interval, unit);


                currentChunk.add(item);
            }
        }

        if (!currentChunk.isEmpty()) {
            result.add(currentChunk);
        }

        return result;
    }
}

