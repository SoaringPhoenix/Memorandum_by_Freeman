package com.example.memorandum;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MemorandumActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    String currentContent;
    String currentDate;
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
        currentContent = intent.getStringExtra("content");
        currentDate = intent.getStringExtra("date");
        editText.setText(currentContent);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分");
        Date date = new Date(System.currentTimeMillis());
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                Date date = new Date(System.currentTimeMillis());
                currentContent = intent.getStringExtra("content");

                if (editText.getText().toString() != null && editText.getText().toString() != "") {
                    if (currentContent == null || currentContent == "") {
                        data.setDate(simpleDateFormat.format(date));
                        data.setContent(editText.getText().toString());
                        data.save();
                    }
                    else if (currentContent != null && currentContent != "" && !(editText.getText().toString().equals(currentContent))) {
                        data.setDate(simpleDateFormat.format(date));
                        data.setContent(editText.getText().toString());
                        data.updateAll("date = ? and content = ?", currentDate, currentContent);
                    }
                } else {
                    finish();
                }
                finish();
                break;
            default:
        }
        return true;
    }
}
