package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.model.Article;
import com.example.myapplication.model.Source;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
        adapter = new NewsRecyclerAdapter(new ArrayList<>(), this, this::removeArticleFromSaved);
        savedArticlesRecyclerView.setAdapter(adapter);

        loadSavedArticles();
    }

    private void loadSavedArticles() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).collection("bookmarkedArticles")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Article> articles = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Article article = documentSnapshot.toObject(Article.class);

                        Map<String, Object> sourceMap = (Map<String, Object>) documentSnapshot.get("source");
                        if (sourceMap != null) {
                            Source source = new Source();
                            source.setId((String) sourceMap.get("id"));
                            source.setName((String) sourceMap.get("name"));
                            article.setSource(source);
                        }

                        articles.add(article);
                    }
                    adapter.updateData(articles);
                })
                .addOnFailureListener(e -> Toast.makeText(SavedActivity.this, "Error loading saved articles", Toast.LENGTH_SHORT).show());
    }

    private void removeArticleFromSaved(Article article) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).collection("bookmarkedArticles").document(article.getUrl())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SavedActivity.this, "Article removed successfully", Toast.LENGTH_SHORT).show();
                    loadSavedArticles();
                })
                .addOnFailureListener(e -> Toast.makeText(SavedActivity.this, "Error removing article", Toast.LENGTH_SHORT).show());
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
                intent = new Intent(SavedActivity.this, ProfileActivity.class);
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