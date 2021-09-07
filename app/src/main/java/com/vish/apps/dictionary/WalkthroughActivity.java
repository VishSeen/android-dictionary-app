package com.vish.apps.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vish.apps.dictionary.adapters.ViewPagerAdapter;
import com.vish.apps.dictionary.adapters.WalkthroughPagerAdapter;

public class WalkthroughActivity extends AppCompatActivity {

    private WalkthroughPagerAdapter walkthroughPagerAdapter;
    private ViewPager2 viewPager2;
    private int pageCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        walkthroughPagerAdapter = new WalkthroughPagerAdapter(WalkthroughActivity.this);

        viewPager2 = findViewById(R.id.act_walkthrough_viewpager);
        viewPager2.setAdapter(walkthroughPagerAdapter);
    }


    public void btnNextClick(View view) {
        if (pageCounter != 13) {
            viewPager2.setCurrentItem(pageCounter);
            pageCounter++;
        } else {
            startActivity(new Intent(WalkthroughActivity.this, MainActivity.class));
        }

        if(viewPager2.getCurrentItem() == 12) {
            Button btnNext = findViewById(R.id.act_walkthrough_btn_next);
            btnNext.setText(getResources().getString(R.string.frag_voice_btn_finish));
        }
    }
}
