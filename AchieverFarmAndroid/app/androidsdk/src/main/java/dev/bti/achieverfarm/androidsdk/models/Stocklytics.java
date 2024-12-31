package dev.bti.achieverfarm.androidsdk.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stocklytics {
    String id;
    List<Metric> metrics;
}
