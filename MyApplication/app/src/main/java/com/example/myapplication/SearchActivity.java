package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Article;
import com.example.myapplication.model.AuthenticatedUser;
import com.example.myapplication.model.Source;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.search_news_recycler);

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
                getNews(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        setupBottomNavigationView();

        setupRecyclerView();
    }

    void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsRecyclerAdapter(articleList, this);
        recyclerView.setAdapter(adapter);
    }

    void getNews(String query) {
        NewsApiClient newsApiClient = new NewsApiClient("997b299131dc4beb8edfceba3e9ad34a");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language(AuthenticatedUser.user.getLanguage().getAbbreviation())
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        runOnUiThread(() -> {
                            articleList.clear();
                            for (com.kwabenaberko.newsapilib.models.Article apiArticle : response.getArticles()) {
                                Article myArticle = convertApiArticleToMyArticle(apiArticle);
                                articleList.add(myArticle);
                            }
                            Log.d("SearchActivity", "Articles fetched: " + articleList.size());
                            adapter.updateData(articleList);
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(SearchActivity.this, "Failed to fetch news articles", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private Article convertApiArticleToMyArticle(com.kwabenaberko.newsapilib.models.Article apiArticle) {
        Article myArticle = new Article();
        myArticle.setAuthor(apiArticle.getAuthor());
        myArticle.setTitle(apiArticle.getTitle());
        myArticle.setDescription(apiArticle.getDescription());
        myArticle.setUrl(apiArticle.getUrl());
        myArticle.setUrlToImage(apiArticle.getUrlToImage());

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(apiArticle.getPublishedAt());
            myArticle.setPublishedAt(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myArticle.setContent(apiArticle.getContent());

        Source mySource = new Source();
        if (apiArticle.getSource() != null) {
            mySource.setId(apiArticle.getSource().getId());
            mySource.setName(apiArticle.getSource().getName());
        }
        myArticle.setSource(mySource);

        return myArticle;
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