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
 * Created by rok on 2017. 6. 8..
 */

public class tel_adapter extends BaseAdapter {
    ArrayList<String> data;
    ArrayList<String> tel;
    Context c;


    public tel_adapter(ArrayList<String> data,ArrayList<String> tel, Context c){
        this.data = data;
        this.tel = tel;
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
            convertView = inflater.inflate(R.layout.tel_adapter,null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.name);
        TextView t2  =(TextView)convertView.findViewById(R.id.tel);
        ImageView i1 = (ImageView)convertView.findViewById(R.id.imageView4);
        textView.setText(data.get(position));
        t2.setText(tel.get(position));
        i1.setImageResource(R.drawable.unknown);
        return convertView;
    }
}
