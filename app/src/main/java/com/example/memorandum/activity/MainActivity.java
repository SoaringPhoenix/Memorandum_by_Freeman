package com.example.memorandum.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.memorandum.R;
import com.example.memorandum.adapter.DataAdapter;
import com.example.memorandum.bean.Data;
import com.example.memorandum.bean.User;
import com.example.memorandum.dao.DataDAO;
import com.example.memorandum.dao.UserDAO;
import com.example.memorandum.ui.DividerItemDecoration;
import com.example.memorandum.ui.SwipeItemLayout;
import com.example.memorandum.util.AppGlobal;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import solid.ren.skinlibrary.base.SkinBaseActivity;


public class MainActivity extends SkinBaseActivity {
    private List<Data> dataList = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefresh;
    private DataAdapter adapter;
    private SearchView searchView;
    private static final String TAG = "MainActivity";
    private DataDAO dataDAO = new DataDAO();
    private UserDAO userDAO = new UserDAO();
    private TextView signIn;
    private TextView signComment;
    NavigationView navigationView;
    View headerView;
    CircleImageView headerImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_main);
        LitePal.getDatabase();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolkit);
        setTitle("备忘录");
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        headerImage = (CircleImageView) headerView.findViewById(R.id.icon_image);
        signIn = (TextView) headerView.findViewById(R.id.sign_in);
        signComment = (TextView) headerView.findViewById(R.id.sign_comment);
        if (!AppGlobal.USERNAME.equals("")) {
            String currentImagePath = userDAO.findImagePath(AppGlobal.USERNAME);
            Glide.with(this).load(currentImagePath).asBitmap().into(headerImage);
//            Bitmap bitmap = BitmapFactory.decodeFile(getExternalCacheDir()+"/head_image.jpg");
//            headerImage.setImageBitmap(bitmap);
            signIn.setText(AppGlobal.NAME);
            signComment.setText("欢迎您登录本应用");
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "You clicked header", Toast.LENGTH_SHORT);
                if (!AppGlobal.USERNAME.equals("")) {
                    String currentImagePath = userDAO.findImagePath(AppGlobal.USERNAME);
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra("imagePath", currentImagePath);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        navigationView.setCheckedItem(R.id.nav_memo);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers(); //关闭菜单
                switch (item.getItemId()) {
                    case R.id.nav_memo:
                        if (AppGlobal.USERNAME != null && !AppGlobal.USERNAME.equals("")) {
                            int userId = userDAO.findUserId(AppGlobal.USERNAME);
                            initUserMemo(userId);
                        }
                        else {
                            initMemo();
                        }
                        setTitle("备忘录");
                        break;
                    case R.id.nav_pending:
                        if (AppGlobal.USERNAME != null && !AppGlobal.USERNAME.equals("")) {
                            int userId = userDAO.findUserId(AppGlobal.USERNAME);
                            initUserPending(userId);
                        }
                        else {
                            initPending();
                        }
                        setTitle("待办");
                        break;
                    case R.id.nav_reminder:
                        if (AppGlobal.USERNAME != null && !AppGlobal.USERNAME.equals("")) {
                            int userId = userDAO.findUserId(AppGlobal.USERNAME);
                            initUserReminder(userId);
                        }
                        else {
                            initReminder();
                        }
                        setTitle("提醒");
                        break;
                    case R.id.nav_favorites:
                        if (AppGlobal.USERNAME != null && !AppGlobal.USERNAME.equals("")) {
                            int userId = userDAO.findUserId(AppGlobal.USERNAME);
                            initUserFavorites(userId);
                        }
                        else {
                            initFavorites();
                        }
                        setTitle("收藏");
                        break;
                    case R.id.nav_settings:
                        if (AppGlobal.NAME != null && !AppGlobal.NAME.equals("")) {
                            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivity(intent);
                        }
                        else {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                        break;
                    case R.id.nav_logout:
                        if (AppGlobal.USERNAME != null && !AppGlobal.USERNAME.equals("")) {
                            finish();
                            AppGlobal.USERNAME = "";
                            AppGlobal.NAME = "";
                            AppGlobal.INSERT_IMAGE = false;
                            AppGlobal.currentImagePath = "";
                        }
                        break;
                }
                return true;

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MemorandumActivity.class); //利用Intent跳转到空的memorandumActivty.class
                v.getContext().startActivity(intent);
            }
        });
        searchView = (SearchView) findViewById(R.id.search_view);
        if (AppGlobal.USERNAME != null && !AppGlobal.USERNAME.equals("")) {
            int userId = userDAO.findUserId(AppGlobal.USERNAME);
            dataList = dataDAO.getUserData(userId);
        }
        else {
            dataList = dataDAO.getData();
        }
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
                if (AppGlobal.USERNAME != null && !AppGlobal.USERNAME.equals("")) {
                    User currentUser = userDAO.findUser(AppGlobal.USERNAME);
                    int userId = userDAO.findUserId(AppGlobal.USERNAME);
                    Log.i(TAG, Integer.toString(userId));
                    if (!TextUtils.isEmpty(newText.trim())) { //判定searchview的输入框是否非空
                        dataList.clear();
                        if (getTitle() == "备忘录") {
                            dataList = dataDAO.queryUserData(newText.trim(), userId);
                        }
                        else if (getTitle() == "待办") {
                            dataList = dataDAO.queryUserPending(newText.trim(), userId);
                        }
                        else if (getTitle() == "提醒") {
                            dataList = dataDAO.queryUserReminder(newText.trim(), userId);
                        }
                        else if (getTitle() == "收藏") {
                            dataList = dataDAO.queryUserFavorites(newText.trim(), userId);
                        }
                    }
                    else { // searchview输入为空
                        dataList.clear();
                        if (getTitle() == "待办") {
                            dataList = dataDAO.getUserPending(userId);
                        }
                        else if (getTitle() == "提醒") {
                            dataList = dataDAO.getUserReminder(userId);
                        }
                        else if (getTitle() == "收藏") {
                            dataList = dataDAO.getUserFavorites(userId);
                        }
                        else {
                            dataList = dataDAO.getUserData(userId);
                        }
                    }
                }
                else {
                    if (!TextUtils.isEmpty(newText)) {
                        dataList.clear();
                        if (getTitle() == "备忘录") {
                            dataList = dataDAO.queryData(newText.trim());
                        }
                        else if (getTitle() == "待办") {
                            dataList = dataDAO.queryPending(newText.trim());
                        }
                        else if (getTitle() == "提醒") {
                            dataList = dataDAO.queryReminder(newText.trim());
                        }
                        else if (getTitle() == "收藏") {
                            dataList = dataDAO.queryFavorites(newText.trim());
                        }
                    } else {
                        if (getTitle() == "待办") {
                            dataList = dataDAO.getPending();
                        }
                        else if (getTitle() == "提醒") {
                            dataList = dataDAO.getReminder();
                        }
                        else if (getTitle() == "收藏") {
                            dataList = dataDAO.getFavorites();
                        }
                        else {
                            dataList = dataDAO.getData();
                        }
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
        if (AppGlobal.NAME != null && !AppGlobal.NAME.equals("")) {
            signIn.setText(AppGlobal.NAME);
        }
        if (AppGlobal.currentImagePath != null && ! AppGlobal.currentImagePath.equals("")) {
            Glide.with(this).load(AppGlobal.currentImagePath).asBitmap().into(headerImage);
        }
//        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
//        TextView signIn = (TextView) headerView.findViewById(R.id.sign_in);
//        TextView signComment = (TextView) headerView.findViewById(R.id.sign_comment);
//        CircleImageView headerImage = (CircleImageView) headerView.findViewById(R.id.icon_image);
//        if (!AppGlobal.USERNAME.equals("")) {
//            String currentImagePath = userDAO.findImagePath(AppGlobal.USERNAME);
//            Glide.with(this).load(currentImagePath).asBitmap().into(headerImage);
////            Bitmap bitmap = BitmapFactory.decodeFile(getExternalCacheDir()+"/head_image.jpg");
////            headerImage.setImageBitmap(bitmap);
//            signIn.setText(AppGlobal.NAME);
//            signComment.setText("欢迎您登录本应用");
//        }

    }

    private void initMemo() {
        dataList.clear();
        dataList = dataDAO.getData();
        initRecyclerView();
    }
    private void initUserMemo(int userId) {
        dataList.clear();
        dataList = dataDAO.getUserData(userId);
        initRecyclerView();
    }
    private void initPending() {
        dataList.clear();
        dataList = dataDAO.getPending();
        initRecyclerView();
    }
    private void initUserPending(int userId) {
        dataList.clear();
        dataList = dataDAO.getUserPending(userId);
        initRecyclerView();
    }

    private void initReminder() {
        dataList.clear();
        dataList = dataDAO.getReminder();
        initRecyclerView();
    }
    private void initUserReminder(int userId) {
        dataList.clear();
        dataList = dataDAO.getUserReminder(userId);
        initRecyclerView();
    }
    private void initFavorites() {
        dataList.clear();
        dataList = dataDAO.getFavorites();
        initRecyclerView();
    }
    private void initUserFavorites(int userId) {
        dataList.clear();
        dataList = dataDAO.getUserFavorites(userId);
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
                        dataList.clear();
                        if (AppGlobal.USERNAME != null && !AppGlobal.USERNAME.equals("")) {
                            int userId = userDAO.findUserId(AppGlobal.USERNAME);
                            if (getTitle() == "待办") {
                                initUserPending(userId);
                            } else if (getTitle() == "提醒") {
                                initUserReminder(userId);
                            } else if (getTitle() == "收藏") {
                                initUserFavorites(userId);
                            } else {
                                initUserMemo(userId);
                            }
                        }
                        else {
                            if (getTitle() == "待办") {
                                initPending();
                            } else if (getTitle() == "提醒") {
                                initReminder();
                            } else if (getTitle() == "收藏") {
                                initFavorites();
                            } else {
                                initMemo();
                            }
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
