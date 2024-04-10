package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Language;
import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;

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
        if (fullNameInput.getText().toString().isEmpty() ||
                usernameInput.getText().toString().isEmpty() ||
                passwordInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (passwordInput.getText().length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void signUpUser() {
        String fullName = fullNameInput.getText().toString();
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        User user = new User(fullName, username, password,"Montreal, CA", Language.EN);

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
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignupActivity.this, "Signup failed:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
