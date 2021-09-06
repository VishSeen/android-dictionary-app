package com.vish.apps.dictionary.fragments;

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

import com.vish.apps.dictionary.DefinitionActivity;
import com.vish.apps.dictionary.R;
import com.vish.apps.dictionary.adapters.DefinitionsListAdapter;
import com.vish.apps.dictionary.util.VoiceResultListener;
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
public class DefinitionFragment extends Fragment implements VoiceResultListener {

    private String mLanguage;
    private String mDeviceLanguage = Locale.getDefault().toString();

    private EditText edtSearch;
    private Spinner spinnerLanguage;
    private DefinitionsListAdapter adapter;
    private String[] predefinedWords;
    private List<Word> mListWords;
    private String url;

    private Resources mResources;

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

        Toast.makeText(getActivity(), "On create", Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_definition, container, false);


        // change language code to match api response
        correctLanguageCode(mDeviceLanguage);

        edtSearch = view.findViewById(R.id.act_main_edt_search);
        spinnerLanguage = view.findViewById(R.id.frag_translation_spinner_change_language);
        adapter = new DefinitionsListAdapter(getContext(), mListWords);
        ListView listView = view.findViewById(R.id.frag_definition_listview);
        listView.setAdapter(adapter);


        loadWords(mLanguage); // loops through array of predefined words
        setSpinnerLanguage(spinnerLanguage, mLanguage); // set spinner to correct language

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word currentWord = (Word) adapterView.getItemAtPosition(i);


                Intent intent = new Intent(getActivity(), DefinitionActivity.class);
                intent.putExtra("Title", currentWord.getTitle());
                intent.putExtra("Definition", currentWord.getDefinition());
                intent.putExtra("Examples", currentWord.getExample());
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

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getActivity(), getResources().getString(R.string.frag_definition_spinner_error), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        mLanguage = "en_US";
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
                        mLanguage = "cr";
                        break;
                }

                loadWords(mLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
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
        url = definitionEntries(toSearch, mLanguage);
        new GoogleDefinition(searchedWord).execute(url);

        if(mListSearched.size() != 0) {
            mListSearched.clear();
        }

        mListSearched.add(searchedWord);

        // clears the listview to show new searched item
        adapter.clear();
        mListWords.addAll(mListSearched);
    }



    /**
     * Load the words and add to list depending on the
     * language selected on the spinner.
     * */
    // TODO: 23/06/2021 clear listview and refresh page on language change
    private void loadWords(String language) {
        // clear the list and adapter
        if(mListWords.size() != 0) {
            mListWords.clear();
            adapter.clear();
        }

        // check the language and loop through words
        if(!(language.equalsIgnoreCase("cr"))) {
            predefinedWords = mResources.getStringArray(R.array.default_words);

            for (String predefinedWord : predefinedWords) {
                Word word = new Word(predefinedWord, getResources().getString(R.string.frag_definition_txt_definition_loading));
                url = definitionEntries(predefinedWord, mLanguage);
                new GoogleDefinition(word).execute(url);

                mListDefinitions.add(word);
            }
        } else {

        }

        // add to list
        mListWords.addAll(mListDefinitions);
        mListDefinitions.clear();
    }



    @Override
    public void onVoiceResult(String voiceText) {
        searchWord(voiceText);
    }

    /**
     * Pass device language code to process
     * and change to the api language code
     * @param language : pass device language
     */
    private void correctLanguageCode(String language) {
        mLanguage = language.substring(0, 2);
        
        if(mLanguage.equals("en")) {
            mLanguage = "en_US";
        }
    }


    private void setSpinnerLanguage(Spinner spin, String language) {
        switch (language) {
            case "en_US":
                spin.setSelection(1);
                break;
            case "fr":
                spin.setSelection(2);
                break;
            case "es":
                spin.setSelection(3);
                break;
            case "de":
                spin.setSelection(4);
                break;
            case "it":
                spin.setSelection(5);
                break;
            case "cr":
                spin.setSelection(6);
                break;
        }
    }




    /**
     * Async for refreshing definitions of the page */
    private String definitionEntries(String word, String language) {
        return "https://api.dictionaryapi.dev/api/v2/entries/" + language + "/" + word;
    }

    private class GoogleDefinition extends AsyncTask<String, Integer, String> {
        private final Word word;

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

                StringBuilder buffer = new StringBuilder();
                String line;

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
            String definition;
            String examples;

            try {
//                System.out.println(result);
                // TODO: 16/06/2021 GET ALL WORDS AND EXAMPLES with ORDERED List
                JSONArray jsonArrayRoot = new JSONArray(result);
                JSONObject jsonObjectWord = jsonArrayRoot.getJSONObject(0);

                JSONArray jsonArrayMeanings = jsonObjectWord.getJSONArray("meanings");
                JSONObject jsonObjectMeanings = jsonArrayMeanings.getJSONObject(0);

                JSONArray jsonArrayDefinitions = jsonObjectMeanings.getJSONArray("definitions");
                JSONObject jsonObjectDefinitions = jsonArrayDefinitions.getJSONObject(0);
                definition = jsonObjectDefinitions.getString("definition");
                word.setDefinition(definition);

                examples = jsonObjectDefinitions.getString("example");
                word.setExample(examples);
            } catch (Exception e) {

            }
            adapter.notifyDataSetChanged();
        }
    }
}


