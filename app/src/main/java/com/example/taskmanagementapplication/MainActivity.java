package com.example.taskmanagementapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanagementapplication.TaskManagementDb.TaskManagementDataBase;
import com.example.taskmanagementapplication.TaskManagementDb.UserDAO;
import com.example.taskmanagementapplication.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    UserDAO userDAO;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String ID = "id";

    EditText usernameText;
    EditText passwordText;
    EditText emailText;

    Button createUserBtn;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usernameText = binding.username;
        passwordText = binding.password;
        emailText = binding.email;

        createUserBtn = binding.createAccountButton;
        loginBtn = binding.loginButtton;

        userDAO = Room.databaseBuilder(this, TaskManagementDataBase.class, TaskManagementDataBase.DATABASE_NAME).allowMainThreadQueries().build().userDAO();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if(isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitUserRegistry();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitUserRegistry() {
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String email = emailText.getText().toString();

        User foundUser = userDAO.isUser(username, email);

        if(foundUser == null) {
            User newUser = new User(username, password, email, true);

            if(validateInput(newUser)) {
                userDAO.insertUser(newUser);
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                User user = userDAO.isUser(username);
                String userId = Integer.toString(user.getUserId());

                preferences = PreferenceManager.getDefaultSharedPreferences(this);
                editor = preferences.edit();
                editor.putString(USERNAME, username);
                editor.putString(EMAIL, email);
                editor.putString(ID, userId);
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
                finish();

                Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
            } else {
                usernameText.setText("");
                passwordText.setText("");
                emailText.setText("");
            }
        } else {
            if(username.matches("^admin")) {
                Toast.makeText(getApplicationContext(), "Cannot create an Admin !", Toast.LENGTH_SHORT).show();
            } else {
                if(username.equals(foundUser.getUsername()) && email.equals(foundUser.getEmail())) {
                    Toast.makeText(getApplicationContext(), "User already Exists !", Toast.LENGTH_SHORT).show();
                } else if(username.equals(foundUser.getUsername())) {
                    Toast.makeText(getApplicationContext(), "Username is taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Email is taken", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean validateInput(User new_user) {

        if (!TextUtils.isEmpty(new_user.getUsername()) && !TextUtils.isEmpty(new_user.getPassword()) && !TextUtils.isEmpty(new_user.getEmail())) {
            if (new_user.getUsername().matches("^[a-zA-Z\\d._-]{4,20}$")) {
                if (new_user.getPassword().matches("^[a-zA-Z\\d._-]{4,20}$")) {
                    if (Patterns.EMAIL_ADDRESS.matcher(new_user.getEmail()).matches()) {
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Username", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Fill in the fields !!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}