package rizelle.bautista.thrive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;  // Import TextView
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class EmployerSigninActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_signin);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.button4);

        // Declare the forgotPasswordButton as TextView
        TextView forgotPasswordButton = findViewById(R.id.ForgotPassword);

        // Set onClickListener for Forgot Password link
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Enter your email first");
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EmployerSigninActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EmployerSigninActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Set onClickListener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password is required");
                    return;
                }

                // Sign in with Firebase Authentication
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(EmployerSigninActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, navigate to dashboard
                                Toast.makeText(EmployerSigninActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EmployerSigninActivity.this, EmployerDashboardActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user
                                Toast.makeText(EmployerSigninActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
