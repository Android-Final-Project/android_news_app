package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.myapplication.customadapter.ProfileArticleAdapter;
import com.example.myapplication.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private User loggedUser;

    TextView textViewProfileFullName;
    TextView textViewProfileLocation;
    TextView textViewProfileSavedArticlesTitle;

    ListView profileArticleList;

    Spinner spinnerNewsLanguage;

    private final List<String> newsLanguage = Arrays.asList("English", "French", "Portuguese");

    private final List<String> savedArticles = Arrays.asList("Tech Giants Launch Climate initiative", "Tech Giants Launch Climate initiative", "Tech Giants Launch Climate initiative");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loggedUser = User.getLogged();

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

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, newsLanguage);
        spinnerNewsLanguage.setAdapter(spinnerAdapter);

    }
}