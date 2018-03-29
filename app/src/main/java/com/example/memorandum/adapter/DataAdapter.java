package com.example.memorandum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.memorandum.MemorandumActivity;
import com.example.memorandum.R;
import com.example.memorandum.bean.Data;
import com.example.memorandum.fragment.MemorandumContentFragment;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by jason on 2018/3/19.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>  {
    private Context context;
    private List<Data> mDataList;

//    @Override
//    public void onItemMove(int fromPosition, int toPosition) {
//        // 交换位置
//        Collections.swap(mDataList, fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
//    }
//
//    @Override
//    public void onItemDissmiss(int position) {
//        Data data = mDataList.get(position);
//        int id = data.getId();
//        DataSupport.delete(Data.class, id);
//        mDataList.remove(position);
//        notifyItemRemoved(position);
//    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        View dataView;
        TextView dataDate;
        TextView dataContent;
        ImageView dataStar;
        Intent intent = new Intent();
        public ViewHolder(View view) {
            super(view);
            dataView = view;
            dataDate = (TextView) view.findViewById(R.id.data_date);
            dataContent = (TextView) view.findViewById(R.id.data_content);
            dataStar = (ImageView) view.findViewById(R.id.data_star);
            View main = itemView.findViewById(R.id.main);
            main.setOnClickListener(this);
            main.setOnLongClickListener(this);
            View delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Data data = mDataList.get(position);
            int id = data.getId();
            switch (v.getId()) {
                case R.id.main:
//                    Toast.makeText(v.getContext(), "点击了main，位置为：" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    String date = data.getDate();
                    String content = data.getContent();
                    Date exactTime = data.getExactTime();
                    int star = data.getStar();
                    intent.setClass(v.getContext(), MemorandumActivity.class);
//                    intent.setClass(v.getContext(), MemorandumContentFragment.class);
                    intent.putExtra("id", id);
                    intent.putExtra("content", content);
                    intent.putExtra("date", date);
                    intent.putExtra("exactTime", exactTime);
                    intent.putExtra("star", star);
                    v.getContext().startActivity(intent);
                    break;
                case R.id.delete:
                    DataSupport.delete(Data.class, id);
                    mDataList.remove(position);
                    notifyItemRemoved(position);
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public DataAdapter(List<Data> dataList) {
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.data_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
//        final Intent intent = new Intent();
//        holder.dataView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Data data = mDataList.get(position);
//                int id = data.getId();
//                String date = data.getDate();
//                String content = data.getContent();
//                Date exactTime = data.getExactTime();
//                int star = data.getStar();
//                Toast.makeText(v.getContext(), "You clicked view" + data.getContent(), Toast.LENGTH_SHORT).show();
//                intent.setClass(context, MemorandumActivity.class);
//                intent.putExtra("id", id);
//                intent.putExtra("content", content);
//                intent.putExtra("date", date);
//                intent.putExtra("exactTime", exactTime);
//                intent.putExtra("star", star);
//                v.getContext().startActivity(intent);
//
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Data data = mDataList.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        try {
            if (!simpleDateFormat.format(date).equals(data.getDate().substring(0, 11))) {
                holder.dataDate.setText(data.getDate().substring(0, 11));
            } else {
                holder.dataDate.setText(data.getDate().substring(12));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (data.getContent().length() <= 20) {
        holder.dataContent.setText(data.getContent());
        showStar(holder, data.getStar());
//        }
//        else {
//            holder.dataContent.setText(data.getContent().substring(0, 22));
//        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private void showStar(ViewHolder holder, int currentId) {
        if (currentId == 1) {
            Glide.with(context).load(R.drawable.star_gold).asBitmap().into(holder.dataStar);
        }
        else {
            Glide.with(context).load(R.drawable.star_plain).asBitmap().into(holder.dataStar);
        }
    }

}
