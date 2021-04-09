package com.example.javabreak;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScheduledBreakAdapter extends RecyclerView.Adapter<ScheduledBreakAdapter.ViewHolder> {

    private ArrayList<ScheduledBreak> scheduledBreakList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView workDuration;
        public TextView breakDuration;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView);
            workDuration = itemView.findViewById(R.id.textView2);
            breakDuration = itemView.findViewById(R.id.textView3);
        }
    }

    public ScheduledBreakAdapter(ArrayList<ScheduledBreak> scheduledBreakList) {
        this.scheduledBreakList = scheduledBreakList;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.card_view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduledBreak scheduledBreak = scheduledBreakList.get(position);

        holder.name.setText(scheduledBreak.getName());
        holder.workDuration.setText(scheduledBreak.getWorkDurationText());
        holder.breakDuration.setText(scheduledBreak.getBreakDurationText());
    }

    @Override
    public int getItemCount() {
        return scheduledBreakList == null ? 0 : scheduledBreakList.size();
    }
}
