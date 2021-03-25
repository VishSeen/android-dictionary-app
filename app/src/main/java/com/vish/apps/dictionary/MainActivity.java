package com.vish.apps.dictionary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vish.apps.dictionary.adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPagerAdapter = new ViewPagerAdapter(MainActivity.this);

        // Initializing views
        ImageButton btnSearch = (ImageButton) findViewById(R.id.act_main_img_btn_settings);
        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.act_main_viewpager);
        viewPager.setAdapter(viewPagerAdapter);

        bottomNavView = (BottomNavigationView) findViewById(R.id.act_main_bottom_navigation);
        bottomNavView.setSelectedItemId(R.id.menu_action_definition);

        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                switch (item.getItemId()){
                    case R.id.menu_action_definition:
                        viewPager.setCurrentItem(0, true);
                        return true;
                    case R.id.menu_action_translation:
                        viewPager.setCurrentItem(1, true);
                        return true;
                }
                return false;
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavView.getMenu().findItem(R.id.menu_action_definition).setChecked(true);
                        break;
                    case 1:
                        bottomNavView.getMenu().findItem(R.id.menu_action_translation).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }
}