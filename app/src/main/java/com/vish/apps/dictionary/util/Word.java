package com.vish.apps.dictionary.util;

public class Word {

    private int mId;
    private String mTitle;
    private String mDefinition;


    public Word(String title) {
        mTitle = title;
    }

    public Word(int id, String title, String definition) {
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

    public void setTitle(String title) {
        mTitle = title;
    }
    public void setDefinition(String definition) {
        mDefinition = definition;
    }
}
