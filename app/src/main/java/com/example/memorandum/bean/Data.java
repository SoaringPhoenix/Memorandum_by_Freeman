package com.example.memorandum.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;


/**
 * Created by jason on 2018/3/19.
 */

public class Data extends DataSupport implements Parcelable {
    private int id;
    private String date;
    @Column(nullable = false)
    private String content;
    private Date exactTime;
    private int star;
    private int pending;
    private int reminder;

    public Data() {

    }

    public Data(String content, String date) {
        this.date = date;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getExactTime() {
        return exactTime;
    }

    public void setExactTime(Date exactTime) {
        this.exactTime = exactTime;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(content);
        dest.writeInt(star);
        dest.writeInt(pending);
        dest.writeInt(reminder);
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {

        @Override
        public Data createFromParcel(Parcel source) {
            Data data = new Data();
            data.id = source.readInt();
            data.date = source.readString();
            data.content = source.readString();
            data.star = source.readInt();
            data.pending = source.readInt();
            data.reminder = source.readInt();
            return data;
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}
