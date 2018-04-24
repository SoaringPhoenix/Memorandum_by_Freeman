package com.example.memorandum.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.memorandum.R;
import com.example.memorandum.bean.User;
import com.example.memorandum.dao.UserDAO;
import com.example.memorandum.ui.HTAlertDialog;
import com.example.memorandum.util.CommonUtility;
import com.example.memorandum.util.RegisterContract;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import solid.ren.skinlibrary.base.SkinBaseActivity;

public class RegisterActivity extends SkinBaseActivity implements View.OnClickListener {
//    private AutoCompleteTextView mEmailView;  //用户名
    private EditText mEmailView;  //用户名
    private EditText mPasswordView;           //密码
    private EditText repasswordView;              //确认密码
    private EditText nicknameView;                  //昵称
    private EditText etCode;
    private Button mEmailSignInButton, codeButton;
    private ImageView iv_show, iv_hide, iv_show_re, iv_hide_re;
    private ImageView iv_photo;
    private String imagePathOrigin = null;
    private static final int REQUEST_CODE=100;
    private Activity activity = null;
    private String title;
    private String[] items;
    private static Context sContext = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("新用户注册");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolkit);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mEmailView = (EditText) findViewById(R.id.email);//查找用户名控件
        mPasswordView = (EditText) findViewById(R.id.password);      //查找密码控件
        repasswordView = (EditText) findViewById(R.id.repassword);         //重复密码控件
        nicknameView = (EditText) findViewById(R.id.nickname);                 //昵称控件
        etCode = (EditText) findViewById(R.id.et_code);

        //注册按钮
         mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
         codeButton = (Button) findViewById(R.id.btn_code);
        iv_show = (ImageView) findViewById(R.id.iv_show);
        iv_show_re = (ImageView) findViewById(R.id.iv_show_re);
        iv_hide = (ImageView) findViewById(R.id.iv_hide);
        iv_hide_re = (ImageView) findViewById(R.id.iv_hide_re);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        mEmailSignInButton.setOnClickListener(this);
        codeButton.setOnClickListener(this);
        iv_show.setOnClickListener(this);
        iv_show_re.setOnClickListener(this);
        iv_hide.setOnClickListener(this);
        iv_hide_re.setOnClickListener(this);
        iv_photo.setOnClickListener(this);
        activity = this;
        sContext = this;
    }
    public static Context getContext() {
        return sContext;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                attemptRegister();//调用函数检查登陆信息是否合法
                break;
            case R.id.btn_code:
                etCode.setText(CommonUtility.getRandNum(6));
                break;
            case R.id.iv_show:
                showPassword();
                editTextEnd();
                break;
            case R.id.iv_show_re:
                showRepassword();
                editTextReEnd();
                break;
            case R.id.iv_hide:
                hidePassword();
                editTextEnd();
                break;
            case R.id.iv_hide_re:
                hideRepassword();
                editTextReEnd();
                break;
            case R.id.iv_photo:
                showCamera();
                break;
            default:
                break;
        }
    }


    public void hidePassword() {
        iv_hide.setVisibility(View.GONE);
        iv_show.setVisibility(View.VISIBLE);
        mPasswordView
                .setTransformationMethod(HideReturnsTransformationMethod
                        .getInstance());
    }
    public void hideRepassword() {
        iv_hide_re.setVisibility(View.GONE);
        iv_show_re.setVisibility(View.VISIBLE);
        repasswordView
                .setTransformationMethod(HideReturnsTransformationMethod
                        .getInstance());
    }

    public void showPassword() {
        iv_show.setVisibility(View.GONE);
        iv_hide.setVisibility(View.VISIBLE);
        mPasswordView
                .setTransformationMethod(PasswordTransformationMethod
                        .getInstance());
    }
    public void showRepassword() {
        iv_show_re.setVisibility(View.GONE);
        iv_hide_re.setVisibility(View.VISIBLE);
        repasswordView
                .setTransformationMethod(PasswordTransformationMethod
                        .getInstance());
    }

    // 切换后将密码EditText光标置于末尾
    private void editTextEnd() {
        CharSequence charSequence = mPasswordView.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
    // 切换后将密码EditText光标置于末尾
    private void editTextReEnd() {
        CharSequence charSequence = repasswordView.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
    /**
     * 输入的检查
     */
    private void attemptRegister() {

        // 初始化控件错误信息
        nicknameView.setError(null);
        mEmailView.setError(null);
        etCode.setError(null);
        mPasswordView.setError(null);
        repasswordView.setError(null);


        // 获取输入信息.
        String nickName = nicknameView.getText().toString().trim();
        String email = mEmailView.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        String repassword = repasswordView.getText().toString().trim();


        boolean cancel = false;
        View focusView = null;

        //检查真实姓名
        if(nickName.equals("")){
            nicknameView.setError("昵称不能为空");
            focusView = nicknameView;
            cancel = true;
        }
        // 检查邮箱
        if ( TextUtils.isEmpty(email) ) {
            mEmailView.setError("请输入手机号");
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(code)) {
            etCode.setError("请输入验证码");
            focusView = etCode;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("请输入密码");
            focusView = etCode;
            cancel = true;
        }
        if (TextUtils.isEmpty(repassword)) {
            repasswordView.setError("请输入确认密码");
            focusView = etCode;
            cancel = true;
        }
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


        if ( cancel ) {
            focusView.requestFocus();
        } else {
            //注册逻辑实现
            /*-------------------------------*/
            User user = new User(email, password, nickName);
            UserDAO userDAO=new UserDAO();
            boolean isSucess=false;
            if(userDAO.checkUsername(email)){
                Toast.makeText(RegisterActivity.this, "用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();
            }
            else{
                isSucess= userDAO.insert(user); //添加到数据库
                if (isSucess){  //合法信息
                    Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(RegisterActivity.this,LoginActivity.class);//转到登陆
                    RegisterActivity.this.startActivity(intent);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "信息不合法，请确认输入", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private String getAvatarName() {

//        return HTApp.getInstance().getDirFilePath() + "org_" + System.currentTimeMillis() + ".png";
        return getExternalCacheDir() + "head_image.jpg";
    }
    // 拍照部分
    private void showCamera() {
        HTAlertDialog HTAlertDialog = new HTAlertDialog(getApplicationContext(), null, new String[]{getString(R.string.attach_take_pic), getString(R.string.image_manager)});
        HTAlertDialog.init(new HTAlertDialog.OnItemClickListner() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(int position) {
                imagePathOrigin = getAvatarName();
                switch (position) {
                    case 0:
                        if(!checkPermission(Manifest.permission.CAMERA)){
                            return;
                        }

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(imagePathOrigin)));
                        getActivity().startActivityForResult(intent, RegisterContract.PHOTO_REQUEST_TAKEPHOTO);
                        break;
                    case 1:
                        if( !checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ){
                            return;
                        }

                        Crop.pickImage(getActivity(), RegisterContract.PHOTO_REQUEST_GALLERY);
                        break;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkPermission(String permissionName) {
        PackageManager pm = getApplicationContext().getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(permissionName,  getApplicationContext().getPackageName()));
        if (permission) {
            return true;
        }else {
            Toast.makeText(RegisterActivity.this, getString(R.string.no_permission_camera), Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{permissionName},
                    REQUEST_CODE);
            return false;

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

    public Activity getActivity() {
        return activity;
    }
}
