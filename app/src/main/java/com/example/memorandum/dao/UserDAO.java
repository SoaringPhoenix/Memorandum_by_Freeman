package com.example.memorandum.dao;

import com.example.memorandum.bean.Data;
import com.example.memorandum.bean.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/4/22.
 */

public class UserDAO {
    private static List<User> userList = new ArrayList<>(); //保存用户信息


    public boolean checkUsername(String userName) {
        userList = DataSupport.select("userName").find(User.class);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            if (userName.equals(user.getUserName())) {
                return true;
            }
        }
        return false;
    }

    public boolean insert(User user) {
        if (checkUsername(user.getUserName())) { //用户名存在返回失败
            return false;
        }
        try {
            userList.add(user);
            User newUser = new User();
            newUser.setUserName(user.getUserName());
            newUser.setPassword(user.getPassword());
            newUser.setNickName(user.getNickName());
            newUser.save();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String findNameByUsername(String userName) {
        userList = DataSupport.select("userName", "nickName").find(User.class);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            if (userName.equals(user.getUserName())) {
                return user.getNickName();
            }
        }
        return "";
    }

    public boolean checkLogin(String userName, String password) {
        userList = DataSupport.select("userName", "password").find(User.class);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            if (userName.equals(user.getUserName()) && password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPassword(String userName, String password) {
        userList = DataSupport.select("userName", "password").where("userName = ?", userName).find(User.class);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            if (!password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public void resetPassword(String userName, String password) {
        userList = DataSupport.select("userName", "password").where("userName = ?", userName).find(User.class);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            user.setPassword(password);
            user.updateAll("userName = ?", userName);
        }
    }

}
