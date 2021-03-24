package com.vish.apps.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DefinitionActivity extends AppCompatActivity {

    private static final String TAG = DefinitionActivity.class.getName();
    private TextView txtTitle;
    private TextView txtDefinition;
    private ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);

        txtTitle = (TextView) findViewById(R.id.act_definition_txt_title);
        txtDefinition = (TextView) findViewById(R.id.act_definition_txt_definition);

        // get info from last activity
        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        String definition = intent.getStringExtra("Definition");

        txtTitle.setText(title);
        txtDefinition.setText(definition);

    }

    public void btnBackClicked(View view) {
        finish();
    }
}