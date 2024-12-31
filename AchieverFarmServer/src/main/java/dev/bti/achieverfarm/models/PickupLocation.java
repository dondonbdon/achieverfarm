package dev.bti.achieverfarm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pickup-locations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PickupLocation {
    @Id
    String id;
    String name;
    String address;
    LatLng latLng;
}
