package dev.bti.achieverfarm.models;

import dev.bti.achieverfarm.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemOptions {
    String lastId, searchTerm;
    ItemType type;
    Integer count;
}
