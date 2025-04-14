package com.joaquinalechao.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EmployerFormActivity extends AppCompatActivity {

    private EditText firstName, lastName, companyName, companyEmail, contactNumber;
    private Button submitButton, returnLoginButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userEmail, userPassword, googleIdToken, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_form);

        // Retrieve user info from Intent
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userPassword = getIntent().getStringExtra("USER_PASSWORD");
        googleIdToken = getIntent().getStringExtra("GOOGLE_ID_TOKEN");
        userType = getIntent().getStringExtra("USER_TYPE");

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);
        companyName = findViewById(R.id.etCompanyName);
        companyEmail = findViewById(R.id.etCompanyEmail);
        contactNumber = findViewById(R.id.etContactNumber);
        submitButton = findViewById(R.id.btnSubmit);
        returnLoginButton = findViewById(R.id.btnReturnLoginButton);

        // Submit button action
        submitButton.setOnClickListener(v -> finalizeSignup());

        // Return to login action
        returnLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void finalizeSignup() {
        String fName = firstName.getText().toString().trim();
        String lName = lastName.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String email = companyEmail.getText().toString().trim();
        String contact = contactNumber.getText().toString().trim();

        if (fName.isEmpty() || lName.isEmpty() || company.isEmpty() || email.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (googleIdToken != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                saveEmployerToFirestore(user.getUid(), fName, lName, company, email, contact);
                            }
                        } else {
                            Toast.makeText(this, "Google Signup Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                saveEmployerToFirestore(user.getUid(), fName, lName, company, email, contact);
                            }
                        } else {
                            Toast.makeText(this, "Email Signup Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveEmployerToFirestore(String userId, String firstName, String lastName,
                                         String company, String email, String contact) {
        Map<String, Object> employerData = new HashMap<>();
        employerData.put("userId", userId);
        employerData.put("email", email);
        employerData.put("userType", userType);
        employerData.put("firstName", firstName);
        employerData.put("lastName", lastName);
        employerData.put("companyName", company);
        employerData.put("contactNumber", contact);

        db.collection("users").document(userId)
                .set(employerData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Employer Registration Complete!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, EmployerDashboardActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error saving employer data.", Toast.LENGTH_SHORT).show());
    }
}
