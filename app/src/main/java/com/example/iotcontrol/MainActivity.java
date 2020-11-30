package com.example.iotcontrol;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.iotcontrol.fragments.home_fragment.HomeFragment;
import com.example.iotcontrol.fragments.settings_fragment.SettingsFragment;
import com.example.iotcontrol.fragments.statistics_fragment.StatisticsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    public static BottomNavigationView bottomNavigationView;
    public static Toolbar myBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBar = findViewById(R.id.toolbar);
        setSupportActionBar(myBar);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuStatistics:
                        pager.setCurrentItem(0);
                        getSupportActionBar().setTitle("Statistics");
                        break;
                    case R.id.menuHome:
                        pager.setCurrentItem(1);
                        getSupportActionBar().setTitle("Devices");
                        break;
                    case R.id.menuSettings:
                        pager.setCurrentItem(2);
                        getSupportActionBar().setTitle("Settings");
                        break;
                }
                return true;
            }
        });

        List<Fragment> list = new ArrayList<>();
        list.add(new StatisticsFragment());
        list.add(new HomeFragment());
        list.add(new SettingsFragment());

        pager = findViewById(R.id.viewPager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list, bottomNavigationView);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(4);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        MainActivity.bottomNavigationView.setSelectedItemId(R.id.menuStatistics);
                        break;
                    case 1:
                        MainActivity.bottomNavigationView.setSelectedItemId(R.id.menuHome);
                        break;
                    case 2:
                        MainActivity.bottomNavigationView.setSelectedItemId(R.id.menuSettings);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        bottomNavigationView.setSelectedItemId(R.id.menuHome); //nastavení hlavní stránky při startu aplikace

        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        boolean nightModeOn = sh.getBoolean("night", false);

        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[] {
                        getResources().getColor(R.color.colorWhite),
                        getResources().getColor(R.color.colorFan),
                        getResources().getColor(R.color.colorAqua),
                }
        );

        if(nightModeOn){
            myBar.setBackgroundColor(getResources().getColor(R.color.colorLayout));
            myBar.setTitleTextColor(getResources().getColor(R.color.colorAqua));
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.colorLayout));
            bottomNavigationView.setItemTextColor(myColorStateList);
            bottomNavigationView.setItemIconTintList(myColorStateList);

        }
    }
}