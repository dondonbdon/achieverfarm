package dev.bti.achieverfarm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Queue;

@Document(collection = "inventory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Inventory {
    @Id
    String id;
    Long lastUpdated;
    Availability today;
    Queue<Availability> futureAvailability;
}
