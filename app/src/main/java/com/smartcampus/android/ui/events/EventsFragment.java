package com.smartcampus.android.ui.events;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.smartcampus.android.R;
import com.smartcampus.android.adapter.EventAdapter;
import com.smartcampus.android.databinding.DialogEventDetailBinding;
import com.smartcampus.android.databinding.FragmentEventsBinding;
import com.smartcampus.android.model.Event;
import com.smartcampus.android.repository.CampusRepository;

import java.util.List;

public class EventsFragment extends Fragment implements EventAdapter.OnEventClickListener {

    private FragmentEventsBinding binding;
    private EventAdapter adapter;
    private CampusRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = CampusRepository.getInstance();

        setupRecyclerView();
        setupCategoryChips();
        setupSwipeRefresh();
        loadEvents();
    }

    private void setupRecyclerView() {
        adapter = new EventAdapter();
        adapter.setOnEventClickListener(this);
        binding.rvEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvEvents.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.primary_navy),
                ContextCompat.getColor(requireContext(), R.color.secondary_light_blue)
        );
        binding.swipeRefresh.setOnRefreshListener(this::loadEvents);
    }

    private void setupCategoryChips() {
        binding.chipGroupEventCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int checkedId = checkedIds.get(0);

            if (checkedId == R.id.chipTechnical) {
                adapter.filterByCategory("Technical");
            } else if (checkedId == R.id.chipCultural) {
                adapter.filterByCategory("Cultural");
            } else if (checkedId == R.id.chipSports) {
                adapter.filterByCategory("Sports");
            } else {
                adapter.filterByCategory("All");
            }

            checkEmptyState();
        });
    }

    private void checkEmptyState() {
        if (adapter.getItemCount() == 0) {
            binding.layoutEmptyEvents.setVisibility(View.VISIBLE);
            binding.rvEvents.setVisibility(View.GONE);
        } else {
            binding.layoutEmptyEvents.setVisibility(View.GONE);
            binding.rvEvents.setVisibility(View.VISIBLE);
        }
    }

    private void loadEvents() {
        if (!isAdded()) return;

        repository.getEvents(requireContext(), new CampusRepository.RepositoryCallback<List<Event>>() {
            @Override
            public void onLoaded(List<Event> events) {
                if (!isAdded()) return;
                binding.swipeRefresh.setRefreshing(false);

                if (events != null && !events.isEmpty()) {
                    adapter.setEvents(events);
                    checkEmptyState();
                } else {
                    binding.layoutEmptyEvents.setVisibility(View.VISIBLE);
                    binding.rvEvents.setVisibility(View.GONE);
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
    public void onEventClick(Event event) {
        showEventDetailDialog(event);
    }

    private void showEventDetailDialog(Event event) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogEventDetailBinding dialogBinding = DialogEventDetailBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        dialogBinding.tvDetailTitle.setText(event.getTitle());
        dialogBinding.tvDetailCategory.setText(event.getCategory());
        dialogBinding.tvDetailDate.setText("Date: " + event.getDate());
        dialogBinding.tvDetailTime.setText("Time: " + event.getTime());
        dialogBinding.tvDetailVenue.setText("Venue: " + event.getVenue());
        dialogBinding.tvDetailOrganizer.setText("Organizer: " + event.getOrganizer());
        dialogBinding.tvDetailDescription.setText(event.getDescription());

        updateRegistrationButton(dialogBinding, event.isRegistered());

        dialogBinding.btnRegisterEvent.setOnClickListener(v -> {
            boolean newState = !event.isRegistered();
            event.setRegistered(newState);
            updateRegistrationButton(dialogBinding, newState);
            adapter.notifyDataSetChanged();

            if (newState) {
                Toast.makeText(requireContext(), "Registered for " + event.getTitle() + "!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Registration cancelled.", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBinding.btnCloseDialog.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void updateRegistrationButton(DialogEventDetailBinding dialogBinding, boolean isRegistered) {
        if (isRegistered) {
            dialogBinding.btnRegisterEvent.setText("Registered ✓ (Click to Cancel)");
            dialogBinding.btnRegisterEvent.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.status_green_success));
        } else {
            dialogBinding.btnRegisterEvent.setText(R.string.register_event);
            dialogBinding.btnRegisterEvent.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_navy));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

