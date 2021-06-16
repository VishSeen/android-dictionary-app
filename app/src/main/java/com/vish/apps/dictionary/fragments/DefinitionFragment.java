package com.vish.apps.dictionary.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vish.apps.dictionary.DefinitionActivity;
import com.vish.apps.dictionary.R;
import com.vish.apps.dictionary.adapters.DefinitionsListAdapter;
import com.vish.apps.dictionary.util.Word;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 */
public class DefinitionFragment extends Fragment {

    private String mDeviceLanguage = Locale.getDefault().toString();

    private EditText edtSearch;
    private Spinner spinnerLanguage;
    private DefinitionsListAdapter adapter;
    private String[] predefinedWords;
    private List<Word> mListWords;
    private String url;

    private Resources mResources;
    private ListView listView;

    private List<Word> mListSearched;
    private List<Word> mListDefinitions;




    public static DefinitionFragment newInstance(int columnCount) {
        DefinitionFragment fragment = new DefinitionFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DefinitionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResources = getResources();
        mListWords = new ArrayList<>();
        mListSearched = new ArrayList<>();
        mListDefinitions = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_definition, container, false);

        // change language code to match api response
        correctLanguageCode(mDeviceLanguage);

        predefinedWords = mResources.getStringArray(R.array.default_words);

        edtSearch = view.findViewById(R.id.act_main_edt_search);
        spinnerLanguage = view.findViewById(R.id.frag_translation_spinner_change_language);
        adapter = new DefinitionsListAdapter(getContext(), mListWords);
        listView = view.findViewById(R.id.frag_definition_listview);
        listView.setAdapter(adapter);

        // loops through array of predefined words to find
        initWord();
        spinnerLanguage.setSelection(1); // TODO: 16/06/2021 function to detect language and set spinner

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word currentWord = (Word) adapterView.getItemAtPosition(i);


                Intent intent = new Intent(getActivity(), DefinitionActivity.class);
                intent.putExtra("Title", currentWord.getTitle());
                intent.putExtra("Definition", currentWord.getDefinition());
                intent.putExtra("Examples", currentWord.getExample());
                intent.putExtra("Synonyms", currentWord.getSynonyms());
                startActivity(intent);
            }
        });


        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            searchWordClick(view);
                            return true;
                        default:
                            break;
                    }
                }

                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    // backspace event
                    if (edtSearch.getText().length() == 0) {
                        adapter.clear();
                        mListWords.addAll(mListDefinitions);
                    }
                }
                return false;
            }
        });
        return view;
    }


    /**
     * Function to initialize word.
     * To be used with language spinner for quick refresh.
     * */
    private void initWord() {
        for (int i = 0; i < predefinedWords.length; i++){
            Word word = new Word(predefinedWords[i], getResources().getString(R.string.frag_definition_txt_definition_loading));
            url = definitionEntries(predefinedWords[i], mDeviceLanguage);
            new GoogleDefinition(word).execute(url);

            mListDefinitions.add(word);
        }

        mListWords.addAll(mListDefinitions);
    }


    public void searchWordClick(View v) {
        String edtText = edtSearch.getText().toString();
        searchWord(edtText);
    }

    public void searchWord(String toSearch) {
        if(edtSearch.getText().length() == 0) {
            edtSearch.setText(toSearch);
        }

        Word searchedWord = new Word(toSearch, "Loading...");
        url = definitionEntries(toSearch, mDeviceLanguage);
        new GoogleDefinition(searchedWord).execute(url);

        if(mListSearched.size() != 0) {
            mListSearched.clear();
        }

        mListSearched.add(searchedWord);

        // clears the listview to show new searched item
        adapter.clear();
        mListWords.addAll(mListSearched);
    }


    private void correctLanguageCode(String language) {
        if(language.equals("fr_fr")) {
            mDeviceLanguage = "fr";
        }
    }

    private String definitionEntries(String word, String language) {
        final String word_id = word.toLowerCase();
        return "https://api.dictionaryapi.dev/api/v2/entries/" + language + "/" + word;
    }



    /**
     * Async for refreshing definitions of the page */
    private class GoogleDefinition extends AsyncTask<String, Integer, String> {
        private Word word;

        public GoogleDefinition(Word wordPre) {
            word = wordPre;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.connect();

                // read the output from the server
                InputStream stream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();
            }
            catch (IOException e) {
                e.printStackTrace();
                edtSearch.setText(""); // TODO: 16/06/2021 Reset listview if no words found

                System.out.println("ERROR IN CONNECTION ...");
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String title;
            String definition;
            String examples;
            String synonyms;

            try {
//                System.out.println(result);
                // TODO: 16/06/2021 GET ALL WORDS AND EXAMPLES with ORDERED List
                JSONArray jsonArrayRoot = new JSONArray(result);
                JSONObject jsonObjectWord = jsonArrayRoot.getJSONObject(0);

                JSONArray jsonArrayMeanings = jsonObjectWord.getJSONArray("meanings");
                System.out.println("LENGTH : " + jsonArrayMeanings.length());
                JSONObject jsonObjectMeanings = jsonArrayMeanings.getJSONObject(0);

                JSONArray jsonArrayDefinitions = jsonObjectMeanings.getJSONArray("definitions");
                JSONObject jsonObjectDefinitions = jsonArrayDefinitions.getJSONObject(0);
                definition = jsonObjectDefinitions.getString("definition");
                word.setDefinition(definition);

                JSONObject jsonObjectExample = jsonArrayDefinitions.getJSONObject(0);
                examples = jsonObjectDefinitions.getString("example");
                word.setExample(examples);



                System.out.println("EXAMPLES : " + examples);
            } catch (Exception e) {

            }
            adapter.notifyDataSetChanged();
        }
    }
}


