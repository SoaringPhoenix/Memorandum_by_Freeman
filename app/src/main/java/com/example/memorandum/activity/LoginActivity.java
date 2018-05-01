package com.example.memorandum.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.memorandum.R;
import com.example.memorandum.dao.UserDAO;
import com.example.memorandum.util.AppGlobal;

import solid.ren.skinlibrary.base.SkinBaseActivity;

public class LoginActivity extends SkinBaseActivity implements View.OnClickListener {
//    private AutoCompleteTextView mEmailView;  //用户名
    private EditText mEmailView;  //用户名
    private EditText mPasswordView;           //密码
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("用户登录");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolkit);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEmailView = (EditText) findViewById(R.id.email);   //用户名控件
        mPasswordView = (EditText) findViewById(R.id.password);         //密码控件
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        boolean isRemember = preferences.getBoolean("remember_password", false);
        if (isRemember) {
            String userName = preferences.getString("userName", "");
            String password = preferences.getString("password", "");
            mEmailView.setText(userName);
            mPasswordView.setText(password);
            rememberPass.setChecked(true);
        }
        //登录按钮
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);
        //注册按钮
        Button registButton= (Button) findViewById(R.id.register);
        registButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                attemptLogin();//调用函数检查登陆信息是否合法
                break;
            case R.id.register:
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                break;
        }
    }
    /**
     * 输入信息的检查
     */
    private void attemptLogin() {

        // 初始化错误信息为null
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // 获取输入信息.
        String userName = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;//是否是非法信息
        View focusView = null;

        //  检查邮箱
        if ( TextUtils.isEmpty(userName) ) {
            mEmailView.setError("请输入用户名");
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)  ) {
            mPasswordView.setError("请输入密码");
            focusView = mPasswordView;
            cancel = true;
        }

        // 检查密码是否有效
        if ( !TextUtils.isEmpty(password) && !isPasswordValid(password) ) {
            mPasswordView.setError("密码太短，请至少输入4位");
            focusView = mPasswordView;
            cancel = true;
        }

        if ( cancel ) {//非法信息
            focusView.requestFocus();//标签用于指定屏幕内的焦点View。
        } else {//合法信息
            //登陆跳转逻辑
            UserDAO userDAO=new UserDAO();
            boolean sussess=userDAO.checkLogin(userName,password);
            if(sussess){  //信息合法
                AppGlobal.NAME=userDAO.findNameByUsername(userName);//保存用户登录信息到全局变量中
                AppGlobal.USERNAME=userName;
                editor = preferences.edit();
                if (rememberPass.isChecked()) {
                    editor.putBoolean("remember_password", true);
                    editor.putString("userName", userName);
                    editor.putString("password", password);
                }
                else {
                    editor.clear();
                }
                editor.apply();
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this,MainActivity.class);
                LoginActivity.this.startActivity(intent);
            }
            else {
                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 密码是否合法：至少需要4位
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

}
