package com.smartcampus.android.ui.dashboard;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.smartcampus.android.R;
import com.smartcampus.android.databinding.FragmentDashboardBinding;
import com.smartcampus.android.model.Announcement;
import com.smartcampus.android.model.AttendanceRecord;
import com.smartcampus.android.model.Event;
import com.smartcampus.android.model.Student;
import com.smartcampus.android.model.TimetableSlot;
import com.smartcampus.android.repository.CampusRepository;
import com.smartcampus.android.ui.announcements.AnnouncementsActivity;
import com.smartcampus.android.ui.main.MainActivity;
import com.smartcampus.android.utils.DateUtils;

import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private CampusRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = CampusRepository.getInstance();

        setupUI();
        setupClickListeners();
        loadAllDashboardData();
    }

    private void setupUI() {
        binding.tvGreeting.setText(DateUtils.getGreeting() + ",");
        binding.tvTodayDate.setText(DateUtils.getFormattedTodayDate());

        binding.swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.primary_navy),
                ContextCompat.getColor(requireContext(), R.color.secondary_light_blue)
        );
        binding.swipeRefresh.setOnRefreshListener(this::loadAllDashboardData);
    }

    private void setupClickListeners() {
        // Quick Action cards
        binding.cardQuickAttendance.setOnClickListener(v -> navigateToTab(R.id.navigation_attendance));
        binding.cardAttendanceSnapshot.setOnClickListener(v -> navigateToTab(R.id.navigation_attendance));

        binding.cardQuickTimetable.setOnClickListener(v -> navigateToTab(R.id.navigation_timetable));
        binding.tvViewAllTimetable.setOnClickListener(v -> navigateToTab(R.id.navigation_timetable));

        binding.cardQuickNotices.setOnClickListener(v -> openAnnouncements());
        binding.tvViewAllAnnouncements.setOnClickListener(v -> openAnnouncements());
        binding.cardLatestNotice.setOnClickListener(v -> openAnnouncements());

        binding.cardQuickEvents.setOnClickListener(v -> navigateToTab(R.id.navigation_events));
        binding.tvViewAllEvents.setOnClickListener(v -> navigateToTab(R.id.navigation_events));
        binding.cardFeaturedEvent.setOnClickListener(v -> navigateToTab(R.id.navigation_events));

        binding.btnViewProfile.setOnClickListener(v -> navigateToTab(R.id.navigation_profile));
        binding.btnNotifications.setOnClickListener(v -> openAnnouncements());
    }

    private void navigateToTab(int tabId) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToTab(tabId);
        }
    }

    private void openAnnouncements() {
        Intent intent = new Intent(requireContext(), AnnouncementsActivity.class);
        startActivity(intent);
    }

    public void loadAllDashboardData() {
        if (!isAdded()) return;

        // 1. Profile Data
        repository.getStudentProfile(requireContext(), new CampusRepository.RepositoryCallback<Student>() {
            @Override
            public void onLoaded(Student student) {
                if (!isAdded() || student == null) return;
                binding.tvStudentName.setText(student.getFullName());
                binding.tvProfileDepartment.setText(student.getDepartment());
                binding.tvProfileIdYear.setText("ID: " + student.getStudentId() + " • " + student.getAcademicYear());

                // Set avatar initials
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
            }
        });

        // 2. Attendance Overview
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
                binding.tvTotalAttendancePercent.setText(String.format(Locale.US, "%.1f%%", percentage));
                binding.tvConductedAndAttended.setText(totalAttended + " Attended / " + totalConducted + " Total");
                binding.pbOverallAttendance.setProgress((int) Math.round(percentage));

                if (percentage < 75.0) {
                    binding.tvAttendanceStatusBadge.setText(R.string.low_attendance_alert);
                    binding.tvAttendanceStatusBadge.setBackgroundResource(R.drawable.bg_badge_red);
                    binding.tvAttendanceStatusBadge.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_red_danger));
                    binding.pbOverallAttendance.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.status_red_danger)));
                } else {
                    binding.tvAttendanceStatusBadge.setText(R.string.good_standing);
                    binding.tvAttendanceStatusBadge.setBackgroundResource(R.drawable.bg_badge_green);
                    binding.tvAttendanceStatusBadge.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_green_success));
                    binding.pbOverallAttendance.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.status_green_success)));
                }
            }

            @Override
            public void onError(String error) {
            }
        });

        // 3. Today's Timetable
        String currentDay = DateUtils.getCurrentDayOfWeek();
        repository.getTimetable(requireContext(), currentDay, new CampusRepository.RepositoryCallback<List<TimetableSlot>>() {
            @Override
            public void onLoaded(List<TimetableSlot> slots) {
                if (!isAdded()) return;

                if (slots != null && !slots.isEmpty()) {
                    binding.tvTodaySlot1.setVisibility(View.VISIBLE);
                    binding.tvTodaySlot1.setText(slots.get(0).getFormattedTiming() + " • " + slots.get(0).getSubjectName() + " (" + slots.get(0).getClassroom() + ")");

                    if (slots.size() > 1) {
                        binding.tvTodaySlot2.setVisibility(View.VISIBLE);
                        binding.tvTodaySlot2.setText(slots.get(1).getFormattedTiming() + " • " + slots.get(1).getSubjectName() + " (" + slots.get(1).getClassroom() + ")");
                    } else {
                        binding.tvTodaySlot2.setVisibility(View.GONE);
                    }

                    if (slots.size() > 2) {
                        binding.tvTodaySlot3.setVisibility(View.VISIBLE);
                        binding.tvTodaySlot3.setText(slots.get(2).getFormattedTiming() + " • " + slots.get(2).getSubjectName() + " (" + slots.get(2).getClassroom() + ")");
                    } else {
                        binding.tvTodaySlot3.setVisibility(View.GONE);
                    }
                } else {
                    binding.tvTodaySlot1.setVisibility(View.VISIBLE);
                    binding.tvTodaySlot1.setText("No classes scheduled for today. Take time to study!");
                    binding.tvTodaySlot2.setVisibility(View.GONE);
                    binding.tvTodaySlot3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {
            }
        });

        // 4. Announcements
        repository.getAnnouncements(requireContext(), new CampusRepository.RepositoryCallback<List<Announcement>>() {
            @Override
            public void onLoaded(List<Announcement> announcements) {
                if (!isAdded() || announcements == null || announcements.isEmpty()) return;

                Announcement topNotice = announcements.get(0);
                binding.tvNoticeTitle.setText(topNotice.getTitle());
                binding.tvNoticeSnippet.setText(topNotice.getDescription());
                binding.tvNoticeCategory.setText(topNotice.getCategory());
                binding.tvNoticeDate.setText(topNotice.getDate());
                binding.tvNoticeImportant.setVisibility(topNotice.isImportant() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(String error) {
            }
        });

        // 5. Featured Event
        repository.getEvents(requireContext(), new CampusRepository.RepositoryCallback<List<Event>>() {
            @Override
            public void onLoaded(List<Event> events) {
                if (!isAdded() || events == null || events.isEmpty()) return;

                Event event = events.get(0);
                binding.tvEventTitle.setText(event.getTitle());
                binding.tvEventVenue.setText(event.getVenue() + " • " + event.getTime());
                binding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onError(String error) {
                if (isAdded()) {
                    binding.swipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
