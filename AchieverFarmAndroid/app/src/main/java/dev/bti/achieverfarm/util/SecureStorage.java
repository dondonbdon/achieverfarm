package dev.bti.achieverfarm.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.auth0.android.jwt.JWT;

import java.io.IOException;
import java.security.GeneralSecurityException;

import dev.bti.achieverfarm.androidsdk.common.Pair;
import dev.bti.achieverfarm.common.Credentials;

public class SecureStorage {
    private static final String PREFS_NAME = "secure_prefs";
    private static final String TOKEN_KEY = "user_token";
    private static final String USER_ID_KEY = "user_id";

    private static SharedPreferences getSharedPreferences(Context context) throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static void saveTokenAndUserId(Context context, Pair<String, String> userIdAndToken) {
        try {
            SharedPreferences prefs = getSharedPreferences(context);
            prefs.edit()
                    .putString(TOKEN_KEY, userIdAndToken.getTwo())
                    .putString(USER_ID_KEY, userIdAndToken.getOne())
                    .apply();

            Credentials.init(context);
        } catch (GeneralSecurityException | IOException e) {
            Log.e("SecureStorage", "Error saving token and user ID", e);
        }
    }

    public static String getToken(Context context) {
        try {
            return getSharedPreferences(context).getString(TOKEN_KEY, null);
        } catch (GeneralSecurityException | IOException e) {
            Log.e("SecureStorage", "Error getting token", e);
            return null;
        }
    }

    public static String getUserId(Context context) {
        try {
            return getSharedPreferences(context).getString(USER_ID_KEY, null);
        } catch (GeneralSecurityException | IOException e) {
            Log.e("SecureStorage", "Error getting user ID", e);
            return null;
        }
    }

    public static void clearData(Context context) {
        try {
            SharedPreferences prefs = getSharedPreferences(context);
            prefs.edit().clear().apply();
        } catch (GeneralSecurityException | IOException e) {
            Log.e("SecureStorage", "Error clearing data", e);
        }
    }

    public static boolean isTokenValid(String token) {
        try {
            JWT jwt = new JWT(token);
            return !jwt.isExpired(10);
        } catch (Exception e) {
            Log.e("SecureStorage", "Error validating token", e);
            return false;
        }
    }
}

