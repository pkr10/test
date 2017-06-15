package com.example.rok.tes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.rok.tes.R.layout.adapter;

public class MainActivity extends AppCompatActivity {
    TextView t1,t2;
    Button b1;
    String temp;
    String date;
    String time;
    String officialtemp;
    SQLiteDatabase database;
    ArrayList<Double> temp1 = new ArrayList<Double>();
    String data ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = (TextView)findViewById(R.id.textView);
        b1 = (Button)findViewById(R.id.test);
        t2 = (TextView)findViewById(R.id.officialtemp);
        Intent intent = getIntent();
        data Data = intent.getParcelableExtra("userList");
        temp = Data.getTemp();
        date = Data.getDate();
        time = Data.getTime();
        Log.d("date", String.valueOf(Double.parseDouble(temp)));
        Log.d("data",date);
        Log.d("time",time);
        new ReceiveWeather().execute();

        int permiisininfo = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permiisininfo == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"SDCard쓰기 권한있음",Toast.LENGTH_SHORT).show();
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(getApplicationContext(),"권한의 필요성 설명",Toast.LENGTH_SHORT).show();

            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }
        try {
            String path = getExternalPath();
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(path +"Movies/"+date+".memo",true));
            bw.write(time +"   "+temp);
            bw.newLine();
            bw.close();
            Toast.makeText(getApplicationContext(), "save complete", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }



    public void onmyclick(View view) {
        switch (view.getId()){
            case R.id.button2:
                Intent intent  = new Intent(MainActivity.this, list.class);
                intent.putExtra("data",date);
                startActivity(intent);
                break;
            case R.id.test:
                Intent intent1 = new Intent(MainActivity.this,Main3Activity.class);
                intent1.putExtra("temp",data);
                startActivity(intent1);
                break;
            case R.id.button4:
                Intent intent2 = new Intent(MainActivity.this,complain.class);
                startActivity(intent2);
                break;

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String str = null;
        if(requestCode ==100){
            if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED)
                str = "SD Card 쓰기권한 승인";
            else str = "SD Card 쓰기권한 거부";
            Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();

        }
    }
    public class ReceiveWeather extends AsyncTask<URL, Integer, Long> {

        ArrayList<Double> Weathers = new ArrayList<Double>();

        protected Long doInBackground(URL... urls) {

            String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=2871025000";

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                parseXML(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Long result) {

                data =
                               officialtemp + "도 ";
            t2.setText("기상청 현재 온도는 :"+data);


        }

        void parseXML(String xml) {
            try {
                String tagName = "";

                boolean onTem = false;

                boolean onPop = false;
                boolean onEnd = false;
                boolean isItemTag1 = false;
                int i = 0;

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(new StringReader(xml));

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("data")) {
                            onEnd = false;
                            isItemTag1 = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT && isItemTag1) {


                        if (tagName.equals("temp") && !onTem) {
                            officialtemp = parser.getText();
                            onTem = true;
                        }

                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (tagName.equals("s06") && onEnd == false) {
                            i++;

                            onTem = false;
                            isItemTag1 = false;
                            onEnd = true;
                        }
                    }

                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
