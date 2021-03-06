package com.example.javabreak.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javabreak.MainActivity;
import com.example.javabreak.R;
import com.example.javabreak.adapters.ScheduledBreakAdapter;
import com.example.javabreak.models.DayOfTheWeek;
import com.example.javabreak.models.ScheduledReminder;
import com.example.javabreak.viewmodels.NewReminderFragmentViewModel;
import com.example.javabreak.viewmodels.SecondFragmentViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class SecondFragment extends Fragment{

    private ScheduledBreakAdapter scheduledBreakAdapter;
    private ExtendedFloatingActionButton fab;
    NewReminderFragmentViewModel newReminderFragmentViewModel;
    TabLayout tabLayout;
    ScheduledReminder scheduledReminder = new ScheduledReminder ();
    SecondFragmentViewModel secondFragmentViewModel;
    Animation slideDown;
    ConstraintLayout createNewInfo;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.floatingActionButton);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        scheduledBreakAdapter = new ScheduledBreakAdapter();
        recyclerView.setAdapter(scheduledBreakAdapter);
        createNewInfo = view.findViewById(R.id.createNewInfo);
        tabLayout = getActivity().findViewById(R.id.tabLayout);

        secondFragmentViewModel = new ViewModelProvider(requireActivity()).get(SecondFragmentViewModel.class);
        secondFragmentViewModel.getAllReminders().observe(requireActivity( ), new Observer<List<ScheduledReminder>>( ) {
            @Override
            public void onChanged(List<ScheduledReminder> scheduledReminders) {
                createNewInfoCheck(scheduledReminders);
                scheduledBreakAdapter.submitList(scheduledReminders);
            }

            private void createNewInfoCheck(List<ScheduledReminder> scheduledReminders) {
                if(scheduledReminders.size () == 0)  {
                    createNewInfo.setVisibility(View.VISIBLE);
                } else
                {
                    createNewInfo.setVisibility(View.GONE);
                }
            }
        });


        //Shrinks or extends FAB depending if RecyclerView is in scrolling STATE
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

        //Removes item from RecyclerView
        scheduledBreakAdapter.setOnItemClickListener(new ScheduledBreakAdapter.OnItemClickListener( ) {
            @Override
            public void onDelete(int position) {
                removeItem(position);
                ((MainActivity)getActivity()).cancelAlarmReminder ();

            }
        });

        //Transition between fragments
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_up,
                        R.anim.slide_down);
                fragmentTransaction.replace(R.id.secondFragment, new NewReminderFragment ());
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

    //Add new reminder card if was created
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newReminderFragmentViewModel =  new ViewModelProvider(requireActivity()).get(NewReminderFragmentViewModel.class);

        newReminderFragmentViewModel.getName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String name) {
                scheduledReminder.setName(name);
            }
        });
        newReminderFragmentViewModel.getWeekDay().observe(getViewLifecycleOwner( ), new Observer<List<DayOfTheWeek>>( ) {
            @Override
            public void onChanged(List<DayOfTheWeek> dayOfTheWeeks) {
                scheduledReminder.setDayOfTheWeeks(dayOfTheWeeks);
                insertItem(scheduledReminder);
            }
        });
        newReminderFragmentViewModel.getWorkFrom().observe(getViewLifecycleOwner( ), new Observer<String>( ) {
            @Override
            public void onChanged(String workFrom)
            {
               scheduledReminder.setWorkFromText(workFrom);
            }
        });
        newReminderFragmentViewModel.getWorkTo().observe(getViewLifecycleOwner( ), new Observer<String>( ) {
            @Override
            public void onChanged(String workTo)
            {
                scheduledReminder.setWorkToText(workTo);
            }
        });
        newReminderFragmentViewModel.getBreakDuration().observe(getViewLifecycleOwner( ), new Observer<String>( ) {
            @Override
            public void onChanged(String breakDuration)
            {
                scheduledReminder.setBreakDurationText(breakDuration);
            }
        });
        newReminderFragmentViewModel.getBreakFrequency().observe(getViewLifecycleOwner( ), new Observer<String>( ) {
            @Override
            public void onChanged(String breakFrequency)
            {
                scheduledReminder.setBreakFrequencyText(breakFrequency);
            }
        });
    }

    public void removeItem(int position) {
        secondFragmentViewModel.delete(scheduledBreakAdapter.getItemAt(position));
    }

    public void insertItem(ScheduledReminder scheduledReminder) {
        secondFragmentViewModel.insert(scheduledReminder);
    }

}