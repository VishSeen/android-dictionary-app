package com.vish.apps.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPrefs = getSharedPreferences("com.vish.apps.dictionary", MODE_PRIVATE);

        startActivity(new Intent(SplashActivity.this, WalkthroughActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if app has run before
        if (sharedPrefs.getBoolean("appHasRun", true)) {
            // show bottom sheet to choose language
            Intent intent = new Intent(SplashActivity.this, WalkthroughActivity.class);
            startActivity(intent);

            // update value in sharedPrefs
            sharedPrefs.edit().putBoolean("appHasRun", false).apply();
        } else {
            new CountDownTimer(2000, 1000) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    Intent intent = new Intent(SplashActivity.this, WalkthroughActivity.class);
                    startActivity(intent);
                }
            }.start();
        }
    }
}