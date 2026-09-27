package com.example.persistencedemo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String title;

    @ColumnInfo(defaultValue = "0")
    public boolean completed;

    public Task(@NonNull String title) {
        this.title = title;
    }
}
