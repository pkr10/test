package com.example.rok.tes;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class todaytemperature extends AppCompatActivity {
    TextView t1,t2;
    String file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todaytemperature);
        t1 = (TextView)findViewById(R.id.today);
        Intent intent = getIntent();
        file = intent.getStringExtra("data");
        BufferedReader br = null;
        try {
            String path = getExternalPath();

            br = new BufferedReader(
                    new FileReader(path + "Movies/" + file));
            String readStr = "";
            String str = null;
            while ((str = br.readLine()) != null)
                readStr += str + "\n";
            br.close();
            t1.setText(readStr.substring(0, readStr.length()-1));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"그래프로 확인하기");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 1:
                Intent intent = new Intent(todaytemperature.this,graph.class);
                intent.putExtra("date",file);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
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
