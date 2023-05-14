package com.example.taskmanagementapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.taskmanagementapplication.TaskManagementDb.TaskManagementDataBase;


@Entity(tableName = TaskManagementDataBase.USERS_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int userId;

    private String username;
    private String password;
    private String email;
    private boolean isAdmin;

    public User(String username, String password, String email, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User Id: " + userId + "\n" +
                "Username: " + username + "\n" +
                "Email: " + email + "\n" +
                "Password: " + password + "\n" +
                "Admin: " + isAdmin;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
