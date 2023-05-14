package com.example.taskmanagementapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanagementapplication.TaskManagementDb.TaskManagementDataBase;
import com.example.taskmanagementapplication.TaskManagementDb.UserDAO;
import com.example.taskmanagementapplication.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private SharedPreferences preferences;
    private boolean isLoggedIn;

    EditText usernameText;
    EditText passwordText;

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String ID = "id";

    Button login_btn;
    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        userDAO = Room.databaseBuilder(this, TaskManagementDataBase.class, TaskManagementDataBase.DATABASE_NAME).allowMainThreadQueries().build().userDAO();

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        login_btn = binding.loginButtton;
        usernameText = binding.username;
        passwordText = binding.password;

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                User foundUser = userDAO.isUser(username);

                if (foundUser != null && !foundUser.isAdmin() && foundUser.getPassword().equals(password)) {
                    String userId = Integer.toString(foundUser.getUserId());

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(USERNAME, foundUser.getUsername());
                    editor.putString(EMAIL, foundUser.getEmail());
                    editor.putString(ID, userId);
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (foundUser != null && foundUser.isAdmin() && foundUser.getPassword().equals(password)) {
                    String userId = Integer.toString(foundUser.getUserId());

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(USERNAME, foundUser.getUsername());
                    editor.putString(EMAIL, foundUser.getEmail());
                    editor.putString(ID, userId);
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    usernameText.setText("");
                    passwordText.setText("");
                }
            }
        });
    }
//    @Override
//    public void onBackPressed() {
//        if (isLoggedIn) {
//            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//    }

}

