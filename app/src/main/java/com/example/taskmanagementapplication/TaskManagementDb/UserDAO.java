package com.example.taskmanagementapplication.TaskManagementDb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmanagementapplication.User;


@Dao
public interface UserDAO {

    @Insert
    void insertUser(User... users);

    @Update
    void updateUser(User... users);

    @Delete
    void deleteUser(User... users);

    @Query("SELECT * FROM " + TaskManagementDataBase.USERS_TABLE + " WHERE username = :username OR email = :email")
    User isUser(String username, String email);

    @Query("SELECT * FROM " + TaskManagementDataBase.USERS_TABLE + " WHERE username = :username")
    User isUser(String username);


}