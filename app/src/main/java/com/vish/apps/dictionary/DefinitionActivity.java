package com.vish.apps.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class DefinitionActivity extends AppCompatActivity {

    private static final String TAG = DefinitionActivity.class.getName();
    private TextToSpeech textToSpeech;
    private final int SPEECH_TITLE_LENGTH = 1800;
    private TextView txtTitle;
    private TextView txtDefinition;
    private TextView txtExamples;
    private ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

        txtTitle = (TextView) findViewById(R.id.act_definition_txt_title);
        txtDefinition = (TextView) findViewById(R.id.act_definition_txt_definition);
        txtExamples = (TextView) findViewById(R.id.act_definition_txt_examples);

        // get info from last activity
        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        String definition = intent.getStringExtra("Definition");
        String examples = intent.getStringExtra("Examples");

        txtTitle.setText(title);
        txtDefinition.setText(definition);

        if(examples == null) {
            ConstraintLayout exampleLayout = (ConstraintLayout) findViewById(R.id.act_definition_layout_examples);
            exampleLayout.setVisibility(View.GONE);
        }

        txtExamples.setText(examples);
    }

    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    public void btnBackClicked(View view) {
        finish();
    }

    public void btnSpeechClicked(View view) {
        String title = txtTitle.getText().toString();
        String definition = txtDefinition.getText().toString();
        textToSpeech.speak(title, TextToSpeech.QUEUE_FLUSH, null);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                textToSpeech.speak(definition, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, SPEECH_TITLE_LENGTH);
    }
}