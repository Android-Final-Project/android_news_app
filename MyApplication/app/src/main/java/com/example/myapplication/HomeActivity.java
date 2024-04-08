package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Article;
import com.example.myapplication.model.AuthenticatedUser;
import com.example.myapplication.model.Source;
import com.example.myapplication.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;

    String language = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.news_recycler_view);
        progressIndicator = findViewById(R.id.progress_bar);

        User loggedUser = AuthenticatedUser.user;

        if(Objects.nonNull(loggedUser) && Objects.nonNull(loggedUser.getLanguage())){
            language = loggedUser.getLanguage().getAbbreviation();
        }

        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);
        btn6 = findViewById(R.id.btn_6);
        btn7 = findViewById(R.id.btn_7);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);

        setupRecyclerView();
        getNews("General");
        setupBottomNavigationView();
    }

    void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsRecyclerAdapter(articleList, this);
        recyclerView.setAdapter(adapter);
    }

    private void saveArticleForLater(Article article) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            String safeDocumentId = article.getUrl().replaceAll("/", "_");

            db.collection("users")
                    .document(userId)
                    .collection("bookmarkedArticles")
                    .document(safeDocumentId)
                    .set(article)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(HomeActivity.this, "Article saved successfully", Toast.LENGTH_SHORT).show();
                        updateAdapterForArticle(article);
                    })
                    .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Error saving article", Toast.LENGTH_SHORT).show());
        }
    }

    private void updateAdapterForArticle(Article article) {
        int position = articleList.indexOf(article);
        if (position != -1) {
            adapter.notifyItemChanged(position);
        }
    }

    void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;
            int itemId = item.getItemId();

            if (itemId == R.id.search_view) {
                intent = new Intent(HomeActivity.this, SearchActivity.class);
            } else if (itemId == R.id.navigation_saved) {
                intent = new Intent(HomeActivity.this, SavedActivity.class);
            } else if (itemId == R.id.navigation_profile) {
                if (AuthenticatedUser.user.isAdmin()) {
                    intent = new Intent(HomeActivity.this, AdminActivity.class);
                } else {
                    intent = new Intent(HomeActivity.this, ProfileActivity.class);
                }
            }

            if (intent != null) {
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    void changeInProgress(boolean show) {
        progressIndicator.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    void getNews(String category) {
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("997b299131dc4beb8edfceba3e9ad34a");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language(language)
                        .category(category)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        runOnUiThread(() -> {
                            changeInProgress(false);
                            articleList.clear();
                            for (com.kwabenaberko.newsapilib.models.Article apiArticle : response.getArticles()) {
                                Article myArticle = convertApiArticleToMyArticle(apiArticle);
                                articleList.add(myArticle);
                            }
                            Log.d("HomeActivity", "Articles fetched: " + articleList.size());
                            adapter.updateData(articleList);
                        });
                    }


                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(HomeActivity.this, "Failed to fetch news articles", Toast.LENGTH_SHORT).show();
                        changeInProgress(false);
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

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        String category = btn.getText().toString();
        getNews(category);
    }
}
