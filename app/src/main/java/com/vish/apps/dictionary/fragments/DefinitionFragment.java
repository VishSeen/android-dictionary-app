package com.vish.apps.dictionary.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        Word wordCat = new Word("03", "Cat", "a small domesticated carnivore, Felis domestica or F. catus, bred in a number of varieties.");
//        Word wordDog = new Word("01", "Dog", "a domesticated canid, Canis familiaris, bred in many varieties.");
//        Word wordHello = new Word("02", "Hello", "used as a greeting or to begin a phone conversation.");
//        Word wordMan = new Word("04", "Man", "a member of the species Homo sapiens or all the members of this species collectively, without regard to sex");
//        Word wordWoman = new Word("05","Woman", "a member of the species Homo sapiens or all the members of this species collectively, without regard to sex");
//        Word wordBaby = new Word("06","Baby", "an infant or very young child.");
//        Word wordHouse = new Word("07", "House", "a building in which people live; residence for human beings.");
//        Word wordCar = new Word("12","Car", "a vehicle running on rails, as a streetcar or railroad car.");
//        Word wordFood = new Word("11","Food", "any nourishing substance that is eaten, drunk, or otherwise taken into the body to sustain life, provide energy, promote growth, etc.");
//        Word wordEarth = new Word("10", "Earth", "the inhabitants of this planet, especially the human inhabitants");
//        Word wordZebra = new Word("13", "Zebra", "any of several horse like African mammals of the genus Equus, each species having a characteristic pattern of black or dark-brown stripes on a whitish background: all zebra species are threatened or endangered.");

//        listWords.add(wordBaby);
//        listWords.add(wordCar);
        listWords.add(wordCat);
//        listWords.add(wordDog);
//        listWords.add(wordEarth);
//        listWords.add(wordFood);
//        listWords.add(wordHello);
//        listWords.add(wordHouse);
//        listWords.add(wordMan);
//        listWords.add(wordWoman);
//        listWords.add(wordZebra);
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

        // on scroll event for scroll view
//        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                if(dy > 0){
//                    mFab.animate().translationY(mFab.getHeight() + getResources().getDimension(R.dimen.fab_margin)).setInterpolator(new LinearInterpolator()).setDuration(200);
//                } else {
//                    mFab.animate().translationY(0).setInterpolator(new LinearInterpolator()).setDuration(200);
//                }
//
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });

        return view;
    }

    private String definitionEntries() {
        final String language = "en-gb";
        final String word = "Hollow";
        final String fields = "definitions"; // can add etymologies or nouns here
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();

        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }

    public void sendRequestOnClick(View v) {
        OxfordRequests oxfordRequests = new OxfordRequests();
        url = definitionEntries();
        oxfordRequests.execute(url);
    }
}