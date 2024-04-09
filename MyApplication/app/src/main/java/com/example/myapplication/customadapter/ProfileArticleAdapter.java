package com.example.myapplication.customadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Article;

import java.util.ArrayList;
import java.util.List;

public class ProfileArticleAdapter extends BaseAdapter {

    private Context context;
    List<Article> articles = new ArrayList<>();

    public ProfileArticleAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.profile_articles_list_item, parent, false);
        }

        Article currentItem = (Article) getItem(position);

        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.profileArticleTitle);

        textViewItemName.setText(currentItem.getTitle());

        return convertView;
    }
}
