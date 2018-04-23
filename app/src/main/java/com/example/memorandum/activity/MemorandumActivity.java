package com.example.memorandum.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memorandum.R;
import com.example.memorandum.bean.Data;
import com.example.memorandum.dao.DataDAO;
import com.example.memorandum.service.AlarmService;
import com.example.memorandum.ui.GooeyMenu;
import com.example.memorandum.ui.RichEditText;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.litepal.crud.DataSupport;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import solid.ren.skinlibrary.base.SkinBaseActivity;

import static com.example.memorandum.util.CommonUtility.resizeImage;

public class MemorandumActivity extends SkinBaseActivity implements GooeyMenu.GooeyMenuInterface, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private RichEditText richEditText;
    private TextView textView;
    private GooeyMenu gooeyMenu;
    private Toast mToast;
    int currentId;
    int currentStar;
    int currentPending;
    int currentReminder;
    String newContent;
    String currentContent;
    String currentDate;
    String currentTime;
    Intent intent;
    Data data = new Data();
    DataDAO dataDAO = new DataDAO();

    private static final int PHOTO_SUCCESS = 1;
    private static final int CAMERA_SUCCESS = 2;

    DatePickerDialog datePickerDialog = null;
    TimePickerDialog timePickerDialog = null;
    String dateSet = "";
    String timeSet = "";
    StringBuilder timeSetTotal = new StringBuilder();
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    private static final String TAG = "MemorandumActivity";
    private static Context sContext = null;

    public static Context getContext() {
        return sContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_memorandum);
        sContext = this;
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

        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

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
                Data data = new Data(currentId);
                currentContent = intent.getStringExtra("content");
                currentDate = simpleDateFormat.format(date);
                currentPending = data.getPending();
                currentReminder = data.getReminder();
                currentStar = data.getStar();
                if (!TextUtils.isEmpty(richEditText.getText())) {
                    if (currentContent == null || currentContent == "") { //目前没有内容，此处为插入
                        newContent = richEditText.getText().toString();
                        Data newData = new Data(currentDate, newContent, date, currentPending, currentReminder, currentStar);
                        dataDAO.insertData(newData);
                    }
                    else if (currentContent != null && currentContent != "" && !(richEditText.getText().toString().equals(currentContent))) { //有内容，此处为更新
                        newContent = richEditText.getText().toString();
                        Data newData = new Data(currentDate, newContent, date, currentPending, currentReminder, currentStar);
                        dataDAO.updateData(newData, currentId);
//                        data.updateAll("date = ? and content = ?",currentDate, currentContent);
                    }
                }
                else {
                    dataDAO.deleteData(currentId);
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
        Data data = getIntent().getParcelableExtra("data");
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
                Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(getImageByCamera, CAMERA_SUCCESS);
                break;
            case 3:
                showToast("reminder");

                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);


                if (data.getReminder() == 2) {
                    data.setReminder(1);
                    data.update(currentId);
                    currentReminder = data.getReminder();
//                    Log.d(TAG, String.valueOf(currentReminder));
                    showToast("取消提醒");
                } else {
                    data.setReminder(2);
                    data.update(currentId);
                    currentReminder = data.getReminder();
//                    Log.d(TAG, String.valueOf(currentReminder));
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
                } else {
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
                } else {
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
                    if (bitmap != null) {
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(MemorandumActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString("[local]" + 1 + "[/local]");
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = richEditText.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = richEditText.getEditableText();
                        if (index < 0 || index >= edit_text.length()) {
                            edit_text.append(spannableString);
                        } else {
                            edit_text.insert(index, spannableString);
                        }
                    } else {
                        Toast.makeText(MemorandumActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CAMERA_SUCCESS:
                    Bundle extras = intent.getExtras();
                    Bitmap originalBitmap1 = (Bitmap) extras.get("data");
                    if (originalBitmap1 != null) {
                        bitmap = resizeImage(originalBitmap1, 720, 1280);
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(MemorandumActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString("[local]" + 1 + "[/local]");
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = richEditText.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = richEditText.getEditableText();
                        if (index < 0 || index >= edit_text.length()) {
                            edit_text.append(spannableString);
                        } else {
                            edit_text.insert(index, spannableString);
                        }
                    } else {
                        Toast.makeText(MemorandumActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void showToast(String msg) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
//        dateSet = year + "-" + month + "-" + day;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%d-%02d-%02d", year, month + 1, day));
        dateSet = stringBuilder.toString();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        timeSetTotal.delete(0, timeSetTotal.length());
//        timeSet = " " + hourOfDay + ":" + minute;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(hourOfDay < 10 ? "0" + hourOfDay : hourOfDay).append(":").append(minute < 10 ? "0" + minute : minute);
        timeSet = stringBuilder.toString();
        timeSetTotal = timeSetTotal.append(dateSet).append(" ").append(timeSet);
        Data data = getIntent().getParcelableExtra("data");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date;
        long notifyTime = 0;
        long currentTime = System.currentTimeMillis();
        try {
            date = simpleDateFormat.parse(timeSetTotal.toString());
            notifyTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i(TAG, "我擦，转换出错了");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "出了其他的错误啊");
        }
        if (notifyTime <= currentTime) {
            showToast("选择时间不能小于当前时间");
            return;
        }
        Log.i(TAG, timeSetTotal.toString());
        Log.i(TAG, "选择时间: " + Integer.toString((int) notifyTime));
        Log.i(TAG, "真实时间: " + Integer.toString((int) currentTime));
        int delayTime = (int) (notifyTime - currentTime);
        AlarmService.addNotification(delayTime, "tick", "您有一条新提醒", data.getContent(), data.getDate(), data.getId());
    }
}
