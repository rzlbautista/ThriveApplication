package com.joaquinalechao.loginsignup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JobSeekerFormActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userEmail, userPassword, userType;

    private EditText firstName, lastName, age, dob, contactNumber;
    private Button submitButton, loginButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_form);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userPassword = getIntent().getStringExtra("USER_PASSWORD");
        userType = getIntent().getStringExtra("USER_TYPE");

        firstName = findViewById(R.id.jobseeker_firstname);
        lastName = findViewById(R.id.jobseeker_lastname);
        age = findViewById(R.id.age);
        dob = findViewById(R.id.dob);
        contactNumber = findViewById(R.id.contact_number);
        submitButton = findViewById(R.id.btnSubmitButton);
        loginButton = findViewById(R.id.btnLoginButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering user...");
        progressDialog.setCancelable(false);

        submitButton.setOnClickListener(view -> validateAndSubmit());

        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(JobSeekerFormActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void validateAndSubmit() {
        String fName = firstName.getText().toString().trim();
        String lName = lastName.getText().toString().trim();
        String userAge = age.getText().toString().trim();
        String birthDate = dob.getText().toString().trim();
        String contact = contactNumber.getText().toString().trim();

        if (fName.isEmpty() || lName.isEmpty() || userAge.isEmpty() || birthDate.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            return;
        }

        submitButton.setEnabled(false);
        progressDialog.show();

        createUserInFirebase(fName, lName, userAge, birthDate, contact);
    }

    private void createUserInFirebase(String fName, String lName, String userAge, String birthDate, String contact) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveUserToFirestore(user.getUid(), fName, lName, userAge, birthDate, contact);
                        } else {
                            showError("Authentication failed. Please try again.");
                        }
                    } else {
                        showError("Signup Failed: " + task.getException().getMessage());
                    }
                });
    }

    private void saveUserToFirestore(String userId, String fName, String lName, String userAge, String birthDate, String contact) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", userEmail);
        userData.put("userType", userType);
        userData.put("firstName", fName);
        userData.put("lastName", lName);
        userData.put("age", userAge);
        userData.put("dateOfBirth", birthDate);
        userData.put("contactNumber", contact);

        db.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Signup Complete!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(JobSeekerFormActivity.this, JobSeekerDashboardActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showError("Database Error: " + e.getMessage()));
    }

    private void showError(String message) {
        progressDialog.dismiss();
        submitButton.setEnabled(true);
        Log.e("JobSeekerForm", message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
