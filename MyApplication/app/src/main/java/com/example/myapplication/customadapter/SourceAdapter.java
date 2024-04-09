package com.example.myapplication.customadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.BanedSources;
import com.kwabenaberko.newsapilib.models.Source;

import java.util.ArrayList;
import java.util.List;

public class SourceAdapter extends BaseAdapter {

    private Context context;
    List<BanedSources> items = new ArrayList<>();

    public SourceAdapter(Context context, List<BanedSources> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.domain_list_item, parent, false);
        }

        BanedSources currentItem = (BanedSources) getItem(position);

        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.domainText);

        textViewItemName.setText(currentItem.getId());

        return convertView;
    }

    public List<BanedSources> getItems(){
        return this.items;
    }
}
