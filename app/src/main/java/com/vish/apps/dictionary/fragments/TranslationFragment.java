package com.vish.apps.dictionary.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;
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
import com.vish.apps.dictionary.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TranslationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TranslationFragment extends Fragment {

    private TextToSpeech textToSpeech;
    private final int SPEECH_TITLE_LENGTH = 1800;

    private Spinner spinnerFrom;
    private Spinner spinnerTo;

    private EditText edtTranslation;
    private TextView txtTranslated;

    private String translateFrom;
    private String translateTo;
    private String mLanguage;
    private String mLanguageTranslatedSpeech;

    private boolean connected;
    private Translate translate;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TranslationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TranslationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TranslationFragment newInstance(String param1, String param2) {
        TranslationFragment fragment = new TranslationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translation, container, false);


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
        spinnerFrom = view.findViewById(R.id.frag_translation_spinner_to_translate);
        spinnerTo = view.findViewById(R.id.frag_translation_spinner_change_language);
        edtTranslation = view.findViewById(R.id.frag_translation_edt_translation);
        ImageButton btnTranslationSpeak = view.findViewById(R.id.frag_translation_part_translation_btn_speak);
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
                // TODO Auto-generated method stub
            }
        });
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("Spinner selected : ", (String) parent.getItemAtPosition(position));
                switch (position) {
                    case 0:
                        Toast.makeText(getActivity(), getResources().getString(R.string.frag_translation_spinner_error), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        mLanguage = "en";
                        break;
                    case 2:
                        mLanguage = "fr";
                        break;
                    case 3:
                        mLanguage = "es";
                        break;
                    case 4:
                        mLanguage = "de";
                        break;
                    case 5:
                        mLanguage = "it";
                        break;
                    case 6:
                        mLanguage = "ht";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        edtTranslation.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (checkInternetConnection()) {
                                //If there is internet connection, get translate service and start translation:
                                getTranslateService();
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



    public void translate (String text, String language) {
        //Get input text to be translated:
        translateFrom = text;
        Translation translation = translate.translate(translateFrom, Translate.TranslateOption.targetLanguage(language), Translate.TranslateOption.model("base"));
        translateTo = translation.getTranslatedText();

        //Translated text and original text are set to TextViews:
        txtTranslated.setText(translateTo);

    }



    public boolean checkInternetConnection() {
        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
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




}