package com.example.taskmanagementapplication.TaskManagementDb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmanagementapplication.Task;

import java.util.List;


@Dao
public interface TaskDAO {

    @Insert
    void insertTask(Task... tasks);

    @Update
    void updateTask(Task... tasks);

    @Delete
    void deleteTask(Task... tasks);

    @Query("SELECT * FROM " + TaskManagementDataBase.TASKS_TABLE + " WHERE userId = :id")
    List<Task> userTasks(int id);

    @Query("SELECT * FROM " + TaskManagementDataBase.TASKS_TABLE + " WHERE userId = :id AND isTaskCompleted = 1 ORDER BY isTaskCompleted DESC")
    List<Task> userCompletedTasks(int id);

    @Query("SELECT COUNT(*) FROM " + TaskManagementDataBase.TASKS_TABLE + " WHERE userId = :id")
    int userTasksCount(int id);

    @Query("SELECT COUNT(*) FROM " + TaskManagementDataBase.TASKS_TABLE + " WHERE userId = :id AND isTaskCompleted = 1")
    int userCompletedTasksCount(int id);



}
