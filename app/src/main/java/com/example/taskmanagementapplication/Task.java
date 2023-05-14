package com.example.taskmanagementapplication;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.example.taskmanagementapplication.TaskManagementDb.TaskManagementDataBase;

import java.util.Date;

@Entity(tableName = TaskManagementDataBase.TASKS_TABLE, foreignKeys = @ForeignKey(entity = User.class,
                                                                                 parentColumns = "userId",
                                                                                 childColumns = "userId",
                                                                                 onDelete = ForeignKey.CASCADE), indices = {@Index("userId")})
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int taskId;

    private int userId;

    @TypeConverters(DateConverter.class)
    private Date dateCreated;

    @TypeConverters(DateConverter.class)
    private Date dateCompleted;

    @TypeConverters(DateConverter.class)
    private Date dueDate;

    private String title;
    private int taskPriority;
    private String description;
    private boolean isTaskCompleted;
    private String category;

    public Task(String title, String description, int userId, int taskPriority, String category, Date dueDate) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.taskPriority = taskPriority;
        this.category = category;
        this.dueDate = dueDate;

        this.dateCreated = new Date();
        this.isTaskCompleted = false ;
        this.dateCompleted = null;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", userId=" + userId +
                ", dateCreated=" + dateCreated +
                ", dateCompleted=" + dateCompleted +
                ", dueDate=" + dueDate +
                ", title='" + title + '\'' +
                ", taskPriority=" + taskPriority +
                ", description='" + description + '\'' +
                ", isTaskCompleted=" + isTaskCompleted +
                ", category='" + category + '\'' +
                '}';
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isTaskCompleted() {
        return isTaskCompleted;
    }

    public void setTaskCompleted(boolean taskCompleted) {
        this.isTaskCompleted = taskCompleted;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}