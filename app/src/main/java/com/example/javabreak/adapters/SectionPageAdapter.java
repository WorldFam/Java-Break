package com.example.javabreak.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.javabreak.fragments.FirstFragment;
import com.example.javabreak.fragments.SecondFragment;
import com.example.javabreak.fragments.ThirdFragment;

public class SectionPageAdapter extends FragmentPagerAdapter {
    public SectionPageAdapter(
            FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        if (position == 0) {

            return new FirstFragment();
        }
        else if(position == 1){

            return new SecondFragment();
        }
        else
        return new ThirdFragment();
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

}