package dev.bti.achieverfarm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ChunkUnit {

    DAY(ChronoUnit.DAYS, 1),
    WEEK(ChronoUnit.WEEKS, 1),
    FORTNIGHT(ChronoUnit.WEEKS, 2),
    MONTH(ChronoUnit.MONTHS, 1),
    YEAR(ChronoUnit.YEARS, 1);

    ChronoUnit unit;
    Integer count;
}


