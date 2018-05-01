package com.example.memorandum.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import com.example.memorandum.util.CommonUtility;

/**
 * Created by jason on 2018/3/29.
 */

public class RichEditText extends AppCompatEditText{
    public RichEditText(Context context) {
        super(context);
    }
    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void insertDrawable(int id) {
        final SpannableString ss = new SpannableString("easy");
        //得到drawable对象，即所有插入的图片
        Drawable d = getResources().getDrawable(id);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //用这个drawable对象代替字符串easy
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        //包括0但是不包括"easy".length()即：4。[0,4)
        ss.setSpan(span, 0, "easy".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        append(ss);
    }
    public void insertBitmap(String path) {
        final SpannableString ss = new SpannableString(" ");
        //得到drawable对象，即所有插入的图片
//        Drawable d = getResources().getDrawable(id);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap newBitmap = CommonUtility.resizeImage(bitmap, 2560, 3840);
        Drawable d = new BitmapDrawable(newBitmap);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //用这个drawable对象代替字符串easy
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        //包括0但是不包括"easy".length()即：4。[0,4)
        ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        append(ss);
    }
}
