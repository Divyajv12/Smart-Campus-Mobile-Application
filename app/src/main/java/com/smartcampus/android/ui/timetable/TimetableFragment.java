package com.smartcampus.android.ui.timetable;

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
import com.smartcampus.android.adapter.TimetableAdapter;
import com.smartcampus.android.databinding.FragmentTimetableBinding;
import com.smartcampus.android.model.TimetableSlot;
import com.smartcampus.android.repository.CampusRepository;
import com.smartcampus.android.utils.DateUtils;

import java.util.List;

public class TimetableFragment extends Fragment {

    private FragmentTimetableBinding binding;
    private TimetableAdapter adapter;
    private CampusRepository repository;
    private String selectedDay = "Monday";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTimetableBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = CampusRepository.getInstance();

        setupRecyclerView();
        setupDayToggleGroup();
        setupSwipeRefresh();

        // Default to current day
        determineInitialDay();
    }

    private void setupRecyclerView() {
        adapter = new TimetableAdapter();
        binding.rvTimetable.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTimetable.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.primary_navy),
                ContextCompat.getColor(requireContext(), R.color.secondary_light_blue)
        );
        binding.swipeRefresh.setOnRefreshListener(() -> loadTimetableForDay(selectedDay));
    }

    private void determineInitialDay() {
        String today = DateUtils.getCurrentDayOfWeek();
        switch (today.toLowerCase()) {
            case "tuesday":
                binding.toggleGroupDays.check(R.id.btnTue);
                break;
            case "wednesday":
                binding.toggleGroupDays.check(R.id.btnWed);
                break;
            case "thursday":
                binding.toggleGroupDays.check(R.id.btnThu);
                break;
            case "friday":
                binding.toggleGroupDays.check(R.id.btnFri);
                break;
            case "saturday":
                binding.toggleGroupDays.check(R.id.btnSat);
                break;
            case "monday":
            default:
                binding.toggleGroupDays.check(R.id.btnMon);
                break;
        }
    }

    private void setupDayToggleGroup() {
        binding.toggleGroupDays.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            if (checkedId == R.id.btnMon) {
                selectedDay = "Monday";
            } else if (checkedId == R.id.btnTue) {
                selectedDay = "Tuesday";
            } else if (checkedId == R.id.btnWed) {
                selectedDay = "Wednesday";
            } else if (checkedId == R.id.btnThu) {
                selectedDay = "Thursday";
            } else if (checkedId == R.id.btnFri) {
                selectedDay = "Friday";
            } else if (checkedId == R.id.btnSat) {
                selectedDay = "Saturday";
            }

            updateDayHeader();
            loadTimetableForDay(selectedDay);
        });
    }

    private void updateDayHeader() {
        binding.tvActiveDayLabel.setText(selectedDay + " Schedule");
        String currentDay = DateUtils.getCurrentDayOfWeek();
        if (selectedDay.equalsIgnoreCase(currentDay)) {
            binding.tvTodayHighlightTag.setVisibility(View.VISIBLE);
        } else {
            binding.tvTodayHighlightTag.setVisibility(View.GONE);
        }
    }

    public void loadTimetableForDay(String day) {
        if (!isAdded()) return;

        repository.getTimetable(requireContext(), day, new CampusRepository.RepositoryCallback<List<TimetableSlot>>() {
            @Override
            public void onLoaded(List<TimetableSlot> slots) {
                if (!isAdded()) return;
                binding.swipeRefresh.setRefreshing(false);

                if (slots != null && !slots.isEmpty()) {
                    binding.layoutEmptyTimetable.setVisibility(View.GONE);
                    binding.rvTimetable.setVisibility(View.VISIBLE);
                    adapter.setSlots(slots);
                } else {
                    binding.layoutEmptyTimetable.setVisibility(View.VISIBLE);
                    binding.rvTimetable.setVisibility(View.GONE);
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

