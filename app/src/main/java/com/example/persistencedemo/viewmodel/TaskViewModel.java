package com.example.persistencedemo.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.persistencedemo.data.Task;
import com.example.persistencedemo.data.TaskRepository;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public LiveData<List<Task>> getTasks() {
        return repository.getTasks();
    }

    public void addTask(String title) {
        String trimmed = title.trim();
        if (!trimmed.isEmpty()) repository.insert(new Task(trimmed));
    }

    public void updateTask(Task task) {
        Task updated = task.copy();
        updated.title = updated.title.trim();
        if (!updated.title.isEmpty()) repository.update(updated);
    }

    public void deleteTask(Task task) {
        repository.delete(task);
    }
}
