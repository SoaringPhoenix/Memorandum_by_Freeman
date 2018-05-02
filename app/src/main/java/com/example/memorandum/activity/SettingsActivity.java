package com.example.memorandum.activity;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.memorandum.R;
import com.example.memorandum.bean.Data;
import com.example.memorandum.ui.DividerItemDecoration;
import com.example.memorandum.util.AppGlobal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import solid.ren.skinlibrary.base.SkinBaseActivity;

public class SettingsActivity extends SkinBaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("设置");
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
