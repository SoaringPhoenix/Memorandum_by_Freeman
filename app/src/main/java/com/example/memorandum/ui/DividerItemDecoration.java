package com.example.memorandum.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jason on 2018/3/19.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    //使用系统自带的listDivider
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    private Drawable mDivider;

    public DividerItemDecoration(Context context) {
        super();
        // 从TypedArray中得到一个Drawable对象
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        mDivider = typedArray.getDrawable(0);

        typedArray.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawDivider(c, parent);
    }

    /**
     * 遍历childView，为每一个childView描绘divider
     *
     * @param c      画布对象
     * @param parent 父控件即RecyclerView
     */
    private void drawDivider(Canvas c, RecyclerView parent) {
        //获取分割线的上边距，即RecyclerView的padding值
        final int top = parent.getPaddingTop();

        //分割线下边距
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();

        //遍历所有item view，为它们的右边方绘制分割线，就是计算出上下左右四个值画一个矩形
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            //画右边的divider
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);

            //画完右边画底边
            mDivider.setBounds(child.getLeft() + child.getPaddingLeft(), child.getBottom() + params.bottomMargin,
                    child.getRight() - child.getPaddingRight(), child.getBottom() + mDivider.getIntrinsicHeight());
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, mDivider.getIntrinsicHeight(), mDivider.getIntrinsicHeight());
    }
}
