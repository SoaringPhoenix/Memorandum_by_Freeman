package com.example.memorandum;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jason on 2018/3/19.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<Data> mDataList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View dataView;
        TextView dataDate;
        TextView dataContent;

        public ViewHolder(View view) {
            super(view);
            dataView = view;
            dataDate = (TextView) view.findViewById(R.id.data_date);
            dataContent = (TextView) view.findViewById(R.id.data_content);
        }
    }

    public DataAdapter(List<Data> dataList) {
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        final Intent intent = new Intent();
        holder.dataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Data data = mDataList.get(position);
                int id = data.getId();
                String date = data.getDate();
                String content = data.getContent();
                Date exactTime = data.getExactTime();
//                Toast.makeText(v.getContext(), "You clicked view" + data.getContent(), Toast.LENGTH_SHORT).show();
                intent.setClass(parent.getContext(), MemorandumActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("content", content);
                intent.putExtra("date", date);
                intent.putExtra("exactTime", exactTime);
                v.getContext().startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Data data = mDataList.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        try {
            if (isToday(simpleDateFormat.format(date), data.getDate().substring(0, 11))) {
                holder.dataDate.setText(data.getDate().substring(12));
            } else {
                holder.dataDate.setText(data.getDate().substring(0, 11));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (data.getContent().length() <= 20) {
            holder.dataContent.setText(data.getContent());
//        }
//        else {
//            holder.dataContent.setText(data.getContent().substring(0, 22));
//        }
    }

    public static boolean isToday(String str, String formatStr) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH) + 1;
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        if (year1 == year2 && month1 == month2 && day1 == day2) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
