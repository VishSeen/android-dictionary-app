package com.vish.apps.dictionary.util;

public class Word {

    private static int mId = 0;
    private String mTitle;
    private String mDefinition;

    public Word (String title, String definition) {
        mId =+ 1;
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
