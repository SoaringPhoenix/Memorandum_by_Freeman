package com.example.memorandum.util;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.memorandum.bean.Data;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public static String getRandNum(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }

    public static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }
}
