package com.vish.apps.dictionary.util;

public class Word {

    private static int mId = 0;
    private String mTitle;
    private String mDefinition;
    private String mEtymology;
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

    public Word (String title, String definition, String example, String synonyms, String etymology) {
        mTitle = title;
        mDefinition = definition;
        mExample = example;
        mSynonyms = synonyms;
        mEtymology = etymology;
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
    public String getEtymology() {
        return mEtymology;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
    public void setDefinition(String definition) {
        mDefinition = definition;
    }
    public String setExample() {
        return mExample;
    }
    public String setSynonyms() {
        return mSynonyms;
    }
    public void setEtymology(String etymology) {
        mEtymology = etymology;
    }
}
