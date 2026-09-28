package com.example.persistencedemo.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.persistencedemo.R;
import com.example.persistencedemo.data.Task;
import com.example.persistencedemo.viewmodel.TaskViewModel;
import java.util.List;

public class MainActivity extends ComponentActivity implements TaskAdapter.Listener {
    private TaskViewModel viewModel;
    private EditText taskInput;
    private TextView counter;
    private TextView emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View root = findViewById(R.id.root);
        int padding = root.getPaddingLeft();
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            view.setPadding(padding + insets.left, padding + insets.top,
                    padding + insets.right, padding + insets.bottom);
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(root);

        taskInput = findViewById(R.id.taskInput);
        counter = findViewById(R.id.counter);
        emptyState = findViewById(R.id.emptyState);
        RecyclerView list = findViewById(R.id.taskList);
        TaskAdapter adapter = new TaskAdapter(this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        viewModel.getTasks().observe(this, tasks -> {
            adapter.submitList(tasks);
            updateCounter(tasks);
            emptyState.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
        });

        findViewById(R.id.addButton).setOnClickListener(v -> {
            String title = taskInput.getText().toString().trim();
            if (title.isEmpty()) {
                taskInput.setError(getString(R.string.empty_title));
                return;
            }
            viewModel.addTask(title);
            taskInput.setText("");
        });
    }

    private void updateCounter(List<Task> tasks) {
        int completed = 0;
        for (Task task : tasks) if (task.completed) completed++;
        String total = getResources().getQuantityString(R.plurals.task_count, tasks.size() == 1 ? 1 : 2, tasks.size());
        counter.setText(tasks.isEmpty() ? total : getString(R.string.counter_format, total,
                getResources().getQuantityString(R.plurals.completed_count, completed, completed)));
    }

    @Override
    public void onCompletedChanged(Task task, boolean completed) {
        Task updated = task.copy();
        updated.completed = completed;
        viewModel.updateTask(updated);
    }

    @Override
    public void onEdit(Task task) {
        View content = getLayoutInflater().inflate(R.layout.dialog_edit_task, null);
        EditText input = content.findViewById(R.id.editTitle);
        input.setText(task.title);
        input.setSelection(input.length());
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.edit_task)
                .setView(content)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, null)
                .create();
        dialog.setOnShowListener(ignored -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String title = input.getText().toString().trim();
                    if (title.isEmpty()) {
                        input.setError(getString(R.string.empty_title));
                        return;
                    }
                    Task updated = task.copy();
                    updated.title = title;
                    viewModel.updateTask(updated);
                    dialog.dismiss();
                }));
        dialog.show();
    }

    @Override
    public void onDelete(Task task) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirmation)
                .setMessage(task.title)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> viewModel.deleteTask(task))
                .show();
    }
}
