package com.example.javabreak.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.javabreak.R;
import com.example.javabreak.adapters.NonSwipeableViewPager;
import com.example.javabreak.adapters.ScheduledBreakAdapter;
import com.example.javabreak.adapters.SectionPageAdapter;
import com.example.javabreak.viewmodel.ReminderViewModel;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity  {
    private int[] tabIcons = {
            R.drawable.ic_baseline_timer_24,
            R.drawable.ic_baseline_toc_24,
            R.drawable.ic_baseline_settings_24
    };

    TabLayout tabLayout;
    NonSwipeableViewPager viewPager;
    ReminderViewModel reminderViewModel;
    private ScheduledBreakAdapter scheduledBreakAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (NonSwipeableViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new SectionPageAdapter(getSupportFragmentManager()));

        tabLayout =  findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Productive").setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setText("Scheduled").setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setText("Settings").setIcon(tabIcons[2]);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#FF9800"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);


            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#FF9800"), PorterDuff.Mode.SRC_IN);
            }


                @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    public void lockViewPager() {
        viewPager.setPagingEnabled(false);
    }

    public void openViewPager() {
        viewPager.setPagingEnabled(true);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
     /*   if (doubleBackToExitPressedOnce) {*/
            fragmentManager.popBackStack( );
            tabLayout.setVisibility(View.VISIBLE);
            openViewPager();
   /*         ExtendedFloatingActionButton extendedFloatingActionButton = secondFragment.getView().findViewById(R.id.floatingActionButton);
            extendedFloatingActionButton.setVisibility(View.VISIBLE);*/
        /*}*/
  /*      this.doubleBackToExitPressedOnce = true;
        Toasty.warning(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);*/


    }



    public void visible()
    {
        tabLayout.setVisibility(View.VISIBLE);
    }
}