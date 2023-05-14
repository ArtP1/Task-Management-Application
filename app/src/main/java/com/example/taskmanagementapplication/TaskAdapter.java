package com.example.taskmanagementapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/*
   Swipe Features from https://www.youtube.com/watch?v=rcSNkSJ624U

*/
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;

    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());
        holder.priorityTextView.setText(String.format(Locale.getDefault(), "Priority: %d", task.getTaskPriority()));
        holder.categoryTextView.setText(String.format(Locale.getDefault(), task.getCategory()));
        holder.dueDateTextView.setText(String.format(Locale.getDefault(), "Complete by: %s", new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(task.getDueDate())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskDetailsActivity.class);
                intent.putExtra("task_title", task.getTitle());
                intent.putExtra("task_description", task.getDescription());
                intent.putExtra("task_priority", task.getTaskPriority());
                intent.putExtra("task_category", task.getCategory());
                intent.putExtra("task_due_date", task.getDueDate().getTime());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public Task getTask(int position) {
        return taskList.get(position);
    }

    public void removeTask(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView descriptionTextView;
        TextView priorityTextView;
        TextView categoryTextView;
        TextView dueDateTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            priorityTextView = itemView.findViewById(R.id.priority_text_view);
            categoryTextView = itemView.findViewById(R.id.category_text_view);
            dueDateTextView = itemView.findViewById(R.id.due_date_text_view);
        }
    }
}