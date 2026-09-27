package com.example.persistencedemo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    LiveData<List<Task>> observeAll();

    @Insert
    long insert(Task task);

    @Query("UPDATE tasks SET completed = :done WHERE id = :id")
    void setDone(long id, boolean done);
}
