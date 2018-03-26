package com.example.memorandum.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;


/**
 * Created by jason on 2018/3/19.
 */

public class Data extends DataSupport {
    private int id;
    private String date;
    @Column(nullable = false)
    private String content;
    private Date exactTime;
    private int star;

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

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
}
