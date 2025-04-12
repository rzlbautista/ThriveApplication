package rizelle.bautista.thrive;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.app.Dialog;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class JobSeekerSignupActivity extends AppCompatActivity {

    EditText firstnameEditText,  lastnameEditText,  emailEditText, usernameEditText, passwordEditText;
    Button signupButton;
    FirebaseAuth mAuth;



    CheckBox termsCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobseeker_signup);
        if (getIntent().getBooleanExtra("terms_agreed", false)) {
            termsCheckbox.setChecked(true);
        }


        mAuth = FirebaseAuth.getInstance();

        firstnameEditText = findViewById(R.id.jobseeker_firstname);
        lastnameEditText = findViewById(R.id.jobseeker_lastname);
        emailEditText = findViewById(R.id.jobseeker_email);
        passwordEditText = findViewById(R.id.jobseeker_password);
        usernameEditText = findViewById(R.id.jobseeker_email); // Make sure this is correct
        signupButton = findViewById(R.id.signUpButton);
        termsCheckbox = findViewById(R.id.checkBox);

        signupButton.setOnClickListener(view -> {
            if (!termsCheckbox.isChecked()) {
                showWarningDialog();  // Show warning before proceeding
                return;
            }

            // Your usual signup logic here...
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

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void showWarningDialog() {
        Dialog dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.activity_warning_message_create_account);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        Button continueBtn = dialog.findViewById(R.id.btn_continue);
        continueBtn.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(JobSeekerSignupActivity.this, TermsAndConditionsActivity.class);
            startActivity(intent);
        });

        dialog.show();
    }


}
