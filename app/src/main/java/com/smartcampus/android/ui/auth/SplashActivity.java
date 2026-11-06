package com.smartcampus.android.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.smartcampus.android.databinding.ActivitySplashBinding;
import com.smartcampus.android.ui.main.MainActivity;
import com.smartcampus.android.utils.SessionManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private static final int SPLASH_DELAY_MS = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SessionManager sessionManager = SessionManager.getInstance(SplashActivity.this);
            Intent intent;
            if (sessionManager.isLoggedIn()) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_DELAY_MS);
    }
}

