package com.vish.apps.dictionary.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vish.apps.dictionary.DefinitionActivity;
import com.vish.apps.dictionary.MainActivity;
import com.vish.apps.dictionary.R;
import com.vish.apps.dictionary.adapters.DefinitionsListAdapter;
import com.vish.apps.dictionary.util.OxfordRequests;
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

//    private DictionaryListAdapter dictionaryListAdapter;
    private List<Word> listWords;
    public String url;
    public String[] predefinedWords= {"Ant", "Aunt", "Baby", "Banana", "Car", "Cat", "Dirty", "Dog", "Doom", "Eat", "Ear", "Exclude", "Hello", "In", "Inner", "Kangaroo", "Kart", "Joke", "Journey", "June", "Lake", "Lobster", "Long"};

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DefinitionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listWords = new ArrayList<>();

        // loops through array of predefined words to find
        for (int i = 0; i < predefinedWords.length; i++){
            Word currentWord = new Word(i, predefinedWords[i], "Fetching definition...");

            url = definitionEntries(predefinedWords[i]);
            new OxfordRequests(currentWord).execute(url);

            listWords.add(currentWord);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_definition, container, false);
        View mFab = getActivity().findViewById(R.id.act_main_fab);

        DefinitionsListAdapter adapter = new DefinitionsListAdapter(getContext(), listWords);

        ListView listView = view.findViewById(R.id.frag_definition_listview);
        listView.setAdapter(adapter);

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
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    mFab.animate().translationY(mFab.getHeight() + getResources().getDimension(R.dimen.fab_margin)).setInterpolator(new LinearInterpolator()).setDuration(200);
                }else{
                    mFab.animate().translationY(0).setInterpolator(new LinearInterpolator()).setDuration(200);
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        adapter.notifyDataSetChanged();

        return view;
    }

    private String definitionEntries(String wordSearch) {
        final String language = "en-gb";
        final String word = wordSearch;
        final String fields = "definitions"; // can add etymologies or nouns here
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();

        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }
}