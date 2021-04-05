package com.vish.apps.dictionary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vish.apps.dictionary.adapters.ViewPagerAdapter;
import com.vish.apps.dictionary.util.OxfordRequests;
import com.vish.apps.dictionary.util.Word;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = DefinitionActivity.class.getName();
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView bottomNavView;
    private EditText edtSearch;
    public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPagerAdapter = new ViewPagerAdapter(MainActivity.this);

        // Initializing views
        ImageButton btnSettings = (ImageButton) findViewById(R.id.act_main_img_btn_settings);
        edtSearch = (EditText) findViewById(R.id.act_main_edt_search);
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

        edtSearch.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View view, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            searchWordClick(view);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
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

    public void searchWordClick(View v) {
        String edtText = edtSearch.getText().toString();

        Word searchedWord = new Word(01, edtText, "Loading...");
        url = definitionEntries(edtText);
        OxfordRequests oxfordRequests = new OxfordRequests(searchedWord);
        oxfordRequests.execute(url);


//        Log.d(TAG, "searchWordClick: Str results " + oxfordRequests.getResultDef());

        Intent intent = new Intent(this, DefinitionActivity.class);
        intent.putExtra("Title", searchedWord.getTitle());
        intent.putExtra("Definition", searchedWord.getDefinition());
        startActivity(intent);
    }

    public String getResults() {
        String edtText = edtSearch.getText().toString();

        Word searchedWord = new Word(01, edtText, "Loading...");
        url = definitionEntries(edtText);

        try {
            return new OxfordRequests(searchedWord).execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Complete";
    }
}