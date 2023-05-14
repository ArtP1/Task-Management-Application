package com.example.taskmanagementapplication.TaskManagementDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.taskmanagementapplication.DateConverter;
import com.example.taskmanagementapplication.Task;
import com.example.taskmanagementapplication.User;


@Database(entities = {Task.class, User.class}, version = 1)
@TypeConverters({DateConverter.class})

public abstract class TaskManagementDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "task_management_db";
    public static final String TASKS_TABLE = "tasks_table";
    public static final String USERS_TABLE = "users_table";

    private static volatile TaskManagementDataBase instance;
    private static final Object LOCK = new Object();

    public abstract TaskDAO taskDAO();

    public abstract UserDAO userDAO();

    public static TaskManagementDataBase getInstance(Context context) {
        if (instance == null){
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), TaskManagementDataBase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
