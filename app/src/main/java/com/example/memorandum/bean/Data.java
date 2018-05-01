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
    @Column(nullable = false)
    private int star;
    @Column(nullable = false)
    private int pending;
    @Column(nullable = false)
    private int reminder;
    private String imagePath;
    private User user;

    public Data() {

    }
    public Data(int id) {
        this.id = id;
    }
    public Data(String content, String date) {
        this.date = date;
        this.content = content;
    }

    public Data(String content, String date, int star, int pending, int reminder) {
        this.content = content;
        this.date = date;
        this.star = star;
        this.pending = pending;
        this.reminder = reminder;
    }
    public Data(String date, String content, Date exactTime, int pending, int reminder, int star) {
        this.date = date;
        this.content = content;
        this.exactTime = exactTime;
        this.pending = pending;
        this.reminder = reminder;
        this.star = star;
    }

    public Data(String date, String content, Date exactTime, int pending, int reminder, int star, User user) {
        this.date = date;
        this.content = content;
        this.exactTime = exactTime;
        this.pending = pending;
        this.reminder = reminder;
        this.star = star;
        this.user = user;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
