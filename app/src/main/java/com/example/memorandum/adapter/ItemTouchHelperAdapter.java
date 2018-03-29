package com.example.memorandum.adapter;

/**
 * Created by jason on 2018/3/26.
 */

public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int fromPosition,int toPosition);
    //数据删除
    void onItemDissmiss(int position);
}
