package com.example.memorandum.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.memorandum.R;
import com.example.memorandum.dao.UserDAO;
import com.example.memorandum.util.AppGlobal;

import solid.ren.skinlibrary.base.SkinBaseActivity;

public class LoginActivity extends SkinBaseActivity {
//    private AutoCompleteTextView mEmailView;  //用户名
    private EditText mEmailView;  //用户名
    private EditText mPasswordView;           //密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("用户登录");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolkit);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
//        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);   //用户名控件
        mEmailView = (EditText) findViewById(R.id.email);   //用户名控件

        mPasswordView = (EditText) findViewById(R.id.password);         //密码控件
        //登录按钮
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();//调用函数检查登陆信息是否合法
            }
        });
        //注册按钮
        Button registButton= (Button) findViewById(R.id.register);
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * 输入信息的检查
     */
    private void attemptLogin() {

        // 初始化错误信息为null
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // 获取输入信息.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;//是否是非法信息
        View focusView = null;

        // 检查密码是否有效
        if ( !TextUtils.isEmpty(password) && !isPasswordValid(password) ) {
            mPasswordView.setError("密码太短，请至少输入4位");
            focusView = mPasswordView;
            cancel = true;
        }

        //  检查邮箱
        if ( TextUtils.isEmpty(email) ) {
            mEmailView.setError("用户名无效");
            focusView = mEmailView;
            cancel = true;
        }

        if ( cancel ) {//非法信息
            focusView.requestFocus();//标签用于指定屏幕内的焦点View。
        } else {//合法信息
            //登陆跳转逻辑
            UserDAO userDAO=new UserDAO();
            boolean sussess=userDAO.checkLogin(email,password);
            if(sussess){  //信息合法
                AppGlobal.NAME=userDAO.findNameByUsername(email);//保存用户登录信息到全局变量中
                AppGlobal.USERNAME=email;
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
