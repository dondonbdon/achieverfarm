package dev.bti.achieverfarm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PickupDetails {
    PickupLocation pickupLocation;
    Instant pickupTimestamp;
}
