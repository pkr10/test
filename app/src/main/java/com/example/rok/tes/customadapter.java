package com.example.rok.tes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rok on 2017. 6. 6..
 */

public class customadapter extends BaseAdapter{
    ArrayList<String> data;
    Context c;
    public customadapter(ArrayList<String> data, Context c){
        this.data = data;
        this.c = c;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(c);
        if(convertView ==null){
            convertView = inflater.inflate(R.layout.adapter,null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.listtext);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
        textView.setText(data.get(position));
        imageView.setImageResource(R.drawable.calandar);
        return convertView;
    }
}
