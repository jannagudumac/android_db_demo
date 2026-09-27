package com.example.persistencedemo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.room.Room;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends ComponentActivity {
    private static final String TAG = "PersistenceDemo";
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private AppDb db;
    private EditText taskInput;
    private TextView statusText;
    private LinearLayout taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskInput = findViewById(R.id.taskInput);
        statusText = findViewById(R.id.statusText);
        taskList = findViewById(R.id.taskList);
        Button addButton = findViewById(R.id.addButton);

        db = Room.databaseBuilder(getApplicationContext(), AppDb.class, "tasks.db")
                .addMigrations(AppDb.MIGRATION_1_2)
                .build();

        db.taskDao().observeAll().observe(this, this::render);

        addButton.setOnClickListener(v -> {
            String title = taskInput.getText().toString().trim();
            if (title.isEmpty()) return;
            taskInput.setText("");
            io.execute(() -> {
                long id = db.taskDao().insert(new Task(title));
                Log.d(TAG, "inserted id=" + id + " title=" + title);
            });
        });
    }

    private void render(List<Task> tasks) {
        taskList.removeAllViews();
        statusText.setText(tasks.size() + " tâche(s) en base");
        Log.d(TAG, "observed rows=" + tasks.size());
        for (Task task : tasks) {
            CheckBox row = new CheckBox(this);
            row.setText("#" + task.id + "  " + task.title);
            row.setChecked(task.completed);
            row.setOnCheckedChangeListener((button, done) -> io.execute(() -> {
                db.taskDao().setDone(task.id, done);
                Log.d(TAG, "completed id=" + task.id + " value=" + done);
            }));
            taskList.addView(row);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        io.shutdown();
    }
}
