package com.example.memorandum.activity;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.memorandum.R;
import com.example.memorandum.bean.Data;
import com.example.memorandum.ui.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import solid.ren.skinlibrary.base.SkinBaseActivity;

public class SettingsActivity extends SkinBaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("设置");
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolkit);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        View rlResetPassword = findViewById(R.id.rl_resetpassword);
        View rlSwitchTheme = findViewById(R.id.rl_switchtheme);
        View rlExportMemo = findViewById(R.id.rl_exportmemo);
        rlResetPassword.setOnClickListener(this);
        rlSwitchTheme.setOnClickListener(this);
        rlExportMemo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_resetpassword:
                startActivity(new Intent(SettingsActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.rl_switchtheme:
                startActivity(new Intent(SettingsActivity.this, ThemeActivity.class));
                break;
            case R.id.rl_exportmemo:

                break;

            default:

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
}
