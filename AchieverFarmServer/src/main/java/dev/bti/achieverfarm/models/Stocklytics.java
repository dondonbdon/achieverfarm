package dev.bti.achieverfarm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "stocklytics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stocklytics {
    @Id
    String id;
    List<Metric> metrics;
}
