package com.smartcampus.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartcampus.android.databinding.ItemTimetableBinding;
import com.smartcampus.android.model.TimetableSlot;

import java.util.ArrayList;
import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder> {

    private final List<TimetableSlot> slots = new ArrayList<>();

    public void setSlots(List<TimetableSlot> newSlots) {
        slots.clear();
        if (newSlots != null) {
            slots.addAll(newSlots);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TimetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTimetableBinding binding = ItemTimetableBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new TimetableViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableViewHolder holder, int position) {
        TimetableSlot slot = slots.get(position);
        holder.bind(slot);
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    static class TimetableViewHolder extends RecyclerView.ViewHolder {
        private final ItemTimetableBinding binding;

        public TimetableViewHolder(ItemTimetableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TimetableSlot slot) {
            binding.tvStartTime.setText(slot.getStartTime());
            binding.tvEndTime.setText(slot.getEndTime());
            binding.tvSubjectCodeBadge.setText(slot.getSubjectCode());
            binding.tvSubjectName.setText(slot.getSubjectName());
            binding.tvClassroom.setText("Room: " + slot.getClassroom());
            binding.tvFaculty.setText("Faculty: " + slot.getFacultyName());
        }
    }
}

