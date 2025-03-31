package com.joaquinalechao.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class IntroSplashLogo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Ensure this XML file exists in res/layout

        // Delay for 3 seconds before redirecting to LoginActivity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroSplashLogo.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close the splash screen so the user can't go back to it
            }
        }, 3000); // 3000ms = 3 seconds
    }
}
