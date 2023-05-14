package com.example.taskmanagementapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.example.taskmanagementapplication.TaskManagementDb.TaskDAO;
import com.example.taskmanagementapplication.TaskManagementDb.TaskManagementDataBase;
import com.example.taskmanagementapplication.TaskManagementDb.UserDAO;
import com.example.taskmanagementapplication.databinding.ActivityAddTaskBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private ActivityAddTaskBinding binding;

    private Calendar calendar;

    SharedPreferences preferences;
    EditText editTextTaskDueDate;
    Button buttonAddTask;

    UserDAO userDAO;
    TaskDAO taskDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editTextTaskDueDate = binding.editTextTaskDueDate;
        buttonAddTask = binding.buttonAddTask;

        userDAO = (UserDAO) Room.databaseBuilder(this, TaskManagementDataBase.class, TaskManagementDataBase.DATABASE_NAME).allowMainThreadQueries().build().userDAO();

        taskDAO = Room.databaseBuilder(this, TaskManagementDataBase.class, TaskManagementDataBase.DATABASE_NAME).allowMainThreadQueries().build().taskDAO();

        editTextTaskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });
    }

    private void showDatePickerDialog() {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                date,
                year,
                month,
                day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void addTask() {
        String taskTitle = binding.editTextTaskTitle.getText().toString().trim();
        String taskDescription = binding.editTextTaskDescription.getText().toString().trim();
        String taskPriority = binding.editTextTaskPriority.getText().toString().trim();
        String taskCategory = binding.editTextTaskCategory.getText().toString().trim();
        String taskDueDateString = binding.editTextTaskDueDate.getText().toString().trim();

        if (taskTitle.isEmpty()) {
            binding.editTextTaskTitle.setError("Task title required");
            binding.editTextTaskTitle.requestFocus();
            return;
        }

        if (taskDescription.isEmpty()) {
            binding.editTextTaskDescription.setError("Task description required");
            binding.editTextTaskDescription.requestFocus();
            return;
        }

        if (taskPriority.isEmpty()) {
            binding.editTextTaskPriority.setError("Task priority required");
            binding.editTextTaskPriority.requestFocus();
            return;
        }

        if (taskCategory.isEmpty()) {
            binding.editTextTaskCategory.setError("Task category required");
            binding.editTextTaskCategory.requestFocus();
            return;
        }

        if (taskDueDateString.isEmpty()) {
            binding.editTextTaskDueDate.setError("Task due date required");
            binding.editTextTaskDueDate.requestFocus();
            return;
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString("username", "");

        Log.d("AddTaskActivity", "Username: " + username);

        User user = userDAO.isUser(username);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date taskDueDate = null;

        try {
            taskDueDate = dateFormat.parse(taskDueDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Task(String title, String description, int userId, int taskPriority, String category, Date dueDate)
        Log.d("AddTaskActivity", "Title: " + taskTitle + "\n" +
                "Description: " + taskDescription + "\n" +
                "User Id: #" + user.getUserId() + "\n" +
                "Priority: " + taskPriority + "\n" +
                "Category: " + taskCategory + "\n" +
                "Due Date: " + taskDueDate);

        Task task = new Task(taskTitle, taskDescription, user.getUserId(), Integer.parseInt(taskPriority), taskCategory, taskDueDate);

        Log.d("AddTaskActivity", task.toString());

        taskDAO.insertTask(task);

        Toast.makeText(this, "Task added", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(AddTaskActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            binding.editTextTaskDueDate.setText(dateFormatter.format(calendar.getTime()));
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}