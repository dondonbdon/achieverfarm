package dev.bti.achieverfarm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.splashscreen.SplashScreen;

import dev.bti.achieverfarm.common.Credentials;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Credentials.GetInstance().isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, AuthChoiceActivity.class));
        }

        finishAffinity();
        finish();
    }
}