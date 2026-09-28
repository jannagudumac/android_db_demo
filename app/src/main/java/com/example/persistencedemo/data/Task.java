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

    // Keep the old ListAdapter snapshot intact so DiffUtil can compare it.
    public Task copy() {
        Task copy = new Task(title);
        copy.id = id;
        copy.completed = completed;
        return copy;
    }
}
