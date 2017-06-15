package com.example.rok.tes;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.Date;

/**
 * Created by rok on 2017. 6. 5..
 */

public class data implements Parcelable{
    String temp;
    String date;
    String time;


    protected data(Parcel in) {
        temp = in.readString();
        date = in.readString();
        time = in.readString();
    }

    public static final Creator<data> CREATOR = new Creator<data>() {
        @Override
        public data createFromParcel(Parcel in) {
            return new data(in);
        }

        @Override
        public data[] newArray(int size) {
            return new data[size];
        }
    };

    public String getDate() {
        return date;
    }

    public String getTemp() {
        return temp;
    }

    public String getTime() {
        return time;
    }

    public data(String temp,String date,String time){
        this.temp = temp;
        this.date = date;
        this.time  = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(temp);
        dest.writeString(date);
        dest.writeString(time);
    }
}
