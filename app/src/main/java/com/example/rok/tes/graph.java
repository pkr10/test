package com.example.rok.tes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class graph extends AppCompatActivity {
    LineGraphSeries<DataPoint> series;
    GraphView graphView;
    String date1;
    ArrayList<Double> temp1 = new ArrayList<Double>();
    SQLiteDatabase database;
    String temp,date,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Intent intent = getIntent();
        date1 = intent.getStringExtra("date");
        database = openOrCreateDatabase("temperature",MODE_PRIVATE,null);
        if(database == null){
            Toast.makeText(getApplicationContext(),"데이터베이스 생성 실패",Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(getApplicationContext(),"데이터베이스 생성 성공",Toast.LENGTH_SHORT).show();

        }
        String sql = "create table if not exists temperature("+
                "temp text not null,"+
                "date text," +
                "time text);";
        try{
            database.execSQL(sql);
            Toast.makeText(getApplicationContext(),"데이터베이스 테디블 생성 완료",Toast.LENGTH_SHORT).show();
        }catch (SQLiteException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"데이터베이스 테이블 생성 실패",Toast.LENGTH_SHORT).show();

        }
        new BackgroundTask().execute();

        String sql3 = "select distinct temp from temperature where date = '"+date1+"';";
        try {
            Cursor result = database.rawQuery(sql3, null);
            temp1.add(Double.parseDouble(result.getString(0)));
            result.moveToFirst();
            while (!result.isAfterLast()) {
                result.moveToNext();
                Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();
            }
            result.close();
        }catch (SQLiteException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
        }

        graphView = (GraphView)findViewById(R.id.graph);
        series = new LineGraphSeries<>(generateData(temp1));
//
        series.setTitle("오늘의 온도");
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(20);
        series.setThickness(10);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setVerticalLabels(new String[]{"0","5","10","15","20","25","30","35","40","45","50"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScrollable(true);
        graphView.addSeries(series);

    }

    private DataPoint[] generateData(ArrayList<Double> a) {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = Double.parseDouble(String.valueOf(i));
            double y = a.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
    class BackgroundTask extends AsyncTask<Void,Void,String> {
        String target;


        @Override
        protected void onPreExecute() {
            target = "http://pkr10.cafe24.com/MeetList3.php";

        }

        @Override
        protected String doInBackground(Void... params) {
//            try{
//                URL url = new URL(target);
//                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String temp ;
//                StringBuilder stringBuilder = new StringBuilder();
//                while((temp = bufferedReader.readLine())!=null){
//                    stringBuilder.append(temp +"\n");
//
//                }
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//                return stringBuilder.toString().trim();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            String sql5 = "insert into temperature values('"+temp+"','"+date+"','"+time+",);";
            Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();

            try{
                database.execSQL(sql5);
            }catch (SQLiteException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                while(count<jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    temp = object.getString("temp");
                    date = object.getString("date");
                    time = object.getString("time");



                    count++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}

