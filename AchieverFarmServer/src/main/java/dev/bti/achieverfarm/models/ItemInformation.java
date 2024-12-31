package dev.bti.achieverfarm.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemInformation {
    String name;
    String description;
    List<String> usesList;
    List<String> packaging;
    AdditionalInformation additionalInformation;
    String photoUrl;
}
