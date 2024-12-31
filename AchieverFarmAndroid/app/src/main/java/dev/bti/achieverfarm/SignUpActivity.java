package dev.bti.achieverfarm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.androidsdk.models.req.UserReq;
import dev.bti.achieverfarm.util.SecureStorage;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpFullNameEdit, signUpEmailEdit, signUpPhoneNumberEdit, signUpPasswordEdit, signUpConPasswordEdit;
    TextView signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        init();
    }

    public void init() {
        initUi();
        initClicks();
    }

    public void initUi() {
        signUpFullNameEdit = findViewById(R.id.signUpFullNameEdit);
        signUpEmailEdit = findViewById(R.id.signUpEmailEdit);
        signUpPhoneNumberEdit = findViewById(R.id.signUpPhoneNumberEdit);
        signUpPasswordEdit = findViewById(R.id.signUpPasswordEdit);
        signUpConPasswordEdit = findViewById(R.id.signUpConPasswordEdit);
        signUpBtn = findViewById(R.id.signUpBtn);
    }

    public void initClicks() {
        signUpBtn.setOnClickListener(v -> {
            signUpBtn.setEnabled(false);
            if (validate()) {
                execute();
            }
        });
    }

    public boolean validate() {
        if (signUpFullNameEdit.getText().toString().trim().isEmpty()) {
            signUpFullNameEdit.setError("Full name is required");
            return false;
        } else if (signUpEmailEdit.getText().toString().trim().isEmpty()) {
            signUpEmailEdit.setError("Email is required");
            return false;
        } else if (signUpPhoneNumberEdit.getText().toString().trim().isEmpty()) {
            signUpPhoneNumberEdit.setError("Phone number is required");
            return false;
        } else if (signUpPasswordEdit.getText().toString().trim().isEmpty()) {
            signUpPasswordEdit.setError("Password is required");
            return false;
        } else if (signUpConPasswordEdit.getText().toString().trim().isEmpty()) {
            signUpConPasswordEdit.setError("Confirm password is required");
            return false;
        } else if (!signUpPasswordEdit.getText().toString().trim().equals(signUpConPasswordEdit.getText().toString())) {
            signUpConPasswordEdit.setError("Passwords do not match");
            return false;
        } else {
            return true;
        }
    }

    public void execute() {
        UserReq userReq = new UserReq();
        userReq.setFullName(signUpFullNameEdit.getText().toString().trim());
        userReq.setEmail(signUpEmailEdit.getText().toString().trim());
        userReq.setPhone(signUpPhoneNumberEdit.getText().toString().trim());
        userReq.setPassword(signUpPasswordEdit.getText().toString().trim());
        userReq.setVerified(false);
        userReq.setIsAdmin(true);

        SDKHelpers.GetInstance(null).getAuthHelper().createWithEmailAndPassword(userReq)
                .addOnSuccessListener(response -> {
                    SecureStorage.saveTokenAndUserId(SignUpActivity.this.getApplicationContext(), response.getData());
                    SignUpActivity.this.startActivity(new Intent(SignUpActivity.this.getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    SignUpActivity.this.finishAffinity();
                    SignUpActivity.this.finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    signUpBtn.setEnabled(true);
                }).execute();
    }
}