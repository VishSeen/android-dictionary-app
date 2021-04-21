package com.vish.apps.dictionary.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vish.apps.dictionary.R;

import java.util.Locale;

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
}