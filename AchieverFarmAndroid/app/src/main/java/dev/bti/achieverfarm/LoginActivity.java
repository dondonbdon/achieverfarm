package dev.bti.achieverfarm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.util.SecureStorage;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmailEdit, loginPasswordEdit;
    TextView loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
        initClicks();
    }

    public void initUi() {
        loginEmailEdit = findViewById(R.id.loginEmailEdit);
        loginPasswordEdit = findViewById(R.id.loginPasswordEdit);
        loginBtn = findViewById(R.id.loginBtn);
    }

    public void initClicks() {
        loginBtn.setOnClickListener(v -> {
            loginBtn.setEnabled(false);

            if (validate()) {
                execute();
            }
        });
    }

    public boolean validate() {
        if (loginEmailEdit.getText().toString().trim().isEmpty()) {
            loginEmailEdit.setError("Email is required");
            return false;
        } else if (loginPasswordEdit.getText().toString().trim().isEmpty()) {
            loginPasswordEdit.setError("Password is required");
            return false;
        } else {
            return true;
        }
    }

    public void execute() {
        String email = loginEmailEdit.getText().toString().trim();
        String password = loginPasswordEdit.getText().toString().trim();

        SDKHelpers.GetInstance(null).getAuthHelper().loginWithEmailAndPassword(email, password)
                .addOnSuccessListener(response -> {
                    SecureStorage.saveTokenAndUserId(getApplicationContext(), response.getData());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finishAffinity();
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    loginBtn.setEnabled(true);
                }).execute();
    }

}