package com.smartcampus.android.ui.attendance;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.smartcampus.android.R;
import com.smartcampus.android.adapter.AttendanceAdapter;
import com.smartcampus.android.databinding.FragmentAttendanceBinding;
import com.smartcampus.android.model.AttendanceRecord;
import com.smartcampus.android.repository.CampusRepository;

import java.util.List;
import java.util.Locale;

public class AttendanceFragment extends Fragment {

    private FragmentAttendanceBinding binding;
    private AttendanceAdapter adapter;
    private CampusRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = CampusRepository.getInstance();

        setupRecyclerView();
        setupSwipeRefresh();
        loadAttendanceData();
    }

    private void setupRecyclerView() {
        adapter = new AttendanceAdapter();
        binding.rvAttendance.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAttendance.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.primary_navy),
                ContextCompat.getColor(requireContext(), R.color.secondary_light_blue)
        );
        binding.swipeRefresh.setOnRefreshListener(this::loadAttendanceData);
    }

    public void loadAttendanceData() {
        if (!isAdded()) return;

        repository.getAttendanceRecords(requireContext(), new CampusRepository.RepositoryCallback<List<AttendanceRecord>>() {
            @Override
            public void onLoaded(List<AttendanceRecord> records) {
                if (!isAdded()) return;
                binding.swipeRefresh.setRefreshing(false);

                if (records != null && !records.isEmpty()) {
                    binding.layoutEmptyAttendance.setVisibility(View.GONE);
                    binding.rvAttendance.setVisibility(View.VISIBLE);
                    adapter.setRecords(records);

                    // Compute aggregate analytics
                    int totalConducted = 0;
                    int totalAttended = 0;
                    int lowAttendanceCount = 0;

                    for (AttendanceRecord r : records) {
                        totalConducted += r.getTotalClasses();
                        totalAttended += r.getAttendedClasses();
                        if (r.isLowAttendance()) {
                            lowAttendanceCount++;
                        }
                    }

                    int totalMissed = Math.max(0, totalConducted - totalAttended);
                    double aggregatePercentage = totalConducted > 0 ? ((double) totalAttended / totalConducted * 100.0) : 0.0;

                    binding.tvAggregatePercent.setText(String.format(Locale.US, "%.1f%%", aggregatePercentage));
                    binding.tvConductedCount.setText("Total Conducted: " + totalConducted);
                    binding.tvAttendedCount.setText("Total Attended: " + totalAttended);
                    binding.tvMissedCount.setText("Total Missed: " + totalMissed);

                    int progress = (int) Math.round(aggregatePercentage);
                    binding.pbAggregateAttendance.setProgress(progress);

                    if (aggregatePercentage < 75.0 || lowAttendanceCount > 0) {
                        binding.pbAggregateAttendance.setProgressTintList(
                                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.status_red_danger))
                        );
                        binding.bannerAttendanceNotice.setBackgroundResource(R.drawable.bg_badge_red);
                        binding.ivNoticeIcon.setImageResource(R.drawable.ic_warning);
                        binding.ivNoticeIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.status_red_danger)));
                        binding.tvAttendanceAdvice.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_red_danger));
                        binding.tvAttendanceAdvice.setText(
                                lowAttendanceCount + " course" + (lowAttendanceCount > 1 ? "s are" : " is") + 
                                " below the mandatory 75% attendance threshold."
                        );
                    } else {
                        binding.pbAggregateAttendance.setProgressTintList(
                                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.status_green_success))
                        );
                        binding.bannerAttendanceNotice.setBackgroundResource(R.drawable.bg_badge_green);
                        binding.ivNoticeIcon.setImageResource(R.drawable.ic_check_circle);
                        binding.ivNoticeIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.status_green_success)));
                        binding.tvAttendanceAdvice.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_green_success));
                        binding.tvAttendanceAdvice.setText("All subjects are in good standing (>= 75%). Keep it up!");
                    }
                } else {
                    binding.layoutEmptyAttendance.setVisibility(View.VISIBLE);
                    binding.rvAttendance.setVisibility(View.GONE);
                }
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

