package com.smartcampus.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartcampus.android.databinding.ItemEventBinding;
import com.smartcampus.android.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<Event> allEvents = new ArrayList<>();
    private final List<Event> displayedEvents = new ArrayList<>();
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(Event event);
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        this.listener = listener;
    }

    public void setEvents(List<Event> events) {
        allEvents.clear();
        displayedEvents.clear();
        if (events != null) {
            allEvents.addAll(events);
            displayedEvents.addAll(events);
        }
        notifyDataSetChanged();
    }

    public void filterByCategory(String category) {
        displayedEvents.clear();
        if (category == null || category.equalsIgnoreCase("All")) {
            displayedEvents.addAll(allEvents);
        } else {
            for (Event e : allEvents) {
                if (e.getCategory().equalsIgnoreCase(category)) {
                    displayedEvents.add(e);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEventBinding binding = ItemEventBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = displayedEvents.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return displayedEvents.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private final ItemEventBinding binding;

        public EventViewHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Event item) {
            binding.tvEventTitle.setText(item.getTitle());
            binding.tvEventCategory.setText(item.getCategory());
            binding.tvEventTime.setText(item.getTime());
            binding.tvEventVenue.setText(item.getVenue());
            binding.tvEventDescription.setText(item.getDescription());

            // Parse Date string if formatted as "Nov 18, 2026"
            String rawDate = item.getDate();
            if (rawDate != null && rawDate.contains(" ")) {
                String[] parts = rawDate.split(" ");
                if (parts.length >= 2) {
                    binding.tvEventMonth.setText(parts[0].toUpperCase());
                    String day = parts[1].replace(",", "");
                    binding.tvEventDay.setText(day);
                }
            } else {
                binding.tvEventMonth.setText("DATE");
                binding.tvEventDay.setText("•");
            }

            if (item.isRegistered()) {
                binding.tvRegisteredBadge.setVisibility(View.VISIBLE);
            } else {
                binding.tvRegisteredBadge.setVisibility(View.GONE);
            }

            View.OnClickListener clickListener = v -> {
                if (listener != null) {
                    listener.onEventClick(item);
                }
            };

            binding.getRoot().setOnClickListener(clickListener);
            binding.btnViewEventDetails.setOnClickListener(clickListener);
        }
    }
}

