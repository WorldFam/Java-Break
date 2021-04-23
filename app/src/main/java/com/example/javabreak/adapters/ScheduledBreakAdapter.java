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
import com.example.javabreak.models.ScheduledBreak;

import java.util.ArrayList;
import java.util.List;

public class ScheduledBreakAdapter extends ListAdapter<ScheduledBreak,ScheduledBreakAdapter.ViewHolder> {

    private OnItemClickListener mListener;

    public ScheduledBreakAdapter(ArrayList<ScheduledBreak> scheduledBreakList)
    {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ScheduledBreak> DIFF_CALLBACK = new DiffUtil.ItemCallback<ScheduledBreak>() {
        @Override
        public boolean areItemsTheSame(ScheduledBreak oldItem, ScheduledBreak newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(ScheduledBreak oldItem, ScheduledBreak newItem) {
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
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDelete(position);
                        }
                    }
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
        return new ViewHolder(view,mListener);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduledBreak scheduledBreak = getItem(position);
        List<DayOfTheWeek> dayOfTheWeeks = scheduledBreak.getDayOfTheWeeks();
        System.out.println(dayOfTheWeeks );
        holder.name.setText(scheduledBreak.getName());
        for (DayOfTheWeek dayOfTheWeek : dayOfTheWeeks ) {
            setWeekDay(holder,dayOfTheWeek);
        }
        holder.workDuration.setText(scheduledBreak.getWorkFromText() + "-" + scheduledBreak.getWorkToText());
        holder.breakDuration.setText(scheduledBreak.getBreakFrequencyText() + " / " + scheduledBreak.getBreakDurationText());
    }

    private void setWeekDay(@NonNull ViewHolder holder, DayOfTheWeek dayOfTheWeek) {
        if (dayOfTheWeek.getWeekDay().equals("Monday") ) {
            holder.monday.setTextColor(Color.parseColor("#FF9800"));
            holder.monday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Tuesday")) {
            holder.tuesday.setTextColor(Color.parseColor("#FF9800"));
            holder.tuesday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Wednesday")) {
            holder.wednesday.setTextColor(Color.parseColor("#FF9800"));
            holder.wednesday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Thursday")) {
            holder.thursday.setTextColor(Color.parseColor("#FF9800"));
            holder.thursday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Friday")) {
            holder.friday.setTextColor(Color.parseColor("#FF9800"));
            holder.friday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Saturday")) {
            holder.saturday.setTextColor(Color.parseColor("#FF9800"));
            holder.saturday.setTypeface(null, Typeface.BOLD);
        }
        if (dayOfTheWeek.getWeekDay().equals("Sunday")) {
            holder.sunday.setTextColor(Color.parseColor("#FF9800"));
            holder.sunday.setTypeface(null, Typeface.BOLD);
        }
    }

    public ScheduledBreak getItemAt(int position) {
        return getItem(position);
    }
}