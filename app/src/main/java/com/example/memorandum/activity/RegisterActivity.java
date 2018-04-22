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
import com.example.memorandum.bean.User;
import com.example.memorandum.dao.UserDAO;

public class RegisterActivity extends AppCompatActivity {
    private AutoCompleteTextView mEmailView;  //用户名
    private EditText mPasswordView;           //密码
    private EditText repasswordView;              //确认密码
    private EditText nicknameView;                  //昵称
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("新用户注册");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolkit);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);//查找用户名控件
        mPasswordView = (EditText) findViewById(R.id.password);      //查找密码控件
        repasswordView = (EditText) findViewById(R.id.repassword);         //重复密码控件
        nicknameView = (EditText) findViewById(R.id.nickname);                 //昵称控件

        //注册按钮
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();//调用函数检查登陆信息是否合法
            }
        });
    }
    /**
     * 输入的检查
     */
    private void attemptLogin() {

        // 初始化控件错误信息
        mEmailView.setError(null);
        mPasswordView.setError(null);
        repasswordView.setError(null);

        // 获取输入信息.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String repassword = repasswordView.getText().toString();
        String nickName = nicknameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检查密码是否有效
        if ( !TextUtils.isEmpty(password) && !isPasswordValid(password) ) {
            mPasswordView.setError("密码过短，请至少输入4位");
            focusView = mPasswordView;
            cancel = true;
        }

        if(!repassword.equals(password)){
            repasswordView.setError("两次密码不一致");
            focusView = repasswordView;
            cancel = true;
        }

        // 检查邮箱
        if ( TextUtils.isEmpty(email) ) {
            mEmailView.setError("用户名无效");
            focusView = mEmailView;
            cancel = true;
        }



        //检查真实姓名
        if(nickName.equals("")){
            nicknameView.setError("真实姓名不能为空");
            focusView = nicknameView;
            cancel = true;
        }



        if ( cancel ) {
            focusView.requestFocus();
        } else {
            //注册逻辑实现
            /*-------------------------------*/

//            String memEmailViewmail = mEmailView.getText().toString();
//            String mrepassword = repasswordView.getText().toString();
//            String nickName = nicknameView.getText().toString();
            User user = new User(email, password, nickName);

            UserDAO userDAO=new UserDAO();
            boolean isSucess=false;

            if(userDAO.checkUsername(email)){
                Toast.makeText(RegisterActivity.this, "用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();

            }
            else{

                isSucess= userDAO.insert(user); //添加到数据库

                if(isSucess){  //合法信息
                    Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(RegisterActivity.this,LoginActivity.class);//转到登陆
                    RegisterActivity.this.startActivity(intent);
            /*-------------------------------*/
                }
                else {
                    Toast.makeText(RegisterActivity.this, "信息不合法，请确认输入", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }
    /**
     * 密码是否和非法，至少需要4位
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }
}
