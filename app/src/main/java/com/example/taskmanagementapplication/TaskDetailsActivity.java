package com.example.taskmanagementapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.taskmanagementapplication.databinding.ActivityTaskDetailsBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskDetailsActivity extends AppCompatActivity {
    private ActivityTaskDetailsBinding binding;

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView priorityTextView;
    private TextView categoryTextView;
    private TextView dueDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTaskDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        titleTextView = binding.taskTitle;
        descriptionTextView = binding.taskDescription;
        priorityTextView = binding.taskPriority;
        categoryTextView = binding.taskCategory;
        dueDateTextView = binding.taskDueDate;
        CalendarView calendarView = binding.calendarView;

        Intent intent = getIntent();
        String title = intent.getStringExtra("task_title");
        String description = intent.getStringExtra("task_description");
        int priority = intent.getIntExtra("task_priority", 0);
        String category = intent.getStringExtra("task_category");
        final long[] dueDateMillis = {intent.getLongExtra("task_due_date", 0)};

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        priorityTextView.setText(String.format(Locale.getDefault(), "Priority: %d", priority));
        categoryTextView.setText(category);
        dueDateTextView.setText(String.format(Locale.getDefault(), "Complete by: %s", new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date(dueDateMillis[0]))));

        calendarView.setDate(dueDateMillis[0]);

        // Add an OnDateChangeListener to the calendar view to respond to changes in the selected date
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                dueDateMillis[0] = calendar.getTimeInMillis();
                dueDateTextView.setText(String.format(Locale.getDefault(), "Complete by: %s", new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date(dueDateMillis[0]))));
            }
        });
    }
}
