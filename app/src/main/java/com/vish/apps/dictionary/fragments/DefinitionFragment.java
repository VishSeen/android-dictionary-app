package com.vish.apps.dictionary.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.vish.apps.dictionary.DefinitionActivity;
import com.vish.apps.dictionary.R;
import com.vish.apps.dictionary.adapters.DefinitionsListAdapter;
import com.vish.apps.dictionary.util.Oxford;
import com.vish.apps.dictionary.util.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * A fragment representing a list of Items.
 */
public class DefinitionFragment extends Fragment {

    private String deviceLanguage = Locale.getDefault().toLanguageTag().toLowerCase();

    private EditText edtSearch;
    private DefinitionsListAdapter adapter;
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
        if(deviceLanguage.equals("fr-fr")) {
            deviceLanguage = "fr";
        }

        System.out.println("DEVICE : " + deviceLanguage);

        String[] predefinedWords = mResources.getStringArray(R.array.default_words);

        edtSearch = view.findViewById(R.id.act_main_edt_search);
        adapter = new DefinitionsListAdapter(getContext(), mListWords);
        listView = view.findViewById(R.id.frag_definition_listview);
        listView.setAdapter(adapter);

        // loops through array of predefined words to find
        for (int i = 0; i < predefinedWords.length; i++){
            Word word = new Word(predefinedWords[i], getResources().getString(R.string.frag_definition_txt_definition_loading));
            url = definitionEntries(predefinedWords[i], deviceLanguage);
            new OxfordDefinition(word).execute(url);

            mListDefinitions.add(word);
        }

        mListWords.addAll(mListDefinitions);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word currentWord = (Word) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), DefinitionActivity.class);
                intent.putExtra("Title", currentWord.getTitle());
                intent.putExtra("Definition", currentWord.getDefinition());
                intent.putExtra("Etymology", currentWord.getEtymology());
                intent.putExtra("Example", currentWord.getExample());
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




    public void searchWordClick(View v) {
        String edtText = edtSearch.getText().toString();
        searchWord(edtText);
    }

    public void searchWord(String toSearch) {
        if(edtSearch.getText().length() == 0) {
            edtSearch.setText(toSearch);
        }

        Word searchedWord = new Word(toSearch, "Loading...");
        url = definitionEntries(toSearch, deviceLanguage);
        new OxfordDefinition(searchedWord).execute(url);

        if(mListSearched.size() != 0) {
            mListSearched.clear();
        }

        mListSearched.add(searchedWord);

        // clears the listview to show new searched item
        adapter.clear();
        mListWords.addAll(mListSearched);
    }




    private String definitionEntries(String wordSearch, String language) {
        final String word = wordSearch;
        final String fields = "definitions"; // can add etymologies or nouns here
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();

        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }



    private class OxfordDefinition extends AsyncTask<String, Integer, String> {
        private Word word;

        public OxfordDefinition(Word wordPre) {
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
            adapter.notifyDataSetChanged();
        }
    }
}


