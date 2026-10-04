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
    // Read
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    LiveData<List<Task>> observeAll();

    // LiveData is an observable object that contains the list of all tasks
    // If an Activity follows this LiveData (.observe) it finds out automatically 
    // if there has been a change in the task list
//---------------------------------------------------------
//     In MainActivity: 

//     viewModel.getTasks().observe(this, tasks -> {
//     adapter.submitList(tasks);
//     updateCounter(tasks); - checks tasks.size() and those where task.completed == true
//     });  --> when you get the list of tasks, show it and update the counter

// Room saves the task in SQLite

//------------------------------------------------
    @Insert
    long insert(Task task);

    @Update
    int update(Task task);

    @Delete
    int delete(Task task);
}
