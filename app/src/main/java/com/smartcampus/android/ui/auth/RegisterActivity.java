package com.smartcampus.android.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartcampus.android.databinding.ActivityRegisterBinding;
import com.smartcampus.android.model.Student;
import com.smartcampus.android.repository.FirebaseManager;
import com.smartcampus.android.ui.main.MainActivity;
import com.smartcampus.android.utils.SessionManager;
import com.smartcampus.android.utils.ValidationUtils;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseManager firebaseManager;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseManager = FirebaseManager.getInstance();
        sessionManager = SessionManager.getInstance(this);

        initListeners();
    }

    private void initListeners() {
        binding.btnRegister.setOnClickListener(v -> attemptRegister());

        binding.tvLogin.setOnClickListener(v -> finish());
    }

    private void attemptRegister() {
        String fullName = binding.etFullName.getText() != null ? binding.etFullName.getText().toString().trim() : "";
        String studentId = binding.etStudentId.getText() != null ? binding.etStudentId.getText().toString().trim() : "";
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String department = binding.etDepartment.getText() != null ? binding.etDepartment.getText().toString().trim() : "";
        String academicYear = binding.etAcademicYear.getText() != null ? binding.etAcademicYear.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";
        String confirmPassword = binding.etConfirmPassword.getText() != null ? binding.etConfirmPassword.getText().toString().trim() : "";

        // Reset errors
        binding.tilFullName.setError(null);
        binding.tilStudentId.setError(null);
        binding.tilEmail.setError(null);
        binding.tilDepartment.setError(null);
        binding.tilAcademicYear.setError(null);
        binding.tilPassword.setError(null);
        binding.tilConfirmPassword.setError(null);

        // Validation checks
        if (TextUtils.isEmpty(fullName)) {
            binding.tilFullName.setError("Full name is required");
            binding.etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(studentId)) {
            binding.tilStudentId.setError("Student ID is required");
            binding.etStudentId.requestFocus();
            return;
        }

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

        if (TextUtils.isEmpty(department)) {
            binding.tilDepartment.setError("Department is required");
            binding.etDepartment.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(academicYear)) {
            binding.tilAcademicYear.setError("Academic year is required");
            binding.etAcademicYear.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Password cannot be empty");
            binding.etPassword.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            binding.tilPassword.setError("Password must be at least 6 characters");
            binding.etPassword.requestFocus();
            return;
        }

        if (!ValidationUtils.arePasswordsMatching(password, confirmPassword)) {
            binding.tilConfirmPassword.setError("Passwords do not match");
            binding.etConfirmPassword.requestFocus();
            return;
        }

        setLoading(true);

        firebaseManager.register(email, password, new FirebaseManager.ActionCallback() {
            @Override
            public void onSuccess() {
                String uid = firebaseManager.getCurrentUser() != null ? firebaseManager.getCurrentUser().getUid() : "uid_" + System.currentTimeMillis();
                Student newStudent = new Student(uid, fullName, email, studentId, department, academicYear);

                // Save profile to Firestore
                firebaseManager.saveStudentProfile(newStudent, new FirebaseManager.ActionCallback() {
                    @Override
                    public void onSuccess() {
                        // Seed initial demo academic data for the new account
                        firebaseManager.seedInitialDataForStudent(uid, new FirebaseManager.ActionCallback() {
                            @Override
                            public void onSuccess() {
                                completeRegistration(newStudent);
                            }

                            @Override
                            public void onError(String errorMessage) {
                                completeRegistration(newStudent);
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        completeRegistration(newStudent);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                Toast.makeText(RegisterActivity.this, "Registration Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void completeRegistration(Student student) {
        setLoading(false);
        sessionManager.saveSession(student, false);
        Toast.makeText(RegisterActivity.this, "Account created successfully! Welcome to Smart Campus.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnRegister.setEnabled(!isLoading);
    }
}

