package com.example.memorandum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.memorandum.activity.MemorandumActivity;
import com.example.memorandum.R;
import com.example.memorandum.bean.Data;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jason on 2018/3/19.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>  {
    private Context context;
    private List<Data> mDataList;
    private Button delete;
    private Button favorite;
    int currentStar;

    private static final String TAG = "DataAdapter";

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        View dataView;
        TextView dataDate;
        TextView dataContent;
        ImageView dataPending;
        ImageView dataReminder;
        ImageView dataStar;
        Intent intent = new Intent();
        public ViewHolder(View view) {
            super(view);
            dataView = view;
            dataDate = (TextView) view.findViewById(R.id.data_date);
            dataContent = (TextView) view.findViewById(R.id.data_content);
            dataPending = (ImageView) view.findViewById(R.id.data_pending);
            dataReminder = (ImageView) view.findViewById(R.id.data_reminder);
            dataStar = (ImageView) view.findViewById(R.id.data_star);
            View main = itemView.findViewById(R.id.main);
            main.setOnClickListener(this);
            main.setOnLongClickListener(this);
            delete = (Button) itemView.findViewById(R.id.delete);
            favorite = (Button) itemView.findViewById(R.id.favorite);
            delete.setOnClickListener(this);
            favorite.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Data data = mDataList.get(position);
            int id = data.getId();
            favorite = (Button) itemView.findViewById(R.id.favorite);
            if (data.getStar() == 2) {
                favorite.setText("取消收藏");
                favorite.setBackgroundResource(R.drawable.btn_mark_grey);
            }
            else {
                favorite.setText("收藏");
                favorite.setBackgroundResource(R.drawable.btn_mark);
            }
            switch (v.getId()) {
                case R.id.main:
//                    Toast.makeText(v.getContext(), "点击了main，位置为：" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    String date = data.getDate();
                    String content = data.getContent();
                    intent.setClass(v.getContext(), MemorandumActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("content", content);
                    intent.putExtra("date", date);
                    intent.putExtra("data", data);
                    v.getContext().startActivity(intent);
                    break;
                case R.id.favorite:
                    if (currentStar == 2) {
                        data.setStar(1);
                        data.update(id);
                        currentStar = data.getStar();
                        Log.d(TAG, String.valueOf(currentStar));
                        favorite.setText("收藏");
                        favorite.setBackgroundResource(R.drawable.btn_mark);
                        Toast.makeText(v.getContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        data.setStar(2);
                        data.update(id);
                        currentStar = data.getStar();
                        Log.d(TAG, String.valueOf(currentStar));
                        favorite.setText("取消收藏");
                        favorite.setBackgroundResource(R.drawable.btn_mark_grey);
                        Toast.makeText(v.getContext(), "已收藏", Toast.LENGTH_SHORT).show();
                    }
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
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Data data = mDataList.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        try {
            if (!simpleDateFormat.format(date).equals(data.getDate().substring(0, 11))) {
                holder.dataDate.setText(data.getDate().substring(5, 11));
            } else {
                holder.dataDate.setText(data.getDate().substring(12));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (data.getContent().length() <= 20) {
        holder.dataContent.setText(data.getContent());
        showPending(holder, data.getPending());
        showReminder(holder, data.getReminder());
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

    private void showPending(ViewHolder holder, int currentPending) {
        if (currentPending == 2) {
            Glide.with(context).load(R.drawable.pending).asBitmap().into(holder.dataPending);
        }
        else {
            Glide.with(context).load(R.drawable.pending_plain).asBitmap().into(holder.dataPending);
        }
    }
    private void showReminder(ViewHolder holder, int currentReminder) {
        if (currentReminder == 2) {
            Glide.with(context).load(R.drawable.reminder).asBitmap().into(holder.dataReminder);
        }
        else {
            Glide.with(context).load(R.drawable.reminder_plain).asBitmap().into(holder.dataReminder);
        }
    }
    private void showStar(ViewHolder holder, int currentStar) {
        if (currentStar == 2) {
            Glide.with(context).load(R.drawable.star).asBitmap().into(holder.dataStar);
        }
        else {
            Glide.with(context).load(R.drawable.star_plain).asBitmap().into(holder.dataStar);
        }
    }

}
