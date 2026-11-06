package com.smartcampus.android.ui.announcements;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.smartcampus.android.R;
import com.smartcampus.android.adapter.AnnouncementAdapter;
import com.smartcampus.android.databinding.ActivityAnnouncementsBinding;
import com.smartcampus.android.model.Announcement;
import com.smartcampus.android.repository.CampusRepository;

import java.util.List;

public class AnnouncementsActivity extends AppCompatActivity {

    private ActivityAnnouncementsBinding binding;
    private AnnouncementAdapter adapter;
    private CampusRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnnouncementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = CampusRepository.getInstance();

        setupToolbar();
        setupRecyclerView();
        setupCategoryChips();
        setupSwipeRefresh();
        loadAnnouncements();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new AnnouncementAdapter();
        binding.rvAnnouncements.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAnnouncements.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.primary_navy),
                ContextCompat.getColor(this, R.color.secondary_light_blue)
        );
        binding.swipeRefresh.setOnRefreshListener(this::loadAnnouncements);
    }

    private void setupCategoryChips() {
        binding.chipGroupCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int checkedId = checkedIds.get(0);

            if (checkedId == R.id.chipExams) {
                adapter.filterByCategory("Exams");
            } else if (checkedId == R.id.chipAcademic) {
                adapter.filterByCategory("Academic");
            } else if (checkedId == R.id.chipEvents) {
                adapter.filterByCategory("Events");
            } else if (checkedId == R.id.chipAdmin) {
                adapter.filterByCategory("Administrative");
            } else {
                adapter.filterByCategory("All");
            }

            checkEmptyState();
        });
    }

    private void checkEmptyState() {
        if (adapter.getItemCount() == 0) {
            binding.layoutEmpty.setVisibility(View.VISIBLE);
            binding.rvAnnouncements.setVisibility(View.GONE);
        } else {
            binding.layoutEmpty.setVisibility(View.GONE);
            binding.rvAnnouncements.setVisibility(View.VISIBLE);
        }
    }

    private void loadAnnouncements() {
        repository.getAnnouncements(this, new CampusRepository.RepositoryCallback<List<Announcement>>() {
            @Override
            public void onLoaded(List<Announcement> announcements) {
                binding.swipeRefresh.setRefreshing(false);
                if (announcements != null && !announcements.isEmpty()) {
                    adapter.setAnnouncements(announcements);
                    checkEmptyState();
                } else {
                    binding.layoutEmpty.setVisibility(View.VISIBLE);
                    binding.rvAnnouncements.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }
}

