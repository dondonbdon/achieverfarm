package dev.bti.achieverfarm.models;

import dev.bti.achieverfarm.common.Pair;
import dev.bti.achieverfarm.common.Triple;
import dev.bti.achieverfarm.enums.WhereGrown;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdditionalInformation {
    Pair<String, String> origin;
    Pair<String, String> variety;
    Triple<Integer, Integer, ChronoUnit> growLength;
    Triple<Integer, Integer, ChronoUnit> avgShelfLife;
    WhereGrown whereGrown;
}
