package com.smartcampus.android.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartcampus.android.databinding.ActivityLoginBinding;
import com.smartcampus.android.model.Student;
import com.smartcampus.android.repository.CampusRepository;
import com.smartcampus.android.repository.FirebaseManager;
import com.smartcampus.android.repository.MockDataProvider;
import com.smartcampus.android.ui.main.MainActivity;
import com.smartcampus.android.utils.SessionManager;
import com.smartcampus.android.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseManager firebaseManager;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseManager = FirebaseManager.getInstance();
        sessionManager = SessionManager.getInstance(this);

        initListeners();
    }

    private void initListeners() {
        binding.btnLogin.setOnClickListener(v -> attemptLogin());

        binding.btnDemoLogin.setOnClickListener(v -> loginWithDemoAccount());

        binding.tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        binding.tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        // Reset errors
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);

        // Validation
        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError("Email is required");
            binding.etEmail.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            binding.tilEmail.setError("Please enter a valid campus email");
            binding.etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Password is required");
            binding.etPassword.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            binding.tilPassword.setError("Password must be at least 6 characters");
            binding.etPassword.requestFocus();
            return;
        }

        setLoading(true);

        firebaseManager.login(email, password, new FirebaseManager.ActionCallback() {
            @Override
            public void onSuccess() {
                String uid = firebaseManager.getCurrentUser() != null ? firebaseManager.getCurrentUser().getUid() : "";
                
                // Fetch student profile from Firestore
                firebaseManager.getStudentProfile(uid, new FirebaseManager.DataCallback<Student>() {
                    @Override
                    public void onSuccess(Student student) {
                        setLoading(false);
                        sessionManager.saveSession(student, false);
                        Toast.makeText(LoginActivity.this, "Welcome " + student.getFullName() + "!", Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Fallback profile if Firestore is freshly configured
                        setLoading(false);
                        Student fallback = new Student(uid, "Student", email, "STU-001", "Engineering", "Current Year");
                        sessionManager.saveSession(fallback, false);
                        navigateToMain();
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, "Login Failed: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginWithDemoAccount() {
        Student demoStudent = MockDataProvider.getDemoStudent();
        sessionManager.saveSession(demoStudent, true);
        Toast.makeText(this, "Logged in with Demo Account (" + demoStudent.getFullName() + ")", Toast.LENGTH_SHORT).show();
        navigateToMain();
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!isLoading);
        binding.btnDemoLogin.setEnabled(!isLoading);
    }
}

