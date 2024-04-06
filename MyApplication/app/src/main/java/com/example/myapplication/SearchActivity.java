package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.myapplication.model.AuthenticatedUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view);
        ImageView searchIcon = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_button);
        if (searchIcon != null) {
            Drawable wrappedDrawable = DrawableCompat.wrap(searchIcon.getDrawable());
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(this, R.color.navy_blue));
            searchIcon.setImageDrawable(wrappedDrawable);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        setupBottomNavigationView();
    }

    void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_search);
        bottomNavigationView.setSelectedItemId(R.id.search_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_saved) {
                intent = new Intent(SearchActivity.this, SavedActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                if(AuthenticatedUser.user.isAdmin()){
                    intent = new Intent(SearchActivity.this, AdminActivity.class);
                } else {
                    intent = new Intent(SearchActivity.this, ProfileActivity.class);
                }
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_home) {
                intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

}