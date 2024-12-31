package dev.bti.achieverfarm.models.req;

import dev.bti.achieverfarm.enums.ItemType;
import dev.bti.achieverfarm.models.ItemInformation;
import dev.bti.achieverfarm.models.PricingInfo;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemReq {
    PricingInfo pricingInfo;
    ItemInformation itemInformation;
    ItemType type;
    StockReq stockReq;
}
