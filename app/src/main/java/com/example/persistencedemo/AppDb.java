package com.example.persistencedemo;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class}, version = 2, exportSchema = true)
public abstract class AppDb extends RoomDatabase {
    public abstract TaskDao taskDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE tasks ADD COLUMN " +
                    "completed INTEGER NOT NULL DEFAULT 0");
        }
    };
}
