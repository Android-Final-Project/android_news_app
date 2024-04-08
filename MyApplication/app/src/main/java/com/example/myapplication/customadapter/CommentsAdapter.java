package com.example.myapplication.customadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Article;
import com.example.myapplication.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends BaseAdapter {

    private Context context;
    List<Comment> comments = new ArrayList<>();

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    public void updateData(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.comments_list_item, parent, false);
        }

        Comment currentItem = (Comment) getItem(position);

        TextView author = (TextView)
                convertView.findViewById(R.id.comment_author);

        TextView text = (TextView)
                convertView.findViewById(R.id.comment_text);

        author.setText(currentItem.getAuthor());
        text.setText(currentItem.getText());

        return convertView;
    }
}
