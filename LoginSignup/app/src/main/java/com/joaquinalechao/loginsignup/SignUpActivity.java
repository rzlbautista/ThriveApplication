package com.joaquinalechao.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private EditText signupEmail, signupPassword;
    private Button signupButton, googleSignUpButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        googleSignUpButton = findViewById(R.id.btnGoogleSignUp);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Email & Password Sign Up
        signupButton.setOnClickListener(view -> {
            String user = signupEmail.getText().toString().trim();
            String pass = signupPassword.getText().toString().trim();

            if (user.isEmpty()) {
                signupEmail.setError("Email cannot be empty");
                return;
            }
            if (pass.isEmpty()) {
                signupPassword.setError("Password cannot be empty");
                return;
            }

            auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                } else {
                    String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(SignUpActivity.this, "SignUp Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Google Sign-Up Button Click
        googleSignUpButton.setOnClickListener(view -> signInWithGoogle());

        loginRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    // Handle Google Sign-In Result
    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        if (account != null) {
                            firebaseAuthWithGoogle(account.getIdToken());
                        }
                    } catch (ApiException e) {
                        Toast.makeText(SignUpActivity.this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
                        Log.e("GoogleSignUp", "Sign-In Failed", e);
                    }
                }
            });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(SignUpActivity.this, "Welcome, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Google Sign-Up Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
