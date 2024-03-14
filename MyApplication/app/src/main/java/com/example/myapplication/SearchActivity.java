package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

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
    }



}