package com.example.rok.tes;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class list extends AppCompatActivity {
    ListView l1;
    ArrayList<String> data = new ArrayList<String>();
    customadapter custom = new customadapter(data,this);
    data Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        l1 = (ListView)findViewById(R.id.listview);
        l1.setAdapter(custom);
        listup();
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(list.this,todaytemperature.class);
                intent.putExtra("data",data.get(position));
                startActivity(intent);

            }
        });

    }

    public void listup(){
        String path = getExternalPath();
        File[] files = new File(path+"Movies").listFiles();
        String str = "";
        data.removeAll(data);
        custom.notifyDataSetChanged();
        for (File f : files) {
            data.add(f.getName().toString());
            custom.notifyDataSetChanged();
        }

    }
    public String getExternalPath(){
        String sdPath = "";
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED))
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";

        else
            sdPath = getFilesDir() +"";

        Toast.makeText(getApplicationContext(), sdPath, Toast.LENGTH_SHORT).show();
        return sdPath;

    }
}
