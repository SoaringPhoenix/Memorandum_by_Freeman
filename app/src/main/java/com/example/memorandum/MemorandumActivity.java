package com.example.memorandum;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.memorandum.bean.Data;
import com.example.memorandum.ui.GooeyMenu;
import com.example.memorandum.ui.RichEditText;

import org.litepal.crud.DataSupport;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.example.memorandum.util.CommonUtility.resizeImage;

public class MemorandumActivity extends AppCompatActivity implements GooeyMenu.GooeyMenuInterface {
    private RichEditText richEditText;
    private TextView textView;
    private ImageView data_star;
    private GooeyMenu gooeyMenu;
    private Toast mToast;
    int currentId;
    int currentStar;
    int currentPending;
    int currentReminder;
    String currentContent;
    String currentDate;
    String currentTime;
    Intent intent;
    Data data = new Data();
    private static final int PHOTO_SUCCESS = 1;
    private static final int CAMERA_SUCCESS = 2;

    private static final String TAG = "MemorandumActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorandum);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        intent = getIntent();
        gooeyMenu = (GooeyMenu) findViewById(R.id.gooey_menu);
        gooeyMenu.setOnMenuListener(this);
        textView = (TextView) findViewById(R.id.exact_time);
        richEditText = (RichEditText) findViewById(R.id.input);
//        textView.bringToFront();
//        richEditText.bringToFront();
        gooeyMenu.bringToFront();
        currentId = intent.getIntExtra("id", 0);
        currentContent = intent.getStringExtra("content");
        currentDate = intent.getStringExtra("date");
        currentTime = intent.getStringExtra("exactTime");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date(System.currentTimeMillis());
        richEditText.setText(currentContent);
        textView.setText(currentDate);
        if (TextUtils.isEmpty(textView.getText())) {
            textView.setText(simpleDateFormat.format(date));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
            case R.id.confirm:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                Date date = new Date(System.currentTimeMillis());
                currentId = intent.getIntExtra("id", 0);
                currentContent = intent.getStringExtra("content");
//                currentTime = intent.getStringExtra("exactTime");

                if (!TextUtils.isEmpty(richEditText.getText())) {
                    if (currentContent == null || currentContent == "") {
                        data.setDate(simpleDateFormat.format(date));
                        data.setExactTime(date);
                        data.setContent(richEditText.getText().toString());
                        data.save();
                    } else if (currentContent != null && currentContent != "" && !(richEditText.getText().toString().equals(currentContent))) {
                        data.setDate(simpleDateFormat.format(date));
                        data.setExactTime(date);
                        data.setContent(richEditText.getText().toString());
                        data.update(currentId);
//                        data.updateAll("date = ? and content = ?",currentDate, currentContent);
                    }
                } else {
                    DataSupport.delete(Data.class, currentId);
                }
                finish();
                break;
            default:
        }
        return true;
    }

    @Override
    public void menuOpen() {
//        showToast("Menu Open");

    }

    @Override
    public void menuClose() {
//        showToast( "Menu Close");
    }

    @Override
    public void menuItemClicked(int menuNumber) {

        currentId = intent.getIntExtra("id", 0);
        Data data =  getIntent().getParcelableExtra("data");
        switch (menuNumber) {
            case 1:
//                showToast("insert picture");
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                getImage.setType("image/*");
                startActivityForResult(getImage, PHOTO_SUCCESS);
                break;
            case 2:
//                showToast("insert photo");
                Intent getImageByCamera= new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(getImageByCamera, CAMERA_SUCCESS);
                break;
            case 3:
                showToast("reminder");
                if (data.getReminder() == 2) {
                    data.setReminder(1);
                    data.update(currentId);
                    currentReminder = data.getReminder();
                    Log.d(TAG, String.valueOf(currentReminder));
                    showToast("取消提醒");
                }
                else {
                    data.setReminder(2);
                    data.update(currentId);
                    currentReminder = data.getReminder();
                    Log.d(TAG, String.valueOf(currentReminder));
                    showToast("已提醒");
                }
                break;
            case 4:
                showToast("pending");
                if (data.getPending() == 2) {
                    data.setPending(1);
                    data.update(currentId);
                    currentPending = data.getPending();
                    Log.d(TAG, String.valueOf(currentPending));
                    showToast("取消待办");
                }
                else {
                    data.setPending(2);
                    data.update(currentId);
                    currentPending = data.getPending();
                    Log.d(TAG, String.valueOf(currentPending));
                    showToast("已待办");
                }
                break;
            case 5:
                showToast("favorite");
//                List<Data> stars;
//                stars = DataSupport.select("star").where("id = ?", String.valueOf(currentId)).find(Data.class);
//                for (Data star: stars) {
//                    currentStar = star.getStar();
//                    Log.d(TAG, String.valueOf(currentStar));
//                }
                if (data.getStar() == 2) {
                    data.setStar(1);
                    data.update(currentId);
                    currentStar = data.getStar();
                    Log.d(TAG, String.valueOf(currentStar));
                    showToast("取消收藏");
                }
                else {
                    data.setStar(2);
                    data.update(currentId);
                    currentStar = data.getStar();
                    Log.d(TAG, String.valueOf(currentStar));
                    showToast("已收藏");
                }
                break;
            default:
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_SUCCESS:
                    //获得图片的uri
                    Uri originalUri = intent.getData();
                    Bitmap bitmap = null;
                    Bitmap originalBitmap = null;
                    try {
                        originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
                        bitmap = resizeImage(originalBitmap, 720, 1280);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(bitmap != null){
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(MemorandumActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString("[local]"+1+"[/local]");
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = richEditText.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = richEditText.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                        }else{
                            edit_text.insert(index, spannableString);
                        }
                    }else{
                        Toast.makeText(MemorandumActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CAMERA_SUCCESS:
                    Bundle extras = intent.getExtras();
                    Bitmap originalBitmap1 = (Bitmap) extras.get("data");
                    if(originalBitmap1 != null){
                        bitmap = resizeImage(originalBitmap1, 720, 1280);
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(MemorandumActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString("[local]"+1+"[/local]");
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index =  richEditText.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = richEditText.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                        }else{
                            edit_text.insert(index, spannableString);
                        }
                    }else{
                        Toast.makeText(MemorandumActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
    public void showToast(String msg){
        if(mToast!=null){
            mToast.cancel();
        }
        mToast= Toast.makeText(this,msg,Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }
}
