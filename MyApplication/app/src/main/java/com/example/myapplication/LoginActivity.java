package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.AuthenticatedUser;
import com.example.myapplication.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    FirebaseDatabase database;

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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(User.DB_REFERENCE);

        Query checkUserData = reference.orderByChild(User.DB_CHILD_USERNAME).equalTo(username);

        checkUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String dbPassword = snapshot.child(username).child(User.DB_CHILD_PASSWORD).getValue(String.class);
                    if(dbPassword.equals(password)){
                        if (rememberMeCheckBox.isChecked()) {
                            saveLoginDetails(username, password);
                        } else {
                            clearLoginDetails();
                        }
                        AuthenticatedUser.user = snapshot.child(username).getValue(User.class);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: Username and/or password are incorrect.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed: Username not found.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Login failed:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
