package com.example.myapplication.customadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileArticleAdapter extends BaseAdapter {

    private Context context;
    List<String> titles = new ArrayList<>();

    public ProfileArticleAdapter(Context context, List<String> titles) {
        this.context = context;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
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

        String currentItem = (String) getItem(position);

        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.profileArticleTitle);

        textViewItemName.setText(currentItem);

        return convertView;
    }
}
