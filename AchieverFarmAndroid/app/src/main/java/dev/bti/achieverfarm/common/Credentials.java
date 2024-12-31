package dev.bti.achieverfarm.common;

import android.content.Context;

import java.util.logging.Logger;

import dev.bti.achieverfarm.util.SecureStorage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Credentials {

    private static volatile Credentials credentials;
    public String TOKEN;
    public String UID;

    public static void init(Context context) {
        if (credentials == null) {
            synchronized (Credentials.class) {
                if (credentials == null) {
                    String userId = SecureStorage.getUserId(context);
                    String token = SecureStorage.getToken(context);
                    credentials = new Credentials(token, userId);
                }
            }
        }
    }

    public static Credentials GetInstance() {
        return credentials;
    }

    public boolean isLoggedIn() {
        return TOKEN != null && UID != null && tokenValid();
    }

    public void logout(Context context) {
        SecureStorage.clearData(context);
        credentials = null;
    }

    public boolean tokenValid() {
        Logger.getGlobal().info("TOKEN: " + TOKEN);
        return SecureStorage.isTokenValid(TOKEN);
    }
}
