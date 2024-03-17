package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser(username.getText().toString(), password.getText().toString());
            }
        });

        TextView signUpText = findViewById(R.id.signUpText);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void authenticateUser(String username, String password) {
        if ("test".equals(username) && "12345".equals(password)) {
            if (rememberMeCheckBox.isChecked()) {
                saveLoginDetails(username, password);
            } else {
                clearLoginDetails();
            }
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            // Show error message
            Toast.makeText(LoginActivity.this, "Login failed: Username and/or password are incorrect.", Toast.LENGTH_LONG).show();
        }
    }

    private void saveLoginDetails(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", username);
        editor.putString("Password", password);
        editor.putBoolean("RememberMe", true);
        editor.apply();
    }

    private void clearLoginDetails() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Username");
        editor.remove("Password");
        editor.remove("RememberMe");
        editor.apply();
    }

    private void checkRememberMe() {
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
