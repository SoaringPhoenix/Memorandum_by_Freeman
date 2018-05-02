package com.example.memorandum.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public List<Data> getUserData(int user_id) {
        dataList = DataSupport.where("user_id = ?", Integer.toString(user_id)).order("exactTime desc").find(Data.class);
        return dataList;
    }

    public List<Data> getPending() {
        dataList = DataSupport.where("pending = ?", "2").order("exactTime desc").find(Data.class);
        return dataList;
    }

    public List<Data> getUserPending(int user_id) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where pending = ? " +
                "and user_id = ? order by exactTime desc", "2", Integer.toString(user_id));
        return dataList;
    }
    public List<Data> getReminder() {
        dataList = DataSupport.where("reminder = ?", "2").order("exactTime desc").find(Data.class);
        return dataList;
    }
    public List<Data> getUserReminder(int user_id) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where reminder = ? " +
                "and user_id = ? order by exactTime desc", "2", Integer.toString(user_id));
        return dataList;
    }

    public List<Data> getFavorites() {
        dataList = DataSupport.where("star = ?", "2").order("exactTime desc").find(Data.class);
        return dataList;
    }

    public List<Data> getUserFavorites(int user_id) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where star = ? " +
                "and user_id = ? order by exactTime desc", "2", Integer.toString(user_id));
        return dataList;
    }

    public List<Data> queryData(String newText) {
        dataList = DataSupport.order("exactTime desc").where("content like ?", "%" + newText + "%").find(Data.class);
        return dataList;
    }

    public List<Data> queryUserData(String newText, int user_id) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where user_id = ? " +
                "and content like ? order by exactTime desc", Integer.toString(user_id), "%" + newText + "%");
        return dataList;
    }

    public List<Data> queryPending(String newText) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where pending = ? " +
                "and content like ? order by exactTime desc", "2", "%" + newText + "%");
        return dataList;
    }

    public List<Data> queryUserPending(String newText, int user_id) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where pending = ? " +
                " and user_id = ? and content like ? order by exactTime desc", "2", Integer.toString(user_id), "%" + newText + "%");
        return dataList;
    }

    public List<Data> queryReminder(String newText) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where reminder = ? " +
                "and content like ? order by exactTime desc", "2", "%" + newText + "%");
        return dataList;
    }

    public List<Data> queryUserReminder(String newText, int user_id) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where reminder = ? " +
                " and user_id = ? and content like ? order by exactTime desc", "2", Integer.toString(user_id), "%" + newText + "%");
        return dataList;
    }

    public List<Data> queryFavorites(String newText) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where star = ? " +
                "and content like ? order by exactTime desc", "2", "%" + newText + "%");
        return dataList;
    }

    public List<Data> queryUserFavorites(String newText, int user_id) {
        dataList = getRawData("select date, content, star, pending, reminder from Data where star = ? " +
                " and user_id = ? and content like ? order by exactTime desc", "2", Integer.toString(user_id), "%" + newText + "%");
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

    public static void updateUser(int currentId,String newText) {

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

    public int memoCount(int user_id) {
        int count = DataSupport.where("user_id = ?", Integer.toString(user_id)).count(Data.class);
        return count;
    }

    public int pendingCount(int user_id) {
        Cursor cursor = DataSupport.findBySQL("select id from Data where user_id = ? and pending = ?", Integer.toString(user_id), "2");
        int count = cursor.getCount();
        return count;
    }

    public int reminderCount(int user_id) {
        Cursor cursor = DataSupport.findBySQL("select id from Data where user_id = ? and reminder = ?", Integer.toString(user_id), "2");
        int count = cursor.getCount();
        return count;
    }

    public int favoriteCount(int user_id) {
        Cursor cursor = DataSupport.findBySQL("select id from Data where user_id = ? and star = ?", Integer.toString(user_id), "2");
        int count = cursor.getCount();
        return count;
    }

    public int imageCount(int user_id) {
        dataList = DataSupport.select("imagePath").where("user_id = ?", Integer.toString(user_id)).find(Data.class);
        int count = 0;
        for (int i = 0; i < dataList.size(); i++) {
            Data data = dataList.get(i);
            if (data.getImagePath()!= null && !data.getImagePath().equals("")) {
                count++;
            }
        }
        return count;
    }
}
