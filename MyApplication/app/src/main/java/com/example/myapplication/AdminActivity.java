package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setupBottomNavigationView();
    }

    void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_profile);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();
            if (itemId == R.id.search_view) {
                intent = new Intent(AdminActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_saved) {
                intent = new Intent(AdminActivity.this, SavedActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_home) {
                intent = new Intent(AdminActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}