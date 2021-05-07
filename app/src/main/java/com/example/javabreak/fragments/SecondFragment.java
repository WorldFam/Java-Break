package com.example.javabreak.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javabreak.R;
import com.example.javabreak.ScheduledReminderFragment;
import com.example.javabreak.adapters.ScheduledBreakAdapter;
import com.example.javabreak.models.DayOfTheWeek;
import com.example.javabreak.models.ScheduledBreak;
import com.example.javabreak.viewmodel.ReminderViewModel;
import com.example.javabreak.viewmodel.ScheduledSecondSharedViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SecondFragment extends Fragment{

    private ScheduledBreakAdapter scheduledBreakAdapter;
    private ExtendedFloatingActionButton fab;
    ScheduledSecondSharedViewModel scheduledSecondSharedViewModel;
    final Handler handler = new Handler();
    TabLayout tabLayout;
    ScheduledBreak scheduledBreak = new ScheduledBreak();
    ReminderViewModel reminderViewModel;
    Animation slideDown;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    RelativeLayout createNewInfo;


    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.floatingActionButton);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        scheduledBreakAdapter = new ScheduledBreakAdapter();
        recyclerView.setAdapter(scheduledBreakAdapter);
        createNewInfo = view.findViewById(R.id.createNewInfo);
        tabLayout = getActivity().findViewById(R.id.tabLayout);

        reminderViewModel = new ViewModelProvider(requireActivity()).get(ReminderViewModel.class);
        reminderViewModel.getAllReminders().observe(requireActivity( ), new Observer<List<ScheduledBreak>>( ) {
            @Override
            public void onChanged(List<ScheduledBreak> scheduledBreaks) {
                createNewInfoCheck(scheduledBreaks);
                scheduledBreakAdapter.submitList(scheduledBreaks);
            }

            private void createNewInfoCheck(List<ScheduledBreak> scheduledBreaks) {
                if(scheduledBreaks.isEmpty())
                {
                    createNewInfo.setVisibility(View.VISIBLE);
                }
                else
                {
                    createNewInfo.setVisibility(View.GONE);
                }
            }
        });

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

        scheduledBreakAdapter.setOnItemClickListener(new ScheduledBreakAdapter.OnItemClickListener( ) {
            @Override
            public void onDelete(int position) {
                removeItem(position);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_up,
                        R.anim.slide_down);
                fragmentTransaction.replace(R.id.secondFragment, new ScheduledReminderFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down_fragment);
                recyclerView.startAnimation(slideDown);
                if(tabLayout != null) {
                    createNewInfo.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        scheduledSecondSharedViewModel =  new ViewModelProvider(requireActivity()).get(ScheduledSecondSharedViewModel.class);
        scheduledSecondSharedViewModel.getName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String name) {
                scheduledBreak.setName(name);
            }
        });
        scheduledSecondSharedViewModel.getWeekDay().observe(getViewLifecycleOwner( ), new Observer<List<DayOfTheWeek>>( ) {
            @Override
            public void onChanged(List<DayOfTheWeek> dayOfTheWeeks) {
                scheduledBreak.setDayOfTheWeeks(dayOfTheWeeks);
                insertItem(scheduledBreak);
            }
        });
        scheduledSecondSharedViewModel.getWorkFrom().observe(getViewLifecycleOwner( ), new Observer<String>( ) {
            @Override
            public void onChanged(String workFrom)
            {
               scheduledBreak.setWorkFromText(workFrom);
            }
        });
        scheduledSecondSharedViewModel.getWorkTo().observe(getViewLifecycleOwner( ), new Observer<String>( ) {
            @Override
            public void onChanged(String workTo)
            {
                scheduledBreak.setWorkToText(workTo);
            }
        });
        scheduledSecondSharedViewModel.getBreakDuration().observe(getViewLifecycleOwner( ), new Observer<String>( ) {
            @Override
            public void onChanged(String breakDuration)
            {
                scheduledBreak.setBreakDurationText(breakDuration);
            }
        });
        scheduledSecondSharedViewModel.getBreakFrequency().observe(getViewLifecycleOwner( ), new Observer<String>( ) {
            @Override
            public void onChanged(String breakFrequency)
            {
                scheduledBreak.setBreakFrequencyText(breakFrequency);


            }
        });
    }

    public void removeItem(int position) {
        reminderViewModel.delete(scheduledBreakAdapter.getItemAt(position));
    }

    public void insertItem(ScheduledBreak scheduledBreak) {
        reminderViewModel.insert(scheduledBreak);
    }

}