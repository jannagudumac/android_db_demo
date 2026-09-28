package com.example.persistencedemo.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {
    // One queue for the process: writes survive Activity recreation and stay ordered.
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final TaskDao taskDao;
    private final LiveData<List<Task>> tasks;

    public TaskRepository(Application application) {
        taskDao = AppDatabase.getInstance(application).taskDao();
        tasks = taskDao.observeAll();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void insert(Task task) {
        executor.execute(() -> taskDao.insert(task));
    }

    public void update(Task task) {
        executor.execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        executor.execute(() -> taskDao.delete(task));
    }
}
