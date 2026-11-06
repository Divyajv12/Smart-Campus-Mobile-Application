package com.smartcampus.android.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartcampus.android.databinding.ActivityEditProfileBinding;
import com.smartcampus.android.model.Student;
import com.smartcampus.android.repository.CampusRepository;
import com.smartcampus.android.utils.SessionManager;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private CampusRepository repository;
    private SessionManager sessionManager;
    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = CampusRepository.getInstance();
        sessionManager = SessionManager.getInstance(this);

        setupToolbar();
        populateCurrentData();
        setupListeners();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void populateCurrentData() {
        currentStudent = sessionManager.getCurrentStudent();
        if (currentStudent != null) {
            binding.etFullName.setText(currentStudent.getFullName());
            binding.etDepartment.setText(currentStudent.getDepartment());
            binding.etAcademicYear.setText(currentStudent.getAcademicYear());
            binding.etPhone.setText(currentStudent.getPhoneNumber());
            binding.etBio.setText(currentStudent.getBio());
        }
    }

    private void setupListeners() {
        binding.btnSaveProfile.setOnClickListener(v -> saveProfileChanges());
    }

    private void saveProfileChanges() {
        String fullName = binding.etFullName.getText() != null ? binding.etFullName.getText().toString().trim() : "";
        String department = binding.etDepartment.getText() != null ? binding.etDepartment.getText().toString().trim() : "";
        String academicYear = binding.etAcademicYear.getText() != null ? binding.etAcademicYear.getText().toString().trim() : "";
        String phone = binding.etPhone.getText() != null ? binding.etPhone.getText().toString().trim() : "";
        String bio = binding.etBio.getText() != null ? binding.etBio.getText().toString().trim() : "";

        binding.tilFullName.setError(null);
        binding.tilDepartment.setError(null);
        binding.tilAcademicYear.setError(null);

        if (TextUtils.isEmpty(fullName)) {
            binding.tilFullName.setError("Name is required");
            binding.etFullName.requestFocus();
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

        if (currentStudent == null) {
            currentStudent = new Student();
            currentStudent.setUid(sessionManager.getCurrentUid());
        }

        currentStudent.setFullName(fullName);
        currentStudent.setDepartment(department);
        currentStudent.setAcademicYear(academicYear);
        currentStudent.setPhoneNumber(phone);
        currentStudent.setBio(bio);

        setLoading(true);

        repository.updateStudentProfile(this, currentStudent, (success, message) -> {
            setLoading(false);
            Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            if (success) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnSaveProfile.setEnabled(!isLoading);
    }
}

