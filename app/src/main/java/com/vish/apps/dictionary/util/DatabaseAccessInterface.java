package com.vish.apps.dictionary.util;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DatabaseAccessInterface {
    @Insert
    public void addWord(Creole creole);

    @Query("SELECT * FROM creole")
    public List<Word> getWords();

    @Query("SELECT title, definition FROM creole WHERE title == :title")
    public Word getCreoleWord(String title);
}
