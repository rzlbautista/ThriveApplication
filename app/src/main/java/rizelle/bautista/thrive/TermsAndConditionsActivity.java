package rizelle.bautista.thrive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TermsAndConditionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        Button agreeButton = findViewById(R.id.agree_button);
        agreeButton.setOnClickListener(v -> {
            Intent intent = new Intent(TermsAndConditionsActivity.this, JobSeekerSignupActivity.class);
            intent.putExtra("terms_agreed", true);
            startActivity(intent);
            finish();
        });
    }
}
