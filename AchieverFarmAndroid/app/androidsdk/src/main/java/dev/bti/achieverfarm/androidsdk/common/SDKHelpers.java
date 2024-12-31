package dev.bti.achieverfarm.androidsdk.common;


import dev.bti.achieverfarm.androidsdk.helpers.AuthHelper;
import dev.bti.achieverfarm.androidsdk.helpers.CartHelper;
import dev.bti.achieverfarm.androidsdk.helpers.ItemHelper;
import dev.bti.achieverfarm.androidsdk.helpers.OrderHelper;
import dev.bti.achieverfarm.androidsdk.helpers.StockHelper;
import dev.bti.achieverfarm.androidsdk.helpers.StocklyticsHelper;
import dev.bti.achieverfarm.androidsdk.helpers.UserHelper;
import lombok.Getter;

@Getter
public class SDKHelpers {

    private final String token;

    private final AuthHelper authHelper;
    private final CartHelper cartHelper;
    private final ItemHelper itemHelper;
    private final OrderHelper orderHelper;
    private final StockHelper stockHelper;
    private final StocklyticsHelper stocklyticsHelper;
    private final UserHelper userHelper;

    private SDKHelpers(String token) {
        this.token = token;
        this.authHelper = new AuthHelper();
        this.cartHelper = new CartHelper(token);
        this.itemHelper = new ItemHelper(token);
        this.orderHelper = new OrderHelper(token);
        this.stockHelper = new StockHelper(token);
        this.stocklyticsHelper = new StocklyticsHelper(token);
        this.userHelper = new UserHelper(token);
    }

    public static SDKHelpers GetInstance(String token) {
        return new SDKHelpers(token);
    }


}
