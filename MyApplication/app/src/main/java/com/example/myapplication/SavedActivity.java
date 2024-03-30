package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SavedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        setupBottomNavigationView();
    }

    void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_saved);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();
            if (itemId == R.id.search_view) {
                intent = new Intent(SavedActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                intent = new Intent(SavedActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_home) {
                intent = new Intent(SavedActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}