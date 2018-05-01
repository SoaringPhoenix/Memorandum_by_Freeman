package com.example.memorandum.dao;

import android.database.Cursor;

import com.example.memorandum.bean.Data;
import com.example.memorandum.bean.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jason on 2018/4/22.
 */

public class DataDAO {
    private static List<Data> dataList = new ArrayList<>();

    public List<Data> getData() {
        dataList = DataSupport.order("exactTime desc").find(Data.class);
        return dataList;
    }

    public List<Data> getPending() {
        dataList = DataSupport.where("pending = ?", "2").order("exactTime desc").find(Data.class);
        return dataList;
    }

//    public List<Data> getUserPending(User user) {
//        dataList = DataSupport.where("pending = ?, user = ?", "2", user).order("exactTime desc").find(Data.class);
//        return dataList;
//    }

    public List<Data> getReminder() {
        dataList = DataSupport.where("reminder = ?", "2").order("exactTime desc").find(Data.class);
        return dataList;
    }

    public List<Data> getFavorites() {
        dataList = DataSupport.where("star = ?", "2").order("exactTime desc").find(Data.class);
        return dataList;
    }

    public List<Data> queryData(String newText) {
        dataList = DataSupport.order("exactTime desc").where("content like ?", "%" + newText + "%").find(Data.class);
        return dataList;
    }

    public List<Data> queryPending(String newText) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where pending = ? " +
                "and content like ? order by exactTime desc", "2", "%" + newText + "%");
        return dataList;
    }

    public List<Data> queryReminder(String newText) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where reminder = ? " +
                "and content like ? order by exactTime desc", "2", "%" + newText + "%");
        return dataList;
    }

    public List<Data> queryFavorites(String newText) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where star = ? " +
                "and content like ? order by exactTime desc", "2", "%" + newText + "%");
        return dataList;
    }

    public static int findCurrentId() {
        dataList = DataSupport.select("id").order("exactTime desc").find(Data.class);
        Data data = dataList.get(0);
        int currentId = data.getId();
        return currentId;
    }
    public static Data findCurrentData() {
        dataList = DataSupport.order("exactTime desc").find(Data.class);
        Data data = dataList.get(0);
        return data;
    }

    public static List<Data> getRawData(String... sql) {
        Cursor cursor = DataSupport.findBySQL(sql);
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                int star = cursor.getInt(cursor.getColumnIndex("star"));
                int pending = cursor.getInt(cursor.getColumnIndex("pending"));
                int reminder = cursor.getInt(cursor.getColumnIndex("reminder"));
                dataList.add(new Data(content, date, star, pending, reminder));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dataList;
    }

    public void insertData(Data data) {
        dataList.add(data);
        Data newData = new Data();
        newData.setDate(data.getDate());
        newData.setContent(data.getContent());
        newData.setExactTime(data.getExactTime());
        newData.setPending(data.getPending());
        newData.setReminder(data.getReminder());
        newData.setStar(data.getStar());
        newData.save();
    }

    public void insertUserData(Data data) {
        dataList.add(data);
        Data newData = new Data();
        newData.setDate(data.getDate());
        newData.setContent(data.getContent());
        newData.setExactTime(data.getExactTime());
        newData.setPending(data.getPending());
        newData.setReminder(data.getReminder());
        newData.setStar(data.getStar());
        newData.setUser(data.getUser());
        newData.save();
    }

    public void updateData(Data data, int currentId) {
        Data newData = new Data();
        newData.setDate(data.getDate());
        newData.setContent(data.getContent());
        newData.setExactTime(data.getExactTime());
        newData.setPending(data.getPending());
        newData.setReminder(data.getReminder());
        newData.setStar(data.getStar());
        newData.update(currentId);
    }

    public void updateUserData(Data data, int currentId) {
        Data newData = new Data();
        newData.setDate(data.getDate());
        newData.setContent(data.getContent());
        newData.setExactTime(data.getExactTime());
        newData.setPending(data.getPending());
        newData.setReminder(data.getReminder());
        newData.setStar(data.getStar());
        newData.setUser(data.getUser());
        newData.update(currentId);
    }

    public void deleteData(int currentId) {
        DataSupport.delete(Data.class, currentId);
    }

    public void updateImagePath(String imagePath, int currentId) {
        Data newData = new Data();
        newData.setImagePath(imagePath);
        newData.update(currentId);
    }
}
