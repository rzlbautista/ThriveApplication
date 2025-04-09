package rizelle.bautista.thrive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class JobSeekerSigninActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton, googleSignInButton, createAccountButton; // Updated to include createAccountButton
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobseeker_signin);

        mAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.PWDusername);
        passwordEditText = findViewById(R.id.Password);
        loginButton = findViewById(R.id.Loginbutton);
        googleSignInButton = findViewById(R.id.SignInGoogleButton); // Google Sign-In Button
        createAccountButton = findViewById(R.id.CreateAccountButton); // Create Account Button

        TextView forgotPasswordTextView = findViewById(R.id.forgotPassword); // Forgot Password TextView

        // Set onClickListener for Forgot Password link
        forgotPasswordTextView.setOnClickListener(view -> {
            String email = usernameEditText.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                usernameEditText.setError("Enter your email first");
                return;
            }

            // Redirect to Employer Forgot Password Activity first
            Intent intent = new Intent(JobSeekerSigninActivity.this, ResetPasswordActivity.class);
            startActivity(intent);  // Navigate to the Employer Forgot Password activity
        });


        // Set onClickListener for Google Sign-In button
        googleSignInButton.setOnClickListener(view -> {
            // Implement Google Sign-In logic here (Optional)
            Toast.makeText(JobSeekerSigninActivity.this, "Google Sign-In functionality here!", Toast.LENGTH_SHORT).show();
            // You can redirect to Google Sign-In flow if needed
        });

        // Set onClickListener for Create Account button
        createAccountButton.setOnClickListener(view -> {
            Intent intent = new Intent(JobSeekerSigninActivity.this, JobSeekerSignupActivity.class);
            startActivity(intent); // Navigate to the Sign-Up activity
        });

        // Login Button Click Listener
        loginButton.setOnClickListener(view -> {
            String email = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                usernameEditText.setError("Email is required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Password is required");
                return;
            }

            // Sign in with Firebase Authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(JobSeekerSigninActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, navigate to job seeker dashboard
                            Toast.makeText(JobSeekerSigninActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(JobSeekerSigninActivity.this, JobSeekerDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user
                            Toast.makeText(JobSeekerSigninActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
