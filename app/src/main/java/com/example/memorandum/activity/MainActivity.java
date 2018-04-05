package com.example.memorandum.activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.memorandum.R;
import com.example.memorandum.adapter.DataAdapter;
import com.example.memorandum.bean.Data;
import com.example.memorandum.ui.DividerItemDecoration;
import com.example.memorandum.ui.SwipeItemLayout;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

import static com.example.memorandum.util.CommonUtility.getRawData;

public class MainActivity extends AppCompatActivity {
    private List<Data> dataList = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefresh;
    private DataAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.getDatabase();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolkit);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        navigationView.setCheckedItem(R.id.nav_memo);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_memo:
                        initMemo();
                        setTitle("备忘录");
                        break;
                    case R.id.nav_pending:
                        initPending();
                        setTitle("待办");
                        break;
                    case R.id.nav_reminder:
                        initReminder();
                        setTitle("提醒");
                        break;
                    case R.id.nav_favorites:
                        initFavorites();
                        setTitle("收藏");
                        break;
                    case R.id.nav_settings:
                        Intent intent = new Intent(MainActivity.this , SettingsActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MemorandumActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        searchView = (SearchView) findViewById(R.id.search_view);
        initData();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DataAdapter(dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                if (!TextUtils.isEmpty(newText)) {
                    dataList.clear();
                    if (getTitle() == "备忘录") {
                        dataList = DataSupport.order("exactTime desc").where("content like ?", "%" + newText + "%").find(Data.class);
                    }
                    else if (getTitle() == "待办") {
                        dataList = getRawData("select date, content, star, pending, reminder from Data where pending = ? " +
                                "and content like ? order by exactTime desc", "2", "%" + newText + "%");
                    }
                    else if (getTitle() == "提醒") {
                        dataList = getRawData("select date, content, star, pending, reminder from Data where reminder = ? " +
                                "and content like ? order by exactTime desc", "2", "%" + newText + "%");
                    }
                    else if (getTitle() == "收藏") {
                        dataList = getRawData("select date, content, star, pending, reminder from Data where star = ? " +
                                "and content like ? order by exactTime desc", "2", "%" + newText + "%");
                    }
                } else {
                    if (getTitle() == "待办") {
                        dataList = DataSupport.where("pending = ?", "2").order("exactTime desc").find(Data.class);
                    }
                    else if (getTitle() == "提醒") {
                        dataList = DataSupport.where("reminder = ?", "2").order("exactTime desc").find(Data.class);
                    }
                    else if (getTitle() == "收藏") {
                        dataList = DataSupport.where("star = ?", "2").order("exactTime desc").find(Data.class);
                    }
                    else {
                        dataList = DataSupport.order("exactTime desc").find(Data.class);
                    }
                }
                adapter = new DataAdapter(dataList);
                recyclerView.setAdapter(adapter);
                return false;
            }
        });
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolkit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.setFocusable(false);
        searchView.setFocusableInTouchMode(false);
        searchView.requestFocus();
    }


    private void initData() {
        dataList = DataSupport.order("exactTime desc").find(Data.class);
    }

    private void initMemo() {
        dataList = DataSupport.order("exactTime desc").find(Data.class);
        initRecyclerView();
    }

    private void initPending() {
        dataList = DataSupport.where("pending = ?", "2").order("exactTime desc").find(Data.class);
        initRecyclerView();
    }

    private void initReminder() {
        dataList = DataSupport.where("reminder = ?", "2").order("exactTime desc").find(Data.class);
        initRecyclerView();
    }

    private void initFavorites() {
        dataList = DataSupport.where("star = ?", "2").order("exactTime desc").find(Data.class);
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DataAdapter(dataList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
//        Date date = new Date(System.currentTimeMillis());
//        for (int i = 0; i < 5; i++) {
//            Data data = new Data( "第" + i + "条数据", simpleDateFormat.format(date));
//            dataList.add(data);
//        }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);//drawer方向
        } else {
            super.onBackPressed();
        }
    }

    private void refreshData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getTitle() == "待办") {
                            initPending();
                        } else if (getTitle() == "提醒") {
                            initReminder();
                        } else if (getTitle() == "收藏") {
                            initFavorites();
                        } else {
                            initMemo();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
