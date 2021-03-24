package com.vish.apps.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.vish.apps.dictionary.adapters.ViewPagerAdapter;
import com.vish.apps.dictionary.adapters.WalkthroughPagerAdapter;

public class WalkthroughActivity extends AppCompatActivity {

    private WalkthroughPagerAdapter walkthroughPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        walkthroughPagerAdapter = new WalkthroughPagerAdapter(WalkthroughActivity.this);

        ViewPager2 viewPager2 = findViewById(R.id.act_walkthrough_viewpager);
        viewPager2.setAdapter(walkthroughPagerAdapter);
    }
}