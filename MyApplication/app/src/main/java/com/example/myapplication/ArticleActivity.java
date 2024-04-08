package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Article;
import com.example.myapplication.model.AuthenticatedUser;
import com.example.myapplication.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class ArticleActivity extends AppCompatActivity {
    TextView articleTitle, articleDate, articleAuthor, articleDescription;
    ImageView articleImage;
    Button btnReadMore;
    ImageButton btnSaveArticle;
    FirebaseDatabase database;

    User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        String url = getIntent().getStringExtra("url");

        articleImage = findViewById(R.id.fullArticle_picture);
        articleTitle = findViewById(R.id.fullArticle_title);
        articleDate = findViewById(R.id.fullArticle_date);
        articleAuthor = findViewById(R.id.fullArticle_author);
        articleDescription = findViewById(R.id.fullArticle_description);
        btnReadMore = findViewById(R.id.btnReadMore);
        btnSaveArticle = findViewById(R.id.btnfullArticle_save);

        String fullArticleImage = getIntent().getStringExtra("fullArticleImage");
        Picasso.get().load(fullArticleImage).error(R.drawable.no_image_icon).placeholder(R.drawable.no_image_icon).into(articleImage);

        String fullArticleDate = getIntent().getStringExtra("fullArticleDate");
        articleDate.setText(fullArticleDate);

        String fullArticleTitle = getIntent().getStringExtra("fullArticleTitle");
        articleTitle.setText(fullArticleTitle);

        String fullArticleAuthor = getIntent().getStringExtra("fullArticleAuthor");
        articleAuthor.setText(fullArticleAuthor);

        String fullArticleDescription = getIntent().getStringExtra("fullArticleDescription");
        articleDescription.setText(fullArticleDescription);

        loggedUser = AuthenticatedUser.user;

        List<Article> articleList  = loggedUser.getSavedArticles();

        Optional<Article> savedArticle = articleList.stream().filter(a -> url.equals(a.getUrl())).findFirst();

        if(savedArticle.isPresent()){
            btnSaveArticle.setImageResource(R.drawable.bookmark_added);
        } else {
            btnSaveArticle.setImageResource(R.drawable.bookmark);
        }

        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                v.getContext().startActivity(intent);
            }
        });

        Article article = new Article();
        article.setUrlToImage(fullArticleImage);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        try {
            Date date = formatter.parse(fullArticleDate);
            article.setPublishedAt(date);
        } catch (ParseException e) {}
        article.setTitle(fullArticleTitle);
        article.setAuthor(fullArticleAuthor);
        article.setDescription(fullArticleDescription);
        article.setUrl(url);

        btnSaveArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Optional<Article> savedArticle = loggedUser.getSavedArticles().stream().filter(article -> url.equals(article.getUrl())).findFirst();

                if(savedArticle.isPresent()){
                    loggedUser.getSavedArticles().remove(savedArticle.get());
                    btnSaveArticle.setImageResource(R.drawable.bookmark);
                } else {
                    loggedUser.getSavedArticles().add(article);
                    btnSaveArticle.setImageResource(R.drawable.bookmark_added);
                }

                database = FirebaseDatabase.getInstance();
                database.getReference(User.DB_REFERENCE).child(loggedUser.getUsername()).setValue(loggedUser, (error, ref) -> {
                    if(Objects.nonNull(error)){
                        Toast.makeText(ArticleActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                AuthenticatedUser.user = loggedUser;
            }
        });

        setupBottomNavigationView();
    }

    void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;
            int itemId = item.getItemId();

            if (itemId == R.id.search_view) {
                intent = new Intent(ArticleActivity.this, SearchActivity.class);
            } else if (itemId == R.id.navigation_saved) {
                intent = new Intent(ArticleActivity.this, SavedActivity.class);
            } else if (itemId == R.id.navigation_home) {
                intent = new Intent(ArticleActivity.this, HomeActivity.class);
            } else if (itemId == R.id.navigation_profile) {
                if (AuthenticatedUser.user.isAdmin()) {
                    intent = new Intent(ArticleActivity.this, AdminActivity.class);
                } else {
                    intent = new Intent(ArticleActivity.this, ProfileActivity.class);
                }
            }

            if (intent != null) {
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}