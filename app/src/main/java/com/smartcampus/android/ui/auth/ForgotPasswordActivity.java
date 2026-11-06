package com.smartcampus.android.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartcampus.android.databinding.ActivityForgotPasswordBinding;
import com.smartcampus.android.repository.FirebaseManager;
import com.smartcampus.android.utils.ValidationUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseManager = FirebaseManager.getInstance();

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.btnSendReset.setOnClickListener(v -> attemptReset());
    }

    private void attemptReset() {
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        binding.tilEmail.setError(null);

        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError("Email is required");
            binding.etEmail.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            binding.tilEmail.setError("Please enter a valid email address");
            binding.etEmail.requestFocus();
            return;
        }

        setLoading(true);

        firebaseManager.sendPasswordReset(email, new FirebaseManager.ActionCallback() {
            @Override
            public void onSuccess() {
                setLoading(false);
                Toast.makeText(ForgotPasswordActivity.this, "Password reset instructions sent to " + email, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                // In case Firebase project is in test or email not found
                Toast.makeText(ForgotPasswordActivity.this, "If this email is registered, a reset link has been dispatched (" + errorMessage + ")", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnSendReset.setEnabled(!isLoading);
    }
}

