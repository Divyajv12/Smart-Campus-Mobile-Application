package com.smartcampus.android.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.smartcampus.android.R;
import com.smartcampus.android.databinding.ItemAttendanceBinding;
import com.smartcampus.android.model.AttendanceRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private final List<AttendanceRecord> records = new ArrayList<>();
    private Context context;

    public void setRecords(List<AttendanceRecord> newRecords) {
        records.clear();
        if (newRecords != null) {
            records.addAll(newRecords);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemAttendanceBinding binding = ItemAttendanceBinding.inflate(
                LayoutInflater.from(context), parent, false
        );
        return new AttendanceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        AttendanceRecord record = records.get(position);
        holder.bind(record);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class AttendanceViewHolder extends RecyclerView.ViewHolder {
        private final ItemAttendanceBinding binding;

        public AttendanceViewHolder(ItemAttendanceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AttendanceRecord record) {
            binding.tvSubjectCode.setText(record.getSubjectCode());
            binding.tvSubjectName.setText(record.getSubjectName());
            binding.tvFacultyName.setText("Faculty: " + record.getFacultyName());

            double percentage = record.getPercentage();
            binding.tvPercentage.setText(String.format(Locale.US, "%.1f%%", percentage));
            binding.tvClassStats.setText("Attended: " + record.getAttendedClasses() + " / " + 
                    record.getTotalClasses() + " classes (Missed: " + record.getMissedClasses() + ")");

            int progress = (int) Math.round(percentage);
            binding.pbSubjectAttendance.setProgress(progress);

            if (record.isLowAttendance()) {
                // Low attendance: Red alert
                binding.tvPercentage.setBackgroundResource(R.drawable.bg_badge_red);
                binding.tvPercentage.setTextColor(ContextCompat.getColor(context, R.color.status_red_danger));
                binding.pbSubjectAttendance.setProgressTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_red_danger))
                );
                binding.tvThresholdNotice.setText("Low Attendance (< 75%)");
                binding.tvThresholdNotice.setTextColor(ContextCompat.getColor(context, R.color.status_red_danger));

                int needed = record.getClassesNeededFor75();
                binding.layoutLowAttendanceWarning.setVisibility(View.VISIBLE);
                binding.tvLowAttendanceWarningText.setText(
                        "Attendance below 75%! Need to attend next " + needed + " consecutive classes."
                );
            } else {
                // Good attendance: Green
                binding.tvPercentage.setBackgroundResource(R.drawable.bg_badge_green);
                binding.tvPercentage.setTextColor(ContextCompat.getColor(context, R.color.status_green_success));
                binding.pbSubjectAttendance.setProgressTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_green_success))
                );
                binding.tvThresholdNotice.setText("Safe (>= 75%)");
                binding.tvThresholdNotice.setTextColor(ContextCompat.getColor(context, R.color.status_green_success));
                binding.layoutLowAttendanceWarning.setVisibility(View.GONE);
            }
        }
    }
}

