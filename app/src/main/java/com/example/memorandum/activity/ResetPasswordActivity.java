package com.example.memorandum.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.memorandum.R;
import com.example.memorandum.bean.User;
import com.example.memorandum.dao.UserDAO;
import com.example.memorandum.util.AppGlobal;
import com.example.memorandum.util.CommonUtility;

import solid.ren.skinlibrary.base.SkinBaseActivity;

public class ResetPasswordActivity extends SkinBaseActivity implements View.OnClickListener {
    private EditText et_usertel;
    private EditText et_code;
    private Button btn_code;
    private EditText et_password;
    private EditText et_password_confirm;
    private Button btn_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setTitle("重置密码");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolkit);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        et_usertel = (EditText) findViewById(R.id.et_usertel);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_code = (Button) findViewById(R.id.btn_code);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_code.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        if (AppGlobal.USERNAME != null && !AppGlobal.USERNAME.equals("")) {
            et_usertel.setText(AppGlobal.USERNAME);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_code:
                et_code.setText(CommonUtility.getRandNum(6));
                break;
            case R.id.btn_ok:
                attemptReset();
                break;

        }
    }

    private void attemptReset() {
        et_usertel.setError(null);
        et_code.setError(null);
        et_password.setError(null);
        et_password_confirm.setError(null);

        String userName = et_usertel.getText().toString().trim();
        String code = et_code.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String password_confirm = et_password_confirm.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(userName)) {
            et_usertel.setError("请输入手机号");
            focusView = et_usertel;
            cancel = true;
        }

        if (!userName.equals(AppGlobal.USERNAME)) {
            et_usertel.setError("请输入正确的手机号以验证身份");
            focusView = et_usertel;
            cancel = true;
        }

        if (TextUtils.isEmpty(code)) {
            et_code.setError("请输入验证码");
            focusView = et_code;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            et_password.setError("请输入密码");
            focusView = et_password;
            cancel = true;
        }
        if (TextUtils.isEmpty(password_confirm)) {
            et_password_confirm.setError("请输入确认密码");
            focusView = et_password_confirm;
            cancel = true;
        }

        if ( !TextUtils.isEmpty(password) && !isPasswordValid(password) ) {
            et_password.setError("密码过短，请至少输入4位");
            focusView = et_password;
            cancel = true;
        }
        if (!password.equals(password_confirm)) {
            et_password_confirm.setError("两次输入的密码不一致");
            focusView = et_password_confirm;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        else {
            User user = new User(userName, password);
            UserDAO userDAO = new UserDAO();
            boolean isSuccess = false;
            if (!userDAO.checkUsername(userName)) {
                et_usertel.setError("您输入的用户不存在");
                focusView = et_usertel;
                cancel = true;
            }
            else {
                isSuccess = userDAO.checkPassword(userName, password);
                if (isSuccess) {
                    userDAO.resetPassword(userName, password);
                    Toast.makeText(ResetPasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(ResetPasswordActivity.this, "修改失败，新密码不能和旧密码相同", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }
}
