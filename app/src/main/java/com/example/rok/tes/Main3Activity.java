package com.example.rok.tes;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

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

public class Main3Activity extends AppCompatActivity {
    TextView t1,t2;
    EditText e1;

    int time = 9999;
    data Data;
    Test test;
    Double sd =30.0;
    String temp ="0";
    ArrayList<Test> data =  new ArrayList<Test>();
String a="11";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        t1 = (TextView)findViewById(R.id.textView);
        t2 = (TextView)findViewById(R.id.nowtemp);
        e1 = (EditText)findViewById(R.id.nowtemp1);
        Intent intent = getIntent();
        String data = intent.getStringExtra("temp");
        t2.setText("바깥온도 : "+ data);
        myThread.start();








    }
    final Handler myhandler = new Handler();
    Thread myThread = new Thread(){
        @Override
        public void run() {
            super.run();
            while(time>0){
                myhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        new BackgroundTask().execute();
                        time--;
                        Push();

                    }
                });
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            time =9999;

        }
    };

    class BackgroundTask extends AsyncTask<Void,Void,String> {
        String target;


        @Override
        protected void onPreExecute() {
            target = "http://pkr10.cafe24.com/MeetList2.php";

        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp ;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine())!=null){
                    stringBuilder.append(temp +"\n");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
                String date="",time="";
                while(count<jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    temp = object.getString("temp");
                    date = object.getString("date");
                    time = object.getString("time");
                    count++;
                }
//                Test test1 = new Test(temp, date, time);
//                data.add(test1);
                t1.setText(temp);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void onmyclick(View view) {
        switch (view.getId()){
            case R.id.button:
                onBackPressed();
                break;
            case R.id.tel:
                Intent intent = new Intent(Main3Activity.this,telephone.class);
                startActivity(intent);
                break;
            case R.id.button5:
                sd = Double.parseDouble(e1.getText().toString());
                break;
        }
    }

    void Push() {
        new BackgroundTask().execute();
        Log.d("tmperature",temp);

        if(Double.parseDouble(temp)>sd) {
            //받아오기
                t1.setTextColor(Color.RED);
                t1.setTextSize(80);
                NotificationManager notificationManager = (NotificationManager) Main3Activity.this.getSystemService(Main3Activity.this.NOTIFICATION_SERVICE);
                Intent intent1 = new Intent(Main3Activity.this.getApplicationContext(), Main3Activity.class); //인텐트 생성.
                Notification.Builder builder = new Notification.Builder(getApplicationContext());
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);//현재 액티비티를 최상으로 올리고, 최상의 액티비티를 제외한 모든 액티비티를 없앤다.
                PendingIntent pendingNotificationIntent = PendingIntent.getActivity(Main3Activity.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setSmallIcon(R.drawable.first).setTicker("경고").setWhen(System.currentTimeMillis())
                        .setNumber(1).setContentTitle("온도가 너무 높아요").setContentText("온도가 너무 높아요")
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingNotificationIntent).setAutoCancel(true).setOngoing(true);
                //해당 부분은 API 4.1버전부터 작동합니다.

                notificationManager.notify(1, builder.build());



        } else {
            t1.setTextColor(Color.BLACK);
            t1.setTextSize(30);

        }


    }


}
