package com.vish.apps.dictionary.util;

public class Word {

    private static int mId = 0;
    private String mTitle;
    private String mDefinition;
    private String mExample;

    public Word () {
        mId =+ 1;
    }

    public Word (String title) {
        mTitle = title;
    }

    public Word (String title, String definition) {
        mTitle = title;
        mDefinition = definition;
    }

    public Word (String title, String definition, String example) {
        mTitle = title;
        mDefinition = definition;
        mExample = example;
    }

    public String getTitle() {
        return mTitle;
    }
    public String getDefinition() {
        return mDefinition;
    }
    public String getExample() {
        return mExample;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
    public void setDefinition(String definition) {
        mDefinition = definition;
    }
    public void setExample(String example) {
        mExample = example;
    }
}
