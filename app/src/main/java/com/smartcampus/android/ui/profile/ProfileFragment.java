package com.smartcampus.android.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.smartcampus.android.R;
import com.smartcampus.android.databinding.FragmentProfileBinding;
import com.smartcampus.android.model.AttendanceRecord;
import com.smartcampus.android.model.Student;
import com.smartcampus.android.repository.CampusRepository;
import com.smartcampus.android.repository.FirebaseManager;
import com.smartcampus.android.ui.auth.LoginActivity;
import com.smartcampus.android.utils.SessionManager;

import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private CampusRepository repository;
    private SessionManager sessionManager;

    private final ActivityResultLauncher<Intent> editProfileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    loadProfileData();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = CampusRepository.getInstance();
        sessionManager = SessionManager.getInstance(requireContext());

        setupSwipeRefresh();
        setupClickListeners();
        loadProfileData();
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.primary_navy),
                ContextCompat.getColor(requireContext(), R.color.secondary_light_blue)
        );
        binding.swipeRefresh.setOnRefreshListener(this::loadProfileData);
    }

    private void setupClickListeners() {
        binding.btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            editProfileLauncher.launch(intent);
        });

        binding.btnAboutApp.setOnClickListener(v -> showAboutAppDialog());

        binding.btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    public void loadProfileData() {
        if (!isAdded()) return;

        repository.getStudentProfile(requireContext(), new CampusRepository.RepositoryCallback<Student>() {
            @Override
            public void onLoaded(Student student) {
                if (!isAdded() || student == null) return;
                binding.swipeRefresh.setRefreshing(false);

                binding.tvProfileName.setText(student.getFullName());
                binding.tvProfileRollNumber.setText("Student ID: " + student.getStudentId());
                binding.tvEmail.setText(student.getEmail());
                binding.tvDepartment.setText(student.getDepartment());
                binding.tvAcademicYear.setText(student.getAcademicYear());
                binding.tvPhone.setText(!student.getPhoneNumber().isEmpty() ? student.getPhoneNumber() : "Not provided");
                binding.tvBio.setText(!student.getBio().isEmpty() ? student.getBio() : "No bio available.");

                // Initials
                String initials = "ST";
                String[] parts = student.getFullName().trim().split("\\s+");
                if (parts.length >= 2 && !parts[0].isEmpty() && !parts[1].isEmpty()) {
                    initials = ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase(Locale.US);
                } else if (parts.length == 1 && !parts[0].isEmpty()) {
                    initials = parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase(Locale.US);
                }
                binding.tvAvatarInitials.setText(initials);
            }

            @Override
            public void onError(String error) {
                if (isAdded()) {
                    binding.swipeRefresh.setRefreshing(false);
                }
            }
        });

        // Load aggregate attendance
        repository.getAttendanceRecords(requireContext(), new CampusRepository.RepositoryCallback<List<AttendanceRecord>>() {
            @Override
            public void onLoaded(List<AttendanceRecord> records) {
                if (!isAdded() || records == null || records.isEmpty()) return;

                int totalConducted = 0;
                int totalAttended = 0;
                for (AttendanceRecord r : records) {
                    totalConducted += r.getTotalClasses();
                    totalAttended += r.getAttendedClasses();
                }

                double percentage = totalConducted > 0 ? ((double) totalAttended / totalConducted * 100.0) : 0.0;
                binding.tvProfileAttendancePercent.setText(String.format(Locale.US, "%.1f%%", percentage));
            }

            @Override
            public void onError(String error) {
            }
        });
    }

    private void showAboutAppDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.about_app)
                .setMessage("Smart Campus Mobile Application\nVersion 1.0.0\n\nBuilt natively with Java & XML for Android. Incorporates Firebase Authentication, Cloud Firestore, Android Navigation Components, and Material Design 3.\n\nDesigned for modern college students to seamlessly monitor attendance, timetable, campus circulars, and events.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showLogoutConfirmationDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.logout_confirmation_title)
                .setMessage(R.string.logout_confirmation_msg)
                .setPositiveButton(R.string.logout, (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void performLogout() {
        FirebaseManager.getInstance().logout();
        sessionManager.clearSession();

        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

