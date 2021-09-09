package com.vish.apps.dictionary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vish.apps.dictionary.R;
import com.vish.apps.dictionary.util.Word;

import java.util.ArrayList;
import java.util.List;

public class DefinitionsListAdapter extends ArrayAdapter<Word> {
    private final Context mContext;
    private List<Word> mListWords;

    public DefinitionsListAdapter(Context context, List<Word> listWords) {
        super(context, -1, listWords);
        mContext = context;
        mListWords = listWords;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.list_dictionary, parent, false);

        TextView txtTitle = (TextView) rootView.findViewById(R.id.list_all_words_txt_title);
        TextView txtDefinition = (TextView) rootView.findViewById(R.id.list_all_words_txt_definition);

        txtTitle.setText(mListWords.get(position).getTitle());
        txtDefinition.setText(mListWords.get(position).getDefinition());

        return rootView;
    }

    public List<Word> getItemList() {
        return mListWords;
    }

    public void setItemList(List<Word> listWords) {
        mListWords = listWords;
    }
}
