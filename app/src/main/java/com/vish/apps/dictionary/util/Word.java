package com.vish.apps.dictionary.util;

public class Word {

    private static int mId = 0;
    private String mTitle;
    private String mDefinition;
    private String mExample;
    private String mSynonyms;

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

    public Word (String title, String definition, String example, String synonyms) {
        mTitle = title;
        mDefinition = definition;
        mExample = example;
        mSynonyms = synonyms;
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
    public String getSynonyms() {
        return mSynonyms;
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
    public void setSynonyms(String synonyms) {
        mSynonyms = synonyms;
    }
}
