package com.example.iotcontrol;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SlidePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private BottomNavigationView bottomNavigationView;


    public SlidePagerAdapter(FragmentManager fm, List<Fragment> fragmentList, BottomNavigationView bottomNavigationView) {
        super(fm);
        this.fragmentList = fragmentList;
        this.bottomNavigationView = bottomNavigationView;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
