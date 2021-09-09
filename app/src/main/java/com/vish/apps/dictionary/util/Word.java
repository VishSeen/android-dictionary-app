package com.vish.apps.dictionary.util;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "creole_sec")
public class Word {

    @PrimaryKey(autoGenerate = true)
    private static int mId = 0;
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "definition")
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
