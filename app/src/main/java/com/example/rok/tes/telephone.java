package com.example.rok.tes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class telephone extends AppCompatActivity {
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> tel = new ArrayList<String>();
    tel_adapter adapter = new tel_adapter(name,tel,this);
    SQLiteDatabase database;
    EditText e1,e2;
    ListView l1;
    int mode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone);
        e1 = (EditText)findViewById(R.id.editText);
        e2 = (EditText)findViewById(R.id.editText2);
        l1 = (ListView)findViewById(R.id.tellist);
        l1.setAdapter(adapter);


        database = openOrCreateDatabase("telephone",MODE_PRIVATE,null);
        if(database == null){
            Toast.makeText(getApplicationContext(),"데이터베이스 생성 실패",Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(getApplicationContext(),"데이터베이스 생성 성공",Toast.LENGTH_SHORT).show();

        }
        String sql = "create table if not exists telephone("+
                "name text not null,"+
                "telnum String primary key not null);";
        try{
            database.execSQL(sql);
            Toast.makeText(getApplicationContext(),"데이터베이스 테디블 생성 완료",Toast.LENGTH_SHORT).show();
        }catch (SQLiteException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"데이터베이스 테이블 생성 실패",Toast.LENGTH_SHORT).show();

        }
        selectdata();
        adapter.notifyDataSetChanged();

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent In = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:/"+tel.get(position)));
                startActivity(In);
                e1.setText(name.get(position));
                e2.setText(tel.get(position));
            }
        });
        l1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                tel.remove(position);
                name.remove(position);

                adapter.notifyDataSetChanged();
                return true;
            }
        });


    }

    public void onmyclick(View view) {
        switch (view.getId()){
            case R.id.add:
                if(e1.getText().toString().equals("")&&e2.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "다입력해주세요", Toast.LENGTH_SHORT).show();
                }
                String sql = "insert into telephone values('"+e1.getText().toString()+"','"+e2.getText().toString()+"');";

                try {
                    database.execSQL(sql);
                    selectdata();
                    Toast.makeText(getApplicationContext(),"삽입성공",Toast.LENGTH_SHORT).show();

                }catch (SQLiteException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"삽입 실패",Toast.LENGTH_SHORT).show();

                }
                Log.d("어레이",String.valueOf(name));


//
                adapter.notifyDataSetChanged();
                break;
            case R.id.update:
                if(e1.getText().toString().equals("")&&e2.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "다입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    String sql2 = "update telephone set name ='" + e1.getText().toString() + "' where telnum = '" + e2.getText().toString() + "';";
                    try {
                        database.execSQL(sql2);
                        selectdata();

                    } catch (SQLiteException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                }

                break;
        }
    }
    public void selectdata(){

        tel.clear();
        name.clear();
        String sql1 = "select name, telnum from telephone;";
        try{
            Cursor result = database.rawQuery(sql1, null);
            result.moveToFirst();
            while(!result.isAfterLast()){
                name.add(result.getString(0));
                tel.add(result.getString(1));
                result.moveToNext();
                adapter.notifyDataSetChanged();

            }

            result.close();




        }catch (SQLiteException e){
            e.printStackTrace();


        }
    }
}
