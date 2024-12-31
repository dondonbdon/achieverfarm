package dev.bti.achieverfarm.androidsdk.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Availability {
    Double total;
    Double available;
    Double sold;
}
