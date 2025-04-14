package com.joaquinalechao.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserType extends AppCompatActivity {
    private String userEmail, userPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        // Retrieve user details from intent (not Firebase yet)
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userPassword = getIntent().getStringExtra("USER_PASSWORD");

        // Ensure userEmail is not null before proceeding
        if (userEmail == null || userPassword == null) {
            Log.e("UserType", "Missing user credentials. Redirecting to signup.");
            Toast.makeText(this, "Session expired. Please sign up again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
            return;
        }

        Button btnEmployer = findViewById(R.id.btnEmployer);
        Button btnJobSeeker = findViewById(R.id.btnJobSeeker);

        btnEmployer.setOnClickListener(v -> proceedToNextStep("Employer"));
        btnJobSeeker.setOnClickListener(v -> proceedToNextStep("Job Seeker"));
    }

    private void proceedToNextStep(String userType) {
        Intent intent;

        if (userType.equals("Employer")) {
            intent = new Intent(UserType.this, EmployerFormActivity.class);
        } else {
            intent = new Intent(UserType.this, JobSeekerFormActivity.class);
        }

        // Pass user information to the next activity (without authenticating yet)
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("USER_PASSWORD", userPassword);
        intent.putExtra("USER_TYPE", userType);

        startActivity(intent);
        finish(); // Prevents going back to this screen
    }
}
