package com.joaquinalechao.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignUpActivity extends AppCompatActivity {
    private EditText signupEmail, signupPassword;
    private Button signupButton, googleSignUpButton;
    private TextView loginText;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        googleSignUpButton = findViewById(R.id.btnGoogleSignUp);
        loginText = findViewById(R.id.loginRedirectText);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signupButton.setOnClickListener(view -> {
            String userEmail = signupEmail.getText().toString().trim();
            String userPassword = signupPassword.getText().toString().trim();

            if (userEmail.isEmpty()) {
                signupEmail.setError("Email cannot be empty");
                return;
            }
            if (userPassword.isEmpty()) {
                signupPassword.setError("Password cannot be empty");
                return;
            }

            // Send data to UserType activity
            Intent intent = new Intent(SignUpActivity.this, UserType.class);
            intent.putExtra("USER_EMAIL", userEmail);
            intent.putExtra("USER_PASSWORD", userPassword);
            startActivity(intent);
            finish();
        });

        googleSignUpButton.setOnClickListener(view -> signInWithGoogle());

        loginText.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        if (account != null && account.getIdToken() != null) {
                            // Pass Google account data to UserType selection
                            Intent intent = new Intent(SignUpActivity.this, UserType.class);
                            intent.putExtra("USER_EMAIL", account.getEmail());
                            intent.putExtra("GOOGLE_ID_TOKEN", account.getIdToken()); // Pass Google Token
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("GoogleSignUp", "Google ID Token is null");
                            Toast.makeText(SignUpActivity.this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ApiException e) {
                        Toast.makeText(SignUpActivity.this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
                        Log.e("GoogleSignUp", "Sign-In Failed", e);
                    }
                }
            });
}
