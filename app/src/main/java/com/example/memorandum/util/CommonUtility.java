package com.example.memorandum.util;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.Gravity;
import android.widget.Toast;

import com.example.memorandum.bean.Data;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/3/29.
 */

public class CommonUtility {

    static List<Data> dataList = new ArrayList<>();
    /**
     * 图片缩放
     * @param originalBitmap 原始的Bitmap
     * @param newWidth 自定义宽度
     * @return 缩放后的Bitmap
     */
    public static Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        //定义欲转换成的宽、高
//            int newWidth = 200;
//            int newHeight = 200;
        //计算宽、高缩放率
        float scanleWidth = (float)newWidth/width;
        float scanleHeight = (float)newHeight/height;
        //创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scanleWidth,scanleHeight);
        //旋转图片 动作
        //matrix.postRotate(45);
        // 创建新的图片Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);
        return resizedBitmap;
    }

    public static List<Data> getRawData(String... sql) {
        Cursor cursor = DataSupport.findBySQL(sql);
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                int star = cursor.getInt(cursor.getColumnIndex("star"));
                int pending = cursor.getInt(cursor.getColumnIndex("pending"));
                int reminder = cursor.getInt(cursor.getColumnIndex("reminder"));
                dataList.add(new Data(content, date, star, pending, reminder));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dataList;
    }
}
