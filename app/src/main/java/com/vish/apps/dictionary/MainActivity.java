package com.vish.apps.dictionary;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vish.apps.dictionary.adapters.ViewPagerAdapter;
import com.vish.apps.dictionary.fragments.DefinitionFragment;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = DefinitionActivity.class.getName();
    private final int REQ_CODE = 100;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView bottomNavView;
    public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPagerAdapter = new ViewPagerAdapter(MainActivity.this);

        // Initializing views
        ImageButton btnSettings = (ImageButton) findViewById(R.id.act_main_img_btn_settings);
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

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

    }

    private String definitionEntries(String wordSearch) {
        final String language = "en-gb";
        final String word = wordSearch;
        final String fields = "definitions"; // can add etymologies or nouns here
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();

        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }

    public void fabSpeechClick(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
        try {
            startActivityForResult(intent, REQ_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry your device not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String speechText = result.get(0).toString();

                    System.out.println("SPEECH : " + speechText);

                    DefinitionFragment definitionFragment = new DefinitionFragment();
                    definitionFragment.searchWord(speechText);
                }
                break;
            }
        }
    }
}