package com.vish.apps.dictionary.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vish.apps.dictionary.R;
import com.vish.apps.dictionary.util.Word;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TranslationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TranslationFragment extends Fragment {


    private TextToSpeech textToSpeech;
    private final int SPEECH_TITLE_LENGTH = 1800;
    private EditText edtTranslation;
    private ImageButton btnTranslationSpeak;
    private TextView txtTranslated;
    private ImageButton btnTranslatedSpeak;

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

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

        edtTranslation = view.findViewById(R.id.frag_translation_edt_translation);
        btnTranslationSpeak = view.findViewById(R.id.frag_translation_part_translation_btn_speak);
        txtTranslated = view.findViewById(R.id.frag_translation_txt_translated);
        btnTranslatedSpeak = view.findViewById(R.id.frag_translation_part_translated_btn_speak);

        edtTranslation.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:

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


    private String translationEntries(String wordSearch, String language) {
        final String word = wordSearch;
        final String fields = "translations"; // can add etymologies or nouns here
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();

        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }



    private class OxfordTranslate extends AsyncTask<String, Integer, String> {
        private Word word;

        public OxfordTranslate(Word wordPre) {
            word = wordPre;
        }

        @Override
        protected String doInBackground(String... params) {
            // replace with app id and app key
            final String app_id = "dd74a1c2";
            final String app_key = "6d767e1897c9cdf435102dcf0d77ad47";

            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id",app_id);
                urlConnection.setRequestProperty("app_key",app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                return stringBuilder.toString();
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR HERE : ");
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String title;
            String definition;
            String example;
            String synonyms;
            String etymology;

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray resultsArray = jsonObject.getJSONArray("results");

                JSONObject lEntries = resultsArray.getJSONObject(0);
                JSONArray lArray = lEntries.getJSONArray("lexicalEntries");

                JSONObject entriesObj = lArray.getJSONObject(0);
                JSONArray entriesArray = entriesObj.getJSONArray("entries");

                JSONObject sensesObj = entriesArray.getJSONObject(0);
                JSONArray sensesArray = sensesObj.getJSONArray("senses");

                JSONObject defObj = sensesArray.getJSONObject(0);
                JSONArray defArray = defObj.getJSONArray("definitions");
                definition = defArray.getString(0);

                word.setDefinition(definition);
            } catch (Exception e) {

            }
        }
    }
}