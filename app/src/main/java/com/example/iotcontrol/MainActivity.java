package com.example.iotcontrol;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

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
            public void onPageScrollStateChanged(int state) {

            }
        });

        MainActivity.bottomNavigationView.setSelectedItemId(R.id.menuHome); //nastavení hlavní stránky při startu aplikace

        //další kod


    }


}