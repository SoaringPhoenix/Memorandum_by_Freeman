package com.example.memorandum;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by jason on 2018/3/19.
 */

public class Data extends DataSupport {
    private int id;
    private String date;
    private String content;
    private String exactTime;
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


    public String getExactTime() {
        return exactTime;
    }

    public void setExactTime(String exactTime) {
        this.exactTime = exactTime;
    }
}
