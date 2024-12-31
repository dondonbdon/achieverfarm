package dev.bti.achieverfarm.androidsdk.models;


import dev.bti.achieverfarm.androidsdk.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class ItemOptions {
    String lastId;
    ItemType type;
    Integer count;
    String searchTerm;
}
