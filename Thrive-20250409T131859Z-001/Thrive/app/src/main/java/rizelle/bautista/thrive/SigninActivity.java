package rizelle.bautista.thrive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SigninActivity extends AppCompatActivity {

    CardView cardJobSeeker, cardEmployer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        cardJobSeeker = findViewById(R.id.cardJobSeeker);
        cardEmployer = findViewById(R.id.cardEmployer);

        cardJobSeeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, JobSeekerSigninActivity.class);
                startActivity(intent);
            }
        });

        cardEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, EmployerSigninActivity.class);
                startActivity(intent);
            }
        });
    }
}
