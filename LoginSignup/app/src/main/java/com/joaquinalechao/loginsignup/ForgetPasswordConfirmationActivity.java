package com.joaquinalechao.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ForgetPasswordConfirmationActivity extends AppCompatActivity {

    private Button backToLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_confirmation);

        // Initialize button
        backToLoginButton = findViewById(R.id.backToLoginButton);

        // Navigate to login screen
        backToLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(ForgetPasswordConfirmationActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}

