package rizelle.bautista.thrive;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText newPasswordEditText, confirmPasswordEditText;
    Button resetPasswordButton, backToLoginButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_forgot_password_new_password);

        mAuth = FirebaseAuth.getInstance();

        // Initialize the views
        newPasswordEditText = findViewById(R.id.newPassword);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        backToLoginButton = findViewById(R.id.backToLogin2);

        // Set onClickListener for Reset Password button
        resetPasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword)) {
                newPasswordEditText.setError("New password is required");
                return;
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                confirmPasswordEditText.setError("Please confirm your password");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Passwords do not match");
                return;
            }

            // Call Firebase or other backend service to reset the password
            mAuth.getCurrentUser().updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                            // Redirect to login page after successful password reset
                            finish();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Failed to reset password", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Set onClickListener for Back to Login button
        backToLoginButton.setOnClickListener(v -> finish()); // Simply finish the activity to go back
    }
}
