package com.example.persistencedemo.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// instruction for Room: 3 parameters
// entities = {Task.class} -> the DB has an entity Task which describs the table tasks
// version = 1 -> first version of the DB structure
// exportSchema = true -> Room exports the DB structure description when the project builds

@Database(entities = {Task.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE; // SINGLETON the app uses the same instance everywhere

    //method that returns taskDao (realisation by Room), allows us to call methods
    //dao.insert(task);
    //dao.update(task);
    //dao.delete(task);
    public abstract TaskDao taskDao();

    public static AppDatabase getInstance(Context context) { //Android context
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) { // synchronized doesn't let two threads 
            // create two different instances
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),  // Room object creation
                            AppDatabase.class, "tasks.db").build(); //the SQLite file is first created 
                            // when the DB is first addressed
                }
            }
        }
        return INSTANCE; 
    }
}
