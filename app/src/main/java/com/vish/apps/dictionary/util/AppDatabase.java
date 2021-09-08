package com.vish.apps.dictionary.util;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = Creole.class, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DatabaseAccessInterface appDatabaseObject();
}
