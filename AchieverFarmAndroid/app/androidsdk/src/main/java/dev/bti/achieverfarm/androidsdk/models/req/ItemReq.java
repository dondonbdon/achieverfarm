package dev.bti.achieverfarm.androidsdk.models.req;

import dev.bti.achieverfarm.androidsdk.enums.ItemType;
import dev.bti.achieverfarm.androidsdk.models.ItemInformation;
import dev.bti.achieverfarm.androidsdk.models.PricingInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemReq {
    PricingInfo pricingInfo;
    ItemInformation itemInformation;
    ItemType type;
    StockReq stockReq;
}
