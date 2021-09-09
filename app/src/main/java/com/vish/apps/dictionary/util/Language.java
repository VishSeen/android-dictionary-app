package com.vish.apps.dictionary.util;

import android.widget.Toast;

import com.vish.apps.dictionary.R;

public class Language {

    private String mLanguage;
    private int mPos;

    public Language(int position) {
        mPos = position;
    }

    public String getLanguage() {
        switch (mPos) {
            case 0:
//                Toast.makeText(getActivity(), getResources().getString(R.string.frag_definition_spinner_error), Toast.LENGTH_SHORT).show();
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

        return mLanguage;
    }
}
