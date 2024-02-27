package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button loginButton;
    private CheckBox rememberMeCheckBox;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usernameinput);
        password = findViewById(R.id.passwordinput);
        loginButton = findViewById(R.id.loginButton);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckbox);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        checkRememberMe();

        loginButton.setOnClickListener(view -> {
            // Authentication logic here
            authenticateUser(username.getText().toString(), password.getText().toString());
        });

        TextView signUpText = findViewById(R.id.signUpText);
        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void authenticateUser(String username, String password) {
        // Here you should add your authentication logic. This is just a placeholder.
        boolean isAuthenticated = true; // replace with actual authentication logic

        if (isAuthenticated) {
            if (rememberMeCheckBox.isChecked()) {
                saveLoginDetails(username, password);
            } else {
                clearLoginDetails();
            }
            // Proceed to the next screen or activity after successful login
            // Intent intent = new Intent(LoginActivity.this, NextActivity.class);
            // startActivity(intent);
        } else {
            // Show error message
        }
    }

    private void saveLoginDetails(String username, String password) {
        // Save login credentials securely. This is a simple implementation.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", username);
        editor.putString("Password", password);
        editor.putBoolean("RememberMe", true);
        editor.apply();
    }

    private void clearLoginDetails() {
        // Clear saved login credentials
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Username");
        editor.remove("Password");
        editor.remove("RememberMe");
        editor.apply();
    }

    private void checkRememberMe() {
        // Check if Remember Me was previously selected and apply saved credentials
        boolean isRememberMe = sharedPreferences.getBoolean("RememberMe", false);
        if (isRememberMe) {
            String savedUsername = sharedPreferences.getString("Username", "");
            String savedPassword = sharedPreferences.getString("Password", "");
            username.setText(savedUsername);
            password.setText(savedPassword);
            rememberMeCheckBox.setChecked(true);
        }
    }
}
