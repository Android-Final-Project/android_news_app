package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private EditText fullNameInput, usernameInput, passwordInput;
    private Button continueButton;
    private ImageView backButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        fullNameInput = findViewById(R.id.fullnameinput);
        usernameInput = findViewById(R.id.usernameinput);
        passwordInput = findViewById(R.id.passwordinput);
        continueButton = findViewById(R.id.continueButton);
        backButton = findViewById(R.id.backButton);

        continueButton.setOnClickListener(view -> {
            if (validateInput()) {
                signUpUser();
            }
        });

        backButton.setOnClickListener(view -> finish());
    }

    private boolean validateInput() {
        // Check if any field is empty
        if (fullNameInput.getText().toString().isEmpty() ||
                usernameInput.getText().toString().isEmpty() ||
                passwordInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if the password is long enough (e.g., at least 6 characters)
        if (passwordInput.getText().length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Add more validation as needed (e.g., regex for username, check password strength)

        return true;
    }

    private void signUpUser() {
        // Here you would add the logic to create a new user account.
        // This might involve sending the data to a server or storing it in a local database.

        // For example, send a network request to your server's sign-up endpoint.
        // This is just a placeholder and will not actually send a network request.
        String fullName = fullNameInput.getText().toString();
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();


        // TODO: Replace FireBase Auth to RealTimeDataBase
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignupActivity.this, "Account created for: " + fullName, Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                     startActivity(intent);
                     finish(); // Finish this activity so the user can't return to it
                } else {
                    String errorMessage = "Something went wrong.";
                    if(Objects.nonNull(task.getException())) {
                        errorMessage = task.getException().getMessage();
                    }
                    Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
