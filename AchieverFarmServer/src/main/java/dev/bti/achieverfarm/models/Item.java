package dev.bti.achieverfarm.models;

import dev.bti.achieverfarm.common.Common;
import dev.bti.achieverfarm.enums.ItemType;
import dev.bti.achieverfarm.models.req.ItemReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item {
    @Id
    String id;
    PricingInfo pricingInfo;
    ItemInformation itemInformation;
    ItemType type;
    String stockId;

    public Item(ItemReq itemReq) {
        this.id = Common.generateRandomId();
        this.pricingInfo = itemReq.getPricingInfo();
        this.itemInformation = itemReq.getItemInformation();
        this.type = itemReq.getType();
    }
}
