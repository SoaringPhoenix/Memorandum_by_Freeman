package com.example.memorandum.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.memorandum.activity.MemorandumActivity;
import com.example.memorandum.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jason on 2018/4/4.
 */

public class AlarmService extends Service {
    static Timer timer = null;

    // 清除通知
    public static void cleanAllNotification() {
        NotificationManager mn = (NotificationManager) MemorandumActivity
                .getContext().getSystemService(NOTIFICATION_SERVICE);
        mn.cancelAll();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // 添加通知
    public static void addNotification(int delayTime, String tickerText,
                                        String contentTitle, String contentText, String currentDate, int currentId) {
        Intent intent = new Intent(MemorandumActivity.getContext(),
                AlarmService.class);
        intent.putExtra("delayTime", delayTime);
        intent.putExtra("tickerText", tickerText);
        intent.putExtra("contentTitle", contentTitle);
        intent.putExtra("content", contentText);
        intent.putExtra("date", currentDate);
        intent.putExtra("id", currentId);
//        intent.putExtra("imagePath", currentImagePath);
        MemorandumActivity.getContext().startService(intent);
    }

    public void onCreate() {
        Log.e("addNotification", "===========create=======");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {

        long period = 24 * 60 * 60 * 1000; // 24小时一个周期
        int delay = intent.getIntExtra("delayTime", 0);
        if (null == timer) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//                AlarmService.this.
                Intent notificationIntent = new Intent();// 点击跳转位置
                notificationIntent.setClass(AlarmService.this, MemorandumActivity.class);
                notificationIntent.putExtra("id", intent.getIntExtra("id", 0));
                notificationIntent.putExtra("date", intent.getStringExtra("date"));
                notificationIntent.putExtra("content", intent.getStringExtra("content"));
//                notificationIntent.putExtra("imagePath", intent.getStringExtra("imagePath"));
                notificationIntent.putExtra("reminder", 1);
//                AlarmService.this.startActivity(notificationIntent);
                PendingIntent contentIntent = PendingIntent.getActivity(
                        AlarmService.this, 0, notificationIntent, 0);

                Notification notification = new NotificationCompat.Builder(getApplicationContext())
                        .setContentIntent(contentIntent)
                        .setContentTitle(intent.getStringExtra("contentTitle"))
                        .setContentText(intent.getStringExtra("content"))
                        .setTicker(intent.getStringExtra("tickerText"))
                        .setSmallIcon(R.mipmap.note)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.note))
                        .setAutoCancel(true)
                        .setVibrate(new long[] { 0, 2000, 1000, 4000 })
                        .build();
                notification.flags = notification.FLAG_INSISTENT;// 声音无限循环
                notificationManager.notify((int) System.currentTimeMillis(), notification);
            }
        }, delay, period);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("addNotification", "===========destroy=======");
        super.onDestroy();
    }
}
