package com.example.taskmanagementapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanagementapplication.TaskManagementDb.TaskDAO;
import com.example.taskmanagementapplication.TaskManagementDb.TaskManagementDataBase;
import com.example.taskmanagementapplication.databinding.ActivityHomeBinding;


public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    Button logout_btn;
    Button tasks_btn;
    Button add_task_btn;
    Button completed_tasks_btn;
    Button profile_btn;

    TextView welcomeUserTextView;
    private TaskDAO taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        logout_btn = binding.logOutBtn;
        tasks_btn = binding.myTasksBtn;
        add_task_btn = binding.addTaskBtn;
        completed_tasks_btn = binding.completedTasksBtn;
        profile_btn = binding.profileBtn;
        welcomeUserTextView = binding.welcomeUser;

        taskDao = Room.databaseBuilder(this, TaskManagementDataBase.class, TaskManagementDataBase.DATABASE_NAME).allowMainThreadQueries().build().taskDAO();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String username = preferences.getString("username", "");
        String welcomeMsg = getString(R.string.welcome_message, username);
        welcomeUserTextView.setText(welcomeMsg);

        add_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddTaskActivity.class);
                startActivity(intent);
                finish();
            }
        });



        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                editor = preferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tasks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userIdStr = preferences.getString("id", "");
                int userIdInt = Integer.parseInt(userIdStr);

                int userTaskCount = taskDao.userTasksCount(userIdInt);

                if(userTaskCount > 0) {
                    Intent intent = new Intent(HomeActivity.this, TasksActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(HomeActivity.this, "You have no Tasks on file !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        completed_tasks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userIdStr = preferences.getString("id", "");
                int userIdInt = Integer.parseInt(userIdStr);

                int userTaskCount = taskDao.userCompletedTasksCount(userIdInt);

                if(userTaskCount > 0) {
                    Intent intent = new Intent(HomeActivity.this, CompletedTasksActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(HomeActivity.this, "You have no Completed Tasks on file !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}