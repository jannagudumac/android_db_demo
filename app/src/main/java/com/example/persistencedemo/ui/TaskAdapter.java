package com.example.persistencedemo.ui;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.persistencedemo.R;
import com.example.persistencedemo.data.Task;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskHolder> {
    public interface Listener {
        void onCompletedChanged(Task task, boolean completed);
        void onEdit(Task task);
        void onDelete(Task task);
    }

    private final Listener listener;
    private static final DiffUtil.ItemCallback<Task> DIFF = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldTask, @NonNull Task newTask) {
            return oldTask.id == newTask.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldTask, @NonNull Task newTask) {
            return oldTask.title.equals(newTask.title) && oldTask.completed == newTask.completed;
        }
    };

    public TaskAdapter(Listener listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = getItem(position);
        holder.completed.setOnCheckedChangeListener(null);
        holder.completed.setChecked(task.completed);
        holder.completed.setContentDescription(holder.itemView.getContext()
                .getString(R.string.complete_task, task.title));
        holder.title.setText(task.title);
        holder.title.setPaintFlags(task.completed
                ? holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                : holder.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        holder.completed.setOnCheckedChangeListener((button, checked) ->
                listener.onCompletedChanged(task, checked));
        holder.edit.setContentDescription(holder.itemView.getContext()
                .getString(R.string.edit_task_description, task.title));
        holder.delete.setContentDescription(holder.itemView.getContext()
                .getString(R.string.delete_task_description, task.title));
        holder.edit.setOnClickListener(v -> listener.onEdit(task));
        holder.delete.setOnClickListener(v -> listener.onDelete(task));
    }

    static class TaskHolder extends RecyclerView.ViewHolder {
        final CheckBox completed;
        final TextView title;
        final View edit;
        final View delete;

        TaskHolder(View view) {
            super(view);
            completed = view.findViewById(R.id.taskCompleted);
            title = view.findViewById(R.id.taskTitle);
            edit = view.findViewById(R.id.editTask);
            delete = view.findViewById(R.id.deleteTask);
        }
    }
}
