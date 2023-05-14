package com.example.taskmanagementapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;


import com.example.taskmanagementapplication.TaskManagementDb.TaskDAO;
import com.example.taskmanagementapplication.TaskManagementDb.TaskManagementDataBase;
import com.example.taskmanagementapplication.databinding.ActivityTasksBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TasksActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private ActivityTasksBinding binding;

    private TaskDAO taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTasksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        taskDao = Room.databaseBuilder(this, TaskManagementDataBase.class, TaskManagementDataBase.DATABASE_NAME).allowMainThreadQueries().build().taskDAO();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userIdStr = preferences.getString("id", "");
        int userIdInt = Integer.parseInt(userIdStr);

        List<Task> taskList = taskDao.userTasks(userIdInt);


        RecyclerView taskRecyclerView = binding.taskRecyclerView;
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TaskAdapter taskAdapter = new TaskAdapter(taskList, this);
        taskRecyclerView.setAdapter(taskAdapter);
        // -----------------------------------------------------
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.LEFT) {
                    int position = viewHolder.getBindingAdapterPosition();
                    Task task = taskAdapter.getTask(position);

                    taskDao.deleteTask(task);
                    taskAdapter.removeTask(position);
                    taskAdapter.notifyItemRemoved(position);

                    Snackbar.make(taskRecyclerView, "Deleted: " + task.getTitle(), Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    taskList.add(position, task);
                                    taskAdapter.notifyItemInserted(position);
                                }
                            }).show();

                } else if (direction == ItemTouchHelper.RIGHT) {
                    int position = viewHolder.getBindingAdapterPosition();
                    Task task = taskAdapter.getTask(position);
                    task.setTaskCompleted(true);

                    taskDao.updateTask(task);
                    taskAdapter.removeTask(position);
                    taskAdapter.notifyItemRemoved(position);

                    Snackbar.make(taskRecyclerView, "Archived: " + task.getTitle(), Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    taskList.add(position, task);
                                    taskAdapter.notifyItemInserted(position);
                                }
                            }).show();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(TasksActivity.this, R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(TasksActivity.this, R.color.green))
                        .addSwipeRightActionIcon(R.drawable.baseline_archive_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
