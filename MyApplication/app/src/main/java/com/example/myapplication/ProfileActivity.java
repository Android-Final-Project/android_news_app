package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.customadapter.ProfileArticleAdapter;
import com.example.myapplication.model.AuthenticatedUser;
import com.example.myapplication.model.Language;
import com.example.myapplication.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private User loggedUser;

    TextView textViewProfileFullName;
    TextView textViewProfileLocation;
    TextView textViewProfileSavedArticlesTitle;

    ListView profileArticleList;

    Spinner spinnerNewsLanguage;

    FirebaseDatabase database;


    private final List<String> newsLanguage = Language.getAllDescriptions();

    private final List<String> savedArticles = Arrays.asList("Tech Giants Launch Climate initiative", "Tech Giants Launch Climate initiative", "Tech Giants Launch Climate initiative");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loggedUser = AuthenticatedUser.user;

        textViewProfileFullName = findViewById(R.id.textViewProfileFullName);
        textViewProfileLocation = findViewById(R.id.textViewProfileLocation);
        textViewProfileSavedArticlesTitle = findViewById(R.id.textViewProfileSavedArticlesTitle);
        profileArticleList = findViewById(R.id.profileArticleList);
        spinnerNewsLanguage = findViewById(R.id.spinnerNewsLanguage);

        textViewProfileFullName.setText(loggedUser.getFullName());
        textViewProfileLocation.setText(loggedUser.getLocation());
        textViewProfileSavedArticlesTitle.setText("Saved articules (3)");

        ProfileArticleAdapter profileArticleAdapter = new ProfileArticleAdapter(this, savedArticles);
        profileArticleList.setAdapter(profileArticleAdapter);

        setUpSpinnerNewsLanguage();

        setupBottomNavigationView();

    }

    private void setUpSpinnerNewsLanguage() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, newsLanguage);
        spinnerNewsLanguage.setAdapter(spinnerAdapter);
        spinnerNewsLanguage.setSelection(spinnerAdapter.getPosition(loggedUser.getLanguage().getDescription()));

        spinnerNewsLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loggedUser.setLanguage(Language.findByDescription((String) parent.getSelectedItem()));
                database = FirebaseDatabase.getInstance();
                database.getReference(User.DB_REFERENCE).child(loggedUser.getUsername()).setValue(loggedUser, (error, ref) -> {
                    if(Objects.nonNull(error)){
                        Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();
            if (itemId == R.id.search_view) {
                intent = new Intent(ProfileActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_saved) {
                intent = new Intent(ProfileActivity.this, SavedActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_home) {
                intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}