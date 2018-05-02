package com.example.memorandum.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/4/3.
 */

public class User extends DataSupport{
    private int id;
    @Column(nullable = false)
    private String userName; //用户名，用来唯一标识用户的token
    @Column(nullable = false)
    private String password; //用户密码，最少4位
    @Column(nullable = false)
    private String nickName; //用户昵称，可在app中修改，可重复
    private String imagePath; //用户头像的图片绝对路径
    private List<Data> dataList = new ArrayList<>(); //用户的对应一个或多个data项的外键集合

    public User() {

    }
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public User(String userName, String password, String nickName) {
        this.userName = userName;
        this.password = password;
        this.nickName = nickName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }
}
