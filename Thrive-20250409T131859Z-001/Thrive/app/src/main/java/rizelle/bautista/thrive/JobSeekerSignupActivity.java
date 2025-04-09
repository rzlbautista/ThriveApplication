package rizelle.bautista.thrive;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class JobSeekerSignupActivity extends AppCompatActivity {

    EditText firstnameEditText,  lastnameEditText,  emailEditText, usernameEditText, passwordEditText;
    Button signupButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobseeker_signup);  // Make sure this is correct



    mAuth = FirebaseAuth.getInstance();

        firstnameEditText = findViewById(R.id.jobseeker_firstname);
        lastnameEditText = findViewById(R.id.jobseeker_lastname);
        emailEditText = findViewById(R.id.jobseeker_email);
        passwordEditText = findViewById(R.id.jobseeker_password);
        signupButton = findViewById(R.id.signUpButton);

        // Sign-up Button Click Listener
        signupButton.setOnClickListener(view -> {
            String firstname = firstnameEditText.getText().toString().trim();
            String lastname = lastnameEditText.getText().toString().trim();
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


            // Create user with Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign-up success, navigate to job seeker dashboard
                            Toast.makeText(JobSeekerSignupActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                            finish();  // Close the signup activity
                        } else {
                            // If sign-up fails, display a message to the user
                            Toast.makeText(JobSeekerSignupActivity.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
