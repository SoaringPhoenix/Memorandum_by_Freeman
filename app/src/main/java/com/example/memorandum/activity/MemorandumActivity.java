package com.example.memorandum.activity;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memorandum.R;
import com.example.memorandum.bean.Data;
import com.example.memorandum.bean.User;
import com.example.memorandum.dao.DataDAO;
import com.example.memorandum.dao.UserDAO;
import com.example.memorandum.service.AlarmService;
import com.example.memorandum.ui.GooeyMenu;
import com.example.memorandum.ui.RichEditText;
import com.example.memorandum.util.AppGlobal;
import com.example.memorandum.util.BitmapToPathUtil;
import com.example.memorandum.util.UriToPathUtil;
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
    String currentImagePath;
    Intent intent;
    Data data = new Data();
    DataDAO dataDAO = new DataDAO();
    UserDAO userDAO = new UserDAO();

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

//    SpannableString spannableString = new SpannableString("[local]" + 2 + "[/local]");
    SpannableString spannableString = new SpannableString(" ");


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
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
//        View confirm = toolbar.findViewById(R.id.confirm);
//        confirm.setVisibility(View.GONE);
        intent = getIntent();
        gooeyMenu = (GooeyMenu) findViewById(R.id.gooey_menu);
        gooeyMenu.setOnMenuListener(this);
        textView = (TextView) findViewById(R.id.exact_time);
        richEditText = (RichEditText) findViewById(R.id.input);
        richEditText.setCursorVisible(false);
        richEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    richEditText.setCursorVisible(true);
                    View confrim = toolbar.findViewById(R.id.confirm);
                    confrim.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        gooeyMenu.bringToFront();
        currentId = intent.getIntExtra("id", 0);
        currentContent = intent.getStringExtra("content");
        currentDate = intent.getStringExtra("date");
        currentTime = intent.getStringExtra("exactTime");
        Log.i(TAG, Integer.toString(intent.getIntExtra("reminder", 0)));
//        if (intent.getIntExtra("reminder", 0) == 0) {
//            Data data = new Data(currentId);
//            data.setReminder(1);
//            data.update(currentId);
//            Log.i(TAG, Integer.toString(data.getReminder()));
//        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date(System.currentTimeMillis());
        richEditText.setText(currentContent);
        textView.setText(currentDate);
//        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        richEditText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (TextUtils.isEmpty(textView.getText())) {
            textView.setText(simpleDateFormat.format(date));
        }
        currentImagePath = intent.getStringExtra("imagePath");
//        Log.i(TAG, currentImagePath);
        if (currentImagePath != null && !currentImagePath.equals("")) {
            richEditText.insertBitmap(currentImagePath);
//            richEditText.setText(currentContent);
        }
//        richEditText.insertBitmap(currentImagePath);
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
                break;
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
                if (!TextUtils.isEmpty(richEditText.getText().toString().trim())) {
                    if (currentContent == null || currentContent == "") { //目前没有内容，此处为插入
                        if (AppGlobal.USERNAME != null && !AppGlobal.USERNAME.equals("")) {
                            User currentUser = userDAO.findUser(AppGlobal.USERNAME);
                            newContent = richEditText.getText().toString();
                            Data newData = new Data(currentDate, newContent, date, currentPending, currentReminder, currentStar, currentUser);
                            dataDAO.insertUserData(newData);
//                            Data newData = new Data(currentDate, newContent, date, currentPending, currentReminder, currentStar);
//                            dataDAO.insertData(newData);
                        }
                        else {
                            newContent = richEditText.getText().toString();
                            Data newData = new Data(currentDate, newContent, date, currentPending, currentReminder, currentStar);
                            dataDAO.insertData(newData);
                        }
                    } else if (currentContent != null && currentContent != "" && !(richEditText.getText().toString().equals(currentContent))) { //有内容，此处为更新
                        if (AppGlobal.USERNAME != null && !AppGlobal.USERNAME.equals("")) {
                            User currentUser = userDAO.findUser(AppGlobal.USERNAME);
                            newContent = richEditText.getText().toString();
                            Data newData = new Data(currentDate, newContent, date, currentPending, currentReminder, currentStar, currentUser);
                            dataDAO.updateUserData(newData, currentId);
                        } else {
                            newContent = richEditText.getText().toString();
                            Data newData = new Data(currentDate, newContent, date, currentPending, currentReminder, currentStar);
                            dataDAO.updateData(newData, currentId);
//                        data.updateAll("date = ? and content = ?",currentDate, currentContent);
                        }
                    }
                }
                else {
                    dataDAO.deleteData(currentId);
                }
                View confirm = findViewById(R.id.confirm);
                confirm.setVisibility(View.GONE);
                RichEditText richEditText = (RichEditText) findViewById(R.id.input);
                richEditText.setCursorVisible(false);
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
    public void menuItemClicked(int menuNumber) { //处理GooeyMenu点击事件的方法

        currentId = intent.getIntExtra("id", 0);//接收intent传递的值
        Data data = getIntent().getParcelableExtra("data");
        switch (menuNumber) {
            case 1:
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT); //用intent调用相册
                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                getImage.setType("image/*");
                startActivityForResult(getImage, PHOTO_SUCCESS); //执行并接收回掉方法返回的值
                break;
            case 2:
                Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE"); //用intent调用相机
                startActivityForResult(getImageByCamera, CAMERA_SUCCESS); //执行并接收回掉方法返回的值
                break;
            case 3:
                if (currentId != 0) { //根据id判断当前是否已经创建新的备忘录
                    timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG); //显示时间和日期dialog
                    datePickerDialog.setYearRange(1985, 2028);
                    datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                }
                else {
                    showToast("请先创建备忘录再进行提醒");
                }
                break;
            case 4:
