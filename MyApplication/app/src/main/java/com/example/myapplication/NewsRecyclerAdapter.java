package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Article;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    private List<Article> articleList;
    private Context context;

    public NewsRecyclerAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        if (Objects.nonNull(article.getSource())){
            holder.sourceTextView.setText(article.getSource().getName());
        } else {
            holder.sourceTextView.setText("");
        }
        Picasso.get().load(article.getUrlToImage()).error(R.drawable.no_image_icon).placeholder(R.drawable.no_image_icon).into(holder.imageView);
        holder.dateTextView.setText(formatDate(article.getPublishedAt()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ArticleActivity.class);
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

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    private String formatDate(Date date) {
        if (date == null) return "Unknown date";
        SimpleDateFormat desiredFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return desiredFormat.format(date);
    }

    public void updateData(List<Article> newArticles) {
        articleList = newArticles;
        notifyDataSetChanged();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, sourceTextView, dateTextView;
        ImageView imageView;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title);
            sourceTextView = itemView.findViewById(R.id.article_source);
            imageView = itemView.findViewById(R.id.article_image_view);
            dateTextView = itemView.findViewById(R.id.article_date);
        }
    }
}
