package com.example.memorandum.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.memorandum.R;
import com.example.memorandum.dao.UserDAO;
import com.example.memorandum.ui.HTAlertDialog;
import com.example.memorandum.util.AppGlobal;
import com.example.memorandum.util.CommonUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ProfileActivity extends RegisterActivity implements View.OnClickListener {
    private RelativeLayout re_avatar,re_name,re_fxid,re_memo,re_pending, re_star,re_qrcode;
    private ImageView iv_avatar;
    private TextView tv_name;
//    private TextView tv_fxid;
    private TextView tv_memo, tv_pending, tv_star;
    private TextView memo_num, pending_num, star_num;
    private String sex;
    public static final int TAKE_PHOTO = 1;
    public  static final int CHOOSE_PHOTO = 2;
    private Uri head_imageUri;
    private Activity activity = this;
    private static Context sContext = null;
    UserDAO userDAO = new UserDAO();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("用户信息");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolkit);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_name = (TextView) findViewById(R.id.tv_name);
//        tv_fxid = (TextView) findViewById(R.id.tv_fxid);
        tv_memo = (TextView) findViewById(R.id.tv_memo);
        tv_star = (TextView) findViewById(R.id.tv_star);
        tv_pending = (TextView) findViewById(R.id.tv_pending);
        memo_num = (TextView) findViewById(R.id.memo_num);
        pending_num = (TextView) findViewById(R.id.pending_num);
        star_num = (TextView) findViewById(R.id.star_num);
        if (AppGlobal.NAME != null && !AppGlobal.NAME.equals("")) {
            tv_name.setText(AppGlobal.NAME);
        }
        intent = getIntent();
        String currentImagePath = intent.getStringExtra("imagePath");
        Glide.with(getContext()).load(currentImagePath).into(iv_avatar);
        re_avatar = (RelativeLayout) findViewById(R.id.re_avatar);
        re_name= (RelativeLayout) findViewById(R.id.re_name);
//        re_fxid = (RelativeLayout) findViewById(R.id.re_fxid);
        re_memo = (RelativeLayout) findViewById(R.id.re_memo);
        re_pending= (RelativeLayout) findViewById(R.id.re_pending);
        re_star = (RelativeLayout) findViewById(R.id.re_star);
        re_qrcode = (RelativeLayout) findViewById(R.id.re_qrcode);

        re_avatar.setOnClickListener(this);
        re_name.setOnClickListener(this);
//        re_fxid.setOnClickListener(this);
        re_memo.setOnClickListener(this);
        re_pending.setOnClickListener(this);
        re_star.setOnClickListener(this);
        re_qrcode.setOnClickListener(this);

        sContext = this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_avatar:
                showCamera();
                break;
            case R.id.re_name:
//                startActivity(new Intent(getActivity(), ProfileUpdateActivity.class)
//                        .putExtra("type", ProfileUpdateActivity.TYPE_NICK)
//                        .putExtra("default", userJson.getString(HTConstant.JSON_KEY_NICK)));
                break;
//            case R.id.re_fxid:
//                if (TextUtils.isEmpty(userJson.getString(HTConstant.JSON_KEY_FXID))) {
//                    startActivity(new Intent(getActivity(), ProfileUpdateActivity.class)
//                            .putExtra("type", ProfileUpdateActivity.TYPE_FXID));
//                }
//                break;
            case R.id.re_memo:
                break;
            case R.id.re_pending:
                break;
            case R.id.re_star:
                break;
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

    // 拍照部分
    private void showCamera() {
        HTAlertDialog HTAlertDialog = new HTAlertDialog(getApplicationContext(), null, new String[]{getString(R.string.attach_take_pic), getString(R.string.image_manager)});
        HTAlertDialog.init(new HTAlertDialog.OnItemClickListner() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        File outputImage = new File(getExternalCacheDir(), UUID.randomUUID().toString() + ".jpg");
                        String outputImagePath = outputImage.getAbsolutePath();
                        AppGlobal.currentImagePath = outputImagePath;
//                        Log.d("RegisterActivity", outputImagePath);
//                        Toast.makeText(RegisterActivity.this, outputImagePath, Toast.LENGTH_SHORT).show();

                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            head_imageUri = FileProvider.getUriForFile(ProfileActivity.this, "com.exmaple.memorandum.fileprovider", outputImage);
                        }
                        else {
                            head_imageUri = Uri.fromFile(outputImage);
                        }
                        openCamera();
                        break;
//
                    case 1:
                        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProfileActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {

                            openAlbum();
                        }
                        break;
//
                }
            }
        });
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, head_imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                    openCamera();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(head_imageUri));
                        iv_avatar.setImageBitmap(bitmap);
//                        AppGlobal.INSERT_IMAGE = true;
                        userDAO.updateImagePath(AppGlobal.currentImagePath, AppGlobal.USERNAME);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://download/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            }
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }
            displayImage(imagePath);
        }
    }
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            Log.d("RegisterActivity", imagePath);
            File albumImage = new File(getExternalCacheDir(), UUID.randomUUID().toString() + ".jpg");
            String albumImagePath = albumImage.getAbsolutePath();
            AppGlobal.currentImagePath = albumImagePath;
            Log.d("RegisterActivity", albumImagePath);
            Toast.makeText(this, albumImagePath, Toast.LENGTH_SHORT).show();
            try {
                if (albumImage.exists()) {
                    albumImage.delete();
                }
                FileOutputStream out;
                out = new FileOutputStream(albumImage);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            iv_avatar.setImageBitmap(bitmap);
            userDAO.updateImagePath(AppGlobal.currentImagePath, AppGlobal.USERNAME);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    public Activity getActivity() {
        return activity;
    }

}
