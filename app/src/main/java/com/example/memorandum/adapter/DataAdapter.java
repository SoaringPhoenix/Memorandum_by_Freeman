package com.example.memorandum.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.example.memorandum.dao.DataDAO;

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
    public static final int favoriteNo = 1;
    public static final int favoriteYes = 2;
    DataDAO dataDAO = new DataDAO();

    private static final String TAG = "DataAdapter";
    private Handler handler = new Handler() {
      public void handleMessage(Message message) { //handler接收并处理消息，根据不同case决定收藏button的样式和文字
          switch (message.what) {
              case favoriteNo:
                  favorite.setText("收藏");
                  favorite.setBackgroundResource(R.drawable.btn_mark);
                  break;
              case favoriteYes:
                  favorite.setText("取消收藏");
                  favorite.setBackgroundResource(R.drawable.btn_mark_grey);
                  break;
              default:
                  favorite.setText("收藏");
                  favorite.setBackgroundResource(R.drawable.btn_mark);
                  break;
          }
      }
    };
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        View dataView;
        TextView dataDate;
        TextView dataContent;
        ImageView dataPending;
        ImageView dataReminder;
        ImageView dataStar;
        Intent intent = new Intent();
        public ViewHolder(View view) {            super(view);
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
            int position = getAdapterPosition(); //根据用户点击获取当前位置
            Data data = mDataList.get(position); //将当前位置传入对应的data对象
            int id = data.getId();
            favorite = (Button) itemView.findViewById(R.id.favorite);
            switch (v.getId()) {
                case R.id.main:
//                    Toast.makeText(v.getContext(), "点击了main，位置为：" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    String date = data.getDate(); //获取当前对象的属性
                    String content = data.getContent();
                    String imagePath = data.getImagePath();
                    intent.setClass(v.getContext(), MemorandumActivity.class);
                    intent.putExtra("id", id); //向intent中传入当前对象的属性的值
                    intent.putExtra("content", content);
                    intent.putExtra("date", date);
                    intent.putExtra("data", data);
                    if (imagePath != null && !imagePath.equals("")) {
                        intent.putExtra("imagePath", imagePath);
                    }
                    v.getContext().startActivity(intent);
                    break;
                case R.id.favorite:
                    currentStar = data.getStar();//获取当前对象的star值
                    Message message = new Message();
                    if (currentStar == 2) {
                        data.setStar(1);
                        data.update(id);
                        currentStar = data.getStar();
                        Log.d(TAG, String.valueOf(currentStar));
                        favorite.setText("收藏");
                        favorite.setBackgroundResource(R.drawable.btn_mark);
                        Toast.makeText(v.getContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                        message.what = favoriteNo; //发送favoriteNo的消息
                    }
                    else {
                        data.setStar(2);
                        data.update(id);
                        currentStar = data.getStar();
                        Log.d(TAG, String.valueOf(currentStar));
                        favorite.setText("取消收藏");
                        favorite.setBackgroundResource(R.drawable.btn_mark_grey);
                        Toast.makeText(v.getContext(), "已收藏", Toast.LENGTH_SHORT).show();
                        message.what = favoriteYes;//发送favoriteYes的消息
                    }
                    handler.sendMessage(message); //使用handler发送消息
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
        int currentId = data.getId();
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
        if (data.getContent() != null) {
            if (data.getContent().length() <= 40) {
                holder.dataContent.setText(data.getContent());
            } else {
                holder.dataContent.setText(data.getContent().substring(0, 40) + "...");
            }
        }
//        else {
//            dataDAO.deleteData(currentId);
//        }
        showPending(holder, data.getPending());
        showReminder(holder, data.getReminder());
        showStar(holder, data.getStar());
        for (int i = 0; i <= position; i++) {
            if (data.getStar() == 2) {
                favorite.setText("取消收藏");
                favorite.setBackgroundResource(R.drawable.btn_mark_grey);


            } else if (data.getStar() == 1) {
                favorite.setText("收藏");
                favorite.setBackgroundResource(R.drawable.btn_mark);
            }
        }
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
            Glide.with(context).load(R.drawable.pending).asBitmap().into(holder.dataPending); //使用Glide向imageview中加载图片
        }
        else {
//            Glide.with(context).load(R.drawable.pending_plain).asBitmap().into(holder.dataPending);
//           holder.dataPending.setBackgroundResource(0);
           holder.dataPending.setWillNotDraw(true); //利用holder找到当前视图位置，设置图片绘制为不绘制
        }
    }
    private void showReminder(ViewHolder holder, int currentReminder) {
        if (currentReminder == 2) {
            Glide.with(context).load(R.drawable.reminder).asBitmap().into(holder.dataReminder);
        }
        else {
//            Glide.with(context).load(R.drawable.reminder_plain).asBitmap().into(holder.dataReminder);
//            holder.dataReminder.setBackgroundResource(0);
            holder.dataReminder.setWillNotDraw(true);
        }
    }
    private void showStar(ViewHolder holder, int currentStar) {

        if (currentStar == 2) {
            Glide.with(context).load(R.drawable.star).asBitmap().into(holder.dataStar);
        }
        else {
//            Glide.with(context).load(R.drawable.star_plain).asBitmap().into(holder.dataStar);
//            holder.dataStar.setBackgroundResource(0);
            holder.dataStar.setWillNotDraw(true);
        }
    }

}
