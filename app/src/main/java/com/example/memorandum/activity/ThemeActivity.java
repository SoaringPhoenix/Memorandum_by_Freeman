package com.example.memorandum.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.memorandum.R;

import solid.ren.skinlibrary.base.SkinBaseActivity;

public class ThemeActivity extends SkinBaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        setTitle("主题选择");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolkit);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        View rlBlue = findViewById(R.id.rl_blue);
        View rlPink = findViewById(R.id.rl_pink);
        View rlNight = findViewById(R.id.rl_night);
        rlBlue.setOnClickListener(this);
        rlPink.setOnClickListener(this);
        rlNight.setOnClickListener(this);
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
//        int style =
        switch (v.getId()) {
            case R.id.rl_blue:
            Toast.makeText(ThemeActivity.this, "蓝色主题", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_pink:
                Toast.makeText(ThemeActivity.this, "粉色主题", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_night:
                Toast.makeText(ThemeActivity.this, "夜间主题", Toast.LENGTH_SHORT).show();
//                Log.d("ThemeActivity", "夜间主题");
                break;
            default:
                break;
        }
    }
}
