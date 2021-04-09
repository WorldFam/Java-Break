package com.example.javabreak;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ScheduledBreak> scheduledBreakList;
    private ScheduledBreakAdapter scheduledBreakAdapter;
    private ExtendedFloatingActionButton fab;
    private CheckBox checkBox;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.fragment_second, container, false);
        scheduledBreakList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.floatingActionButton);
        checkBox = view.findViewById(R.id.Monday);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        scheduledBreakAdapter = new ScheduledBreakAdapter(scheduledBreakList);
        recyclerView.setAdapter(scheduledBreakAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.extend();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isExtended()) {
                    fab.shrink();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewScheduledReminder.class);
                startActivity(intent);
                scheduledBreakList.add(new ScheduledBreak("My first scheduled time!", "09:00 - 16:00", "15 minutes" ));
                scheduledBreakAdapter.notifyItemInserted(scheduledBreakList.size()-1);
            }
        });




        return view;
    }

/*    public void insertItem(ScheduledBreak scheduledBreak) {
        scheduledBreakList.add(scheduledBreak);
        scheduledBreakAdapter.notifyItemInserted(scheduledBreakList.size() );
    }
    public void removeItem(int position) {
        scheduledBreakList.remove(position);
        scheduledBreakAdapter.notifyItemRemoved(position);
    }*/

}