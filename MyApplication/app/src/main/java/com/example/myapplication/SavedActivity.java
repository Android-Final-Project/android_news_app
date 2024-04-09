package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Article;
import com.example.myapplication.model.AuthenticatedUser;
import com.example.myapplication.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SavedActivity extends AppCompatActivity {

    RecyclerView savedArticlesRecyclerView;
    NewsRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        setupBottomNavigationView();

        savedArticlesRecyclerView = findViewById(R.id.saved_articles_recycler_view);
        savedArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsRecyclerAdapter(new ArrayList<>(), this);
        savedArticlesRecyclerView.setAdapter(adapter);

        loadSavedArticles();
    }

    private void loadSavedArticles() {
        User user = AuthenticatedUser.user;
        if(Objects.nonNull(user)){
            List<Article> listSavedArticles = user.getSavedArticles();
            adapter.updateData(listSavedArticles);
        }

    }

    void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_saved);
        bottomNavigationView.setSelectedItemId(R.id.navigation_saved);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;
            int itemId = item.getItemId();

            if (itemId == R.id.search_view) {
                intent = new Intent(SavedActivity.this, SearchActivity.class);
            } else if (itemId == R.id.navigation_saved) {
                return true;
            } else if (itemId == R.id.navigation_profile) {
                if (AuthenticatedUser.user.isAdmin()) {
                    intent = new Intent(SavedActivity.this, AdminActivity.class);
                } else {
                    intent = new Intent(SavedActivity.this, ProfileActivity.class);
                }
            } else if (itemId == R.id.navigation_home) {
                intent = new Intent(SavedActivity.this, HomeActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

}