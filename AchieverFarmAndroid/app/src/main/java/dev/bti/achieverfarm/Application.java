package dev.bti.achieverfarm;

import android.util.Log;

import java.util.HashMap;
import java.util.List;

import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.androidsdk.models.Item;
import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.common.Credentials;
import lombok.Getter;
import lombok.Setter;


public class Application extends android.app.Application {

    @Getter
    static GlobalCartHelper globalCartHelper;

    @Getter
    static HashMap<String, Item> itemCache = new HashMap<>();

    @Getter
    @Setter
    static String currCart = "";

    static public void appendItemCache(List<Item> items) {
        items.forEach(item -> itemCache.put(item.getId(), item));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initCredentials();
        initCart();
    }

    private void initCredentials() {
        Credentials.init(getApplicationContext());
    }

    private void initCart() {
        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getCartHelper()
                .getCart(Constants.UID)
                .addOnSuccessListener(response -> {
                    Log.d("Response", response.toString());
                    globalCartHelper = new GlobalCartHelper(response.getData(), new HashMap<>());
                }).execute();
    }

    private void updateShoppingCart(String id) {
        currCart = id.isEmpty() ? "" : id;
    }
}
