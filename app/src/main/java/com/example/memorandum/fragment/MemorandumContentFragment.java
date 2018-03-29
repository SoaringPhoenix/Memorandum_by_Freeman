package com.example.memorandum.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memorandum.R;
import com.example.memorandum.bean.Data;
import com.example.memorandum.ui.GooeyMenu;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jason on 2018/3/28.
 */

public class MemorandumContentFragment extends Fragment {
    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.memorandum_content_fragment, container, false);

        return root;
    }




}