//                checkBox.setText("admin");
//                checkBox.setTextColor(Color.BLACK);

//                RelativeLayout checkboxlayout = (RelativeLayout) findViewById(R.id.checkboxlayout);
//                RelativeLayout.LayoutParams relativeLayoutParams = null;
//                int rowCount = 1; // 行总数
//                int colCount = 1; // 列总数（这里不含第一列）
//                CheckBox checkBox = null;
//                int chk_id = 1000;
//                for (int i = 0; i < rowCount; i++) { // 控制行
//                    checkBox = new CheckBox(getContext());
//                    checkBox.setId(chk_id += 10);
//                    relativeLayoutParams = new RelativeLayout.LayoutParams(
//                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    if (0 == i) { // 如果是第一行第一列，单独处理
//                        relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//                        relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                    } else {
//                        relativeLayoutParams.addRule(RelativeLayout.ALIGN_LEFT,
//                                chk_id - 10);
//                        relativeLayoutParams.addRule(RelativeLayout.BELOW, chk_id - 10);
//                    }
//                    checkBox.setText(String.valueOf(chk_id));
//                    checkBox.setLayoutParams(relativeLayoutParams);
//                    checkboxlayout.addView(checkBox);
//                    // ******************
//                    for (int j = 1; j < colCount; j++) { // 控制列
//                        checkBox = new CheckBox(this);
//                        checkBox.setId(chk_id + j);
//                        checkBox.setText(String.valueOf(chk_id + j));
//                        relativeLayoutParams = new RelativeLayout.LayoutParams(
//                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        relativeLayoutParams.addRule(RelativeLayout.RIGHT_OF, chk_id
//                                + j - 1);
//                        relativeLayoutParams.addRule(RelativeLayout.ALIGN_TOP, chk_id
//                                + j - 1);
//                        checkBox.setLayoutParams(relativeLayoutParams);
//                        checkboxlayout.addView(checkBox);
//                    }
//                }
//                checkBox.setButtonDrawable(R.color.transparent);

                if (currentId != 0) {
                    if (data.getPending() == 2) { //根据当前pending的值，给出不同提示，相应的进行值的修改
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
                }
                else {
                    showToast("请先创建备忘录再进行待办");
                }
                break;
            case 5:
//                List<Data> stars;
//                stars = DataSupport.select("star").where("id = ?", String.valueOf(currentId)).find(Data.class);
//                for (Data star: stars) {
//                    currentStar = star.getStar();
//                    Log.d(TAG, String.valueOf(currentStar));
//                }
                if (currentId != 0) {
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
                }
                else {
                    showToast("请先创建备忘录再进行收藏");
                }
                break;
            default:
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intentImage) {
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_SUCCESS:
                    //获得图片的uri
                    Uri originalUri = intentImage.getData();
                    String imagePath = UriToPathUtil.getImageAbsolutePath(this, originalUri); //将uri转换为文件绝对路径
                    Log.i(TAG, imagePath);
                    currentId = intent.getIntExtra("id", 0);
                    Data newData = new Data(currentId);
                    newData.setImagePath(imagePath);
                    newData.update(currentId); //根据id更新图片路径
                    dataDAO.updateImagePath(imagePath, currentId);
                    Bitmap bitmap = null;
                    Bitmap originalBitmap = null;
                    try {
                        originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
                        bitmap = resizeImage(originalBitmap, 720, 1280);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (originalBitmap != null) {
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(MemorandumActivity.this, originalBitmap);
//                        Log.i(TAG, imageSpan.toString());
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        //  用ImageSpan对象替换face
//                        spannableString.setSpan(imageSpan, 0, "[local]2[local]".length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        spannableString.setSpan(imageSpan, 0, "[]".length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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
                    Bundle extras = intentImage.getExtras();
                    Bitmap originalPhotoBitmap = (Bitmap) extras.get("data");
                    if (originalPhotoBitmap != null) {
                        String photoPath = BitmapToPathUtil.saveBitmap(this, originalPhotoBitmap);
                        Log.i(TAG, photoPath);
                        currentId = intent.getIntExtra("id", 0);
                        Data anotherData = new Data(currentId);
                        anotherData.setImagePath(photoPath);
                        anotherData.update(currentId);
                        dataDAO.updateImagePath(photoPath, currentId);
                        bitmap = resizeImage(originalPhotoBitmap, 720, 1280);//重新定制图片大小为720*1280
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(MemorandumActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        currentId = intent.getIntExtra("id", 0);
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
            data.setReminder(1);
            data.update(currentId);
            AlarmService.cleanAllNotification();
            return;
        }
        Log.i(TAG, timeSetTotal.toString());
        Log.i(TAG, "选择时间: " + Integer.toString((int) notifyTime));
        Log.i(TAG, "真实时间: " + Integer.toString((int) currentTime));
        int delayTime = (int) (notifyTime - currentTime);
        currentImagePath = intent.getStringExtra("imagePath");

        AlarmService.addNotification(delayTime, "tick", "您有一条新提醒", data.getContent(), data.getDate(), data.getId());
//        Log.i(TAG, currentImagePath);
        data.setReminder(2);
        data.update(currentId);
        showToast("已提醒");
    }
}
