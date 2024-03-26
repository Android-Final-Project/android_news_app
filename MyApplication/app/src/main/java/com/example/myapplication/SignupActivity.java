package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private EditText fullNameInput, usernameInput, passwordInput;
    private Button continueButton;
    private ImageView backButton;

    private FirebaseAuth mAuth;

    FirebaseDatabase database;

    DatabaseReference reference;

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
        User user = new User(fullName, username, password,"Montreal, CA");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference(User.DB_REFERENCE);

        Query checkUserData = reference.orderByChild(User.DB_CHILD_USERNAME).equalTo(username);
        checkUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(SignupActivity.this, "Signup failed: Username already exists.", Toast.LENGTH_LONG).show();
                } else {

                    reference.child(username).setValue(user, (error, ref) -> {
                        if(Objects.nonNull(error)){
                            Toast.makeText(SignupActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "Account created for: " + user.getFullName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Finish this activity so the user can't return to it
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignupActivity.this, "Signup failed:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
//        reference.child(username).setValue(new User(fullName,username,password,"Montreal, CA")).addOnCompleteListener(this, new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(SignupActivity.this, "Account created for: " + fullName, Toast.LENGTH_SHORT).show();
//                     Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                     startActivity(intent);
//                     finish(); // Finish this activity so the user can't return to it
//                } else {
//                    String errorMessage = "Something went wrong.";
//                    if(Objects.nonNull(task.getException())) {
//                        errorMessage = task.getException().getMessage();
//                    }
//                    Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });




    }
}
