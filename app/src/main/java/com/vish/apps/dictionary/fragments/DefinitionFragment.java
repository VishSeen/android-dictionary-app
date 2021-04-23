package com.vish.apps.dictionary.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class DefinitionFragment extends Fragment {

    public static DefinitionFragment newInstance(int columnCount) {
        DefinitionFragment fragment = new DefinitionFragment();
        return fragment;
    }


    private EditText edtSearch;
    private DefinitionsListAdapter adapter;
    private List<Word> mListWords;
    private String url;

    private Resources mResources;
    private String[] predefinedWords;
//    private String[] predefinedWords= {"Ant", "Aunt", "Baby", "Banana", "Car", "Cat", "Dirty", "Dog", "Doom", "Eat", "Ear", "Exclude", "Hello", "In", "Inner", "Kangaroo", "Kart", "Joke", "Journey", "June", "Lake", "Lobster", "Long"};
    private ListView listView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DefinitionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListWords = new ArrayList<>();
        mResources = getResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_definition, container, false);

        predefinedWords = mResources.getStringArray(R.array.default_words);

        edtSearch = view.findViewById(R.id.act_main_edt_search);
        adapter = new DefinitionsListAdapter(getContext(), mListWords);
        listView = view.findViewById(R.id.frag_definition_listview);
        listView.setAdapter(adapter);

        // loops through array of predefined words to find
        for (int i = 0; i < predefinedWords.length; i++){
            Word current = new Word(predefinedWords[i], "Loading...");
            url = definitionEntries(predefinedWords[i], "en-gb");
            new Oxford(current).execute(url);

            mListWords.add(current);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word currentWord = (Word) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), DefinitionActivity.class);
                intent.putExtra("Title", currentWord.getTitle());
                intent.putExtra("Definition", currentWord.getDefinition());
                startActivity(intent);
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


        return view;
    }

    public void searchWordClick(View v) {
        getSearchText();
    }

    public void getSearchText() {
//        String edtText = edtSearch.getText().toString();
//
//        Word searchedWord = new Word(01, edtText, "Loading...");
//        url = definitionEntries(edtText);
//        Oxford oxford = new Oxford();
//        oxford.execute(url);
//
//        Intent intent = new Intent(this, DefinitionActivity.class);
//        intent.putExtra("Title", searchedWord.getTitle());
//        intent.putExtra("Definition", searchedWord.getDefinition());
//        startActivity(intent);
    }

    private String definitionEntries(String wordSearch, String language) {
        final String word = wordSearch;
        final String fields = "definitions"; // can add etymologies or nouns here
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();

        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }

    private String urlDefinition(String wordSearch, String language) {
        final String word = wordSearch;
        final String fields = "definitions"; // can add etymologies or nouns here
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();

        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }
}