package com.moutamid.mycalender;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.moutamid.mycalender.ui.main.SectionsPagerAdapter;
import com.moutamid.mycalender.databinding.ActivityAllDataBinding;

public class AllData extends AppCompatActivity {

    private ActivityAllDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAllDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ViewPager viewPager = binding.viewPager;

        TabLayout tabs = binding.tabs;
        tabs.addTab(tabs.newTab().setText("Diary"));
        tabs.addTab(tabs.newTab().setText("Notes"));
        tabs.addTab(tabs.newTab().setText("To Do"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
        tabs.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabs.setTabTextColors(R.color.newBg, R.color.text);
        tabs.setupWithViewPager(viewPager);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),tabs.getTabCount());
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}