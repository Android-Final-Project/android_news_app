package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.customadapter.ProfileArticleAdapter;
import com.example.myapplication.model.Article;
import com.example.myapplication.model.AuthenticatedUser;
import com.example.myapplication.model.Language;
import com.example.myapplication.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    TextView textViewProfileFullName;
    TextView textViewProfileLocation;
    TextView textViewProfileSavedArticlesTitle;
    ListView profileArticleList;
    Spinner spinnerNewsLanguage;
    FirebaseDatabase database;

    private User loggedUser;

    private final List<String> newsLanguage = Language.getAllDescriptions();

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


        List<Article> articles = loggedUser.getSavedArticles();
        textViewProfileFullName.setText(loggedUser.getFullName());
        textViewProfileLocation.setText(loggedUser.getLocation());
        textViewProfileSavedArticlesTitle.setText(String.format("Saved articules (%s)", articles.size()));

        ProfileArticleAdapter profileArticleAdapter = new ProfileArticleAdapter(this, articles);
        profileArticleList.setAdapter(profileArticleAdapter);

        profileArticleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = (Article) parent.getItemAtPosition(position);

                Intent intent = new Intent(ProfileActivity.this, ArticleActivity.class);
                intent.putExtra("url", article.getUrl());

                intent.putExtra("fullArticleImage", article.getUrlToImage());

                if(Objects.nonNull(article.getPublishedAt())){
                    intent.putExtra("fullArticleDate", formatDate(article.getPublishedAt()));
                } else {
                    intent.putExtra("fullArticleDate", "");
                }

                intent.putExtra("fullArticleTitle", article.getTitle());
                intent.putExtra("fullArticleAuthor", article.getAuthor());
                intent.putExtra("fullArticleDescription", article.getDescription());
                startActivity(intent);
            }
        });

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
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
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

    private String formatDate(Date date) {
        if (date == null) return "Unknown date";
        SimpleDateFormat desiredFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return desiredFormat.format(date);
    }
}