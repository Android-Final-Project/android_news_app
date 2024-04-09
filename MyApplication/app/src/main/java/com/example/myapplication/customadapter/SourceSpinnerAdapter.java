package com.example.myapplication.customadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.kwabenaberko.newsapilib.models.Source;

import java.util.ArrayList;
import java.util.List;

public class SourceSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<String> itens = new ArrayList<>();

    public SourceSpinnerAdapter(Context context, List<String> itens) {
        this.context = context;
        this.itens = itens;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.domain_spinner_list_item, parent, false);
        }
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.domainText);
        textViewItemName.setText((String) getItem(position));
        return convertView;
    }
}
