package dev.bti.achieverfarm;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AuthChoiceActivity extends AppCompatActivity {

    TextView authChoiceLogin, authChoiceSignUp, termsOfUseAndPrivacyPolicy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_choice);

        init();
    }

    public void init() {
        initUi();
        initClicks();
        initSpans();
    }

    public void initUi() {
        authChoiceLogin = findViewById(R.id.authChoiceLogin);
        authChoiceSignUp = findViewById(R.id.authChoiceSignUp);
        termsOfUseAndPrivacyPolicy = findViewById(R.id.termsOfUseAndPrivacyPolicy);
    }

    public void initClicks() {
        authChoiceLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        authChoiceSignUp.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
    }

    public void initSpans() {
        String text = ContextCompat.getString(this, R.string.terms_of_use_and_privacy_policy);
        SpannableString spannableString = new SpannableString(text);

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#EE5C17")), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 13, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#EE5C17")), 16, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsOfUseAndPrivacyPolicy.setText(spannableString);
    }
}
