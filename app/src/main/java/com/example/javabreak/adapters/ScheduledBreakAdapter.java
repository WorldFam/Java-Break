package com.example.javabreak.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javabreak.R;
import com.example.javabreak.models.DayOfTheWeek;
import com.example.javabreak.models.ScheduledReminder;

import java.util.List;

public class ScheduledBreakAdapter extends ListAdapter<ScheduledReminder,ScheduledBreakAdapter.ViewHolder> {

    private OnItemClickListener mListener;

    public ScheduledBreakAdapter()
    {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ScheduledReminder> DIFF_CALLBACK = new DiffUtil.ItemCallback<ScheduledReminder>() {
        @Override
        public boolean areItemsTheSame(ScheduledReminder oldItem, ScheduledReminder newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(ScheduledReminder oldItem, ScheduledReminder newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    public interface OnItemClickListener {

        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView monday;
        public TextView tuesday;
        public TextView wednesday;
        public TextView thursday;
        public TextView friday;
        public TextView saturday;
        public TextView sunday;
        public TextView workDuration;
        public TextView breakDuration;
        public ImageButton deleteButton;
        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.tag);
            monday = itemView.findViewById(R.id.Monday);
            tuesday = itemView.findViewById(R.id.Tuesday);
            wednesday = itemView.findViewById(R.id.Wednesday);
            thursday = itemView.findViewById(R.id.Thursday);
            friday = itemView.findViewById(R.id.Friday);
            saturday = itemView.findViewById(R.id.Saturday);
            sunday = itemView.findViewById(R.id.Sunday);
            workDuration = itemView.findViewById(R.id.textView2);
            breakDuration = itemView.findViewById(R.id.textView3);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {
                            listener.onDelete(position);
                            resetWeekDay( );
                        }
                }

                //UnMark week day colors of reminder when deleting entry
             private void resetWeekDay() {
                    monday.setTextColor(Color.parseColor("#80BFB3B3"));
                    monday.setTypeface(null, Typeface.NORMAL);
                    tuesday.setTextColor(Color.parseColor("#80BFB3B3"));
                    tuesday.setTypeface(null, Typeface.NORMAL);
                    wednesday.setTextColor(Color.parseColor("#80BFB3B3"));
                    wednesday.setTypeface(null, Typeface.NORMAL);
                    thursday.setTextColor(Color.parseColor("#80BFB3B3"));
                    thursday.setTypeface(null, Typeface.NORMAL);
                    friday.setTextColor(Color.parseColor("#80BFB3B3"));
                    friday.setTypeface(null, Typeface.NORMAL);
                    saturday.setTextColor(Color.parseColor("#80BFB3B3"));
                    saturday.setTypeface(null, Typeface.NORMAL);
                    sunday.setTextColor(Color.parseColor("#80BFB3B3"));
                    sunday.setTypeface(null, Typeface.NORMAL);
                }
            });
        }

    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.card_view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view, mListener);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduledReminder scheduledReminder = getItem(position);
        List<DayOfTheWeek> dayOfTheWeeks = scheduledReminder.getDayOfTheWeeks();
        holder.name.setText(scheduledReminder.getName());

        //Loop through weekdays and mark if selected
        for (DayOfTheWeek dayOfTheWeek : dayOfTheWeeks ) {
            setWeekDay(holder,dayOfTheWeek);
        }

        holder.workDuration.setText(scheduledReminder.getWorkFromText() + "-" + scheduledReminder.getWorkToText());
        holder.breakDuration.setText(scheduledReminder.getBreakFrequencyText() + " / " + scheduledReminder.getBreakDurationText());
    }

    private void setWeekDay(@NonNull ViewHolder holder, DayOfTheWeek dayOfTheWeek) {
        if (dayOfTheWeek.getWeekDay().equals("Monday")  && dayOfTheWeek.isChecked()) {
            holder.monday.setTextColor(Color.parseColor("#FF9800"));
            holder.monday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Tuesday") && dayOfTheWeek.isChecked()) {
            holder.tuesday.setTextColor(Color.parseColor("#FF9800"));
            holder.tuesday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Wednesday") && dayOfTheWeek.isChecked()) {
            holder.wednesday.setTextColor(Color.parseColor("#FF9800"));
            holder.wednesday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Thursday") && dayOfTheWeek.isChecked()) {
            holder.thursday.setTextColor(Color.parseColor("#FF9800"));
            holder.thursday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Friday") && dayOfTheWeek.isChecked()) {
            holder.friday.setTextColor(Color.parseColor("#FF9800"));
            holder.friday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Saturday") && dayOfTheWeek.isChecked()) {
            holder.saturday.setTextColor(Color.parseColor("#FF9800"));
            holder.saturday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Sunday") && dayOfTheWeek.isChecked()) {
            holder.sunday.setTextColor(Color.parseColor("#FF9800"));
            holder.sunday.setTypeface(null, Typeface.BOLD);
        }
    }

    public ScheduledReminder getItemAt(int position) {
        return getItem(position);
    }
}