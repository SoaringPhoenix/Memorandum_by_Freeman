package com.example.memorandum;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.text.SimpleDateFormat;

public class MemorandumActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    int currentId;
    String currentContent;
    String currentDate;
    String currentTime;
    Intent intent;

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
        textView = (TextView) findViewById(R.id.exact_time);
        editText = (EditText) findViewById(R.id.input);
        intent = getIntent();
        currentId = intent.getIntExtra("id", 0);
        currentContent = intent.getStringExtra("content");
        currentDate = intent.getStringExtra("date");
        currentTime = intent.getStringExtra("exactTime");
        editText.setText(currentContent);
        textView.setText(currentDate);
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
                Data data = new Data();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                SimpleDateFormat simpleDateFormatfull = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
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
}
