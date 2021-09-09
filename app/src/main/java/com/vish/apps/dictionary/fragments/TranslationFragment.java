package com.vish.apps.dictionary.fragments;

import static com.vish.apps.dictionary.CameraActivity.CAMERA_TEXT;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.vish.apps.dictionary.CameraActivity;
import com.vish.apps.dictionary.DefinitionActivity;
import com.vish.apps.dictionary.R;
import com.vish.apps.dictionary.RoomActivity;
import com.vish.apps.dictionary.util.Language;
import com.vish.apps.dictionary.util.VoiceResultListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class TranslationFragment extends Fragment implements VoiceResultListener {

    private TextToSpeech textToSpeech;
    private final int SPEECH_TITLE_LENGTH = 1800;

    private EditText edtTranslation;
    private TextView txtTranslated;

    private String mLanguage;
    private String mLanguageTranslatedSpeech;

    private Translate translate;


    public TranslationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translation, container, false);
        ImageButton imageButton = view.findViewById(R.id.frag_translation_btn_switch);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RoomActivity.class));
            }
        });

        // create TTS and set language to default device
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });



        // getting views
        Spinner spinnerFrom = view.findViewById(R.id.frag_translation_spinner_to_translate);
        Spinner spinnerTo = view.findViewById(R.id.frag_translation_spinner_change_language);
        edtTranslation = view.findViewById(R.id.act_camera_translation_edt_extracted_txt);
        ImageButton btnTranslationSpeak = view.findViewById(R.id.frag_translation_part_translation_btn_speak);
        ImageButton btnCamera = view.findViewById(R.id.frag_translation_part_translation_btn_camera);
        txtTranslated = view.findViewById(R.id.frag_translation_txt_translated);
        ImageButton btnTranslatedSpeak = view.findViewById(R.id.frag_translation_part_translated_btn_speak);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.array_translate_language, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(adapter);


        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (position == 0) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.frag_translation_spinner_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), getResources().getString(R.string.frag_translation_spinner_error), Toast.LENGTH_SHORT).show();

                Language changeLanguage = new Language(position);
                mLanguage = changeLanguage.getLanguage();

                // automatic change translated word on spinner change
                if (!(edtTranslation.getText().toString().isEmpty())) {
                    translate(edtTranslation.getText().toString(), mLanguage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        edtTranslation.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (checkInternetConnection()) {
                                translate(edtTranslation.getText().toString(), mLanguage);

                            } else {
                                //If not, display "no connection" warning:
                                txtTranslated.setText(getResources().getString(R.string.frag_translation_txt_connec_error));
                            }
                            return true;
                        default:
                            break;
                    }
                }

                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    // backspace event
                    if (edtTranslation.getText().length() == 0) {
                    }
                }
                return false;
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open the camera => create an Intent object
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 101);
            }
        });

        btnTranslationSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String translation = edtTranslation.getText().toString();

                // if edt text empty, speak error
                if (translation.isEmpty()) {
                    translation = getResources().getString(R.string.frag_translation_txt_error);
                }

                textToSpeech.speak(translation, TextToSpeech.QUEUE_FLUSH, null);
            }

        });

        btnTranslatedSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String translated = txtTranslated.getText().toString();

                textToSpeech.speak(translated, TextToSpeech.QUEUE_FLUSH, null);
            }

        });

        return view;
    }

    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }



    public void translate(String text, String language) {
        //If there is internet connection, get translate service and start translation:
        getTranslateService();

        //Get input text to be translated:
        Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage(language), Translate.TranslateOption.model("base"));
        String translateTo = translation.getTranslatedText();

        //Translated text and original text are set to TextViews:
        txtTranslated.setText(translateTo);

    }



    public boolean checkInternetConnection() {
        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        boolean connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }


    public void getTranslateService() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = getResources().openRawResource(R.raw.credentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

    // an interface which gets the voice result and start the translation
    @Override
    public void onVoiceResult(String voiceText) {
        edtTranslation.setText(voiceText);

        translate(voiceText, mLanguage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Intent intent = new Intent(getActivity(), CameraActivity.class);
            Bundle bundle = data.getExtras();
            intent.putExtras(bundle);

            startActivityForResult(intent, CAMERA_TEXT);
        }

        if (resultCode == CAMERA_TEXT) {
            Bundle bundle = data.getExtras();
            String cameraText = bundle.getString("camera_result");

            onVoiceResult(cameraText);
        }
    }
}