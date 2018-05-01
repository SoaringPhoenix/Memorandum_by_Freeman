package com.example.memorandum.util;

/**
 * Created by jason on 2018/4/22.
 * 存放当前用户名和昵称
 */

public class AppGlobal {
    public static  String USERNAME="";     //当前登录用户用户名
    public static  String NAME="";         //当前登录用户昵称
    public static boolean INSERT_IMAGE = false; //当前是否插入头像
    public static String currentImagePath = "";

    public static void main(String[] args) {
        System.out.println(USERNAME);
    }
}
