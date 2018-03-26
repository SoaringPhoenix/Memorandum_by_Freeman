package com.example.memorandum;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.memorandum.bean.Data;
import com.example.memorandum.ui.GooeyMenu;

import org.litepal.crud.DataSupport;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MemorandumActivity extends AppCompatActivity implements GooeyMenu.GooeyMenuInterface {
    private EditText editText;
    private TextView textView;
    private ImageView data_star;
    private GooeyMenu gooeyMenu;
    private Toast mToast;
    int currentId;
    String currentContent;
    String currentDate;
    String currentTime;
    Intent intent;
    Data data = new Data();

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
        gooeyMenu = (GooeyMenu) findViewById(R.id.gooey_menu);
        gooeyMenu.setOnMenuListener(this);
        textView = (TextView) findViewById(R.id.exact_time);
        editText = (EditText) findViewById(R.id.input);
        intent = getIntent();
        currentId = intent.getIntExtra("id", 0);
        currentContent = intent.getStringExtra("content");
        currentDate = intent.getStringExtra("date");
        currentTime = intent.getStringExtra("exactTime");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date(System.currentTimeMillis());
        editText.setText(currentContent);
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
                currentTime = intent.getStringExtra("exactTime");

                if (!TextUtils.isEmpty(editText.getText())) {
                    if (currentContent == null || currentContent == "") {
                        data.setDate(simpleDateFormat.format(date));
                        data.setExactTime(date);
                        data.setContent(editText.getText().toString());
                        data.save();
                    } else if (currentContent != null && currentContent != "" && !(editText.getText().toString().equals(currentContent))) {
                        data.setDate(simpleDateFormat.format(date));
                        data.setExactTime(date);
                        data.setContent(editText.getText().toString());
                        data.update(currentId);
//                        data.updateAll("date = ? and content = ?",currentDate, currentContent);
                    }
                } else {
                    DataSupport.delete(Data.class, currentId);
                    finish();
                }
                finish();
                break;
            default:
        }
        return true;
    }

    @Override
    public void menuOpen() {
        showToast("Menu Open");

    }

    @Override
    public void menuClose() {
        showToast( "Menu Close");
    }

    @Override
    public void menuItemClicked(int menuNumber) {
        showToast( "Menu item clicked : " + menuNumber);
        switch (menuNumber) {
            case 1:
                intent = getIntent();
                currentId = intent.getIntExtra("id", 0);
                if (currentId == 0) {
                    currentId = 1;
                }
                else if (currentId == 1) {
                    currentId = 0;
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
        }
    }
    private void showToast(String msg){
        if(mToast!=null){
            mToast.cancel();
        }
        mToast= Toast.makeText(this,msg,Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }


}
