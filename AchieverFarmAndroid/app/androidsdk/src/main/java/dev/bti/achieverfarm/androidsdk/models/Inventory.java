package dev.bti.achieverfarm.androidsdk.models;

import java.util.Queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Inventory {
    Long lastUpdated;
    Availability today;
    Queue<Availability> futureAvailability;
}
