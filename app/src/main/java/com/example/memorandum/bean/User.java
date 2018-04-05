package com.example.memorandum.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/4/3.
 */

public class User {
    private int id;
    private String name;
    private String password;
    private String mail;
    private List<Data> dataList = new ArrayList<>();
}
