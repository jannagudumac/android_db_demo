package com.example.persistencedemo.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @NonNull
    public String title;
    public boolean completed;

    public Task(@NonNull String title) {
        this.title = title;
        this.completed = false;
    }

    // copy() creates a new Task object before an update, 
    // so DiffUtil can correctly compare the old and new versions.
    public Task copy() {
        Task copy = new Task(title);
        copy.id = id;
        copy.completed = completed;
        return copy;
    }
}
