package com.vish.apps.dictionary.util;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "creole")
public class Creole {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String word;
    @ColumnInfo(name = "definition")
    private String definition;
    @ColumnInfo(name = "definition_en")
    private String definitionEn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDefinitionEn() {
        return definitionEn;
    }

    public void setDefinitionEn(String definitionEn) {
        this.definitionEn = definitionEn;
    }
}
