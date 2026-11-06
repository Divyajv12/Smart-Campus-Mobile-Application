package com.smartcampus.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartcampus.android.databinding.ItemAnnouncementBinding;
import com.smartcampus.android.model.Announcement;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {

    private final List<Announcement> allAnnouncements = new ArrayList<>();
    private final List<Announcement> displayedAnnouncements = new ArrayList<>();

    public void setAnnouncements(List<Announcement> announcements) {
        allAnnouncements.clear();
        displayedAnnouncements.clear();
        if (announcements != null) {
            allAnnouncements.addAll(announcements);
            displayedAnnouncements.addAll(announcements);
        }
        notifyDataSetChanged();
    }

    public void filterByCategory(String category) {
        displayedAnnouncements.clear();
        if (category == null || category.equalsIgnoreCase("All")) {
            displayedAnnouncements.addAll(allAnnouncements);
        } else {
            for (Announcement a : allAnnouncements) {
                if (a.getCategory().equalsIgnoreCase(category)) {
                    displayedAnnouncements.add(a);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAnnouncementBinding binding = ItemAnnouncementBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new AnnouncementViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = displayedAnnouncements.get(position);
        holder.bind(announcement);
    }

    @Override
    public int getItemCount() {
        return displayedAnnouncements.size();
    }

    static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        private final ItemAnnouncementBinding binding;

        public AnnouncementViewHolder(ItemAnnouncementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Announcement item) {
            binding.tvNoticeTitle.setText(item.getTitle());
            binding.tvNoticeDescription.setText(item.getDescription());
            binding.tvCategoryBadge.setText(item.getCategory());
            binding.tvNoticeDate.setText(item.getDate());
            binding.tvNoticeAuthor.setText(item.getAuthor());

            if (item.isImportant()) {
                binding.tvImportantBadge.setVisibility(View.VISIBLE);
            } else {
                binding.tvImportantBadge.setVisibility(View.GONE);
            }
        }
    }
}

