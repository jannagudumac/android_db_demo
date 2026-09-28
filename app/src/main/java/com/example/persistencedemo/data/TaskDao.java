package com.example.persistencedemo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    LiveData<List<Task>> observeAll();

    @Insert
    long insert(Task task);

    @Update
    int update(Task task);

    @Delete
    int delete(Task task);
}
