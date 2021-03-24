package com.vish.apps.dictionary.util;

public class Word {

    private String mId;
    private String mTitle;
    private String mDefinition;

    public Word(String id, String title, String definition) {
        mId = id;
        mTitle = title;
        mDefinition = definition;
    }

    public String getTitle() {
        return mTitle;
    }
    public String getDefinition() {
        return mDefinition;
    }

}
